package com.yy.ent.platform.modules.telnet;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.env.Environment;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * TelnetScanner
 *
 * @author suzhihua
 * @date 2015/10/26.
 */
public class TelnetScanner extends ClassPathBeanDefinitionScanner {
    private static final Logger logger = LoggerFactory.getLogger(TelnetScanner.class);

    private Map<String, Class<?>> beanDefinitionMap = new HashMap<String, Class<?>>();

    public TelnetScanner(BeanDefinitionRegistry registry) {
        super(registry);
    }

    public TelnetScanner(BeanDefinitionRegistry registry, boolean useDefaultFilters) {
        super(registry, useDefaultFilters);
    }

    public TelnetScanner(BeanDefinitionRegistry registry, boolean useDefaultFilters, Environment environment) {
        super(registry, useDefaultFilters, environment);
    }

    @Override
    protected Set<BeanDefinitionHolder> doScan(String... basePackages) {
        Set<BeanDefinitionHolder> beanDefinitionHolders = super.doScan(basePackages);
        initBean(beanDefinitionHolders);
        return beanDefinitionHolders;
    }

    private void initBean(Set<BeanDefinitionHolder> beanDefinitionHolders) {
        Class<?> aClass;
        String name;
        Telnet annotation;
        try {
            for (BeanDefinitionHolder beanDefinitionHolder : beanDefinitionHolders) {
                String beanClassName = beanDefinitionHolder.getBeanDefinition().getBeanClassName();
                aClass = Class.forName(beanClassName);
                annotation = aClass.getAnnotation(Telnet.class);
                name = annotation.name();
                if (!StringUtils.isAlphanumeric(name)) {
                    throw new IllegalArgumentException("name must be alpha or numeric!");
                }
                beanDefinitionMap.put(name.toUpperCase(), aClass);
            }
        } catch (Exception e) {
            logger.error("TelnetScanner error", e);
        }
    }

    public Map<String, Class<?>> getBeanDefinitionMap() {
        return Collections.unmodifiableMap(beanDefinitionMap);
    }
}
