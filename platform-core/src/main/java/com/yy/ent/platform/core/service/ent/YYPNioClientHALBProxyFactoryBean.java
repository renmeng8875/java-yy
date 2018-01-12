package com.yy.ent.platform.core.service.ent;

import com.yy.ent.clients.daemon.DaemonClientService;
import com.yy.ent.clients.halb.proxy.YYPNioClientHALBProxy;
import com.yy.ent.commons.halbproxy.HALBProxy;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;

/**
 * WebdbHalbService
 *
 * @author qinbo
 * @date 2015/7/23.
 */
public class YYPNioClientHALBProxyFactoryBean implements FactoryBean<YYPNioClientHALBProxy> {
    public YYPNioClientHALBProxy getObject() throws Exception {
        YYPNioClientHALBProxy proxy = new YYPNioClientHALBProxy(strategy, period, dbConfigPath.getURL().getPath(), clientService);
        proxy.initDaemonConfig();
        return proxy;
    }

    public Class<YYPNioClientHALBProxy> getObjectType() {
        return YYPNioClientHALBProxy.class;
    }

    public boolean isSingleton() {
        return true;
    }

    private HALBProxy.Strategy strategy;
    private long period;
    private Resource dbConfigPath;

    @Autowired
    private DaemonClientService clientService;

    public void setStrategy(HALBProxy.Strategy strategy) {
        this.strategy = strategy;
    }

    public void setPeriod(long period) {
        this.period = period;
    }

    public void setDbConfigPath(Resource dbConfigPath) {
        this.dbConfigPath = dbConfigPath;
    }

    public void setClientService(DaemonClientService clientService) {
        this.clientService = clientService;
    }
}
