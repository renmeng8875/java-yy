package com.yy.ent.platform.core.service.ent;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.core.io.Resource;

import com.yy.ent.commons.halbproxy.HALBProxy;
import com.yy.ent.external.service.EntProxyHalbService;

/**
 * WebdbHalbService
 *
 * @author qinbo
 * @date 2015/7/23.
 */
public class EntProxyHalbServiceFactoryBean implements FactoryBean<EntProxyHalbService> {
    public EntProxyHalbService getObject() throws Exception {
        EntProxyHalbService entProxyHalbService = new EntProxyHalbService();
        //System.out.println(BeanFactory.getBeanByClass(DaemonClientService.class));
        entProxyHalbService.init(strategy, period, dbConfigPath.getURL().getPath(), accessPolicy);
        return entProxyHalbService;
    }

    public Class<EntProxyHalbService> getObjectType() {
        return EntProxyHalbService.class;
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
