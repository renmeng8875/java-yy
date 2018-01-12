package com.yy.ent.platform.core.mybatis;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * DataSourceInfo，数据源信息，对应配置文件解释
 *
 * @author suzhihua
 * @date 2015/8/10.
 */
public class DataSourceInfo {
    private String driver;
    private Integer dbIndex;

    private String url;

    private String username;

    private String passwd;

    private Integer maxActive;

    private Integer minIdle;

    private Integer maxIdle;

    public DataSourceInfo(String driver, Integer dbIndex, String url, String username, String passwd, Integer maxActive, Integer minIdle, Integer maxIdle) {
        this.driver = driver;
        this.dbIndex = dbIndex;
        this.url = url;
        this.username = username;
        this.passwd = passwd;
        this.maxActive = maxActive;
        this.minIdle = minIdle;
        this.maxIdle = maxIdle;
    }

    public String getDriver() {
        return driver;
    }

    public Integer getDbIndex() {
        return dbIndex;
    }

    public String getUrl() {
        return url;
    }

    public String getUsername() {
        return username;
    }

    public String getPasswd() {
        return passwd;
    }

    public Integer getMaxActive() {
        return maxActive;
    }

    public Integer getMinIdle() {
        return minIdle;
    }

    public Integer getMaxIdle() {
        return maxIdle;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
