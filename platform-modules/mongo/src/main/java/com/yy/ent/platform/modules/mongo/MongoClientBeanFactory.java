package com.yy.ent.platform.modules.mongo;

import com.mongodb.MongoClient;
import com.mongodb.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.FactoryBean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * MongoClientBeanFactory
 *
 * @author suzhihua
 * @date 2015/9/24.
 */
public class MongoClientBeanFactory implements FactoryBean<MongoClient> {
    private String userName;
    private String password;
    private String database;

    private String host;
    private int port;

    private int socketTimeout = 0;
    private int maxWaitTime = 1000 * 60 * 2;
    private int connectTimeout = 1000 * 10;
    private int connectionsPerHost = 100;

    private ReadPreference readPreference = ReadPreference.primary();
    private WriteConcern writeConcern = WriteConcern.ACKNOWLEDGED;


    private MongoClientOptions mongoClientOptions;
    private MongoClient mongoClient;
    private String servers;

    @Override
    public MongoClient getObject() throws Exception {
        if (mongoClient == null) {
            mongoClient = getMongoClient();
        }
        return mongoClient;
    }

    protected MongoClient getMongoClient() {
        List<MongoCredential> credentialsList = null;
        if (userName != null && password != null) {
            MongoCredential credential = MongoCredential.createCredential(userName, database, password.toCharArray());
            credentialsList = Arrays.asList(credential);
        } else {
            credentialsList = Collections.<MongoCredential>emptyList();
        }

        if (mongoClientOptions == null) {
            MongoClientOptions.Builder builder = MongoClientOptions.builder();
            builder.socketTimeout(socketTimeout);
            builder.maxWaitTime(maxWaitTime);
            builder.connectTimeout(connectTimeout);
            builder.connectionsPerHost(connectionsPerHost);
            builder.readPreference(readPreference);
            builder.writeConcern(writeConcern);
            mongoClientOptions = builder.build();
        }

        List<ServerAddress> list = new ArrayList<ServerAddress>();
        if (StringUtils.isNotBlank(host)) list.add(new ServerAddress(host, port));
        if (StringUtils.isNotBlank(servers)) {
            String[] split = servers.split(";|,");
            String[] array;
            for (String s : split) {
                array = s.split(":");
                list.add(new ServerAddress(array[0], Integer.parseInt(array[1])));
            }
        }
        return new MongoClient(list, credentialsList, mongoClientOptions);
    }

    @Override
    public Class<?> getObjectType() {
        return MongoClient.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setSocketTimeout(int socketTimeout) {
        this.socketTimeout = socketTimeout;
    }

    public void setMaxWaitTime(int maxWaitTime) {
        this.maxWaitTime = maxWaitTime;
    }

    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public void setConnectionsPerHost(int connectionsPerHost) {
        this.connectionsPerHost = connectionsPerHost;
    }

    public String getServers() {
        return servers;
    }

    public void setServers(String servers) {
        this.servers = servers;
    }

    public void setReadPreference(String readPreference) {
        this.readPreference = ReadPreference.valueOf(readPreference);
    }

    public void setWriteConcern(String writeConcern) {
        this.writeConcern = WriteConcern.valueOf(writeConcern);
    }

    public void setMongoClientOptions(MongoClientOptions mongoClientOptions) {
        this.mongoClientOptions = mongoClientOptions;
    }
}
