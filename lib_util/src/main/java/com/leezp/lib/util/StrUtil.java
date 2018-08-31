package com.leezp.lib.util;

import java.util.Locale;

/**
 * Created by Leeping on 2018/6/27.
 * email: 793065165@qq.com
 */

public class StrUtil {
    /**
     * 格式化字符串
     * @param format
     * @param args
     * @return
     */
    public static String stringFormat(String format,Object... args){
        return String.format(Locale.getDefault(),format,args);
    }

    /**
     * 字符串效验
     * @return
     */
    public static boolean validate(String str){
        return str!=null && str.length()>0;
    }
    /**
     * 检测一组字符串是否有效
     */
    public static boolean validateGroup(String... strings) {
        for (String str : strings){
            if (!validate(str)) return false;
        }
        return true;
    }

     public static final String placeholder = "\u3000";
    /**
     *
     * @param str 内容字符
     * @param placeholdersFront 前占位符个数
     * @param placeholdersBack 后占位符个数
     * @return 拼接之后的字符串
     */
    public static String colon(String str,int placeholdersFront,int placeholdersBack){
        StringBuffer s = new StringBuffer();

        if (placeholdersFront>0){
            for (int i=0 ; i<placeholdersFront ; i++){
                s.append(placeholder);
            }
        }
        s.append(str);

        if (placeholdersBack>0){
            for (int i=0 ; i<placeholdersBack ; i++){
                s.append(placeholder);
            }
        }
        return s.toString();
    }
}
