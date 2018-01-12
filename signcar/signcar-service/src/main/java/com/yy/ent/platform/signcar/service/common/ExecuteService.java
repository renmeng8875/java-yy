package com.yy.ent.platform.signcar.service.common;

import com.yy.ent.platform.core.service.ent.MetricsService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * ExecuteService
 *
 * @author suzhihua
 * @date 2015/11/2.
 */
@Service
public class ExecuteService extends BaseService {
    private ExecutorService pool = Executors.newFixedThreadPool(500);

    @Autowired
    private MetricsService metricsService;

    public ExecuteService() {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                pool.shutdown();
            }
        });
    }

    /**
     * 执行方法
     *
     * @param callback
     * @param threadName  线程名，用于跟踪日志,会拼上metriceName
     * @param metricsName 鹰眼统计用
     */
    public void execute(final ExecuteCallback callback, String threadName, final String metricsName) {
        if (StringUtils.isBlank(threadName)) {
            threadName = Thread.currentThread().getName();
        }
        final String threadNameToUse = threadName + "_" + metricsName;
        pool.execute(new Runnable() {
            @Override
            public void run() {
                long begin = System.currentTimeMillis();
                boolean isSuccess = false;
                Thread thread = Thread.currentThread();
                String oldName = thread.getName();
                thread.setName(threadNameToUse);
                try {
                    logger.debug(">>> invoke {}", metricsName);
                    callback.doInAction();
                    isSuccess = true;
                } catch (Throwable e) {
                    logger.error("ExecuteService.execute {}", metricsName, e);
                } finally {
                    long end = System.currentTimeMillis() - begin;
                    logger.debug("<<< invoke {} ,time: {} ms", metricsName, end);
                    metricsService.report("executeInvoke", metricsName, end, isSuccess);
                    thread.setName(oldName);
                }
            }
        });
    }
}



