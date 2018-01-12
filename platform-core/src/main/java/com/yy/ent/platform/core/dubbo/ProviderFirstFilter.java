package com.yy.ent.platform.core.dubbo;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.rpc.*;
import com.yy.ent.platform.core.exception.BaseException;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

/**
 * ProviderFilter
 *
 * @author suzhihua
 * @date 2015/9/11.
 */
@Activate(group = Constants.PROVIDER, order = -1000000)
public class ProviderFirstFilter implements Filter {
    private static final Logger logger = LoggerFactory.getLogger(ProviderFirstFilter.class);

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        long begin = System.currentTimeMillis();
        Thread thread = Thread.currentThread();
        String oldName = thread.getName();
        String method = invocation.getMethodName();
        String clazz = invoker.getInterface().getSimpleName();
        try {
            String randomId = invocation.getAttachment(ConsumerFirstFilter.RANDOM_ID);
            if (StringUtils.isBlank(randomId)) {
                randomId = RandomStringUtils.randomAlphanumeric(10);
            }
            thread.setName(method + "-" + randomId);
            logger.info(">>> invoke method:{}#{}, args:{}", clazz, method, Arrays.toString(invocation.getArguments()));
            return invoker.invoke(invocation);
        } catch (Throwable e) {
            logger.error("unknown invoke method:{}#{} error", clazz, method, e);
            return new RpcResult(new BaseException(BaseException.CODE_UN_KNOWN, e.getMessage()));
        } finally {
            logger.info("<<< invoke method:{}#{}, time: {} ms", clazz, method, System.currentTimeMillis() - begin);
            thread.setName(oldName);
        }
    }
}
