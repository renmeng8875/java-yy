package com.yy.ent.platform.core.mybatis;

/**
 * DbIndex，按数字面值索引
 *
 * @author suzhihua
 * @date 2015/9/7.
 */
public class DbIndex implements IDbIndex {
    public final static String TYPE = "index";

    @Override
    public Long getDbIndex(Object obj) {
        if (obj instanceof Number) {
            return ((Number) obj).longValue();
        } else {
            return Long.parseLong(obj.toString());
        }
    }
}
