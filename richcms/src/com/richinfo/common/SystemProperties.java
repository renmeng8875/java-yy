package com.richinfo.common;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;


public class SystemProperties 
{
	private static SystemProperties instance;
	
	private static final String JOIN = ".";
	
	private long lastModify = 0L;
	
	private Resource resource;
	
	private Map<String,Map<String,String>> cache = new HashMap<String, Map<String,String>>();
	
	public static SystemProperties getInstance()
	{
		if(instance==null){
			instance = new SystemProperties();
		}
		return instance;
	}
	
	private SystemProperties()
	{
		initResource();
	}
	
	@SuppressWarnings("unchecked")
	private void initResource()
	{
		ResourceLoader loader = new DefaultResourceLoader();
        resource = loader.getResource("classpath:systemGlobal.xml");
        SAXReader saxReader = new SAXReader();
		Document document = null;
		try {
			document = saxReader.read(resource.getFile());
			lastModify = resource.getFile().lastModified();
		} catch (Exception e) {
			e.printStackTrace();
		}
		Element root = document.getRootElement();
		List<Element> moduleList = root.elements("module");
		for(Element module:moduleList)
		{
			String preix = module.attributeValue("name");
			Map<String,String> moduleMap = new HashMap<String,String>();
			cache.put(preix, moduleMap);
			List<Element> propertyList = module.elements("property");
			
			for(Element item:propertyList)
			{
				moduleMap.put(item.attributeValue("name"), item.attributeValue("value"));
			}
		}
		
	}
	
	public String getProperty(String key)
	{
		long currentMill = 0L;
		try 
		{
			currentMill = resource.getFile().lastModified();
		} 
		catch (IOException e)
		{
			e.printStackTrace();
		}
		if(currentMill!=lastModify)
		{
			initResource();
		}
		String[] pairs = StringUtils.split(key, JOIN);
		String value = cache.get(pairs[0]).get(pairs[1]);
		return StringUtils.isEmpty(value)?"":value;
	}
	
	@SuppressWarnings("deprecation")
	public void setProperty(String id,String value)
	{
		ResourceLoader loader = new DefaultResourceLoader();
        resource = loader.getResource("classpath:systemGlobal.xml");
        SAXReader saxReader = new SAXReader();
		Document document = null;
		try {
			document = saxReader.read(resource.getFile());
		} catch (Exception e) {
			e.printStackTrace();
		}
		String[] pairs = StringUtils.split(id, JOIN);
		Element element = (Element)document.selectSingleNode("/configuration/module[@name='"+pairs[0]+"']/property[@name='"+pairs[1]+"']");
		element.setAttributeValue("value", value);
		FileOutputStream fileOutputStream = null;
		XMLWriter xmlWriter = null;
		try {
			OutputFormat outputFormat = OutputFormat.createPrettyPrint();
			outputFormat.setEncoding("UTF-8");
			outputFormat.setIndent(true);
			outputFormat.setIndent("	");
			outputFormat.setNewlines(true);
			fileOutputStream = new FileOutputStream(resource.getFile());
			xmlWriter = new XMLWriter(fileOutputStream, outputFormat);
			xmlWriter.write(document);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (xmlWriter != null) {
				try {
					xmlWriter.close();
				} catch (IOException e) {
				}
			}
			IOUtils.closeQuietly(fileOutputStream);
		}
	
		
	}
	
	public Map<String,Map<String,String>> getConfigMap()
	{
		long currentMill = 0L;
		try 
		{
			currentMill = resource.getFile().lastModified();
		} 
		catch (IOException e)
		{
			e.printStackTrace();
		}
		if(currentMill!=lastModify)
		{
			initResource();
		}
		return this.cache;
	}
	
	
	
}
