package com.yy.ent.platform.modules.telnet;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;

/**
 * InvokeTelnetHandler
 *
 * @author suzhihua
 * @date 2015/10/21.
 */
@Telnet(name = "invoke", desc = "invoke class.method(args)")
public class InvokeTelnetHandler implements ITelnetHandler {
    private static final Logger logger = LoggerFactory.getLogger(InvokeTelnetHandler.class);

    @Override
    public String exec(Channel channel, String message) {
        try {
            return invoke(message);
        } catch (Throwable e) {
            return e.toString();
        }
    }

    private String invoke(String message) throws Exception {
        String service = null;
        int i = message.indexOf("(");
        if (i < 0 || !message.endsWith(")")) {
            return "Invalid parameters, format: service.method(args)";
        }
        String method = message.substring(0, i).trim();
        String args = message.substring(i + 1, message.length() - 1).trim();
        i = method.lastIndexOf("#");
        if (i < 0) {
            i = method.lastIndexOf(".");
        }
        service = method.substring(0, i).trim();
        method = method.substring(i + 1).trim();
        JSONArray list;
        try {
            list = (JSONArray) JSON.parse("[" + args + "]");
        } catch (Throwable t) {
            return "Invalid json argument, cause: " + t.getMessage();
        }
        Class<?> clazz = null;
        Object bean = null;
        try {
            clazz = Class.forName(service);
            bean = TelnetService.beanFactory.getBean(clazz);
        } catch (Exception e) {
            //ignore
        }
        if (bean == null) {
            try {
                bean = TelnetService.beanFactory.getBean(service);
            } catch (Exception e) {
                //ignore
            }
        }
        if (bean != null) {
            clazz = bean.getClass();
        }
        Object invoke = null;
        Throwable throwable = null;
        for (Method method1 : clazz.getMethods()) {
            //此处只是简单地匹配方法名及个数
            if (method1.getName().equals(method) && Modifier.isPublic(method1.getModifiers()) && method1.getParameterTypes().length == list.size()) {
                try {
                    Type[] parameterTypes = method1.getGenericParameterTypes();
                    int length = parameterTypes.length;
                    Object[] params = new Object[length];
                    for (int j = 0; j < length; j++) {
                        if (list.get(j).getClass() == parameterTypes[j]) {
                            params[j] = list.get(j);
                        } else {
                            params[j] = JSON.parseObject(JSON.toJSONString(list.get(j)), parameterTypes[j]);
                        }
                    }
                    long begin = System.currentTimeMillis();
                    logger.info("invoke " + clazz + "#" + method1.getName() + ",args" + JSON.toJSONString(params));
                    invoke = method1.invoke(bean, params);
                    logger.info("invoke time: {} ms", System.currentTimeMillis() - begin);
                    if (method1.getReturnType() != Void.class) {
                        return JSON.toJSONString(invoke);
                    } else {
                        return "OK";
                    }
                } catch (Throwable e) {
                    if (e instanceof InvocationTargetException)
                        e = ((InvocationTargetException) e).getTargetException();
                    logger.warn("invoke error", e);
                    throwable = e;
                }
            }
        }

        if (throwable != null) {
            return throwable.toString();
        } else {
            return "method not found";
        }
    }
}
