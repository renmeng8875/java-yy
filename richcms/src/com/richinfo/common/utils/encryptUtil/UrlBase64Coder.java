
package com.richinfo.common.utils.encryptUtil;

import org.apache.commons.codec.binary.Base64;

/**
* <p>类文件说明:Url Base64组件  </p>      
* @mailTo renmeng@richinfo.cn
* @copyright richinfo 
* @author renmeng  
* @date 2014-2-20
 */
public abstract class UrlBase64Coder {

	/**
	 * 字符编码
	 */
	public final static String ENCODING = "UTF-8";

	/**
	 * Url Base64编码
	 * 
	 * @param data
	 *            待编码数据
	 * @return String 编码数据
	 * @throws Exception
	 */
	public static String encode(String data) throws Exception {

		// 执行编码
		byte[] b =  Base64.encodeBase64(data.getBytes(ENCODING));

		return new String(b, ENCODING);
	}

	/**
	 * Url Base64解码
	 * 
	 * @param data
	 *            待解码数据
	 * @return String 解码数据
	 * @throws Exception
	 */
	public static String decode(String data) throws Exception {

		// 执行解码
		byte[] b = Base64.decodeBase64(data.getBytes(ENCODING));

		return new String(b, ENCODING);
	}

}
