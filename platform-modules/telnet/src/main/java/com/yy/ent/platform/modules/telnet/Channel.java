package com.yy.ent.platform.modules.telnet;

import java.util.HashMap;
import java.util.Map;

/**
 * Channel
 *
 * @author suzhihua
 * @date 2015/10/21.
 */
public class Channel {
    private Map<String, Object> attributes = new HashMap<String, Object>();

    public Object getAttribute(String key) {
        return attributes.get(key);
    }

    public <T> T getAttribute(String key, T defValue) {
        T o = (T) attributes.get(key);
        return o == null ? defValue : o;
    }

    public void setAttribute(String key, Object value) {
        attributes.put(key, value);
    }
}
