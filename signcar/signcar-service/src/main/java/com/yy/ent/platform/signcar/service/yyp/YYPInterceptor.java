package com.yy.ent.platform.signcar.service.yyp;

import com.yy.ent.cherrio.handle.RequestFilter;
import com.yy.ent.cherrio.interceptor.Interceptor;
import com.yy.ent.cherrio.invocation.HandlerInvocation;
import com.yy.ent.platform.core.service.ent.MetricsService;
import com.yy.ent.platform.core.spring.SpringHolder;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * YYPInterceptor
 *
 * @author suzhihua
 * @date 2015/10/28.
 */
public class YYPInterceptor implements Interceptor {
    protected Logger logger = LoggerFactory.getLogger(YYPInterceptor.class);
    private MetricsService metricsService = SpringHolder.getBean(MetricsService.class);

    @Override
    public String intercept(HandlerInvocation invocation) throws Exception {
        String uri = "unknown";
        try {
            uri = RequestFilter.getRequest().getHeader().uri();
        } catch (Exception e) {
            logger.warn("get uri error");
        }
        long begin = System.currentTimeMillis();
        Thread thread = Thread.currentThread();
        String oldName = thread.getName();

        String invoke = null;
        int code = MetricsService.CODE_SUCCESS;
        try {
            thread.setName(RandomStringUtils.randomAlphanumeric(10) + "_" + uri);
            logger.debug(">>> invoke {}", uri);
            invoke = invocation.invoke();
        } catch (Throwable e) {
            code = MetricsService.CODE_ERROR;
            logger.error("yyp error", e);
        } finally {
            long end = System.currentTimeMillis() - begin;
            logger.debug("<<< invoke {} time: {} ms", uri, end);
            Integer result = Result.RESULT.get();
            if (result != null && result.intValue() != 0) {
                code = 9000 + result;
                Result.RESULT.set(0);
            }
            metricsService.report("yypInvoke", uri, end, code);
            thread.setName(oldName);
        }
        return invoke;
    }
}
