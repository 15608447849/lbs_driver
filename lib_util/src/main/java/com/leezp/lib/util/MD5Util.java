package com.leezp.lib.util;

import java.security.MessageDigest;

/**
 * Created by user on 2018/3/13.
 */

public class MD5Util {
    /**
     * 获取一段字节数组的md5
     * @param buffer
     * @return
     */
    public static byte[] getBytesMd5(byte[] buffer) {
        byte[] result = null;
        try {
            result =  MessageDigest.getInstance("MD5").digest(buffer);
        } catch (Exception e) {
        }
        return result;
    }

    /**
     * byte->16进制字符串
     * @param bytes
     * @return
     */
    public static String byteToHexString(byte[] bytes) {
        StringBuffer hexStr = new StringBuffer();
        int num;
        for (int i = 0; i < bytes.length; i++) {
            num = bytes[i];
            if(num < 0) {
                num += 256;
            }
            if(num < 16){
                hexStr.append("0");
            }
            hexStr.append(Integer.toHexString(num));
        }
        return hexStr.toString().toUpperCase();
    }


    /**
     * MD5 string 加密
     * @param str
     * @return
     */
    public static String encryption(String str){
        return byteToHexString(getBytesMd5(str.getBytes()));
    }
}
