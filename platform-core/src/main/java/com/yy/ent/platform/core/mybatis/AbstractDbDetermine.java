package com.yy.ent.platform.core.mybatis;

/**
 * AbstractDbDetermine，选择数据源
 *
 * @author suzhihua
 * @date 2015/8/10.
 */
public abstract class AbstractDbDetermine implements IDbDetermine {

    @Override
    public String getType() {
        return TYPE_DEFAULT;
    }
}
