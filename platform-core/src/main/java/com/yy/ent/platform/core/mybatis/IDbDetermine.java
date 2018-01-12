package com.yy.ent.platform.core.mybatis;

/**
 * IDbDetermine
 *
 * @author suzhihua
 * @date 2015/9/7.
 */
public interface IDbDetermine {
    String TYPE_DEFAULT = "default";

    /**
     * @return
     * @see IDbIndex
     */
    Object getDbKey();

    /**
     * @return
     * @see RoutingDbIndex#getDbDetermineByType(String)
     */
    String getType();
}
