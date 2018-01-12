package com.yy.ent.platform.core.service.ent;

import com.yy.ent.commons.halbproxy.HALBProxy;
import com.yy.ent.external.service.UdbServiceAgentHalbService;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.core.io.Resource;

/**
 * UdbServiceAgentHalbService
 *
 * @author suzhihua
 * @date 2015/7/23.
 */
public class UdbServiceAgentHalbServiceFactoryBean implements FactoryBean<UdbServiceAgentHalbService> {

    public UdbServiceAgentHalbService getObject() throws Exception {
        UdbServiceAgentHalbService udbServiceAgentHalbService = new UdbServiceAgentHalbService();
        udbServiceAgentHalbService.init(strategy, period, cfgPath.getURL().getPath(), accessPolicy, countInterval);
        return udbServiceAgentHalbService;
    }

    public Class<UdbServiceAgentHalbService> getObjectType() {
        return UdbServiceAgentHalbService.class;
    }

    public boolean isSingleton() {
        return true;
    }

    private HALBProxy.Strategy strategy;
    private long period;
    private Resource cfgPath;
    private int accessPolicy;
    private int countInterval;

    public void setStrategy(HALBProxy.Strategy strategy) {
        this.strategy = strategy;
    }

    public void setPeriod(long period) {
        this.period = period;
    }

    public void setCfgPath(Resource cfgPath) {
        this.cfgPath = cfgPath;
    }

    public void setAccessPolicy(int accessPolicy) {
        this.accessPolicy = accessPolicy;
    }

    public void setCountInterval(int countInterval) {
        this.countInterval = countInterval;
    }
}
