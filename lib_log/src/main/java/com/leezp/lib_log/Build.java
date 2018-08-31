package com.leezp.lib_log;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by Leeping on 2018/8/20.
 * email: 793065165@qq.com
 */

public class Build {
    boolean isWriteConsole = true;//输出控制台
    boolean isWriteFile = false;//写入文件
    boolean isWriteThreadInfo = false; //打印线程信息
    int logFileSizeLimit = 500 * 1024; //500kb
    SimpleDateFormat dateFormat = new SimpleDateFormat("[yyy-MM-dd HH:mm:ss]", Locale.CHINA);
    String logFolderPath = Environment.getExternalStorageDirectory().getAbsolutePath()+ File.separatorChar + "AppLogger";
    String logFileName = "console";
    String tag = "logger";
    int level = Log.VERBOSE;
    int methodLineCount = 0;
    final String BLANK = "^";
    long threadTime = 30 * 1000;

    Build(){}

    public Build setWriteConsole(boolean writeConsole) {
        isWriteConsole = writeConsole;
        return this;
    }

    public Build setWriteFile(boolean writeFile) {
        isWriteFile = writeFile;
        return this;
    }

    public Build setLogFileSizeLimit(int logFileSizeLimit) {
        this.logFileSizeLimit = logFileSizeLimit;
        return this;
    }

    public Build setDateFormat(SimpleDateFormat dateFormat) {
        this.dateFormat = dateFormat;
        return this;
    }

    public Build setLogFolderPath(String logFolderPath) {
        this.logFolderPath = logFolderPath;
        return this;
    }

    public Build setLogFileName(String logFileName) {
        this.logFileName = logFileName;
        return this;
    }

    public Build setTag(String tag) {
        this.tag = tag;
        return this;
    }

    public Build setLevel(int level) {
        this.level = level;
        return this;
    }

    public Build setMethodLineCount(int methodLineCount) {
        if (methodLineCount>4) methodLineCount = 4;
        if (methodLineCount<0) methodLineCount=0;
        this.methodLineCount = methodLineCount;
        return this;
    }

    public Build setThreadTime(long threadTime) {
        this.threadTime = threadTime;
        return this;
    }

    public Build setWriteThreadInfo(boolean writeThreadInfo) {
        isWriteThreadInfo = writeThreadInfo;
        return this;
    }
}
