package com.yy.ent.platform.core.mybatis;

/**
 * IDbIndex，选择数据源索引，Math.abs(getDbIndex(obj)/dbsize)
 *
 * @author suzhihua
 * @date 2015/9/7.
 */
public interface IDbIndex {
    Long getDbIndex(Object obj);
}
