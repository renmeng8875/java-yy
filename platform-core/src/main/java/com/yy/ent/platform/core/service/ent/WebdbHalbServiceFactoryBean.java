package com.yy.ent.platform.core.service.ent;

import com.yy.ent.commons.halbproxy.HALBProxy;
import com.yy.ent.external.service.WebdbHalbService;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.core.io.Resource;

/**
 * WebdbHalbService
 *
 * @author suzhihua
 * @date 2015/7/23.
 */
public class WebdbHalbServiceFactoryBean implements FactoryBean<WebdbHalbService> {
    public WebdbHalbService getObject() throws Exception {
        WebdbHalbService webdbHalbService = new WebdbHalbService();
        webdbHalbService.init(strategy, period, dbConfigPath.getURL().getPath(), accessPolicy);
        return webdbHalbService;
    }

    public Class<WebdbHalbService> getObjectType() {
        return WebdbHalbService.class;
    }

    public boolean isSingleton() {
        return true;
    }

    private HALBProxy.Strategy strategy;
    private long period;
    private Resource dbConfigPath;
    private int accessPolicy;

    public void setStrategy(HALBProxy.Strategy strategy) {
        this.strategy = strategy;
    }

    public void setPeriod(long period) {
        this.period = period;
    }

    public void setDbConfigPath(Resource dbConfigPath) {
        this.dbConfigPath = dbConfigPath;
    }

    public void setAccessPolicy(int accessPolicy) {
        this.accessPolicy = accessPolicy;
    }
}
