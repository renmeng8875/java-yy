package com.yy.ent.platform.signcar.service.task;

import com.yy.ent.platform.core.spring.SpringHolder;
import com.yy.ent.platform.core.util.FileUtil;
import com.yy.ent.platform.signcar.service.activity.ActivityConfigService;
import com.yy.ent.platform.signcar.service.heart.HeartNotifyService;
import com.yy.ent.platform.signcar.service.heart.HeartService;
import com.yy.ent.platform.signcar.service.heart.HeartStatisticService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import java.util.Properties;

/**
 * Task
 *
 * @author suzhihua
 * @date 2015/11/2.
 */
public class Task {
    protected static final Logger logger = LoggerFactory.getLogger(Task.class);
    protected static final String KEY_PREV = "task.";

    private static Properties properties;

    static {
        ClassPathResource resource = new ClassPathResource("application.properties");
        properties = FileUtil.loadResource(resource);

        try {
            ClassPathResource resource2 = new ClassPathResource("special.properties");
            Properties properties2 = FileUtil.loadResource(resource2);
            properties.putAll(properties2);
        } catch (Throwable e) {
            logger.warn("load special.properties error", e);
        }
    }

    public static void init() {
        Task.execute(Task.class, "heartClearData", "0 0 3 ? * *", true);
        Task.execute(Task.class, "heartRecommendLog", "0 0 2 ? * *", true);
        Task.execute(Task.class, "doNotifyConsumeHeart", "0/1 * * ? * *", true);

        Task.execute(Task.class, "doNotifyHeart", "0/1 * * ? * *", true);//兑心用，到时可以下掉

        Task.execute(Task.class, "doHeartStatistic", "0 30 1 ? * *", true);//每天凌晨1：30抓取昨天欢聚云的统计信息
        
        Task.execute(Task.class, "doNotifyFansIdolIntimacy", "0/1 * * ? * *", true);//通知粉丝主播亲密度,1s一次
    }

    public static void heartClearData() {
        HeartService service = SpringHolder.getBean(HeartService.class);
        service.clearData();
    }

    public static void heartRecommendLog() {
        HeartService service = SpringHolder.getBean(HeartService.class);
        service.recommendLog();
    }

    public static void doNotifyConsumeHeart() {
        HeartNotifyService service = SpringHolder.getBean(HeartNotifyService.class);
        service.doNotifyConsumeHeart();
    }
    public static void doNotifyFansIdolIntimacy() {
        HeartNotifyService service = SpringHolder.getBean(HeartNotifyService.class);
        service.doNotifyFansIdolIntimacy();
    }
    
    public static void doNotifyHeart() {
        HeartNotifyService service = SpringHolder.getBean(HeartNotifyService.class);
        service.doNotifyHeart();
    }

    public static void doHeartStatistic() {
        HeartStatisticService service = SpringHolder.getBean(HeartStatisticService.class);
        service.heartStatisticHandle();
    }

    /**
     * 定时跑更新活动
     */
    public static void reloadActivityConfigService() {
        ActivityConfigService service = SpringHolder.getBean(ActivityConfigService.class);
        service.reload();
    }
    

    /**
     * 执行
     *
     * @param methodName
     * @param cronExpresstion
     * @param isConf          是否读配置,key=KEY_PREV+methodName
     */
    public static void execute(Class clazz, String methodName, String cronExpresstion, boolean isConf) {
        if (isConf && !"1".equals(properties.get(KEY_PREV + methodName))) {
            logger.info("ignore Task {}", methodName);
            return;
        }
        logger.info("init Task {}", methodName);
        TaskScheduler.executeCronSchedule(clazz, methodName, cronExpresstion);
    }
}
