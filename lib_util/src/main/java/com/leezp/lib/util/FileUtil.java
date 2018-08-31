package com.leezp.lib.util;

import java.io.File;

/**
 * Created by Leeping on 2018/7/31.
 * email: 793065165@qq.com
 */

public class FileUtil {
    /**检查目录 存在返回true
     * 不存在自动创建
     * */
    public static boolean isFolderExist(String dir,boolean autoCreate){
        File dirs = new File(dir);
        if(!dirs.exists() && autoCreate){
            return dirs.mkdirs();
        }
        return true;
    }
}
