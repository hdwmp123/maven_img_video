package com.king.test;


import org.apache.commons.codec.binary.Base64;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder; 
 
/** 
 * 对url加密的加密解密算法，这样的加密结果只有数字和字母 
 * @author Administrator 
 * 
 */ 
public class Base64Encoding { 
    private static final BASE64Decoder decoder = new BASE64Decoder(); 
    private static final BASE64Encoder encoder = new BASE64Encoder(); 
    private static final Base64 base64 = new Base64(); 
 
    /** 
     * BASE64加密 
     *  
     * @param key 
     * @return 
     * @throws Exception 
     */ 
    public static String encryptBASE64(String key) throws Exception { 
        if (key == null || key.length() < 1) { 
            return ""; 
        } 
        // return new String(encoder.encode(key.getBytes())); 
        // return new String(base64.encodeBase64URLSafe((new String(encoder.encode(key.getBytes()))).getBytes())); 
        return new String(base64.encodeBase64URLSafe((key).getBytes())); 
    } 
     
     
    public void changeCodeGBK(){ 
         
    } 
    /** 
     * BASE64解密 
     *  
     * @param key 
     * @return 
     * @throws Exception 
     */ 
    public static String decryptBASE64(String key) throws Exception { 
        if (key == null || key.length() < 1) { 
            return ""; 
        } 
        return new String(decoder.decodeBuffer(new String(base64.decodeBase64(key.getBytes())))); 
        //return new String(base64.decodeBase64(key.getBytes())); 
    } 
     
    public static void main(String[] args) throws Exception { 
        String s=Base64Encoding.encryptBASE64("仅限于方石榴平台,不做其他用途"); 
        System.out.println(s); 
        System.out.println(Base64Encoding.decryptBASE64(s)); 
    } 
} 
