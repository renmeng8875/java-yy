package com.yy.ent.platform.signcar.service.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yy.ent.platform.signcar.common.mongodb.ActivityConfig;
import com.yy.ent.platform.signcar.repository.mongo.ActivityConfigRepository;
import com.yy.ent.platform.signcar.service.car.ActivityService;
import com.yy.ent.platform.signcar.service.common.BaseService;
import com.yy.ent.platform.signcar.service.common.CommonService;

@Service
public class ActivityConfigService extends BaseService {
    @Autowired
    private ActivityConfigRepository activityConfigRepository;
    @Autowired
    private ActivityService activityService;
    @Autowired
    private CommonService commonService;

    private volatile Map<Long, ActivityConfig> activityConfigIdMap = new HashMap<Long, ActivityConfig>();
    private volatile Map<String, ArrayList<ActivityConfig>> activityConfigNameList = new HashMap<String, ArrayList<ActivityConfig>>();
 

    public void reload() {
        init();
    }

    public void init() {
        //Set<String> news = activityLoad();
        //activityInit(news);

    }

   

    public ActivityConfig getActivityConfig(long activityId) {
        return activityConfigIdMap.get(activityId);
    }

    public void execute(String name, ActivityConfigCallback callback) {
        List<ActivityConfig> activityConfigs = activityConfigNameList.get(name);
        if (activityConfigs == null) {
            return;
        }
        for (ActivityConfig activityConfig : activityConfigs) {
            try {
                callback.doInActivityConfig(activityConfig);
            } catch (Exception e) {
                logger.error("doInActivityConfig error,name={},activityId={}", name, activityConfig.getActivityId(), e);
            }
        }
    }

    /**
     * 取当前活动id，防止没配置活动起不来，不存在时默认返回-1
     *
     * @param name
     * @return
     */
    public Long getCurrentActivityId(String name) {
        Long activityId = -1L;
        List<ActivityConfig> activityConfigs = activityConfigNameList.get(name);
        if (CollectionUtils.isEmpty(activityConfigs)) {
            return activityId;
        }
        for (ActivityConfig activityConfig : activityConfigs) {
            activityId = activityConfig.getActivityId();
            if (isActivityOpen(activityId)) {
                return activityId;
            }
        }
        return activityId;
    }

    /**
     * 取当前活动id，防止没配置活动起不来，不存在时默认返回-1
     *
     * @return
     */
    public Long getCurrentActivityId() {
        Long activityId = -1L;
        for (Long _activityId : activityConfigIdMap.keySet()) {
            if (isActivityOpen(_activityId)) {
                return _activityId;
            }
        }
        return activityId;
    }

    /**
     * 取当第一个活动id，防止没配置活动起不来，不存在时默认返回-1
     *
     * @param name
     * @return
     */
    public Long getFirstActivityId(String name) {
        Long activityId = -1L;
        List<ActivityConfig> activityConfigs = activityConfigNameList.get(name);
        if (CollectionUtils.isNotEmpty(activityConfigs)) {
            activityId = activityConfigs.get(0).getActivityId();
        }
        return activityId;
    }

    /**
     * 取当最近一个活动id，防止没配置活动起不来，不存在时默认返回-1
     *
     * @param name
     * @return
     */
    public Long getLastActivityId(String name) {
        Long activityId = -1L;
        List<ActivityConfig> activityConfigs = activityConfigNameList.get(name);
        if (CollectionUtils.isEmpty(activityConfigs)) {
            return activityId;
        }
        //查找正运行的
        for (ActivityConfig activityConfig : activityConfigs) {
            activityId = activityConfig.getActivityId();
            if (isActivityOpen(activityId)) {
                return activityId;
            }
        }
        //找不到正在运行的，查找最近结束的
        if (activityId == -1) {
            int size = activityConfigs.size();
            for (int i = size - 1; i >= 0; i--) {
                activityId = activityConfigs.get(i).getActivityId();
                if (isActivityOver(activityId)) {
                    return activityId;
                }
            }
        }
        //还找不到默认返回第一个
        if (activityId == -1) {
            return activityConfigs.get(0).getActivityId();
        }

        return activityId;
    }

    /**
     * 活动结束
     *
     * @param delayedMinute 延时多少分钟内还不算结束
     * @return
     */
    public boolean isActivityOver(long activityId, int delayedMinute) {
        long now = commonService.currentTimeMillis() - delayedMinute * 60000;
        ActivityConfig activityConfig = activityConfigIdMap.get(activityId);
        long endTime = activityConfig.getEndTime().getTime();
        return now > endTime;
    }

    public boolean isActivityOver(long activityId) {
        long now = commonService.currentTimeMillis();
        ActivityConfig activityConfig = activityConfigIdMap.get(activityId);
        long endTime = activityConfig.getEndTime().getTime();
        return now > endTime;
    }

    /**
     * 旋风车队活动是否开启
     *
     * @return
     */
    public boolean isActivityOpen(long activityId) {
        long now = commonService.currentTimeMillis();
        ActivityConfig activityConfig = activityConfigIdMap.get(activityId);
        long endTime = activityConfig.getEndTime().getTime();
        long beginTime = activityConfig.getBeginTime().getTime();
        boolean result = now >= beginTime && now <= endTime;
        logger.debug("isActivityOpen={}", result);
        return result;
    }


    public void updateActivityConfig(ActivityConfig activityConfig) {
        activityConfigRepository.update(activityConfig);
    }
}
