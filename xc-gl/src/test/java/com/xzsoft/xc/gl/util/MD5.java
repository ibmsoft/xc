package com.xzsoft.xc.gl.util;

import java.security.MessageDigest;

public class MD5 {
	/***
	 * 
	  * @Title: string2MD5 方法名
	  * @Description：MD5加码 生成32位md5码 
	  * @param：@param inStr
	  * @param：@return 设定文件
	  * @return String 返回类型
	 */
    public static String string2MD5(String inStr){  
        MessageDigest md5 = null;  
        try{  
            md5 = MessageDigest.getInstance("MD5");  
        }catch (Exception e){  
            System.out.println(e.toString());  
            e.printStackTrace();  
            return "";  
        }  
        char[] charArray = inStr.toCharArray();  
        byte[] byteArray = new byte[charArray.length];  
  
        for (int i = 0; i < charArray.length; i++)  
            byteArray[i] = (byte) charArray[i];  
        byte[] md5Bytes = md5.digest(byteArray);  
        StringBuffer hexValue = new StringBuffer();  
        for (int i = 0; i < md5Bytes.length; i++){  
            int val = ((int) md5Bytes[i]) & 0xff;  
            if (val < 16)  
                hexValue.append("0");  
            hexValue.append(Integer.toHexString(val));  
        }  
        return hexValue.toString();  
    }  
    /**
     *  
      * @Title: convertMD5 方法名
      * @Description：加密解密算法 执行一次加密，两次解密 
      * @param：@param inStr
      * @param：@return 设定文件
      * @return String 返回类型
     */
    public static String convertMD5(String inStr){  
        char[] a = inStr.toCharArray();  
        for (int i = 0; i < a.length; i++){  
            a[i] = (char) (a[i] ^ 't');  
        }  
        String s = new String(a);  
        return s;  
    }  
    /**
     * 
      * @Title: main 方法名
      * @Description：测试主函数  
      * @param：@param args 设定文件
      * @return void 返回类型
     */
    public static void main(String args[]) {  
        String s = new String("30700ade3a17bf9ef77e255a2c0056ee");
        System.out.println("原始：" + s);
        System.out.println("MD5后：" + string2MD5(s));
        System.out.println("加密的：" + convertMD5("a860f779eeb2b29720fac2237f623bae"));
        System.out.println("解密的：" + convertMD5(convertMD5(s)));
    }  
}