package com.example.com.jglx.android.app.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/*******************************************************************************************************
* @ClassName: MD5 
* @Description: TODO(这里用一句话描述这个类的作用) 
* @author lilifeng
* @date 2015年8月8日 下午5:59:28 
*  
********************************************************************************************************
*/ 
public class MD5 {
	// 加解密统一使用的编码方式
		private final static String encoding = "utf-8";
	 /**利用MD5进行加密
     * @param str  待加密的字符串
     * @return  加密后的字符串
     * @author lilifeng
     * @throws NoSuchAlgorithmException  没有这种产生消息摘要的算法
     * @throws UnsupportedEncodingException  
     */
    public static String EncoderByMd5(String str) throws NoSuchAlgorithmException, UnsupportedEncodingException{
        //确定计算方法
        MessageDigest md5=MessageDigest.getInstance("MD5");
        //加密后的字符串
        byte[] bytes=md5.digest(str.getBytes(encoding));
        String newstr = byteArrayToString(bytes);
        System.out.println(newstr);
        return newstr;
    }
    
    /**
     * @param byteArray
     * @return String
     * @author lilifeng
     * @return
     */
    public static String byteArrayToString(byte[] byteArray) {  

        // 首先初始化一个字符数组，用来存放每个16进制字符  
        char[] hexDigits = {'0','1','2','3','4','5','6','7','8','9', 'A','B','C','D','E','F' };  
        // new一个字符数组，这个就是用来组成结果字符串的（解释一下：一个byte是八位二进制，也就是2位十六进制字符（2的8次方等于16的2次方））  
        char[] resultCharArray =new char[byteArray.length * 2];  
        // 遍历字节数组，通过位运算（位运算效率高），转换成字符放到字符数组中去  
        int index = 0; 
        for (byte b : byteArray) {  
           resultCharArray[index++] = hexDigits[b>>> 4 & 0xf];  
           resultCharArray[index++] = hexDigits[b& 0xf];  
        }
        // 字符数组组合成字符串返回  
        return new String(resultCharArray);  
    }


}
