package com.yy.ent.platform.core.mybatis;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * ConnectionInterceptor,分库使用,先查找注解DbHash,再查找接口IDbHash,查到则返回
 *
 * @author suzhihua
 * @date 2015/8/7.
 */
@Intercepts({@Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class})
        , @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class})})
public class ConnectionInterceptor implements org.apache.ibatis.plugin.Interceptor {
    private static final Map<String, CacheInfo> DB_CACHE_INFO = new HashMap<String, CacheInfo>();

    public Object intercept(Invocation invocation) throws Throwable {
        MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
        String id = mappedStatement.getId();
        CacheInfo cacheInfo = getCacheInfo(id);

        //表示使用默认数据源
        if (cacheInfo == null || cacheInfo.getIndex() < 1) {
            return invocation.proceed();
        }

        Object dbKey = getDbKey(invocation.getArgs()[1], cacheInfo);
        if (dbKey == null) {
            return invocation.proceed();
        }

        //需要选择数据源
        DbIndexInfo dbIndexInfo = new DbIndexInfo(cacheInfo.getType(), dbKey);
        RoutingDataSource.setDbIndexInfoThreadLocal(dbIndexInfo);
        try {
            return invocation.proceed();
        } finally {
            RoutingDataSource.clearDbHash();
        }

    }

    private Object getDbKey(Object args, CacheInfo cacheInfo) {
        int dbHashParamIndex = cacheInfo.getIndex();
        Object param;
        if (args instanceof Map) {
            Map params = (Map) args;
            //mybatis默认param1,param2...
            param = params.get("param" + dbHashParamIndex);
        } else {
            param = args;
        }

        if (param == null) {
            return null;
        }
        if (param instanceof IDbDetermine) {
            IDbDetermine dbDetermine = (IDbDetermine) param;
            param = dbDetermine.getDbKey();
            //此处需要设置type
            cacheInfo.setType(dbDetermine.getType());
        }
        return param;
    }

    /**
     * 索引从1(mybatis参数从1开始的)开始,0为没有
     *
     * @param id
     * @return
     * @throws ClassNotFoundException
     */
    private CacheInfo getCacheInfo(String id) throws ClassNotFoundException {
        CacheInfo cacheInfo = DB_CACHE_INFO.get(id);
        if (cacheInfo != null) {
            return cacheInfo;
        }
        int dot = id.lastIndexOf(".");
        String _clazz = id.substring(0, dot);
        String _method = id.substring(dot + 1);
        Method[] methods = Class.forName(_clazz).getMethods();
        Method method = null;
        //此处无法取方法参数,只能一个一个对比,即接口不能有重复的方法名,mybatis也是要求这是这样子
        for (Method m : methods) {
            if (_method.equals(m.getName())) {
                method = m;
                break;
            }
        }
        int i = 1;
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        //先查找注解的参数
        for (Annotation[] parameterAnnotation : parameterAnnotations) {
            for (Annotation annotation : parameterAnnotation) {
                if (annotation.annotationType() == DbDetermine.class) {
                    cacheInfo = new CacheInfo(i, ((DbDetermine) annotation).type());
                    DB_CACHE_INFO.put(id, cacheInfo);
                    return cacheInfo;
                }
            }
            i++;
        }
        //再查找AbstractDbDetermine接口
        Class<?>[] parameterTypes = method.getParameterTypes();
        i = 1;
        for (Class<?> parameterType : parameterTypes) {
            if (AbstractDbDetermine.class.isAssignableFrom(parameterType)) {
                //type需要运行时子类才行读取到
                cacheInfo = new CacheInfo(i, null);
                DB_CACHE_INFO.put(id, cacheInfo);
                return cacheInfo;
            }
            i++;
        }
        //表示使用默认数据源
        cacheInfo = new CacheInfo(0, null);
        DB_CACHE_INFO.put(id, cacheInfo);
        return cacheInfo;
    }

    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    public void setProperties(Properties properties) {
    }


}

/**
 * 放数据源信息
 */
class CacheInfo {
    /**
     * 参数索引，从1开始，0为不存在
     */
    private int index;
    /**
     * @see RoutingDbIndex#getDbDetermineByType(String)
     */
    private String type;

    public CacheInfo(int index, String type) {
        this.index = index;
        this.type = type;
    }

    protected void setIndex(int index) {
        this.index = index;
    }

    protected void setType(String type) {
        this.type = type;
    }

    public int getIndex() {
        return index;
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
