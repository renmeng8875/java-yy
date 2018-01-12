package com.yy.ent.platform.core.cache;

import org.aspectj.lang.ProceedingJoinPoint;

import java.lang.reflect.Method;

/**
 * ExpressionRoot
 * 方法参数第一个：p0,param0,a0,args0,定义的参数类型 依此类推
 *
 * @author suzhihua
 * @date 2015/8/13.
 */
public class ExpressionRoot {
    //当前执行方法
    private Method method;
    //当前执行方法名
    private String methodName;
    //当前执行目标，即类this指向
    private Object target;
    //当前执行目标类名
    private Class<?> targetClass;
    //当前执行目标原始类名
    private Class<?> sourceClass;
    //当前执行目标原始类名精简
    private String sourceSimpleClass;
    //当前方法形参列表
    private Object[] args;
    //切入点，可以取相关所有变量
    private ProceedingJoinPoint call;

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Object getTarget() {
        return target;
    }

    public void setTarget(Object target) {
        this.target = target;
    }

    public Class<?> getTargetClass() {
        return targetClass;
    }

    public void setTargetClass(Class<?> targetClass) {
        this.targetClass = targetClass;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }

    public ProceedingJoinPoint getCall() {
        return call;
    }

    public void setCall(ProceedingJoinPoint call) {
        this.call = call;
    }

    public Class<?> getSourceClass() {
        return sourceClass;
    }

    public void setSourceClass(Class<?> sourceClass) {
        this.sourceClass = sourceClass;
    }

    public String getSourceSimpleClass() {
        return sourceSimpleClass;
    }

    public void setSourceSimpleClass(String sourceSimpleClass) {
        this.sourceSimpleClass = sourceSimpleClass;
    }
}
