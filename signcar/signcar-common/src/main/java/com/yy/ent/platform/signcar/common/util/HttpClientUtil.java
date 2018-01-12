package com.yy.ent.platform.signcar.common.util;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;

public class HttpClientUtil {
	private final static int TIME_OUT = 10000;
	private final static int SO_TIME_OUT = 10000;

	public static String sendHttpByPost(String uri, Map<String, String> params, Integer timeOut, Integer soTimeOut) throws Exception {
		HttpClient client = new HttpClient();
		PostMethod method = new PostMethod(uri);

		List<NameValuePair> nvps = new ArrayList<NameValuePair>();

		Set<String> keySet = params.keySet();
		NameValuePair[] pairs = new NameValuePair[params.size()];
		for (String key : keySet) {
			nvps.add(new NameValuePair(key, params.get(key)));
		}
		for (int i = 0; i < nvps.size(); i++) {
			pairs[i] = nvps.get(i);
		}
		method.setRequestBody(pairs);

		String result = "";

		try {
			HttpConnectionManagerParams managerParams = client.getHttpConnectionManager().getParams();
			// 设置连接超时时间(单位毫秒)
			managerParams.setConnectionTimeout(null == timeOut ? TIME_OUT : timeOut);
			// 设置读数据超时时间(单位毫秒)
			managerParams.setSoTimeout(null == soTimeOut ? SO_TIME_OUT : soTimeOut);
			int statusCode = client.executeMethod(method);
			if (statusCode == HttpStatus.SC_OK) {
				result = handleData(method);
			}
		} catch (Exception e) {
			throw e;
		} finally {
			method.releaseConnection();
		}
		return result;
	}
	
	public static String sendHttpByGet(String uri) throws Exception {
		return sendHttpByGet(uri, TIME_OUT, SO_TIME_OUT);
	}

	public static String sendHttpByGet(String uri, int timeOut, int soTimeOut) throws Exception {
		HttpClient client = new HttpClient();
		GetMethod method = new GetMethod(uri);
		String result = "";
		try {
			HttpConnectionManagerParams managerParams = client.getHttpConnectionManager().getParams();
			// 设置连接超时时间(单位毫秒)
			managerParams.setConnectionTimeout(timeOut);
			// 设置读数据超时时间(单位毫秒)
			managerParams.setSoTimeout(soTimeOut);
			int statusCode = client.executeMethod(method);
			if (statusCode == HttpStatus.SC_OK) {
			    result = handleData(method);
			}
		} catch (Exception e) {
			throw new Exception("发送数据到接口请求时出现异常:" + e.getMessage());
		} finally {
			method.releaseConnection();
		}
		return result;
	}

	private static String handleData(HttpMethod method) throws Exception{
	    String result = "";
	    BufferedInputStream bis = new BufferedInputStream(method.getResponseBodyAsStream());
        byte[] bytes = new byte[1024];
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        int count = 0;
        while ((count = bis.read(bytes)) != -1) {
            bos.write(bytes, 0, count);
        }
        byte[] strByte = bos.toByteArray();
        result = new String(strByte, 0, strByte.length, "UTF-8");
        bos.close();
        bis.close();
        return result;
	} 
	
	
	
}
