package com.yy.ent.platform.core.service.ent;

import com.yy.ent.commons.htmlized.Htmlized;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;

/**
 * HtmlizedFactoryBean
 *
 * @author suzhihua
 * @date 2015/8/25.
 */
public class HtmlizedFactoryBean implements FactoryBean<Htmlized>, InitializingBean, DisposableBean {
    private Resource configPath;
    private Htmlized htmlized;

    public void setConfigPath(Resource configPath) {
        this.configPath = configPath;
    }

    public Htmlized getObject() throws Exception {
        if (htmlized == null) afterPropertiesSet();
        return htmlized;
    }

    public Class<?> getObjectType() {
        return Htmlized.class;
    }

    public boolean isSingleton() {
        return true;
    }

    public void afterPropertiesSet() throws Exception {
        Assert.notNull(configPath, "configPath can't be null");
        if (htmlized == null) {
            htmlized = new Htmlized(configPath.getURL().getPath());
            htmlized.start();
        }
    }

    public void destroy() throws Exception {
        if (htmlized != null && htmlized.isStarted()) {
            htmlized.stop();
        }
    }
}
