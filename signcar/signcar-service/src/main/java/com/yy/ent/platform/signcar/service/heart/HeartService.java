package com.yy.ent.platform.signcar.service.heart;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.ScanParams;
import redis.clients.jedis.ScanResult;

import com.yy.ent.external.service.WebdbHalbService;
import com.yy.ent.platform.core.redis.RedisTemplate;
import com.yy.ent.platform.signcar.common.TaskbarInfo;
import com.yy.ent.platform.signcar.common.constant.RedisKeyConstant;
import com.yy.ent.platform.signcar.common.mongodb.HeartTotal;
import com.yy.ent.platform.signcar.common.util.HashUtil;
import com.yy.ent.platform.signcar.common.util.TimeUtil;
import com.yy.ent.platform.signcar.repository.mongo.HeartAddDetailRepository;
import com.yy.ent.platform.signcar.repository.mongo.HeartConsumeDetailRepository;
import com.yy.ent.platform.signcar.repository.mongo.HeartTotalRepository;
import com.yy.ent.platform.signcar.service.common.BaseService;
import com.yy.ent.platform.signcar.service.common.CommonService;
import com.yy.ent.platform.signcar.service.common.WebdbService;
import com.yy.ent.platform.signcar.service.log.Log;

@Service
public class HeartService extends BaseService  implements InitializingBean{
    public static final int MAX_HEART_NUM = 100;//最大心数
    private static final int TIME_EXPIRE = 3600 * 9;//定时器3点执行，即存12小时过期数据
    private static final int HASH_NUM = 2000;//日志相关key hash个数
    private ExecutorService handleLogsPool = Executors.newFixedThreadPool(2);//处理推荐日志线程数，不能设置太大，redis扛不住
    @Autowired
    private HeartTotalRepository heartTotalRepository;
    @Autowired
    private HeartAddDetailRepository heartAddDetailRepository;
    @Autowired
    private HeartConsumeDetailRepository heartConsumeDetailRepository;
    @Autowired
    private RedisTemplate redisTemplate;
    @Value("${HeartService.minTime}")
    private int minTime = 5;
    @Value("${HeartService.maxTime}")
    private int maxTime = 60;
    @Value("${HeartService.tip}")
    private String tip;
    @Autowired
    private CommonService commonService;
    @Autowired
    private WebdbService webdbService;
    
    @Autowired
    private WebdbHalbService webdbHalbService;

    @Value("${HeartService.isOpen}")
    private Boolean isOpen;
    
    @Value("${closeTime}")
    private String closeTimeStr;

    private Set<Date[]> closeTimeSet;
    
    private Set<String> shortChidSet; //短频道号集合
    
    private Set<String> longChidSet; //长频道号集合
    

    public HeartService()  {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                handleLogsPool.shutdown();
            }
        });
       
    }
    
    @Override
	public void afterPropertiesSet() throws Exception {
    	 initChid();
    	 initCloseTime();
    	 //test();
	}
    
    private void initCloseTime() throws ParseException{
    	closeTimeSet = new HashSet<Date[]>();
    	String[] ss = closeTimeStr.split(",");
    	for(String s:ss){
    		String[] timestr = s.split("_");
    		Date[] dateProid = new Date[2];
    		dateProid[0] = TimeUtil.parseTimeDate(timestr[0]);
    		dateProid[1] = TimeUtil.parseTimeDate(timestr[1]);
    		closeTimeSet.add(dateProid);
    	}
    	
    }
    
    
    private boolean isInCloseTime(Date now){
    	
    	for(Date[] dateProid:closeTimeSet){
    		if(now.after(dateProid[0])&&now.before(dateProid[1])){
    			return true;
    		}
    	}
    	return false;
    }
    
    private void initChid() {
    	shortChidSet = new HashSet<String>();
    	longChidSet = new HashSet<String>();
        ClassPathResource resource = new ClassPathResource("blackList.ini");
        File f = null;
        List<String> lines = new ArrayList<String>();
		try {
			f = resource.getFile();
			lines = FileUtils.readLines(f);
		} catch (IOException e1) {
			logger.warn("init blackList.ini error",e1);
		}
       
        for(String line:lines)
        {
        	shortChidSet.add(StringUtils.trim(line));
        	try {
				longChidSet.add(webdbHalbService.getSIdByAId(line));
			} catch (Exception e) {
				logger.warn("getSIdByAId error",e);
			}
        }
        logger.info("shortChidSet:{}",shortChidSet);
        logger.info("longChidSet:{}",longChidSet);
    }
    
    public boolean isDisplay(String topid){
    	Date now = new Date();
    	if(longChidSet.contains(topid)) {
    		if(isInCloseTime(now)){
    			return false;
    		}
    	}
    	return true;
    }

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    public void setTime(int minTime, int maxTime) {
        logger.info("minTime={},maxTime={}", minTime, maxTime);
        if (minTime > 0 && maxTime > 0 && maxTime > minTime) {
            this.minTime = minTime;
            this.maxTime = maxTime;
        } else {
            logger.warn("time error:minTime={},maxTime={}", minTime, maxTime);
        }
    }

    /**
     * 添加流水号
     *
     * @param uid
     * @param serial
     * @return 若不存在则添加返回true，已经存在返回false
     */
    public boolean addSerial(long uid, String serial) {
        final String key = RedisKeyConstant.HEART_CONSUME_SERIAL.getKey(uid, serial);

        return redisTemplate.execute(new RedisTemplate.JedisAction<Boolean>() {
            @Override
            public Boolean action(Jedis jedis) {
                Long setnx = jedis.setnx(key, commonService.getYYYYMMDDHHMMSS());
                boolean result = (setnx == 1);
                if (result) {
                    jedis.expire(key, 60 * 60 * 24);//一天过期
                }
                return result;
            }
        });
    }

    /**
     * 清除流水号
     *
     * @param uid
     * @param serial
     */
    public void clearSerial(long uid, String serial) {
        final String key = RedisKeyConstant.HEART_CONSUME_SERIAL.getKey(uid, serial);
        redisTemplate.del(key);
    }

    /**
     * 增加心数量
     *
     * @param fansUid
     * @param
     * @return 返回当前用户心数
     */
    public int addHeart(long fansUid, int num, String serial) {
        logger.info("addHeart fansUid={},num={},serial={}", fansUid, num, serial);
        Date now = commonService.newDate();
        HeartTotal heartTotal = heartTotalRepository.incrHeartTotalNum(fansUid, num, now);
        if (heartTotal.getNum() > MAX_HEART_NUM) {
            logger.info("fansUid {},over {},num={}", fansUid, MAX_HEART_NUM, heartTotal.getNum());
            heartTotal = heartTotalRepository.incrHeartTotalNum(fansUid, MAX_HEART_NUM - heartTotal.getNum(), now);
        }
        String key = RedisKeyConstant.HEART_TOTAL.getKey(commonService.getYYYYMMDD(), hashLong(fansUid));
        redisTemplate.hset(key, String.valueOf(fansUid), heartTotal.getNum().toString());
        heartAddDetailRepository.insert(fansUid, num, now, serial);
        return heartTotal.getNum();
    }

    public int addHeart(long fansUid, int num) {
        return addHeart(fansUid, num, "");
    }

    /**
     * 送心
     *
     * @param fansUid
     * @param idolUid
     * @param num
     * @return 返回当前用户心数, <0为送心失败
     */
    public int consumeHeart(long fansUid, long idolUid, int num) {
        logger.info("consumeHeart fansUid={},idolUid={},num={}", fansUid, idolUid, num);
        int heartTotalNum = heartTotalRepository.getHeartTotalNum(fansUid);
        if (heartTotalNum < num) {
            logger.info("1.fansUid {},idolUid {},num {},heartTotalNum {}", fansUid, idolUid, num, heartTotalNum);
            return -1;
        }
        Date now = commonService.newDate();
        HeartTotal heartTotal = heartTotalRepository.incrHeartTotalNum(fansUid, -num, now);
        if (heartTotal.getNum() < 0) {
            logger.info("2.fansUid {},idolUid {},num {},heartTotalNum {}", fansUid, idolUid, num, heartTotalNum);
            heartTotalRepository.incrHeartTotalNum(fansUid, num, now);
            return -1;
        }
        updateRedis(idolUid, fansUid, num, heartTotal.getNum());
        heartConsumeDetailRepository.insert(fansUid, idolUid, num, now);
        return heartTotal.getNum();
    }

    /**
     * 初始化任务条信息
     *
     * @param idolUid
     * @param fansUid
     * @return
     */
    public TaskbarInfo getTaskbarInfo(final long idolUid, final long fansUid, final boolean isGetWebdbInfo) {
        TaskbarInfo taskbarInfo = new TaskbarInfo();
        String yyyymmdd = commonService.getYYYYMMDD();
        taskbarInfo.setFansHeartConsumeNum(getFansHeartConsumeNum(fansUid, yyyymmdd));
        taskbarInfo.setFansHeartNum(getFansHeartNum(fansUid));
        if (idolUid > 0) {
            taskbarInfo.setIdolFansNum(getIdolFansNum(idolUid, yyyymmdd));
            taskbarInfo.setIdolHeartNum(getIdolHeartNum(idolUid, yyyymmdd));
            if (isGetWebdbInfo) {
                String idolUidStr = String.valueOf(idolUid);
                taskbarInfo.setIdolHead(webdbService.getHead(idolUidStr));
                taskbarInfo.setIdolNick(webdbService.getNice(idolUidStr));
            }
        }
        return taskbarInfo;
    }

    private void updateRedis(final long idolUid, final long fansUid, final int consumeNum, final Integer num) {
        redisTemplate.execute(new RedisTemplate.JedisActionNoResult() {
            @Override
            public void action(Jedis jedis) {
                String yyyymmdd = commonService.getYYYYMMDD();
                int hash = hashLong(fansUid);
                Pipeline pipelined = jedis.pipelined();
                //用户每日对某主播送心总数（生成推荐日志也是用这个）
                String key = RedisKeyConstant.HEART_CONSUME_TOTAL.getKey(yyyymmdd, hash);
                pipelined.hincrBy(key, idolUid + "_" + fansUid, consumeNum);
                //用户拥有心记录
                key = RedisKeyConstant.HEART_TOTAL.getKey(yyyymmdd, hash);
                pipelined.hset(key, String.valueOf(fansUid), num.toString());
                //用户每日送心总数
                key = RedisKeyConstant.HEART_CONSUME_FANSUID_TOTAL_NUM.getKey(yyyymmdd, hash);
                pipelined.hincrBy(key, String.valueOf(fansUid), consumeNum);
                //主播每日收心粉丝列表
                key = RedisKeyConstant.HEART_CONSUME_FANSUID_SET.getKey(yyyymmdd, idolUid);
                pipelined.sadd(key, String.valueOf(fansUid));
                //主播每日收心总数
                key = RedisKeyConstant.HEART_CONSUME_TOTAL_NUM.getKey(yyyymmdd);
                pipelined.hincrBy(key, String.valueOf(idolUid), consumeNum);
                pipelined.sync();
            }
        });

    }

    /**
     * 取用户当前拥有心数
     *
     * @param fansUid
     * @return
     */
    public int getFansHeartNum(long fansUid) {
        int heartTotalNum = heartTotalRepository.getHeartTotalNum(fansUid);
        return heartTotalNum;
    }

    /**
     * 取用户对某主播今天送心数
     *
     * @param idolUid
     * @return
     */
    public int getFansHeartConsumeNum(long idolUid, long fansUid, String yyyymmdd) {
        String key = RedisKeyConstant.HEART_CONSUME_TOTAL.getKey(yyyymmdd, hashLong(fansUid));
        String str = redisTemplate.hget(key, idolUid + "_" + fansUid);
        return str == null ? 0 : Integer.parseInt(str);
    }

    /**
     * 取用户今天送心数
     *
     * @param fansUid
     * @return
     */
    public int getFansHeartConsumeNum(long fansUid, String yyyymmdd) {
        String key = RedisKeyConstant.HEART_CONSUME_FANSUID_TOTAL_NUM.getKey(yyyymmdd, hashLong(fansUid));
        String str = redisTemplate.hget(key, String.valueOf(fansUid));
        return str == null ? 0 : Integer.parseInt(str);
    }

    /**
     * 取主播今天收到的心数
     *
     * @param idolUid
     * @return
     */
    public long getIdolHeartNum(long idolUid, String yyyymmdd) {
        String key = RedisKeyConstant.HEART_CONSUME_TOTAL_NUM.getKey(yyyymmdd);
        String str = redisTemplate.hget(key, String.valueOf(idolUid));
        return str == null ? 0 : Long.parseLong(str);
    }

    /**
     * 取主播今天收到心的粉丝数
     *
     * @param idolUid
     * @return
     */
    public long getIdolFansNum(long idolUid, String yyyymmdd) {
        String key = RedisKeyConstant.HEART_CONSUME_FANSUID_SET.getKey(yyyymmdd, idolUid);
        Long scard = redisTemplate.scard(key);
        return scard == null ? 0 : scard.longValue();
    }

    /**
     * 清除过期数据
     */
    public void clearData() {
        redisTemplate.execute(new RedisTemplate.JedisActionNoResult() {
            @Override
            public void action(Jedis jedis) {
                //取昨天的
                String yyyymmdd = commonService.getYYYYMMDD(-1);
                Pipeline pipelined = jedis.pipelined();
                String key;
                for (int hash = 0; hash < HASH_NUM; hash++) {
                    //用户每日对某主播送心总数（生成推荐日志也是用这个）
                    key = RedisKeyConstant.HEART_CONSUME_TOTAL.getKey(yyyymmdd, hash);
                    pipelined.expire(key, TIME_EXPIRE);
                    //用户拥有心记录
                    key = RedisKeyConstant.HEART_TOTAL.getKey(yyyymmdd, hash);
                    pipelined.expire(key, TIME_EXPIRE);
                    //用户每日送心总数
                    key = RedisKeyConstant.HEART_CONSUME_FANSUID_TOTAL_NUM.getKey(yyyymmdd, hash);
                    pipelined.expire(key, TIME_EXPIRE);
                }

                //主播每日收心总数
                key = RedisKeyConstant.HEART_CONSUME_TOTAL_NUM.getKey(yyyymmdd);
                pipelined.expire(key, TIME_EXPIRE);
                pipelined.sync();
                //主播每日收心粉丝列表
                key = RedisKeyConstant.HEART_CONSUME_FANSUID_SET.getKey(yyyymmdd, "*");
                logger.info("begin HEART_CONSUME_FANSUID_SET,keys match:{}", key);
//                String script = "local r= redis.call(KEYS[1],KEYS[2]);for i,v in pairs(r) do redis.call(KEYS[3],v,KEYS[4]) end;return #r";
//                Object eval = jedis.eval(script, 4, "keys", key, "expire", String.valueOf(TIME_EXPIRE));
                int num = 0;
                final ScanParams params = new ScanParams();
                params.count(50000);
                params.match(key);
                ScanResult<String> scan;
                String cursor = "0";
                List<String> list;
                int currentHandle = 0;
                logger.info("begin HEART_CONSUME_FANSUID_SET");
                do {
                    scan = jedis.scan(cursor, params);
                    cursor = scan.getStringCursor();
                    list = scan.getResult();
                    if (list != null) {
                        logger.info("currentHandle:{},cursor:{},list.size:{}", currentHandle, cursor, list.size());
                        currentHandle += list.size();
                        for (String s : list) {
                            pipelined.expire(s, TIME_EXPIRE);
                            num++;
                            if (num % 10000 == 0) {
                                pipelined.sync();
                            }
                        }
                        pipelined.sync();
                    }
                } while (!"0".equals(cursor));//redis.cursor为0时表示结束
                pipelined.sync();
                logger.info("end HEART_CONSUME_FANSUID_SET expire keys:{}", num);
            }
        });
    }

    /**
     * 生成推荐日志
     */
    public void recommendLog() {
        //执行前一天的统计
        final String yyyymmdd = commonService.getYYYYMMDD(-1);
        recommendLog(yyyymmdd);
    }

    public void recommendLog(final String yyyymmdd) {
        logger.info("begin recommendLog:{}", yyyymmdd);
        //取出今天送心用户-主播关系记录及数据
        for (int hash = 0; hash < HASH_NUM; hash++) {
            handleLogs(yyyymmdd, hash);
        }
        logger.info("end recommendLog:{}", yyyymmdd);
    }

    private void handleLogs(final String day, final int hash) {
        handleLogsPool.execute(new Runnable() {
            @Override
            public void run() {
                Thread thread = Thread.currentThread();
                String oldName = thread.getName();
                thread.setName("handleLogs" + "_" + hash + "_" + RandomStringUtils.randomAlphanumeric(10));
                try {
                    String key = RedisKeyConstant.HEART_CONSUME_TOTAL.getKey(day, hash);
                    Map<String, String> consumeTotals = redisTemplate.hgetAll(key);
                    logger.info("consumeTotals.size={}", consumeTotals.size());
                    if (consumeTotals.size() == 0) {
                        return;
                    }

                    key = RedisKeyConstant.HEART_TOTAL.getKey(day, hash);
                    Map<String, String> heartTotals = redisTemplate.hgetAll(key);
                    logger.info("heartTotals.size={}", heartTotals.size());

                    key = RedisKeyConstant.HEART_CONSUME_FANSUID_TOTAL_NUM.getKey(day, hash);
                    Map<String, String> consumeFansUidTotals = redisTemplate.hgetAll(key);
                    logger.info("consumeFansUidTotals.size={}", consumeFansUidTotals.size());

                    Map<String, Integer> heartAllTotals = new HashMap<String, Integer>(consumeFansUidTotals.size());
                    Integer tmp1;
                    Integer tmp2;
                    for (Map.Entry<String, String> entry : consumeFansUidTotals.entrySet()) {
                        tmp1 = getInt(heartTotals.get(entry.getKey()));
                        tmp2 = getInt(entry.getValue());
                        heartAllTotals.put(entry.getKey(), tmp1 + tmp2);
                    }
                    logger.info("heartAllTotals.size={}", heartAllTotals.size());

                    heartTotals = null;
                    consumeFansUidTotals = null;

                    Integer heartConsume;
                    String[] split;
                    for (Map.Entry<String, String> entry : consumeTotals.entrySet()) {
                        key = entry.getKey();//idolUid_fansUid
                        heartConsume = Integer.parseInt(entry.getValue());
                        split = key.split("_");
                        handleLog(day, split[0], split[1], heartConsume, getInt(heartAllTotals.get(split[1])));
                    }
                    consumeTotals = null;
                } catch (Throwable e) {
                    logger.error("handleLogs_" + hash + " error", e);
                } finally {
                    logger.info("handleLogs_" + hash + " end");
                    thread.setName(oldName);
                }
            }
        });
    }

    private void handleLog(String day, String idolUid, String fansUid, int heartConsume, int heartAllTotal) {
        int time = calcTime(heartConsume, heartAllTotal);
        Log.recommendLog(fansUid, idolUid, time, day);
    }

    /**
     * 当前消费心数，心包总数
     *
     * @param heartConsume
     * @param heartAllTotal
     * @return
     */
    public int calcTime(int heartConsume, int heartAllTotal) {
        double rate = heartConsume * heartConsume / 1.0 / heartAllTotal;
        double _time = rate * (maxTime - minTime) / 100 + minTime;
        int time = (int) Math.round(_time);
        time = time > maxTime ? maxTime : time;//正常不会到这里
        time = time < 0 ? 0 : time;//正常不会到这里
        return time * 60;
    }

    private Integer getInt(String s) {
        return StringUtils.isNotBlank(s) ? Integer.parseInt(s) : 0;
    }

    private int getInt(Integer i) {
        return i != null ? i.intValue() : 0;
    }

    private int hashLong(long num) {
        return HashUtil.hashLong(num, HASH_NUM);
    }
    
    public Boolean getIsOpen() {
        return isOpen;
    }

    public void setIsOpen(boolean isOpen) {
        this.isOpen = isOpen;
    }
    
    
    public static void main(String[] args) throws ParseException {
    	Date now1 = TimeUtil.parseTimeDate("2016-12-12 20:10:00");
    	Date now2 = TimeUtil.parseTimeDate("2016-12-13 20:10:05");
    	System.out.println(now2.after(now1));
	}

	
}
