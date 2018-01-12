package com.yy.ent.platform.signcar.service.task;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

public class ReflectionJob implements Job{
	
	private static Logger log = LoggerFactory.getLogger(TaskScheduler.class);
	
	public ReflectionJob() {
    }
	
	@Override
	public void execute(JobExecutionContext paramJobExecutionContext)
			throws JobExecutionException {
		try {
			JobDataMap map = paramJobExecutionContext.getTrigger().getJobDataMap();
			
            Class jobClass = (Class)map.get("jobClass");
			String methodName = map.getString("methodName");
			log.info("ReflectionJob jobClass = {}, methodName = {}", jobClass, methodName);
			Method method = null;
			try{
				method = jobClass.getMethod(methodName);
				method.invoke(this);
			}catch (NoSuchMethodException e) {
				method = jobClass.getMethod(methodName, new Class[]{JobExecutionContext.class});
				method.invoke(this, paramJobExecutionContext);
			}
		} catch (Exception e) {
			log.warn("ReflectionJob error.", e);
		}
	}
}
