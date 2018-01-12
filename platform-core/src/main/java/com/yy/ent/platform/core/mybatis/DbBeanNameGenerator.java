package com.yy.ent.platform.core.mybatis;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.AnnotationBeanNameGenerator;

/**
 * DbBeanNameGenerator，生成bean名字，用于多数据源时bean名字冲突解决
 *
 * @author suzhihua
 * @date 2015/8/10.
 */
public class DbBeanNameGenerator extends AnnotationBeanNameGenerator {
    private String dbName;

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    @Override
    public String generateBeanName(BeanDefinition definition, BeanDefinitionRegistry registry) {
        return dbName + "_" + super.generateBeanName(definition, registry);
    }
}
