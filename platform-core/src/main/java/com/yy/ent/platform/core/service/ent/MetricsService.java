package com.yy.ent.platform.core.service.ent;

import com.duowan.sysop.hawk.metrics.client2.Protocol;
import com.duowan.sysop.hawk.metrics.client2.attribute.UriTag;
import com.duowan.sysop.hawk.metrics.client2.type.DefMetricsValue;
import com.duowan.sysop.hawk.metrics.client2.type.DefaultModel;
import com.duowan.sysop.hawk.metrics.client2.type.MetricsModelFactory;
import com.duowan.sysop.hawk.metrics.client2.type.TimeScale;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class MetricsService {
    public static final int CODE_SUCCESS = 200;
    public static final int CODE_ERROR = 500;
    protected static final Logger logger = LoggerFactory.getLogger(MetricsService.class);

    private ConcurrentMap<String, MetricsModelFactory> map = new ConcurrentHashMap<String, MetricsModelFactory>();
    private String scales = "50,100,150,200,300,500,800,1000,1500,2000,3000,5000,8000,10000";
    private String businessName = "test";
    private String environmentName = "dev";

    private MetricsModelFactory getFactory(String serviceName) {
        MetricsModelFactory metricsModelFactory = map.get(serviceName);
        if (metricsModelFactory == null) {
            metricsModelFactory = MetricsModelFactory.instance(businessName, "1.0", serviceName, "1.0", Protocol.HTTP, new TimeScale(scales), 0, CODE_SUCCESS);
            map.putIfAbsent(serviceName, metricsModelFactory);
            metricsModelFactory = map.get(serviceName);
        }
        return metricsModelFactory;
    }

    /**
     * 上报数据(成功)
     *
     * @param serviceName
     * @param methodName
     * @param duration    单位ms
     */
    public void report(String serviceName, String methodName, long duration) {
        report(serviceName, methodName, duration, CODE_SUCCESS);
    }

    /**
     * 上报数据(失败)
     *
     * @param serviceName
     * @param methodName
     * @param duration    单位ms
     */
    public void reportError(String serviceName, String methodName, long duration) {
        report(serviceName, methodName, duration, CODE_ERROR);
    }

    /**
     * 上报数据
     *
     * @param serviceName
     * @param methodName
     * @param duration
     * @param isSuccess   <code>code=isSuccess ? CODE_SUCCESS : CODE_ERROR</code>
     */
    public void report(String serviceName, String methodName, long duration, boolean isSuccess) {
        report(serviceName, methodName, duration, isSuccess ? CODE_SUCCESS : CODE_ERROR);
    }

    /**
     * 上报数据
     *
     * @param serviceName
     * @param methodName
     * @param duration    单位ms
     * @param code
     */
    public void report(String serviceName, String methodName, long duration, int code) {
        try {
            serviceName = serviceName + "-" + environmentName;
            methodName = methodName.replaceAll("/", "_");
            logger.debug("MetricsService report,serviceName:" + serviceName + ",methodName:" + methodName + ",duration:" + duration + ",code:" + code);
            MetricsModelFactory factory = getFactory(serviceName);
            DefaultModel metrics = factory.defaultModel(UriTag.CLIENT_SIDE);
            DefMetricsValue value = metrics.get(methodName);
            if (value != null && value.isValid()) {
                value.markDurationAndCode(duration, code);
            }
        } catch (Throwable e) {
            logger.warn("MetricsService report error", e);
        }
    }

    public void setScales(String scales) {
        this.scales = scales;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public void setEnvironmentName(String environmentName) {
        this.environmentName = environmentName;
    }
}
