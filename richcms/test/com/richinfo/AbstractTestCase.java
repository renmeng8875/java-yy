package com.richinfo;

import org.junit.Before;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public abstract class AbstractTestCase 
{
	
	private BeanFactory beanFactory ;
	
	@Before
	public void setUp() throws Exception
	{
		beanFactory = new ClassPathXmlApplicationContext("applicationContext.xml");
	}
	
	public Object getBean(String beanName)
	{
		Object result =beanFactory.getBean(beanName);
		return result;
	}
	

}
