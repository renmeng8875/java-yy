package com.yy.ent.platform.core.cache;

import java.lang.annotation.*;

/**
 * Cacheable，先查找缓存，没有则执行方法体，满足条件才设置缓存
 *
 * @author suzhihua
 * @date 2015/8/13.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Inherited
@Documented
public @interface Cacheable {
    /**
     * 键值名，可以用el表达式
     *
     * @return
     */
    String key() default "";

    /**
     * 过期时间单位秒,-1是无限时间
     *
     * @return
     */
    int expire() default -1;

    /**
     * 条件符合才执行设置缓存，可以用el表达式
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
