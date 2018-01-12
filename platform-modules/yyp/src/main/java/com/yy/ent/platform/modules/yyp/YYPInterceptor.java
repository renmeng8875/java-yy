package com.yy.ent.platform.modules.yyp;

import com.yy.ent.cherrio.handle.RequestFilter;
import com.yy.ent.cherrio.interceptor.Interceptor;
import com.yy.ent.cherrio.invocation.HandlerInvocation;
import com.yy.ent.cherrio.yyp.YYProtoHeader;
import com.yy.ent.commons.protopack.base.Header;
import com.yy.ent.platform.core.exception.BaseException;
import com.yy.ent.platform.core.service.ent.MetricsService;
import com.yy.ent.platform.core.spring.SpringHolder;
import com.yy.ent.srv.protocol.MobileComboHeader;
import com.yy.ent.srv.protocol.YYComboHeader;
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
    protected BaseYYPHandler baseYYPHandler = new BaseYYPHandler();
    private MetricsService metricsService = SpringHolder.getBean(MetricsService.class);

    @Override
    public String intercept(HandlerInvocation invocation) throws Exception {
        String uri = "unknown";
        Header header = RequestFilter.getRequest().getHeader();
        try {
            uri = header.uri();
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
            int errorCode;
            if (e instanceof BaseException) {
                errorCode = ((BaseException) e).getErrorCode();
                errorCode = (errorCode == 0 ? Result.ERROR_UNKNOWN : code);
            } else {
                errorCode = Result.ERROR_UNKNOWN;
            }
            String errorMsg = e.getMessage();
            errorHandle(header, errorCode, errorMsg == null ? "" : errorMsg);
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

    private void errorHandle(Header header, int code, String msg) throws Exception {
        try {
            Result result = Result.newResult(code);
            result.put("msg", msg);
            if (header instanceof MobileComboHeader) {
                baseYYPHandler.responseMobile(result);
            } else if (header instanceof YYComboHeader) {
                baseYYPHandler.responsePC(result);
            } else if (header instanceof YYProtoHeader) {
                baseYYPHandler.responseServer(result);
            }
        } catch (Throwable e2) {
            logger.warn("yyp errorHandle", e2);
        }
    }
}
