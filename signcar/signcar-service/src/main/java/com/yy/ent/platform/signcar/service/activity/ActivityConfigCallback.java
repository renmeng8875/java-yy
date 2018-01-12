package com.yy.ent.platform.signcar.service.activity;

import com.yy.ent.platform.signcar.common.mongodb.ActivityConfig;

/**
 * ActivityConfigCallback
 *
 * @author suzhihua
 * @date 2015/11/23.
 */
public interface ActivityConfigCallback {
    void doInActivityConfig(ActivityConfig activityConfig) throws Exception;
}
