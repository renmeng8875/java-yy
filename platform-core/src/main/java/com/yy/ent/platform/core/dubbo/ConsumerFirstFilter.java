package com.yy.ent.platform.core.dubbo;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.rpc.*;
import com.yy.ent.platform.core.web.filter.ServletControllerContext;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ConsumerFirstFilter
 *
 * @author suzhihua
 * @date 2015/9/11.
 */
@Activate(group = Constants.CONSUMER, order = -1000000)
public class ConsumerFirstFilter implements Filter {
    private static final Logger logger = LoggerFactory.getLogger(ConsumerFirstFilter.class);
    public static final String RANDOM_ID = "randomId";

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        String method = invocation.getMethodName();
        String clazz = invoker.getInterface().getSimpleName();

        String randomId = ServletControllerContext.getRandomId();
        if (StringUtils.isBlank(randomId)) {
            randomId = RandomStringUtils.randomAlphanumeric(10);
        }
        RpcContext.getContext().setAttachment(RANDOM_ID, randomId);
        long begin = System.currentTimeMillis();
        Result result;
        try {
            logger.info(">> invoke method:{}#{}", clazz, method);
            result = invoker.invoke(invocation);
        } finally {
            logger.info("<< invoke method:{}#{}, time: {} ms", clazz, method, System.currentTimeMillis() - begin);
        }
        return result;
    }
}
