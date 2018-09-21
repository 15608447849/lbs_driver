package com.leezp.lib_log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Leeping on 2018/8/20.
 * email: 793065165@qq.com
 */

public class LogFileHandler {

    static {
        System.loadLibrary("native-lib");
    }

    void clear(Build build){
        try {
            File folder = new File(build.logFolderPath);
            if (!folder.exists()) return;
            File files[] = folder.listFiles();
            ArrayList<File> delFile = new ArrayList<>();
            long time;
            for (File file : files){
                try {
                    time = System.currentTimeMillis()/1000 - getFileCreateTime(file.getCanonicalPath());
                    if (time > build.storageDays*24*60*60){
                        delFile.add(file);
                    }
                } catch (IOException ignored) {
                }
            }
            //删除文件
            for (File file : delFile){
                file.delete();
            }
        } catch (Exception e) {
        }
    }


    private native long getFileCreateTime(String filePath);

    void handle(Build build,String msg) throws Exception{
        if (!build.isWriteFile) return;

        File folder = new File(build.logFolderPath);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        File file = getLogFile(folder,build.logFileName,build.logFileSizeLimit,0);

        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(file, true);
            fileWriter.append(msg);
            fileWriter.append("\n");
            fileWriter.flush();
        } catch (IOException ignored) {
        }finally {
            if(fileWriter!=null){
                try {fileWriter.close();} catch (IOException ignored) {}
            }
        }

    }

    private File getLogFile(File folder, String fileName, int limit,int index) {
        File newFile = new File(folder, String.format("%s_%s.log", fileName, index));
        if (newFile.exists()) {
           //如果文件存在 - 判断文件大小
           if (newFile.length() >= limit){
               index++;
               return  getLogFile(folder,fileName,limit,index);
           }
        }
        return newFile;
    }
}
