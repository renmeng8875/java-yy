package com.richinfo.common.utils.encryptUtil;

import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;

import org.springframework.util.StringUtils;

import com.richinfo.common.SystemProperties;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class RSACoder {
	/**
	 * 非对称加密密钥算法
	 */
	public static final String KEY_ALGORITHM = "RSA";
	
	/**
	 * RSA密钥长度 
	 * 默认1024位，
	 * 密钥长度必须是64的倍数， 
	 * 范围在512至65536位之间。
	 */
	private static final int KEY_SIZE = 512;

	/**
	 * 公钥
	 */
	private static final String PUBLIC_KEY = "RSAPublicKey";

	/**
	 * 私钥
	 */
	private static final String PRIVATE_KEY = "RSAPrivateKey";
	
	
	
	public static Map<String,String> initKey() throws Exception
	{
		// 实例化密钥对生成器
		KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(KEY_ALGORITHM);

		// 初始化密钥对生成器
		keyPairGen.initialize(KEY_SIZE);

		// 生成密钥对
		KeyPair keyPair = keyPairGen.generateKeyPair();

		// 公钥
		RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();

		// 私钥
		RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();

		// 封装密钥
		Map<String, String> keyMap = new HashMap<String, String>(2);

		keyMap.put(PUBLIC_KEY, getKeyString(publicKey));
		keyMap.put(PRIVATE_KEY, getKeyString(privateKey));
		return keyMap;
		
	}

	/**
	* 方法说明:由密钥字符串生成公钥  
	* @param key 密钥字符串（经过base64编码）
	* @return PublicKey
	* @author renmeng
	* @copyright richinfo 
	* @date 2014-7-29
	*/
	public static PublicKey getPublicKey(String key) throws Exception 
	{
		byte[] keyBytes = (new BASE64Decoder()).decodeBuffer(key);
		X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		PublicKey publicKey = keyFactory.generatePublic(keySpec);
		return publicKey;
	}

	/**
	* 方法说明:由密钥字符串生成私钥  
	* @param key 密钥字符串（经过base64编码）
	* @return PublicKey
	* @author renmeng
	* @copyright richinfo 
	* @date 2014-7-29
	*/
	public static PrivateKey getPrivateKey(String key) throws Exception 
	{
		byte[]keyBytes = (new BASE64Decoder()).decodeBuffer(key);
		PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
		return privateKey;
	}

	/**
	* 方法说明:64位编码方式序列化密钥便于保存  
	* @param key 密钥参数 
	* @author renmeng
	* @copyright richinfo 
	* @date 2014-7-29
	 */
	public static String getKeyString(Key key) throws Exception 
	{
		byte[] keyBytes = key.getEncoded();
		String s = (new BASE64Encoder()).encode(keyBytes);
		return s;
	}
	
	/**
	* 方法说明: 公钥加密文本  
	* @param plainText  待加密文本
	* @param publicKeyStr  64位编码的公钥字符串
	* @author renmeng
	* @copyright richinfo 
	* @date 2014-7-29
	 */
	public static String RSAEncode(String plainText,String publicKeyStr) throws Exception
	{
		byte[] text = plainText.getBytes();
		Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);
		cipher.init(Cipher.ENCRYPT_MODE, getPublicKey(publicKeyStr));
		byte[] enBytes = cipher.doFinal(text);
		String entext = new BASE64Encoder().encode(enBytes);
		return entext;
		
	}
	
	/**
	* 方法说明:私钥解密文本
	* @param encodeText   待解密的字符串
	* @param privateKeyStr 64位编码过的私钥字符串
	* @author renmeng
	* @copyright richinfo 
	* @date 2014-7-29
	*/
	public static String RSADecode(String encodeText,String privateKeyStr)throws Exception
	{
		byte[] text = (new BASE64Decoder()).decodeBuffer(encodeText);
		Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);
		cipher.init(Cipher.DECRYPT_MODE, getPrivateKey(privateKeyStr));
		byte[] deBytes = cipher.doFinal(text);
		return new String(deBytes);
	}

	public static void main(String[] args) throws Exception {
		String publickeyString = SystemProperties.getInstance().getProperty("encryption.RSAPublicKey");
		publickeyString = StringUtils.trimAllWhitespace(publickeyString);
		String privatekeyString = SystemProperties.getInstance().getProperty("encryption.RSAPrivateKey");
		privatekeyString = StringUtils.trimAllWhitespace(privatekeyString);
		String text = "中华人民共和国";
		text = RSAEncode(text, publickeyString);
		System.err.println(text);
		text = RSADecode(text, privatekeyString);
		System.err.println(text);
	
		
		

	}

}
