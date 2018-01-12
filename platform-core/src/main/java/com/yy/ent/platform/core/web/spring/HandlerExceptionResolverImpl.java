package com.yy.ent.platform.core.web.spring;

import com.alibaba.dubbo.rpc.RpcException;
import com.alibaba.fastjson.JSONObject;
import com.yy.ent.platform.core.exception.BaseException;
import com.yy.ent.platform.core.web.filter.ServletControllerContext;
import com.yy.ent.platform.core.web.util.HttpUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;

/**
 * HandlerExceptionResolverImpl
 *
 * @author suzhihua
 * @date 2015/7/22.
 */
public class HandlerExceptionResolverImpl implements HandlerExceptionResolver {
    protected Logger logger = LoggerFactory.getLogger(HandlerExceptionResolverImpl.class);

    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        response.setStatus(HttpServletResponse.SC_OK);
        ModelAndView modelAndView = new ModelAndView();
        String servletPath = request.getServletPath().toLowerCase();
        logger.warn("error: " + servletPath, ex);
        BaseException exception = getBaseException(ex);
        ServletControllerContext.setException(exception);
        request.setAttribute("exception", exception);
        if (servletPath.endsWith(".json")) {
            JSONObject result = HttpUtil.wrapJsonResultError(exception);
            HttpUtil.renderJson(result.toJSONString());
        } else if (BaseException.CODE_NO_LOGIN == exception.getErrorCode()) {
            //没登录
            modelAndView.setViewName("/error/login");
            return modelAndView;
        } else {
            //默认html
            modelAndView.setViewName("/error/500");
        }
        return modelAndView;
    }

    /**
     * 包装异常
     *
     * @param ex
     * @return
     */
    private BaseException getBaseException(Exception ex) {
        while (ex instanceof InvocationTargetException) {
            ex = (Exception) ((InvocationTargetException) ex).getTargetException();
        }
        if (ex instanceof BaseException) {
            return (BaseException) ex;
        }
        if (ex instanceof RpcException) {
            int code = ((RpcException) ex).getCode();
            logger.warn("RpcException code:{}", code);
            if (code != 0) {
                return new BaseException(BaseException.CODE_RPC_ERROR, "服务异常", ex);
            }
            Exception cause = (Exception) ex.getCause();
            if (cause != null && cause instanceof BaseException) {
                return (BaseException) cause;
            }
            return new BaseException(BaseException.CODE_RPC_ERROR, "服务异常", ex);
        }
        if (!(ex instanceof BaseException)) {
            String message = ex.getMessage();
            if (StringUtils.isNotBlank(message)) {
                ex = new BaseException(BaseException.CODE_UN_KNOWN, message, ex);
            } else
                ex = new BaseException(BaseException.CODE_UN_KNOWN, "未知错误", ex);
        }
        return (BaseException) ex;
    }
}

