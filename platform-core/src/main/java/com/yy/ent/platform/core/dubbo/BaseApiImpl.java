package com.yy.ent.platform.core.dubbo;

import com.alibaba.dubbo.rpc.RpcContext;
import com.alibaba.dubbo.rpc.RpcException;
import com.alibaba.dubbo.rpc.service.GenericException;
import com.alibaba.dubbo.rpc.service.GenericService;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * BaseApiImpl,泛化实现，为了dubbo能透传异常
 *
 * @author suzhihua
 * @date 2015/8/25.
 */
public class BaseApiImpl implements GenericService {
    protected Logger logger = LoggerFactory.getLogger(BaseApiImpl.class);
    private Class<? extends BaseApiImpl> clazz = getClass();

    @Override
    public Object $invoke(String method, String[] parameterTypes, Object[] args) throws GenericException {
        long begin = System.currentTimeMillis();
        Exception ex = null;
        Thread thread = Thread.currentThread();
        String oldName = thread.getName();
        try {
            String randomId = RpcContext.getContext().getAttachment(ConsumerFirstFilter.RANDOM_ID);
            if (StringUtils.isBlank(randomId)) {
                randomId = RandomStringUtils.randomAlphanumeric(10);
            }
            thread.setName(method + "-" + randomId);
            Class[] methodParameterTypes = getMethodParameterTypes(parameterTypes);
            logger.info(">>> invoke method:{}#{}({}), args:{}", clazz.getName(), method, Arrays.toString(methodParameterTypes), Arrays.toString(args));
            Method methodToUse = clazz.getMethod(method, methodParameterTypes);
            Object result = methodToUse.invoke(this, args);
            return result;
        } catch (InvocationTargetException e) {
            ex = (Exception) e.getTargetException();
            while (ex instanceof InvocationTargetException) {
                ex = (Exception) ((InvocationTargetException) ex).getTargetException();
            }
        } catch (Exception e) {
            ex = e;
        } finally {
            logger.info("<<< invoke method:{}, time: {} ms", method, System.currentTimeMillis() - begin);
            thread.setName(oldName);
        }
        logger.warn("error invoke method:{}#{}, args:{}", clazz.getName(), method, Arrays.toString(args), ex);
        throw new RpcException(ex);
    }

    private Class[] getMethodParameterTypes(String[] parameterTypes) throws ClassNotFoundException {
        Class[] classes = null;
        if (parameterTypes != null) {
            classes = new Class[parameterTypes.length];
            int i = 0;
            for (String parameterType : parameterTypes) {
                classes[i++] = Class.forName(parameterType);
            }
        }
        return classes;
    }
}
