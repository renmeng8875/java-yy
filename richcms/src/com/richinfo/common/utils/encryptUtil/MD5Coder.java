
package com.richinfo.common.utils.encryptUtil;

import org.apache.commons.codec.digest.DigestUtils;

/**
* <p>类文件说明:DSA安全编码组件  </p>      
* @mailTo renmeng@richinfo.cn
* @copyright richinfo 
* @author renmeng  
* @date 2014-2-20
 */
public abstract class MD5Coder {

	/**
	 * MD5加密
	 * 
	 * @param data
	 *            待加密数据
	 * @return byte[] 消息摘要
	 * 
	 * @throws Exception
	 */
	public static byte[] encodeMD5(String data) throws Exception {

		// 执行消息摘要
		return DigestUtils.md5(data);
	}

	/**
	 * MD5加密
	 * 
	 * @param data
	 *            待加密数据
	 * @return byte[] 消息摘要
	 * 
	 * @throws Exception
	 */
	public static String encodeMD5Hex(String data) throws Exception {

		// 执行消息摘要
		return DigestUtils.md5Hex(data);
	}
}
