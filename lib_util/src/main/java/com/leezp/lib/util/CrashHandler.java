package com.leezp.lib.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by user on 2018/3/6.
 * UncaughtException处理类,当程序发生Uncaught异常的时候,有该类来接管程序,并记录发送错误报告.
 */

public class CrashHandler implements Thread.UncaughtExceptionHandler {
    private CrashHandler() {
    }
    private static class Hodler{private static final CrashHandler INSTANCE = new CrashHandler(); } //CrashHandler实例
    /** 获取CrashHandler实例 ,单例模式 */
    public static CrashHandler getInstance() {
        return Hodler.INSTANCE;
    }

    //程序的Context对象
    private Context mContext;
    //用来存储设备信息和异常信息
    private final Map<String, String> devInfos = new HashMap<>();

    //系统默认的UncaughtException处理类
    private Thread.UncaughtExceptionHandler mDefaultHandler;

    /**
     * 初始化
     *
     * @param context
     */
    public void init(Context context) {
        if (context==null) throw new NullPointerException("app context is null.");
        mContext = context;
        //获取系统默认的UncaughtException处理器
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        //设置该CrashHandler为程序的默认处理器
        Thread.setDefaultUncaughtExceptionHandler(this);
        //收集设备参数信息
        collectDeviceInfo();
    }
    /**
     * 初始化
     *
     * @param context
     */
    public void init(Context context, Callback callback) {
        this.init(context);
        setCallback(callback);
    }
    private Callback callback;

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    private void collectDeviceInfo() {
        try {
            PackageManager pm = mContext.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(mContext.getPackageName(), PackageManager.GET_ACTIVITIES);
            if (pi != null) {
                String versionName = pi.versionName == null ? "null" : pi.versionName;
                String versionCode = pi.versionCode + "";
                devInfos.put("应用信息",mContext.getPackageName()+"-"+versionName+"-"+versionCode);
            }
        } catch (PackageManager.NameNotFoundException e) {
        }
        devInfos.put("系统生厂商", Build.BRAND);
        devInfos.put("硬件制造商", Build.MANUFACTURER);
        devInfos.put("型号", Build.MODEL);
        devInfos.put("cpu_abi", Arrays.toString(Build.SUPPORTED_ABIS));
        devInfos.put("指纹",Build.FINGERPRINT);
        devInfos.put("序列号",Build.SERIAL);
        devInfos.put("系统时间", TimeUtil.formatUTC(Build.TIME,null));
        devInfos.put("安卓系统版本号",Build.VERSION.RELEASE);
        devInfos.put("安卓SDK",Build.VERSION.SDK_INT+"");
        devInfos.put("分辨率",mContext.getResources().getDisplayMetrics().toString());

    }

    /**
     * 当UncaughtException发生时会转入该函数来处理
     */
    @Override
    public void uncaughtException(Thread t, Throwable e) {

        if (!handleException(e) && mDefaultHandler != null) {
            //如果应用程序没有处理则让系统默认的异常处理器来处理
            mDefaultHandler.uncaughtException(t, e);
        }
    }
    /**
     * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.
     *
     * @param ex
     * @return true:如果处理了该异常信息;否则返回false.
     */
    private boolean handleException(Throwable ex) {
        if (ex == null) return false;
        StringBuffer sb = printDevInfo();
        sb.append(ErrorUtil.printExceptInfo(ex));
        try {
            //保存文件
            String fileName = TimeUtil.formatUTC(new Date().getTime(),null) + ".log";
            String filePath = mContext.getFilesDir()+File.separator+"crash";
            File dirFile = new File(filePath);
            if (!dirFile.exists()){
                dirFile.mkdirs();
            }

            final String fpStr = dirFile.getCanonicalFile()+File.separator+fileName;
            FileOutputStream fos = new FileOutputStream(fpStr);
            fos.write(sb.toString().getBytes());
            fos.close();
            //发送日志 node
            if (callback != null){
                callback.crash(fpStr,ex);
            }
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    private StringBuffer printDevInfo() {
        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, String> entry : devInfos.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            sb.append(key + "=" + value + "\n");
        }
        return sb;
    }

    public interface Callback{
        void crash(String crashFilePath,Throwable ex);
    }

}
