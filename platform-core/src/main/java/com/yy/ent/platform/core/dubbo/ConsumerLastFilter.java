package com.yy.ent.platform.core.dubbo;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.rpc.*;
import com.yy.ent.platform.core.service.ent.MetricsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ConsumerFirstFilter
 *
 * @author suzhihua
 * @date 2015/9/11.
 */
@Activate(group = Constants.CONSUMER, order = 1000000)
public class ConsumerLastFilter implements Filter {
    private static final Logger logger = LoggerFactory.getLogger(ConsumerLastFilter.class);
    private MetricsService metricsService;

    /**
     * 此处没有使用dubbo spi，
     * 使用spring注入
     * 需要有一个MetricsService类为metricsService的bean，如没有则无法注入同时会打错误日志，但不影响使用
     *
     * @param metricsService
     */
    public void setMetricsService(MetricsService metricsService) {
        this.metricsService = metricsService;
    }

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        String method = invocation.getMethodName();
        String clazz = invoker.getInterface().getSimpleName();
        long begin = System.currentTimeMillis();
        Result result = null;
        try {
            logger.info("> invoke method:{}#{}", clazz, method);
            result = invoker.invoke(invocation);
        } finally {
            long end = System.currentTimeMillis() - begin;
            if (metricsService != null) {
                int code = (result != null && !result.hasException()) ? MetricsService.CODE_SUCCESS : MetricsService.CODE_ERROR;
                metricsService.report("dubboConsumerInvoke", invoker.getInterface().getName() + "#" + method, end, code);
            }
            logger.info("< invoke method:{}#{}, time: {} ms", clazz, method, end);
        }
        return result;
    }
}
