package com.yy.ent.platform.core.cache;

import java.lang.annotation.*;

/**
 * CacheEvict，先执行方法体，满足条件才清除缓存
 *
 * @author suzhihua
 * @date 2015/8/13.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Inherited
@Documented
public @interface CacheEvict {
    /**
     * 键值名，可以用el表达式
     *
     * @return
     */
    String key() default "";

    /**
     * 条件符合才执行消除缓存，可以用el表达式
     *
     * @return
     */
    String condition() default "";

    /**
     * 类型
     *
     * @return
     * @see CacheManager
     */
    String type() default "";
}
