package com.yy.ent.platform.signcar.service.heart;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Joiner;
import com.yy.ent.platform.core.redis.RedisTemplate;
import com.yy.ent.platform.signcar.common.constant.RedisKeyConstant;
import com.yy.ent.platform.signcar.common.mongodb.HeartStatistic;
import com.yy.ent.platform.signcar.common.util.HttpClientUtil;
import com.yy.ent.platform.signcar.repository.mongo.HeartStatisticRepository;
import com.yy.ent.platform.signcar.service.BussinessConf;
import com.yy.ent.platform.signcar.service.common.BaseService;
import com.yy.ent.platform.signcar.service.common.CommonService;
import com.yy.ent.platform.signcar.service.yyp.Result;

@Service
public class HeartStatisticService extends BaseService{
    
    @Autowired
    private BussinessConf conf;
    
    @Autowired
    private HeartService heartService;
    
    @Autowired
    private CommonService commonService ;
    
    @Autowired
    private RedisTemplate redisTemplate;
    
    @Autowired
    private HeartStatisticRepository heartStatisticRepository;
   
    private static final int FETCHLIMIT = 100;
    
    /**
     * @Desc:调用欢聚云接口获取单个主播的用户占用量和名次
     * @param yyyymmdd
     * @param idolUid
     * @throws Exception
     * @return:JSONObject
     * @author: renmeng  
     * @date: 2016年1月21日 下午5:42:40
     */
    public JSONObject fetchSingleLiveData(String yyyymmdd,String idolUid) throws Exception{
        JSONObject result = null;
        String url = String.format(conf.getCoundUrl(), idolUid,yyyymmdd);
        String resultJsonStr = null;
        try {
            resultJsonStr = HttpClientUtil.sendHttpByGet(url);
        } catch (Exception e) {
            logger.warn("fetch live Data error,try again",e);
            try {
                resultJsonStr = HttpClientUtil.sendHttpByGet(url);
            } catch (Exception e1) {
                throw e1;
            }
        }
        if(StringUtils.isNotBlank(resultJsonStr)){
            JSONObject json = (JSONObject)JSONObject.parse(resultJsonStr);
            JSONArray array = json.getJSONArray("data");
            if(array!=null&&array.size()>0){
                result = array.getJSONObject(0);
            }
        }else{
            throw new Exception("fetch result empty!");
        }
        return result;
    }
    
    /**
     * @Desc:获取开播之前的数据，先去欢聚云取数据，没有数据再取数据库的数据，都没有返回错误码-1
     * @param idolUid
     * @return
     * @throws Exception
     * @return:Result
     * @author: renmeng  
     * @date: 2016年1月28日 上午10:29:34
     */
    public Result getBeforeLiveData(String idolUid) throws Exception{
        String yyyymmdd = commonService.getYYYYMMDD(-1);
        JSONObject json = null;
        try {
            json = fetchSingleLiveData(yyyymmdd,idolUid);
        } catch (Exception e) {
            logger.info(e.getMessage());
        }
        int code = 0;
        if(json==null){
            json = new JSONObject();
            HeartStatistic h = heartStatisticRepository.findLastHeartStatistic(Long.valueOf(idolUid),yyyymmdd);
            if(h.getIdolUid()==-1l){
                code = -1;
            }else{
                json.put("id",h.getIdolUid());
                json.put("rank",h.getRank());
                json.put("num", h.getCoverNum());
                yyyymmdd = h.getStatDate();
            }
        }
        
        Result result = Result.newResult(code).put("date", yyyymmdd).put("num", json.getLong("num")).put("uid", json.getLong("id")).put("rank", json.getDouble("rank"));
        return result;
    }
    

    public List<HeartStatistic> queryHeartStatistic(long idolUid,String startDate,String endDate) throws Exception{
        List<HeartStatistic> list = heartStatisticRepository.findHeartStatistic(idolUid, startDate,endDate);
        return list;
    }
    
    public Set<String> getKeys(){
        String yyyymmdd = commonService.getYYYYMMDD(-1);
        String key = RedisKeyConstant.HEART_CONSUME_TOTAL_NUM.getKey(yyyymmdd);
        Set<String> set = redisTemplate.hkeys(key);
        return set;
    }
    
    /**
     * @Desc:多线程批量抓取所有主播的数据并入库
     * @return:void
     * @author: renmeng  
     * @date: 2016年1月21日 下午6:57:14
     */
    public void heartStatisticHandle(){
        logger.info("heart statistic hadndle start,current time is:{}",commonService.getYYYYMMDDHHMMSS());
        String yyyymmdd = commonService.getYYYYMMDD(-1);
        ExecutorService threadPool =  Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()*3);
        Set<String> keyset = getKeys();
        logger.info("heart statistic hadndle keys set size:{}",keyset.size());
        List<Future<List<HeartStatistic>>> threadGroup = new ArrayList<Future<List<HeartStatistic>>>();
        List<String> uidList = new ArrayList<String>(FETCHLIMIT);
        Iterator<String> it = keyset.iterator();
        while(it.hasNext()){
            uidList.add(it.next());
            if(uidList.size()>=FETCHLIMIT){
                String url = getUrl(uidList);
                uidList.clear();
                
                Future<List<HeartStatistic>> future =threadPool.submit(new FetchTask(url,yyyymmdd) );
                threadGroup.add(future);            
            
            }
        }    
        if(uidList.size()>0){
            String url = getUrl(uidList);
            uidList.clear();
            Future<List<HeartStatistic>> future = threadPool.submit(new FetchTask(url,yyyymmdd));
            threadGroup.add(future);
        }
     
        logger.info("heart statistic hadndle threadGroup size:{}",threadGroup.size());
        
        for(Future<List<HeartStatistic>> f:threadGroup){
            List<HeartStatistic> list = null ;
            try {
                list = f.get();
                logger.info("fetch result list size:{}",list.size());
                heartStatisticRepository.insertAll(list);
            } catch (InterruptedException e) {
                logger.warn("fetch error!",e);
            } catch (ExecutionException e) {
                logger.warn("fetch error!",e);
            }
            
        }
        threadPool.shutdown();
        //滚动清理三个月之前的数据
        heartStatisticRepository.delHeartStatByDate(commonService.getYYYYMMDD(-90));
        logger.info("heart statistic hadndle finish,current time is:{}",commonService.getYYYYMMDDHHMMSS());
        
    }
    
    public String getUrl(List<String> uidList){
        String uidListStr = Joiner.on(',').join(uidList);
        String yyyymmdd = commonService.getYYYYMMDD(-1);
        String url = String.format(conf.getCoundUrl(), uidListStr,yyyymmdd);
        return url;
    }
    

    
    private class FetchTask implements Callable<List<HeartStatistic>>{
        private String url;
        private String date;
        public FetchTask(String url,String date){
            this.url = url;
            this.date = date;
        }
        @Override
        public List<HeartStatistic> call() throws Exception {
            List<HeartStatistic> list = new ArrayList<HeartStatistic>();
            try {
                String resultJsonStr = HttpClientUtil.sendHttpByGet(url);
                if(StringUtils.isNotBlank(resultJsonStr)){
                    JSONObject json = (JSONObject)JSONObject.parse(resultJsonStr);
                    JSONArray array = json.getJSONArray("data");
                    if(array!=null&&array.size()>0){
                        for(int i=0;i<array.size();i++)
                        {
                            JSONObject innerJson = array.getJSONObject(i);
                            HeartStatistic stat = new HeartStatistic();
                            long idolUid = innerJson.getLong("id");
                            stat.setCoverNum(innerJson.getLong("num"));
                            stat.setRank(innerJson.getDouble("rank"));
                            stat.setFansNum(heartService.getIdolFansNum(idolUid, date));
                            stat.setHeartNum(heartService.getIdolHeartNum(idolUid, date));
                            stat.setIdolUid(idolUid);
                            stat.setStatDate(date);
                            list.add(stat);
                        }
                    }
                }
            } catch (Exception e) {
                logger.warn("thread fetch error!",e);
            }
            return list;
        }
        
      
        
    }

    
}

