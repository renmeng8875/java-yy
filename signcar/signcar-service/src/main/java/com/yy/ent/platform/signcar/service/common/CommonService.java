package com.yy.ent.platform.signcar.service.common;

import com.yy.ent.platform.core.redis.RedisTemplate;
import com.yy.ent.platform.signcar.common.constant.RedisKeyConstant;
import com.yy.ent.platform.signcar.common.util.TimeUtil;
import com.yy.ent.platform.signcar.service.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;

@Service
public class CommonService extends BaseService {
    protected static boolean isTest = false;

    @Autowired
    private RedisTemplate redisTemplate;

    static {
        String isTestStr = System.getProperty("isTest", "0");
        isTest = "1".equals(isTestStr) || "true".equalsIgnoreCase(isTestStr);

    }

    public Date newDate() {
        return new Date(currentTimeMillis());
    }

    public Calendar newCalendar() {
        Calendar instance = Calendar.getInstance();
        instance.setTimeInMillis(currentTimeMillis());
        return instance;
    }

    public long currentTimeMillis() {
        return isTest ? currentTimeMillisTest() : System.currentTimeMillis();
    }

    private long currentTimeMillisTest() {
        Date date;
        try {
            String sysTime = redisTemplate.get(RedisKeyConstant.SYS_DATE.getKey());
            date = TimeUtil.parseTimeDate(sysTime);
        } catch (Throwable e) {
            date = new Date();
        }
        return date.getTime();
    }

    public String clearTestDate() {
        redisTemplate.del(RedisKeyConstant.SYS_DATE.getKey());
        return getYYYYMMDDHHMMSS();
    }

    public String setTestDate(String str) {
        String oldDay = getYYYYMMDD();
        redisTemplate.set(RedisKeyConstant.SYS_DATE.getKey(), str);
        String newDay = getYYYYMMDD();
        Task.heartClearData();
        if (Integer.parseInt(newDay) > Integer.parseInt(oldDay)) {
            Task.reloadActivityConfigService();
            Task.heartRecommendLog();
        }
        return getYYYYMMDDHHMMSS();
    }

    public String addTestDay(int day) {
        Calendar instance = Calendar.getInstance();
        instance.setTimeInMillis(currentTimeMillis());
        instance.add(Calendar.DAY_OF_YEAR, day);
        String s = TimeUtil.formatTimeEx(instance.getTime());
        return setTestDate(s);
    }

    public String getYYYYMMDD() {
        Date date = new Date(currentTimeMillis());
        return TimeUtil.formatYYYYMMDD(date);
    }

    public String getYYYYMMDDHHMMSS() {
        Date date = new Date(currentTimeMillis());
        return TimeUtil.formatTimeEx(date);
    }

    public String getYYYYMMDD(int day) {
        Calendar instance = Calendar.getInstance();
        instance.setTimeInMillis(currentTimeMillis());
        instance.add(Calendar.DAY_OF_YEAR, day);
        return TimeUtil.formatYYYYMMDD(instance.getTime());
    }
}
