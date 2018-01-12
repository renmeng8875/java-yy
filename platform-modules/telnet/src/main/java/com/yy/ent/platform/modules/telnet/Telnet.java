package com.yy.ent.platform.modules.telnet;

import java.lang.annotation.*;

/**
 * Telnet
 *
 * @author suzhihua
 * @date 2015/10/26.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Telnet {
    String name() default "";

    String desc() default "";

    String usage() default "";
}
