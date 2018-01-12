package com.yy.ent.platform.core.mybatis;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * DbHash，注解确认dbindex类型，dbkey直接使用参数值
 *
 * @author suzhihua
 * @date 2015/8/10.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface DbDetermine {
    /**
     * @return
     * @see RoutingDbIndex#getDbDetermineByType(String)
     */
    String type() default IDbDetermine.TYPE_DEFAULT;
}
