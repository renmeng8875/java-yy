package com.yy.ent.platform.signcar.service.yyp;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.yy.ent.cherrio.annotation.URI;
import com.yy.ent.platform.core.spring.SpringHolder;
import com.yy.ent.platform.signcar.common.constant.YYPConstant;
import com.yy.ent.platform.signcar.common.mongodb.HeartStatistic;
import com.yy.ent.platform.signcar.common.yyp.HeartStatisticMarshal;
import com.yy.ent.platform.signcar.service.BussinessConf;
import com.yy.ent.platform.signcar.service.common.CommonService;
import com.yy.ent.platform.signcar.service.common.WebdbService;
import com.yy.ent.platform.signcar.service.heart.HeartService;
import com.yy.ent.platform.signcar.service.heart.HeartStatisticService;
import com.yy.ent.srv.builder.Dispatch;
import com.yy.ent.srv.protocol.Constants;

public class HeartStatisticHandler extends BaseYYPHandler {
    private HeartService heartService = SpringHolder.getBean(HeartService.class);
    
    private HeartStatisticService heartStatisticService = SpringHolder.getBean(HeartStatisticService.class);
    
    private CommonService commonService = SpringHolder.getBean(CommonService.class);
    
    private WebdbService webdbService = SpringHolder.getBean(WebdbService.class);
    
    private BussinessConf conf = SpringHolder.getBean(BussinessConf.class);
    
    //开始直播前调用
    @Dispatch(uri = Constants.MSG_MAX_RECV_SERVER_PROXY_PC, max = YYPConstant.PC.MAX, min = YYPConstant.PC.REQUEST_IDOL_LIVE_BEGIN)
    public void beforeLiveTip() throws Exception{
        getPublicComboYYHeaderPC("beginLiveTip");
        Long uid = getComboYYHeader().getUid().longValue();
        logger.info("heartStatisticHandler beginLiveTip parameter=>uid:{}",uid);
        Result result = heartStatisticService.getBeforeLiveData(String.valueOf(uid));
        logger.info("heartStatisticHandler beforeLiveTip result=>code:{},data:{}",result.getResult() ,result.toJSONString());
        responsePC(result);
    }
    
    //结束直播弹窗
    @Dispatch(uri = Constants.MSG_MAX_RECV_SERVER_PROXY_PC, max = YYPConstant.PC.MAX, min = YYPConstant.PC.REQUEST_IDOL_LIVE_END)
    public void afterLiveTip() throws Exception{
        getPublicComboYYHeaderPC("endLiveTip");
        
        Long uid = getComboYYHeader().getUid().longValue();
        //string yyno 
        logger.info("heartStatisticHandler endLiveTip parameter=>uid:{}",uid);
 
        Result result = Result.newResult(0);
       
        responsePC(result);
    }
    
    /**
     * @Desc:后台展示图表信息的数据接口,最多展示90天的数据
     * @throws Exception
     * @return:void
     * @author: renmeng  
     * @date: 2016年1月21日 下午4:34:36
     */
    @URI(YYPConstant.SERVER.REQUEST_IDOL_STATISTICINFO)
    public void idolStatisticInfo() throws Exception{
        HeartStatisticMarshal marshal = getStatisticInfo();
        logger.info("heartStatisticHandler idolStatisticInfo result=>{}",marshal);
        responseServer(YYPConstant.SERVER.RESPONSE_IDOL_STATISTICINFO,marshal);
        
    }
    
    /**
     * @Desc:兼容社区entproxy新加的接口
     * @throws Exception
     * @return:void
     * @author: renmeng  
     * @date: 2016年2月24日 下午3:28:29
     */
    @URI(YYPConstant.SERVER.REQUEST_IDOL_STATISTICINFO1)
    public void idolStatisticInfo1() throws Exception{
        HeartStatisticMarshal marshal = getStatisticInfo();
        logger.info("heartStatisticHandler idolStatisticInfo1 result=>{}",marshal);
        responseServer(YYPConstant.SERVER.RESPONSE_IDOL_STATISTICINFO1,marshal);
        
    }
    
    private HeartStatisticMarshal getStatisticInfo(){
        HeartStatisticMarshal marshal = new HeartStatisticMarshal();
        try {
            long uid = getRequest().popLong();
            String startDate = getRequest().popString();
            String endDate = getRequest().popString();
            logger.info("heartStatisticHandler idolStatisticInfo parameter=>uid:{},startDate:{},endDate:{}",uid,startDate,endDate);
            List<HeartStatistic> list = heartStatisticService.queryHeartStatistic(uid,startDate,endDate);
            JSONObject json = new JSONObject();
            json.put("uid", uid);
            json.put("nick",webdbService.getNice(String.valueOf(uid)));
            json.put("dataList", list);
            json.put("isDisplay",conf.isDisplay());
            marshal.result = 0;
            marshal.data = json.toJSONString();
        } catch (Exception e) {
            marshal.result = -1;
            marshal.data = "";
            logger.error("idolStatisticInfo error",e);    

        }
        return marshal;
    }
    
    
   
    
    
}
