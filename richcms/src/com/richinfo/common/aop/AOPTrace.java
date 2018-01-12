package com.richinfo.common.aop;

import java.lang.reflect.Method;

import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import net.sf.json.JsonConfig;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.richinfo.common.Constants;
import com.richinfo.common.SystemContext;
import com.richinfo.common.annotation.RenmSelf;
import com.richinfo.common.entity.BaseEntity;
import com.richinfo.common.utils.dateUtil.DateTimeUtil;
import com.richinfo.logs.dao.TraceDao;
import com.richinfo.logs.entity.Trace;
import com.richinfo.privilege.entity.User;

/**
* <p>类文件说明:用户操作跟踪类，面向切面编程，利用动态代理和反射功能
* 实现无缝切入，监控用户所有对数据库做修改的方法  </p>      
* @mailTo renmeng@richinfo.cn
* @copyright richinfo 
* @author renmeng  
* @date 2014-2-25
 */
@Aspect
public class AOPTrace {
	
	private final static Logger log = Logger.getLogger("operationLog");
	
	private  TraceDao traceDao ;
	
	@Autowired
    @Qualifier("TraceDao")
	public void setTraceDao(TraceDao traceDao) {
		this.traceDao = traceDao;
	}


	@After(value="execution(* com.richinfo..service..*.*(..))&&!execution(* com.richinfo..service..*.get*(..))&&!execution(* com.richinfo..service..*.query*(..))&&!execution(* com.richinfo..service..*.find*(..))&&!execution(* com.richinfo..service..*.list*(..))")
    public void after(JoinPoint jp) {
		
		JsonConfig config = new JsonConfig();
		Object target = jp.getTarget();
		Object[] objArr = jp.getArgs();
		for(Object obj :objArr)
		{
			if(obj instanceof BaseEntity)
			{
				BaseEntity arg= (BaseEntity)obj;
				String [] attributes = arg.getExcludesAttributes();
				if(attributes!=null)
				{
					config.setExcludes(attributes);
				}
				break;
			}
		}
		Signature st = jp.getSignature();
		Class<?>[] parameterTypes = ((MethodSignature)st).getMethod().getParameterTypes();
		String method = st.getName();
		String mem ="此方法没有添加注解,无法获取注释,请在此方法上方添加@RenmSelf注解";
		try {
			Method m = target.getClass().getMethod(method, parameterTypes);
			RenmSelf rsf = m.getAnnotation(RenmSelf.class);
			if(rsf!=null)
			{
				mem = rsf.methodDesc();
			}
		} catch (NoSuchMethodException e1) {
			e1.printStackTrace();
		} catch (SecurityException e1) {
			e1.printStackTrace();
		}
		
		JSONArray json = null;
		try {
			json = JSONArray.fromObject(jp.getArgs(),config);
		} catch (Exception e) {
			e.printStackTrace();
		}
        Trace trace = new Trace();
        int userId = -1;
        String userName = "";
        HttpSession session = SystemContext.getSessionContext();
        
        User user = null;
        if(session!=null)
        {
        	user = (User)session.getAttribute(Constants.CURRENT_USER_ACCOUNT);
        	if(user!=null)
        	{
        		userId = user.getUserId();
        		userName = user.getUserName();
        	}
        }
        trace.setUserId(userId);
        trace.setSignature(jp.getSignature().toString());
        String params = json!=null?json.toString():"无法动态解析出参数！";
        if(!StringUtils.isEmpty(params)&&params.length()>600)
        {
        	params = params.substring(0, 600);
        }	
        trace.setParams(params);
        trace.setMem(mem);
        trace.setUserName(userName);
        trace.setLoginIP(user == null?"":user.getLoginIp()+"");
        trace.setCtime(DateTimeUtil.getNowDateTime("yyyy-MM-dd HH:mm:ss"));
        try {
			traceDao.merge(trace);
		} catch (Exception e) {
			
			log.error("追踪操作发生异常，请检查参数！", e);
		}
        log.info("userId="+userId+"|userName="+userName+"|method="+jp.getSignature()+"|params="+params);
        
    }
}
