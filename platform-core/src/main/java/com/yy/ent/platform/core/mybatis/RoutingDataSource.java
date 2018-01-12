package com.yy.ent.platform.core.mybatis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import java.util.Map;

/**
 * RoutingDataSource,分布式数据源
 *
 * @author suzhihua
 * @date 2015/8/7.
 */
public class RoutingDataSource extends AbstractRoutingDataSource {
    private Logger logger = LoggerFactory.getLogger(RoutingDataSource.class);
    private int dbSize = 1;
    private RoutingDbIndex routingDbIndex;
    private final static ThreadLocal<DbIndexInfo> DB_INDEX_INFO_THREAD_LOCAL = new ThreadLocal<DbIndexInfo>() {
        @Override
        protected DbIndexInfo initialValue() {
            return null;
        }
    };

    public static void setDbIndexInfoThreadLocal(DbIndexInfo dbIndexInfoThreadLocal) {
        DB_INDEX_INFO_THREAD_LOCAL.set(dbIndexInfoThreadLocal);
    }

    public static void clearDbHash() {
        DB_INDEX_INFO_THREAD_LOCAL.remove();
    }

    @Override
    protected Object determineCurrentLookupKey() {
        DbIndexInfo dbIndexInfo = DB_INDEX_INFO_THREAD_LOCAL.get();
        if (dbIndexInfo == null || dbIndexInfo.getDbKey() == null) {
            logger.info("使用默认db");
            return null;
        } else {
            IDbIndex dbDetermineByType = routingDbIndex.getDbDetermineByType(dbIndexInfo.getType());
            Long dbIndex = dbDetermineByType.getDbIndex(dbIndexInfo.getDbKey());
            int index = (int) (dbIndex % dbSize);
            index = Math.abs(index);
            logger.info("使用db.index=" + index);
            return dbIndex;
        }
    }

    @Override
    public void setTargetDataSources(Map<Object, Object> targetDataSources) {
        super.setTargetDataSources(targetDataSources);
        if (targetDataSources != null) dbSize = targetDataSources.size();
    }

    public void setRoutingDbIndex(RoutingDbIndex routingDbIndex) {
        this.routingDbIndex = routingDbIndex;
    }
}
