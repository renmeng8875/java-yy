package com.yy.ent.platform.core.dubbo;

import com.google.common.base.Splitter;
import com.yy.ent.platform.core.util.HostInfoUitl;
import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.TypedStringValue;
import org.springframework.beans.factory.support.ManagedMap;
import org.springframework.util.Assert;

import java.util.Collections;
import java.util.List;

/**
 * SpringProtocolConfig
 *
 * @author suzhihua
 * @date 2015/10/13.
 */
public class SpringProtocolConfig implements BeanFactoryPostProcessor, InitializingBean {
    private List<String> idsList;

    public void setIds(String ids) {
        idsList = Splitter.onPattern(",|;").trimResults().omitEmptyStrings().splitToList(ids);
    }

    public void setIdsList(List<String> idsList) {
        this.idsList = Collections.unmodifiableList(idsList);
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        BeanDefinition beanDefinition;
        for (String id : idsList) {
            beanDefinition = beanFactory.getBeanDefinition(id);
            PropertyValue propertyValue = beanDefinition.getPropertyValues().getPropertyValue("parameters");
            ManagedMap parameters;
            if (propertyValue != null) {
                parameters = (ManagedMap) propertyValue.getValue();
                addProperty(parameters);
            } else {
                parameters = new ManagedMap();
                addProperty(parameters);
                beanDefinition.getPropertyValues().addPropertyValue("parameters", parameters);
            }
        }
    }

    protected void addProperty(ManagedMap parameters) {
        parameters.put("hostinfo", new TypedStringValue(HostInfoUitl.getHostInfo(), String.class));
        parameters.put("hostid", new TypedStringValue(HostInfoUitl.getHostId(), String.class));
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(idsList, "idsList can't be null");
    }
}
