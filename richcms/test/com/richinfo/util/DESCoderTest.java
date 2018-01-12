
package com.richinfo.util;

import static org.junit.Assert.assertEquals;

import org.apache.commons.codec.binary.Base64;
import org.junit.Test;

import com.richinfo.common.Constants;
import com.richinfo.common.utils.encryptUtil.DESCoder;
import com.richinfo.common.utils.encryptUtil.MD5Coder;


public class DESCoderTest {


	@Test
	public final void desTest() throws Exception {
		String inputStr = "彩讯科技有限公司139";
		byte[] inputData = inputStr.getBytes();
		System.err.println("原文:" + inputStr);
		// 初始化密钥
		byte[] key = DESCoder.initKey();
		String keystr = Base64.encodeBase64String(key);
		System.out.println("密钥："+keystr);
		//byte[] key2 = Base64.decodeBase64(keystr);
		//assertEquals(key,key2);
		// 加密
		inputData = DESCoder.encrypt(inputData, key);
		System.err.println("加密后:" + inputData);
		// 解密
		byte[] outputData = DESCoder.decrypt(inputData, key);
		String outputStr = new String(outputData);
		System.err.println("解密后:" + outputStr);
		// 校验
		assertEquals(inputStr, outputStr);
	}
	
	@Test
	public void testDecode() throws Exception{
		String keystr="/tq8IIk0odw=";
		String ecrptstr = "[B@1cd2d49";
		byte[] key = Base64.decodeBase64(keystr);
		// 解密
		byte[] outputData = DESCoder.decrypt(ecrptstr.getBytes(), key);
		String outputStr = new String(outputData);
		System.err.println("解密后:" + outputStr);
		
	}
	
	@Test
	public  void printMd5() throws Exception 
	{
		String encode = MD5Coder.encodeMD5Hex(MD5Coder.encodeMD5Hex("admin1234"));
		System.err.println(encode);
	}
	
	@Test
	public void printDeveloperKey() throws Exception
	{
		String encode = MD5Coder.encodeMD5Hex(MD5Coder.encodeMD5Hex("mmportdev@2014")+Constants.SALT);
		System.err.println(encode);
	}

	
}
