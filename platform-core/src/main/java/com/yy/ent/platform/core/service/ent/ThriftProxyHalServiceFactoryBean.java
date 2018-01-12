package com.yy.ent.platform.core.service.ent;

import com.yy.ent.clients.thrift.service.ThriftProxyHalService;
import com.yy.ent.commons.halbproxy.HALBProxy;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.core.io.Resource;

/**
 * ThriftProxyHalService
 *
 * @author qinbo
 * @date 2015/7/23.
 */
public class ThriftProxyHalServiceFactoryBean implements FactoryBean<ThriftProxyHalService> {
    public ThriftProxyHalService getObject() throws Exception {
        ThriftProxyHalService ThriftProxyHalService = new ThriftProxyHalService();
        //System.out.println(BeanFactory.getBeanByClass(DaemonClientService.class));
        ThriftProxyHalService.init(strategy, period, dbConfigPath.getURL().getPath(), accessPolicy);
        return ThriftProxyHalService;
    }

    public Class<ThriftProxyHalService> getObjectType() {
        return ThriftProxyHalService.class;
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
