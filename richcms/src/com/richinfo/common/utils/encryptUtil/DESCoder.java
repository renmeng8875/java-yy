
package com.richinfo.common.utils.encryptUtil;

import java.security.Key;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

import org.apache.commons.codec.binary.Base64;

/**
* <p>类文件说明:DES安全编码组件 </p>      
* @mailTo renmeng@richinfo.cn
* @copyright richinfo 
* @author renmeng  
* @date 2014-2-20
 */
public abstract class DESCoder {

	public static final String KEY_ALGORITHM = "DES";
	
	public static final String CIPHER_ALGORITHM = "DES/ECB/PKCS5PADDING";
	
	public static final String CIPHER_DES_CBC_PKCS5PADDING = "DES/CBC/PKCS5Padding";
	
	private static byte[]ivParameter={(byte)0x12,(byte)0x34,(byte)0x56,(byte)0x78,(byte)0x90,(byte)0xAB,(byte)0xCD,(byte)0xEF};

	

	/**
	* 方法说明:解密  
	* @param data 待解密数据字符数组
	* @param key 密钥
	* @return byte[] 字符数组
	* @author renmeng
	* @copyright richinfo 
	* @date 2014-5-7
	*/
	public static byte[] decrypt(byte[] data, byte[] key) throws Exception
	{
		Key k = toKey(key);
		Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
		cipher.init(Cipher.DECRYPT_MODE, k);
		return cipher.doFinal(data);
	}
	

	/**
	* 方法说明: 加密方法 
	* @param data 待加密字符数组
	* @param key 密钥
	* @return byte[] 加密后的字符数组
	* @author renmeng
	* @copyright richinfo 
	* @date 2014-5-7
	*/
	public static byte[] encrypt(byte[] data, byte[] key) throws Exception 
	{
		Key k = toKey(key);
		Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
		cipher.init(Cipher.ENCRYPT_MODE, k);
		return cipher.doFinal(data);
	}
	
	public static byte[] initKey() throws Exception 
	{
		KeyGenerator kg = KeyGenerator.getInstance(KEY_ALGORITHM);
		kg.init(56, new SecureRandom());
		SecretKey secretKey = kg.generateKey();
		return secretKey.getEncoded();
	}
	
	private static Key toKey(byte[] key) throws Exception 
	{
		DESKeySpec dks = new DESKeySpec(key);
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(KEY_ALGORITHM);
		SecretKey secretKey = keyFactory.generateSecret(dks);
		return secretKey;
	}
	
	public static byte[]desEncrypt(byte[]plainText,byte[] key)throws Exception{
		IvParameterSpec iv=new IvParameterSpec(ivParameter);
		DESKeySpec dks=new DESKeySpec(key);
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(KEY_ALGORITHM);
		SecretKey secretKey=keyFactory.generateSecret(dks);
		Cipher cipher=Cipher.getInstance(CIPHER_DES_CBC_PKCS5PADDING);
		cipher.init(Cipher.ENCRYPT_MODE, secretKey,iv);
		byte data[]=plainText;
		byte encryptedData[]=cipher.doFinal(data);
		return encryptedData;
	}
	
	public static String encrypt(String input,String key){
		String result="";
		try {
			result=Base64.encodeBase64String(desEncrypt(input.getBytes(),key.getBytes()));
		} catch (Exception e) {
		}
		return result;
	}
}
