package com.yy.ent.platform.core.dubbo;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.rpc.*;
import com.alibaba.dubbo.rpc.service.GenericService;
import com.yy.ent.platform.core.exception.BaseException;
import com.yy.ent.platform.core.service.ent.MetricsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ProviderFilter
 *
 * @author suzhihua
 * @date 2015/9/11.
 */
@Activate(group = Constants.PROVIDER, order = 1000000)
public class ProviderLastFilter implements Filter {
    private static final Logger logger = LoggerFactory.getLogger(ProviderLastFilter.class);
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
        long begin = System.currentTimeMillis();
        String method = invocation.getMethodName();
        String clazz = invoker.getInterface().getSimpleName();
        Result result;
        boolean isSuccess = true;
        try {
            logger.info("> invoke method:{}#{}", clazz, method);
            result = invoker.invoke(invocation);
            if (result.hasException()) {
                isSuccess = false;
                if (GenericService.class != invoker.getInterface()) {
                    Throwable exception = result.getException();
                    logger.error("invoke method:{}#{} error", clazz, method, exception);
                    if ((exception instanceof RpcException) || (exception instanceof BaseException)) {
                        return result;
                    }
                    // 否则，BaseException
                    return new RpcResult(new BaseException(BaseException.CODE_UN_KNOWN, exception.getMessage()));
                }
            }
        } finally {
            long end = System.currentTimeMillis() - begin;
            if (metricsService != null) {
                int code = isSuccess ? MetricsService.CODE_SUCCESS : MetricsService.CODE_ERROR;
                metricsService.report("dubboProviderInvoke", invoker.getInterface().getName() + "#" + method, end, code);
            }
            logger.info("< invoke method:{}#{}, time: {} ms", clazz, method, end);
        }
        return result;
    }
}
