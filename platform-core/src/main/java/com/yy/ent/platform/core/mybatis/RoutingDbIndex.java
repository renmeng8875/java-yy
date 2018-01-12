package com.yy.ent.platform.core.mybatis;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import java.util.Map;

/**
 * RoutingDbDetermine,选择dbIndex
 *
 * @author suzhihua
 * @date 2015/9/7.
 */
public class RoutingDbIndex implements InitializingBean {
    private Map<String, ? extends IDbIndex> targetDbIndexs;
    private IDbIndex defaultDbIndex;

    public void setTargetDbIndexs(Map<String, ? extends IDbIndex> targetDbIndexs) {
        this.targetDbIndexs = targetDbIndexs;
    }

    public void setDefaultDbIndex(IDbIndex defaultDbIndex) {
        this.defaultDbIndex = defaultDbIndex;
    }

    public IDbIndex getDbDetermineByType(String type) {
        if (type == null) return defaultDbIndex;
        IDbIndex dbIndex = targetDbIndexs.get(type);
        return dbIndex == null ? defaultDbIndex : dbIndex;
    }

    @Override
    public void afterPropertiesSet() {
        Assert.notNull(targetDbIndexs, "targetDbIndexs can't be null");
        Assert.notNull(defaultDbIndex, "defaultDbIndex can't be null");
    }
}
