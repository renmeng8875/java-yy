package com.yy.ent.platform.core.web.spring;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.yy.ent.platform.core.exception.BaseException;
import com.yy.ent.platform.core.web.util.HttpUtil;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.converter.HttpMessageNotWritableException;

import java.io.IOException;

/**
 * JsonHttpMessageConverter
 *
 * @author suzhihua
 * @date 2015/7/22.
 */
public class JsonHttpMessageConverter extends FastJsonHttpMessageConverter {

    /**
     * 统一包装返回json
     *
     * @param obj
     * @param outputMessage
     * @throws IOException
     * @throws HttpMessageNotWritableException
     */
    @Override
    protected void writeInternal(Object obj, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
        JSONObject json;
        if (obj instanceof BaseException) {
            json = HttpUtil.wrapJsonResultError((BaseException) obj);
        } else {
            json = HttpUtil.wrapJsonResultSuccess(obj);
        }
        super.writeInternal(json, outputMessage);
    }
}
