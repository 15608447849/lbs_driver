package com.leezp.lib.singlepageapplication.use;

import com.leezp.lib_log.LLog;

/**
 * Created by Leeping on 2018/5/7.
 * email: 793065165@qq.com
 */

public class SpaPrt {
    public static boolean isPrint = false;
    private static String TAG = "单页面框架";
    public static void print(String msg){
        if (isPrint) {
            LLog.print(TAG,"[",msg,"]");
        }
    }
}
