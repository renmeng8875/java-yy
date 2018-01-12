package com.yy.ent.platform.core.mybatis;

import com.alibaba.druid.pool.DruidDataSource;
import com.yy.ent.commons.base.encrypt.DBConfigDecrypt;
import com.yy.ent.commons.base.valid.BlankUtil;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.Resource;

import java.io.Closeable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * RoutingDataSourceFactoryBean
 *
 * @author suzhihua
 * @date 2015/8/10.
 */
public class RoutingDataSourceFactoryBean implements FactoryBean<RoutingDataSource>, InitializingBean, DisposableBean {
    private final static Logger logger = LoggerFactory.getLogger(RoutingDataSource.class);
    private Resource conf;
    private List<DataSourceInfo> dataSources = new ArrayList<DataSourceInfo>();
    private RoutingDataSource routingDataSource;
    private Map<Object, Object> dataSourceMap;

    public Resource getConf() {
        return conf;
    }

    public void setConf(Resource conf) {
        this.conf = conf;
    }


    public RoutingDataSource getObject() throws Exception {
        if (routingDataSource == null) {
            initDataSource();
        }
        return routingDataSource;
    }

    private void initDataSource() {
        routingDataSource = new RoutingDataSource();
        DruidDataSource dataSource;
        dataSourceMap = new HashMap<Object, Object>();
        for (DataSourceInfo info : dataSources) {
            dataSource = new DruidDataSource();
            dataSource.setDriverClassName(info.getDriver());
            dataSource.setUrl(info.getUrl());
            dataSource.setUsername(info.getUsername());
            dataSource.setPassword(info.getPasswd());
            dataSource.setMaxActive(info.getMaxActive());
            dataSource.setMinIdle(info.getMinIdle());
            // dataSource.setMaxIdle(info.getMaxIdle());
            dataSource.setDefaultAutoCommit(true);
            dataSourceMap.put(info.getDbIndex(), dataSource);
        }
        routingDataSource.setTargetDataSources(dataSourceMap);
        routingDataSource.setDefaultTargetDataSource(dataSourceMap.get(0));

        //此处没有配置spring配置文件，多数据源每个都要配置比较麻烦，直接写死了
        RoutingDbIndex routingDbIndex = new RoutingDbIndex();
        Map<String, IDbIndex> dbIndexs = new HashMap<String, IDbIndex>();
        DbHash defaultDbIndex = new DbHash();
        dbIndexs.put(DbHash.TYPE, defaultDbIndex);
        dbIndexs.put(DbIndex.TYPE, new DbIndex());
        routingDbIndex.setTargetDbIndexs(dbIndexs);
        routingDbIndex.setDefaultDbIndex(defaultDbIndex);
        routingDbIndex.afterPropertiesSet();

        routingDataSource.setRoutingDbIndex(routingDbIndex);
        routingDataSource.afterPropertiesSet();
    }

    public Class<?> getObjectType() {
        return RoutingDataSource.class;
    }

    public boolean isSingleton() {
        return true;
    }

    public void afterPropertiesSet() throws Exception {
        initConf();
    }

    private void initConf() throws Exception {
        SAXReader saxReader = new SAXReader();
        try {
            Element root = saxReader.read(conf.getFile()).getRootElement();

            Element mapping = (Element) root.selectSingleNode("//db/datasource-mapping");
            String pwdEncrypt = mapping.attributeValue("pwd-encrypt");
            boolean isEncrypt = true;
            if (!BlankUtil.isBlank(pwdEncrypt)) {
                isEncrypt = Boolean.valueOf(pwdEncrypt);
            }
            List<?> datasource = mapping.elements(); // 得到datasource元素下的子元素集合
            if (datasource == null) {
                throw new IllegalArgumentException("datasource-mapping error, datasource is null!");
            }
            // 循环遍历集合中的每一个元素 
            for (Object obj : datasource) {
                Element element = (Element) obj;
                Integer indexText = getNodeValueInteger(element, "index");
                String driverText = getNodeValue(element, "driver");
                String urlText = getNodeValue(element, "url");
                String userText = getNodeValue(element, "user");
                String passwdText = getNodeValue(element, "password");
                Integer maxActiveText = getNodeValueInteger(element, "maxActive");
                Integer minIdleText = getNodeValueInteger(element, "minIdle");
                Integer maxIdleText = getNodeValueInteger(element, "maxIdle");

                if (isEncrypt) {
                    try {
                        DBConfigDecrypt dbDecrypt = new DBConfigDecrypt();
                        passwdText = dbDecrypt.dbPasswdDecrypt(passwdText);
                    } catch (Exception e) {
                        logger.warn("please set " + conf.getFilename() + " pwd-encrypt to false or set .so to correct position", e);
                    }
                }
                DataSourceInfo dataSource = new DataSourceInfo(driverText, indexText, urlText, userText, passwdText, maxActiveText, minIdleText, maxIdleText);
                dataSources.add(dataSource);
            }
        } catch (DocumentException ex) {
            throw new RuntimeException("配置文件解析错误：" + ex.getMessage(), ex);
        }
    }

    private String getNodeValue(Element element, String nodeName) {
        Node node = element.selectSingleNode(nodeName);
        if (node == null) {
            throw new IllegalArgumentException("datasource-mapping " + nodeName + " is null!");
        }
        return node.getText().trim();
    }

    private Integer getNodeValueInteger(Element element, String nodeName) {
        return Integer.valueOf(getNodeValue(element, nodeName));
    }

    public void destroy() throws Exception {
        if (dataSourceMap != null) {
            for (Object ds : dataSourceMap.values()) {
                ((Closeable) ds).close();
            }
        }
    }
}
