package com.yy.ent.platform.core.web.filter;

import com.yy.ent.platform.core.service.ent.MetricsService;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * ServletControllerContextFilter
 *
 * @author suzhihua
 * @date 2015/7/23.
 */
public class ServletControllerContextFilter implements Filter {
    protected Logger logger = LoggerFactory.getLogger(ServletControllerContextFilter.class);
    private static MetricsService metricsService;

    public static void setMetricsService(MetricsService metricsService) {
        ServletControllerContextFilter.metricsService = metricsService;
    }

    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        long begin = System.currentTimeMillis();
        Thread thread = Thread.currentThread();
        String oldName = thread.getName();

        HttpServletRequest httpServletRequest = (HttpServletRequest) req;
        HttpServletResponse httpServletResponse = (HttpServletResponse) resp;
        String servletPath = httpServletRequest.getServletPath();
        try {
            String randomId = RandomStringUtils.randomAlphanumeric(10);
            thread.setName(servletPath + "-" + randomId);
            logger.info(">>> invoke url:{}", servletPath);
            ServletControllerContext.init(httpServletRequest, httpServletResponse, randomId);
            chain.doFilter(req, resp);
        } finally {
            long end = System.currentTimeMillis() - begin;
            if (metricsService != null) {
                int code = ServletControllerContext.getException() == null ? MetricsService.CODE_SUCCESS : MetricsService.CODE_ERROR;
                metricsService.report("webInvoke", servletPath.replaceAll("/","_"), end, code);
            }
            logger.info("<<< invoke url:{}, time: {} ms", servletPath, end);
            thread.setName(oldName);
            ServletControllerContext.destroy();
        }
    }

    public void init(FilterConfig config) throws ServletException {

    }

}
