package com.yy.ent.platform.core.web.util;

import com.alibaba.fastjson.JSONObject;
import com.yy.ent.platform.core.exception.BaseException;
import com.yy.ent.platform.core.web.filter.ServletControllerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * JsonUtil
 *
 * @author suzhihua
 * @date 2015/7/22.
 */
public class HttpUtil {
    /**
     * 成功返回码
     */
    private static final int CODE_SUCCESS = 0;
    protected static final Logger logger = LoggerFactory.getLogger(HttpUtil.class);

    /**
     * 包装json结果
     *
     * @param result
     * @param obj
     * @param msg
     * @return
     */
    public static JSONObject wrapJsonResult(int result, Object obj, String msg) {
        JSONObject json;
        if (obj instanceof JSONObject) {
            json = (JSONObject) obj;
        } else {
            json = new JSONObject();
            json.put("data", obj);
        }

        if(json.get("code") == null) {
            json.put("code", result);
        }
        json.put("msg", msg);
        return json;
    }

    /**
     * 包装jsond成功结果
     *
     * @param obj
     * @return
     */
    public static JSONObject wrapJsonResultSuccess(Object obj) {
        return wrapJsonResult(CODE_SUCCESS, obj, null);
    }

    /**
     * 包装json失败结果
     *
     * @param e
     * @return
     */
    public static JSONObject wrapJsonResultError(BaseException e) {
        return wrapJsonResult(e.getErrorCode(), null, e.getMessage());
    }


    /**
     * 渲染HTML
     *
     * @param html HTML内容
     */
    public static void renderHtml(String html) {
        render(html, "text/html;charset=UTF-8");
    }

    /**
     * 渲染text
     *
     * @param text
     */
    public static void renderText(String text) {
        render(text, "text/plain;charset=UTF-8");
    }

    /**
     * 渲染json
     *
     * @param json
     */
    public static void renderJson(String json) {
        render(json, "application/json;charset=UTF-8");
    }

    /**
     * 渲染json
     *
     * @param json
     */
    public static void renderJsonp(String callBackKey, String json) {
        if(callBackKey == null || callBackKey.trim().equals("")) {
            render(json, "application/json;charset=UTF-8");
        } else {
            render(callBackKey + "(" + json + ");", "application/javascript;charset=UTF-8");
        }
    }

    /**
     * 渲染
     *
     * @param text
     * @param contentType
     */
    public static void render(String text, String contentType) {
        if (text == null) {
            text = "";
        }
        HttpServletResponse response = getResponse();
        response.setContentType(contentType);
        try {
            response.getWriter().write(text);
            response.flushBuffer();
        } catch (IOException e) {
            logger.error("渲染出错", e);
        }
    }

    /**
     * 取response
     *
     * @return
     */
    public static HttpServletResponse getResponse() {
        return ServletControllerContext.getResponse();
    }

    /**
     * 取request
     *
     * @return
     */
    public static HttpServletRequest getRequest() {
        return ServletControllerContext.getRequest();
    }

}
