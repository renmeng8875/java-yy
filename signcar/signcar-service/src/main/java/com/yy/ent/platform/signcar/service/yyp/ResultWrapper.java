package com.yy.ent.platform.signcar.service.yyp;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * ResultWrapper
 *
 * @author suzhihua
 * @date 2015/10/27.
 */
public class ResultWrapper {
    /**
     * 成功返回码
     */
    private static final int CODE_SUCCESS = 0;
    protected static final Logger logger = LoggerFactory.getLogger(ResultWrapper.class);

    /**
     * 包装json结果
     *
     * @param result
     * @param data
     * @param msg
     * @return
     */
    public static String wrapJsonResult(int result, Object data, String msg) {
        JSONObject json;
        if (data != null) {
            if (data instanceof JSONObject) {
                json = (JSONObject) data;
            } else {
                json = new JSONObject();
                json.put("data", data);
            }
        } else {
            json = new JSONObject();
        }
        json.put("result", result);
        if (msg != null) json.put("msg", msg);
        String resultJson = json.toJSONString();
        int length = resultJson.length();
        if (length > 2000) {
            logger.info("resultJson too long,size:" + length);
        } else {
            logger.info("resultJson:" + resultJson);
        }
        return resultJson;
    }

    /**
     * 包装json成功结果
     *
     * @param data
     * @return
     */
    public static String wrapJsonResultSuccess(Object data) {
        return wrapJsonResult(CODE_SUCCESS, data, null);
    }

    public static String wrapJsonResultError(int result, Object data, String msg) {
        return wrapJsonResult(result, data, msg);
    }

   
}
