package com.yy.ent.platform.core.web.filter;

import com.yy.ent.platform.core.exception.BaseException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * ServletContext
 *
 * @author suzhihua
 * @date 2015/7/23.
 */
public class ServletControllerContext {
    private static ThreadLocal<Context> context = new ThreadLocal<Context>() {
        @Override
        protected Context initialValue() {
            return new Context();
        }
    };

    public static void destroy() {
        context.remove();
    }

    public static void init(HttpServletRequest request, HttpServletResponse response, String randomId) throws ServletException {
        Context context = ServletControllerContext.context.get();
        context.setRequest(request);
        context.setResponse(response);
        context.setRandomId(randomId);
    }

    public static HttpServletRequest getRequest() {
        return context.get().getRequest();
    }

    public static HttpServletResponse getResponse() {
        return context.get().getResponse();
    }

    public static String getRandomId() {
        return context.get().getRandomId();
    }

    public static BaseException getException() {
        return context.get().getException();
    }

    public static void setException(BaseException exception) {
        context.get().setException(exception);
    }

    public static class Context {
        private HttpServletRequest request;
        private HttpServletResponse response;
        private String randomId;
        private BaseException exception;

        public String getRandomId() {
            return randomId;
        }

        public void setRandomId(String randomId) {
            this.randomId = randomId;
        }

        public HttpServletRequest getRequest() {
            return request;
        }

        protected void setRequest(HttpServletRequest request) {
            this.request = request;
        }

        public HttpServletResponse getResponse() {
            return response;
        }

        protected void setResponse(HttpServletResponse response) {
            this.response = response;
        }

        public BaseException getException() {
            return exception;
        }

        public void setException(BaseException exception) {
            this.exception = exception;
        }
    }
}