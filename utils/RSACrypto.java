package com.common.utils;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 */
public class RSACrypto {
	public static final String KEY_ALGORITHM = "RSA";
	public static final String SIGNATURE_ALGORITHM = "MD5withRSA";

	private static final String PUBLIC_KEY = "RSAPublicKey";
	private static final String PRIVATE_KEY = "RSAPrivateKey";

	private static final String PUB_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDeC15Mvw0AEn4yFb3hoVZyv3/XmqG3u+SzzzQW9wqdAeryc7tYH2iKcASlt7rhGxdD7qXRqDuoIs15CW303fjewEOYJppbbUJDDo8lCG+HzvuQLi/JnBXR7OP0Aq+HuTmG030yrJc4xZWD+2Z2CGKlm9/QYNhFDXpgzEOMfYd8JQIDAQAB";
	private static final String PPIC_KEY = "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBAN4LXky/DQASfjIV"
											+ "veGhVnK/f9eaobe75LPPNBb3Cp0B6vJzu1gfaIpwBKW3uuEbF0PupdGoO6gizXkJ"
											+ "bfTd+N7AQ5gmmlttQkMOjyUIb4fO+5AuL8mcFdHs4/QCr4e5OYbTfTKslzjFlYP7"
											+ "ZnYIYqWb39Bg2EUNemDMQ4x9h3wlAgMBAAECgYEAj/g1C3PwvPRAqL7aIDrY74vl"
											+ "G2mio5wLfN7DeoRxeD3vFYcemNO8l85r5PtNC3niSuSo1VjMXYc7TNhlWBi4rJg0"
											+ "SHmPGcmyaYv0Sb83GtLSzguKLDE9CleR2duDFZEIVhdnNSckw+lGjKHhUZDn3YJl"
											+ "Iyu82xugf21IRp6ZVB0CQQDv0UowErQHF8M87wNNFGPBg7PM7v+KvuwW7E6dmJdF"
											+ "3VUtQ9tgsEeh653HTEuk/EzcMsJL8UztnZhtm4nisCqbAkEA7QcO1T85mng+iPDO"
											+ "1Nc0HUp0b86YnCYn6kTS2o5bWuUnmJCJXTqoiyf5ln0tCB0XjkoCKnhIGNouBdg2"
											+ "hNcAPwJBALHEbj3cX16ymqLceZH1f0y/F9TdhMeQZulHGa9woRHE05LOLhwqD69T"
											+ "PI2zXI9cBo1jgutQXzn0DD2Tgk5rT+kCQQCHYzKrx7Nlci12FuZtWIch+/dKLd2p"
											+ "w3mlLK6rvAiegKn/UP/FSs4evjSoKtPgAI4F7mwSSiYUW/tRx1BMzGoNAkBDfojb"
											+ "NamSXJiih1whEFLlI8Id/tphEHADgroRs6FQ3GYVaM1BEZqxnPSabfd1+uJ6nQ28" + "LEP9LDC/fhO/xQfk";
	//private static final String PPIC_KEY = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAL/0gcV5ZXs4EQ1eJpVEpjuxHBBfuNk7HIBO28wUUlY6eagzrD1FqKO6s72okj9SIc3E3ad7ZgKhS60PkWHg9sdI4pv+SEKmK6s9ZMFWMyZFg/wLco94kg5a+/h73IkN8tg0M0PqNAc/GJRMZAKOqFryt9I3iEKw6FHcYcYvK4GDAgMBAAECgYBi3THBXYbw8a2Grwk8NAFSGSQVItf3ukQxqWv3LjhCBRgHk6WQYUI8IbrVYe8BAfI7IrvuuNWPYTeyyKPeP5mZoSKn9lNtREEzavMFRJEGm4yRMLoeX46BBohqvrIExLqV/HW0Po0BndpkbUd133Xhj/3IV92d0J7BhriVksAh4QJBAPJ2LtqByifDJRZH1Jg4Ou85LOuB/scKPh37eYYZxWm67U5GlVKVlsqCz1SMxrXnTCi4hdILOq+OyAj0Kha4UBsCQQDKrF5jcUG49JfWlGft4VnxI6dEYT6DOrjqlIQvShZN9avGFoq7WLkm4FnlSlrA1cz1WqA4e8o06yfTYb7lsrq5AkEAwIzWnLTiJD23oEAfN1vAgea4I7ey68LYndruIW8p9dN7TeSEBXfCrsS/fnfl/uPW+afArpQ4YN9VQLcXDcAF7QJAP+KJdisTHmvrGLZ69B7erQYiCYnlnJP8CeV6rTVbRoOz+6LLZDaZVknNzFrdtH8wNf61Flje6kNdsGHVPK5T8QJAYq+hrDwBI/J+SfBucuznN7NEH+6FPB/ypqQD69A9/myHpdQavrD41Pbiq6nYAbNohv1vMuuxFV3p1361x+EGZQ==";

	public static byte[] decryptBASE64(String key) {
		return Base64.decodeBase64(key);
	}

	public static String encryptBASE64(byte[] bytes) {
		return Base64.encodeBase64String(bytes);
	}

	/**
	 * 用私钥对信息生成数字签名
	 *
	 * @param data
	 *            加密数据
	 * @param privateKey
	 *            私钥
	 * @return
	 * @throws Exception
	 */
	public static String sign(byte[] data, String privateKey) throws Exception {
		// 解密由base64编码的私钥
		byte[] keyBytes = decryptBASE64(privateKey);
		// 构造PKCS8EncodedKeySpec对象
		PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
		// KEY_ALGORITHM 指定的加密算法
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		// 取私钥匙对象
		PrivateKey priKey = keyFactory.generatePrivate(pkcs8KeySpec);
		// 用私钥对信息生成数字签名
		Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
		signature.initSign(priKey);
		signature.update(data);
		return encryptBASE64(signature.sign());
	}

	/**
	 * 校验数字签名
	 *
	 * @param data
	 *            加密数据
	 * @param publicKey
	 *            公钥
	 * @param sign
	 *            数字签名
	 * @return 校验成功返回true 失败返回false
	 * @throws Exception
	 */
	public static boolean verify(byte[] data, String publicKey, String sign) throws Exception {
		// 解密由base64编码的公钥
		byte[] keyBytes = decryptBASE64(publicKey);
		// 构造X509EncodedKeySpec对象
		X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
		// KEY_ALGORITHM 指定的加密算法
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		// 取公钥匙对象
		PublicKey pubKey = keyFactory.generatePublic(keySpec);
		Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
		signature.initVerify(pubKey);
		signature.update(data);
		// 验证签名是否正常
		return signature.verify(decryptBASE64(sign));
	}

	public static byte[] decryptByPrivateKey(byte[] data, String key) throws Exception {
		// 对密钥解密
		byte[] keyBytes = decryptBASE64(key);
		// 取得私钥
		PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		Key privateKey = keyFactory.generatePrivate(pkcs8KeySpec);
		// 对数据解密
		Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
		cipher.init(Cipher.DECRYPT_MODE, privateKey);
		return cipher.doFinal(data);
	}

	/**
   * 解密<br>
   * 用私钥解密
   *
   * @param data
   * @param key
   * @return
   * @throws Exception
   */
  public static byte[] decryptByPrivateKey(String data)
      throws Exception {
	  String key=PPIC_KEY;
    return decryptByPrivateKey(decryptBASE64(data),key);
  }

	/**
	 * 解密<br>
	 * 用公钥解密
	 *
	 * @param data
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static byte[] decryptByPublicKey(byte[] data, String key) throws Exception {
		// 对密钥解密
		byte[] keyBytes = decryptBASE64(key);
		// 取得公钥
		X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		Key publicKey = keyFactory.generatePublic(x509KeySpec);
		// 对数据解密
		Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
		cipher.init(Cipher.DECRYPT_MODE, publicKey);
		return cipher.doFinal(data);
	}

	/**
	 * 加密<br>
	 * 用公钥加密
	 *
	 * @param data
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static byte[] encryptByPublicKey(String data, String key) throws Exception {
		// 对公钥解密
		byte[] keyBytes = decryptBASE64(key);
		// 取得公钥
		X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		Key publicKey = keyFactory.generatePublic(x509KeySpec);
		// 对数据加密
		Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
		cipher.init(Cipher.ENCRYPT_MODE, publicKey);
		return cipher.doFinal(data.getBytes());
	}

	/**
	 * 加密<br>
	 * 用私钥加密
	 *
	 * @param data
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static byte[] encryptByPrivateKey(byte[] data, String key) throws Exception {
		// 对密钥解密
		byte[] keyBytes = decryptBASE64(key);
		// 取得私钥
		PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		Key privateKey = keyFactory.generatePrivate(pkcs8KeySpec);
		// 对数据加密
		Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
		cipher.init(Cipher.ENCRYPT_MODE, privateKey);
		return cipher.doFinal(data);
	}

	/**
	 * 取得私钥
	 *
	 * @param keyMap
	 * @return
	 * @throws Exception
	 */
	public static String getPrivateKey(Map<String, Key> keyMap) throws Exception {
		Key key = (Key) keyMap.get(PRIVATE_KEY);
		return encryptBASE64(key.getEncoded());
	}

	/**
	 * 取得公钥
	 *
	 * @param keyMap
	 * @return
	 * @throws Exception
	 */
	public static String getPublicKey(Map<String, Key> keyMap) throws Exception {
		Key key = keyMap.get(PUBLIC_KEY);
		return encryptBASE64(key.getEncoded());
	}

	/**
	 * 初始化密钥
	 *
	 * @return
	 * @throws Exception
	 */
	public static Map<String, Key> initKey() throws Exception {
		KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(KEY_ALGORITHM);
		keyPairGen.initialize(1024);
		KeyPair keyPair = keyPairGen.generateKeyPair();
		Map<String, Key> keyMap = new HashMap(2);
		keyMap.put(PUBLIC_KEY, keyPair.getPublic());// 公钥
		keyMap.put(PRIVATE_KEY, keyPair.getPrivate());// 私钥
		return keyMap;
	}
}