package com.aramis.library.utils.md5;

import java.io.IOException;

/**
 * 
 *
 */
public class NslmCryptoUtils {
	public final static String KEY="fyhnslm!@#$1";
	/**
	 * 加密
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public static String encrypt(String data) throws Exception{
		return DesUtil.encrypt(data, KEY);
	}
	/**
	 * 解密
	 * @param data
	 * @return
	 * @throws IOException
	 * @throws Exception
	 */
	public static String decrypt(String data) throws IOException, Exception{
		return DesUtil.decrypt(data, KEY);
	}
	
//	public static void main(String[] args) {
//        String pwd = "123456";
//        try {
//            System.out.println("==================:"+encrypt(pwd));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
}
