package com.yy.ent.platform.core.cache;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.lang.reflect.Method;

/**
 * Spel，spring表达式解释
 *
 * @author suzhihua
 * @date 2015/8/13.
 */
public class SpelParser implements ExpressionParser {
    private EvaluationContext context;
    private ProceedingJoinPoint call;

    public SpelParser(ProceedingJoinPoint call) {
        this.call = call;
    }

    public <T> T getValue(String el, Class<T> clazz) {
        return getValue(el, clazz, null);
    }

    public <T> T getValue(String el, Class<T> clazz, Object result) {
        if (!el.contains("#")) {
            return getDefaultValue(el, clazz);
        }
        if (context == null) {
            initContext();
        }
        if (result != null) context.setVariable("result", result);
        SpelExpressionParser parser = new SpelExpressionParser();
        Expression expression = parser.parseExpression(el);
        T _result = expression.getValue(context, clazz);
        return _result;
    }

    private <T> T getDefaultValue(String el, Class<T> clazz) {
        if (clazz == Boolean.class) {
            if ("".equals(el)) {
                return (T) Boolean.TRUE;
            }
            return (T) Boolean.valueOf(el);
        }
        return (T) el;
    }

    private void initContext() {
        ExpressionRoot root = new ExpressionRoot();
        Method method = getMethod(call);
        root.setMethod(method);
        root.setMethodName(method.getName());
        root.setTarget(call.getTarget());
        root.setTargetClass(call.getTarget().getClass());
        root.setSourceClass(method.getDeclaringClass());
        root.setSourceSimpleClass(method.getDeclaringClass().getSimpleName());
        root.setArgs(call.getArgs());
        root.setCall(call);
        context = new StandardEvaluationContext(root);
        int i = 0;
        String paramType;
        for (Object arg : call.getArgs()) {
            context.setVariable("p" + i, arg);
            context.setVariable("param" + i, arg);
            context.setVariable("a" + i, arg);
            context.setVariable("arg" + i, arg);
            paramType = method.getParameterTypes()[i].getSimpleName();
            context.setVariable(paramType.substring(0, 1).toLowerCase() + paramType.substring(1), arg);
            i++;
        }
    }

    private Method getMethod(ProceedingJoinPoint jp) {
        MethodSignature joinPointObject = (MethodSignature) jp.getSignature();
        Method method = joinPointObject.getMethod();
        return method;
    }
}
