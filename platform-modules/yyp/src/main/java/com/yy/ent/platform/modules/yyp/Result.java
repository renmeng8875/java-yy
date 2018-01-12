package com.yy.ent.platform.modules.yyp;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yy.ent.commons.protopack.base.Marshallable;
import com.yy.ent.commons.protopack.base.Pack;
import com.yy.ent.commons.protopack.base.Unpack;
import com.yy.ent.commons.protopack.marshal.StringMarshal;
import com.yy.ent.commons.protopack.marshal.UintMarshal;
import com.yy.ent.commons.protopack.util.Uint;
import com.yy.ent.srv.handler.MessageResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Result
 *
 * @author suzhihua
 * @date 2015/12/19.
 */
public class Result implements Marshallable {
    public static final int ERROR_ILLEGAL_ARGS = 102;
    public static final int ERROR_UNKNOWN = 101;

    public static final ThreadLocal<Integer> RESULT = new ThreadLocal<Integer>();

    protected static final Logger logger = LoggerFactory.getLogger(Result.class);
    private int result = 0;
    private Map<String, Object> data;
    private Object dataRoot;

    public static Result newResult() {
        return new Result();
    }

    public static Result newResult(String key, Object value) {
        Result result = new Result();
        result.put(key, value);
        return result;
    }

    public static Result newResult(Map<String, Object> data) {
        return new Result(data);
    }

    public static Result newResult(int result, Map<String, Object> data) {
        return new Result(result, data);
    }

    public static Result newResult(int result) {
        return new Result(result);
    }

    private Result() {
    }

    private Result(int result, Map<String, Object> data) {
        this.result = result;
        this.data = data;
    }

    private Result(Map<String, Object> data) {
        this.data = data;
    }

    public Object get(String key) {
        return data != null ? data.get(key) : null;
    }

    public boolean isSuccess() {
        return result == 0;
    }

    public Result put(String key, Object value) {
        if (data == null) {
            data = new HashMap<String, Object>();
        }
        data.put(key, value);
        return this;
    }

    public Object getDataRoot() {
        return dataRoot;
    }

    public Result setDataRoot(Object dataRoot) {
        this.dataRoot = dataRoot;
        return this;
    }

    public Result(int result) {
        this.result = result;
    }

    public int getResult() {
        return result;
    }

    public Result setResult(int result) {
        this.result = result;
        return this;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public Result setData(Map<String, Object> data) {
        this.data = data;
        return this;
    }

    public String toJSONString() {
        JSONObject jsonObject = new JSONObject();
        //jsonObject.put("result", result);
        if (dataRoot != null) {
            Object _data = JSON.toJSON(dataRoot);
            if (_data instanceof JSONObject) {
                jsonObject.putAll((JSONObject) _data);
            } else {
                jsonObject.put("main", _data);
            }
        }
        if (data != null) {
            jsonObject.putAll(data);
        }

        String resultJson = jsonObject.toJSONString();
        int length = resultJson.length();
        if (length > 2000) {
            logger.info("resultJson too long,size:" + length);
        } else {
            logger.info("resultJson:" + resultJson);
        }
        return resultJson;
    }

    public MessageResponse toMessageResponse(int max, int min) throws Exception {
        MessageResponse res = new MessageResponse(max, min);
        res.putUint(Uint.toUInt(result));
        res.putString(toJSONString());
        if (result != 0) {
            RESULT.set(result);
            logger.info("error result:{}", result);
        }
        return res;
    }

    public String toString() {
        return result + "\t" + toJSONString();
    }

    @Override
    public void marshal(Pack pack) {
        pack.putMarshallable(new UintMarshal(Uint.toUInt(result)));
        pack.putMarshallable(new StringMarshal(toJSONString()));
        if (result != 0) {
            RESULT.set(result);
            logger.info("error result:{}", result);
        }
    }

    @Override
    public void unmarshal(Unpack unpack) {
        result = unpack.popUInt().intValue();
        String json = unpack.popVarstr();
        JSONObject jsonObject = JSON.parseObject(json);
        int length = json.length();
        if (length > 2000) {
            logger.info("resultJson too long,size:" + length);
        } else {
            logger.info("resultJson:" + json);
        }
        data = jsonObject;
    }
}
