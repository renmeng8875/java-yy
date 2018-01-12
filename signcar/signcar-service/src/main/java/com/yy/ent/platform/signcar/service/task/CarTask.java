package com.yy.ent.platform.signcar.service.task;

import com.yy.ent.platform.core.spring.SpringHolder;
import com.yy.ent.platform.signcar.common.mongodb.ActivityConfig;
import com.yy.ent.platform.signcar.service.activity.ActivityConfigCallback;
import com.yy.ent.platform.signcar.service.activity.ActivityConfigService;
import com.yy.ent.platform.signcar.service.car.ActivityService;
import com.yy.ent.platform.signcar.service.car.NotifyService;
import com.yy.ent.platform.signcar.service.car.RankService;

/**
 * CarTask
 *
 * @author suzhihua
 * @date 2015/11/18.
 */
public class CarTask {
    static ActivityConfigService activityConfigService;

    public static void init() {
        activityConfigService = SpringHolder.getBean(ActivityConfigService.class);
        //Task.execute(CarTask.class, "doNotityAll", "2/30 * * ? * *", true);//定时跑更新频道相关信息
        //Task.execute(CarTask.class, "cacheRank", "15/30 * * ? * *", true);//定时跑更新榜单
        //Task.execute(CarTask.class, "closeAll", "0 0/1 * ? * *", true);//定时检测活动是否结束
        //Task.execute(CarTask.class, "openAll", "0 0/1 * ? * *", true);//定时检测活动是否开启
    }


    /**
     * 定时跑更新频道相关信息
     */
    public static void doNotityAll() {
        final NotifyService service = SpringHolder.getBean(NotifyService.class);
        activityConfigService.execute("car", new ActivityConfigCallback() {
            @Override
            public void doInActivityConfig(ActivityConfig activityConfig) throws Exception {
                service.doNotifyAll(activityConfig.getActivityId());
            }
        });
    }

    /**
     * 定时跑更新榜单
     */
    public static void cacheRank() {
        final RankService service = SpringHolder.getBean(RankService.class);
        activityConfigService.execute("car", new ActivityConfigCallback() {
            @Override
            public void doInActivityConfig(ActivityConfig activityConfig) throws Exception {
                service.setCacheRank(activityConfig.getActivityId());
            }
        });
    }

    /**
     * 关闭活动条
     */
    public static void closeAll() throws Exception {
        final ActivityService service = SpringHolder.getBean(ActivityService.class);
        activityConfigService.execute("car", new ActivityConfigCallback() {
            @Override
            public void doInActivityConfig(ActivityConfig activityConfig) throws Exception {
                service.closeAll(activityConfig.getActivityId());
            }
        });

    }

    /**
     * 打开活动条
     */
    public static void openAll() throws Exception {
        final ActivityService service = SpringHolder.getBean(ActivityService.class);
        activityConfigService.execute("car", new ActivityConfigCallback() {
            @Override
            public void doInActivityConfig(ActivityConfig activityConfig) throws Exception {
                service.openAll(activityConfig.getActivityId());
            }
        });
    }
    
}
