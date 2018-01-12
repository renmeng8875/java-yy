package com.yy.ent.platform.core.service.ent;

import com.yy.ent.clients.daemon.DaemonClientService;
import com.yy.ent.clients.halb.proxy.YYPClientHALBProxy;
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
public class YYPClientHALBProxyFactoryBean implements FactoryBean<YYPClientHALBProxy> {
    public YYPClientHALBProxy getObject() throws Exception {
        YYPClientHALBProxy yypClientHALBProxy = new YYPClientHALBProxy(strategy, period, dbConfigPath.getURL().getPath(), clientService);
        yypClientHALBProxy.initDaemonConfig();
        return yypClientHALBProxy;
    }

    public Class<YYPClientHALBProxy> getObjectType() {
        return YYPClientHALBProxy.class;
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
