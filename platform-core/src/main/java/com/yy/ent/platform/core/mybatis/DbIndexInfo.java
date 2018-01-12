package com.yy.ent.platform.core.mybatis;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * DbIndexInfo,数据源相关信息
 *
 * @author suzhihua
 * @date 2015/9/7.
 */
public class DbIndexInfo {
    private String type;
    private Object dbKey;

    public DbIndexInfo(String type, Object dbKey) {
        this.type = type;
        this.dbKey = dbKey;
    }

    public String getType() {
        return type;
    }


    public Object getDbKey() {
        return dbKey;
    }


    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
