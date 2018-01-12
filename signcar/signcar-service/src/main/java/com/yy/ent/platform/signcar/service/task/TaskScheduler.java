package com.yy.ent.platform.signcar.service.task;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * 任务调度器
 */
public class TaskScheduler {
    private static Logger log = LoggerFactory.getLogger(TaskScheduler.class);
    private static SchedulerFactory sf = new StdSchedulerFactory();
    private static int jobKey = 0;

    /**
     * 执行cron表达式任务
     *
     * @param jobClass        执行任务的类
     * @param methodName      任务类中执行的方法
     * @param cronExpresstion Cron表达式
     */
    public static void executeCronSchedule(Class<?> jobClass, String methodName, String cronExpresstion) {
        int _jobKey = jobKey++;
        JobDetail job;
        CronTrigger trigger;
        Date ft;
        try {
            Scheduler sched = sf.getScheduler();
            job = JobBuilder.newJob(ReflectionJob.class).withIdentity("job" + _jobKey, "group" + _jobKey).build();
            JobDataMap map = new JobDataMap();
            map.put("jobClass", jobClass);
            map.put("methodName", methodName);
            trigger = TriggerBuilder.newTrigger()
                    .withIdentity("trigger" + _jobKey, "group" + _jobKey)
                    .withSchedule(CronScheduleBuilder.cronSchedule(cronExpresstion))
                    .usingJobData(map)
                    .build();

            ft = sched.scheduleJob(job, trigger);
            log.info(job.getKey() + " has been scheduled to run at: " + ft
                    + " and repeat based on expression: "
                    + trigger.getCronExpression());
            sched.start();
        } catch (SchedulerException e) {
            log.warn("WorkScheduler error.", e);
        }
    }

}
