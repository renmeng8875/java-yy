package com.yy.ent.platform.core.web.spring;

import com.yy.ent.platform.core.service.ent.MetricsService;
import com.yy.ent.platform.core.spring.SpringHolder;
import com.yy.ent.platform.core.web.filter.ServletControllerContextFilter;

/**
 * SpringApplicationHolder
 *
 * @author suzhihua
 * @date 2015/10/16.
 */
public class SpringApplicationContextHolder extends SpringHolder {
    private static MetricsService metricsService;

    public void setMetricsService(MetricsService metricsService) {
        SpringApplicationContextHolder.metricsService = metricsService;
        ServletControllerContextFilter.setMetricsService(metricsService);
    }

    public static MetricsService getMetricsService() {
        return metricsService;
    }
}
