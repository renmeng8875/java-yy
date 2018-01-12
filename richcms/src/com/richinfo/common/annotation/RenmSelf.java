package com.richinfo.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = {ElementType.METHOD,ElementType.TYPE,ElementType.ANNOTATION_TYPE})
public @interface RenmSelf 
{
	String methodDesc() default "开发此方法的程序员很懒，未曾为此方法添加描述！";  
	String[] recordParams() default {}; 
}
