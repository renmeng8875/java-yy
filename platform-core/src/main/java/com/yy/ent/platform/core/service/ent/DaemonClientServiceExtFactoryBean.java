package com.yy.ent.platform.core.service.ent;

import com.yy.ent.commons.base.inject.BeanFactory;
import com.yy.ent.external.service.DaemonClientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.core.io.Resource;

/**
 * WebdbHalbService
 *
 * @author qinbo
 * @date 2015/7/23.
 */
public class DaemonClientServiceExtFactoryBean implements FactoryBean<DaemonClientService> {
    private Logger logger = LoggerFactory.getLogger(DaemonClientServiceExtFactoryBean.class);
    public DaemonClientService getObject() throws Exception {
        DaemonClientService daemonClientService = null;
        try {
            daemonClientService = new DaemonClientService(dbConfigPath.getURL().getPath());
        } catch (UnsatisfiedLinkError e) {
            logger.error("daemonClientService init error",e);
        }
        //将daemonClientService放到cherry 的BeanFactory，entproxy和yyp组件初始化时需要从cherry 的BeanFactory中拿daemonClientService
        BeanFactory.prepare(DaemonClientService.class.getName(), daemonClientService);
//        System.out.println("regist"+BeanFactory.getBeanByClass(DaemonClientService.class));
        return daemonClientService;
    }

    public Class<DaemonClientService> getObjectType() {
        return DaemonClientService.class;
    }

    public boolean isSingleton() {
        return true;
    }

    private Resource dbConfigPath;


    public void setDbConfigPath(Resource dbConfigPath) {
        this.dbConfigPath = dbConfigPath;
    }
}
