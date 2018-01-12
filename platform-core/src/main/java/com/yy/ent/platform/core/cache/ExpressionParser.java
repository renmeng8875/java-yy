package com.yy.ent.platform.core.cache;

/**
 * ExpressionParser，表达式操作类
 *
 * @author suzhihua
 * @date 2015/8/13.
 */
public interface ExpressionParser {
    public <T> T getValue(String el, Class<T> clazz);

    public <T> T getValue(String el, Class<T> clazz, Object result);
}
