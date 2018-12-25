package com.leezp.lib.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.os.Looper;
import android.support.design.widget.Snackbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

/**
 * Created by Leeping on 2018/4/16.
 * email: 793065165@qq.com
 */

public class AppUtil {

    public static void showSnackBar(final View rootView,final String msg,final int tag,
                                    final String bgColor,final String textColor,
                                    final String actionText, final View.OnClickListener action){
        rootView.post(
                new Runnable() {
                    @Override
                    public void run() {
                        Snackbar snackbar = Snackbar.make(rootView, msg, tag);
                        if (bgColor!=null){
                            View v = snackbar.getView();
                            v.setBackgroundColor(Color.parseColor(bgColor));
                            if (textColor!=null){
                                TextView textView =  v.findViewById(R.id.snackbar_text);
                                textView.setTextColor(Color.parseColor(textColor));//更改文本颜色
                            }
                        }
                        if (textColor!=null) snackbar.setActionTextColor(Color.parseColor(textColor));
                        if (actionText!=null && action!=null ) snackbar.setAction(actionText,action);
                        snackbar.show();
                    }
                }
        );
    }

    public static void showLongSnackBar(final View rootView, final String s) {
        showLongSnackBar(rootView,s,"#ECEFF1","#448AFF");
    }

    public static void showLongSnackBar( View rootView,  String s,String bgColor,String textColor) {
        showSnackBar(rootView,s,Snackbar.LENGTH_LONG,bgColor,textColor,null,null);
    }

    public static void showSnackBar( View rootView,  String s) {
        showSnackBar(rootView,s,"#ECEFF1","#448AFF");
    }
    public static void showSnackBar( View rootView,  String s,String bgcolor,String textcolor) {
        showSnackBar(rootView,s,Snackbar.LENGTH_SHORT,bgcolor,textcolor,null,null);
    }

    /**
     * 隐藏软键盘
     * @param activity
     */
    public static void hideSoftInputFromWindow(Activity activity){
        try {
                       View v = activity.getCurrentFocus();

            if (v!=null && v.getWindowToken()!=null){
                InputMethodManager inputMethodManager =  ((InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE));
                if (inputMethodManager!=null)  inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     *是否打开无线模块
     * @param context
     * @return
     */
    public static boolean isOpenWifi(Context context){
        WifiManager mWifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        return mWifiManager.isWifiEnabled();
    }

    /**
     * @param context 上下文
     * @return 仅仅是用来判断网络连接
     */
    @SuppressLint("MissingPermission")
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            try {
                return cm.getActiveNetworkInfo().isAvailable();
            } catch (Exception ignored) {
            }
        }
        return false;
    }

    /**
     * 判断GPS是否开启，GPS或者AGPS开启一个就认为是开启的
     * @param context
     * @return true 表示开启
     */
    public static boolean isOenGPS(Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        assert locationManager != null;
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }
    //打开GPS设置界面
    public static void openGPS(Context context){
        Intent intent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
        // 打开GPS设置界面
        context.startActivity(intent);
    }

    //检查UI线程
    public static boolean checkUIThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }


    /**
     * 判断 跟视图 是否存在 指定的 view 类类型
     */
    public static boolean checkViewRootExistTargetViewClassType(View viewRoot, Class viewClass) {
//        Log.w("判断类类型",viewRoot.getClass().getCanonicalName());
        if (viewRoot.getClass().getName().equals(viewClass.getName())){
            return true;
        }else
        if (viewRoot instanceof ViewGroup){
             ViewGroup vp = (ViewGroup) viewRoot;

             for (int i = 0; i< vp.getChildCount();i++){
                 View view = vp.getChildAt(i);
                 if (checkViewRootExistTargetViewClassType(view,viewClass)){
                     return true;
                 }
             }
        }
            return false;
    }

    /**
     * 限制edittext 不能输入中文
     * @param editText
     */
    public static void setEditTextInhibitingChinese(final EditText editText){
        TextWatcher textWatcher=new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String txt = s.toString();
                //注意返回值是char数组
                char[] stringArr = txt.toCharArray();
                for (int i = 0; i < stringArr.length; i++) {
                    //转化为string
                    String value = new String(String.valueOf(stringArr[i]));
                    Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
                    Matcher m = p.matcher(value);
                    if (m.matches()) {
                        editText.setText(editText.getText().toString().substring(0, editText.getText().toString().length() - 1));
                        editText.setSelection(editText.getText().toString().length());
                        return;
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        editText.addTextChangedListener(textWatcher);
    }
    public static void setEditTextInhibitingChinese(EditText... editTexts){
        for (EditText editText : editTexts) setEditTextInhibitingChinese(editText);
    }

    /**
     * 获取当前进程名
     */
    public static String getCurrentProcessName(Context context) {
        int pid = android.os.Process.myPid();
        String processName = "";
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (manager!=null){
            for (ActivityManager.RunningAppProcessInfo process : manager.getRunningAppProcesses()) {
                if (process.pid == pid) {
                    processName = process.processName;
                }
            }
        }
        return processName;
    }

    /**
     * 判断当前进程是否是主进程
     */
    public static boolean checkCurrentIsMainProgress(Context context){
        return checkCurrentIsMainProgress(context,AppUtil.getCurrentProcessName(context));
    }
    public static boolean checkCurrentIsMainProgress(Context context,String currentProgressName){
        return context.getPackageName().equals(currentProgressName);
    }

    /**
     * 获取外置卡的文件临时存储路径
     * @param context 上下文
     * @param fileDir 指定的文件目录
     * @param fileName 指定的文件名
     * @return
     */
    public static String getExternalStorageTempFilePath(Context context, String fileDir, String fileName) {
        try {
            if (!StrUtil.validate(fileDir)) fileDir = context.getPackageName()+ File.separator+"temp";
            if (!StrUtil.validate(fileName)) fileName = TimeUtil.formatUTCByCurrent("yyyyMMddhhmmss");
            String dir =  Environment.getExternalStorageDirectory().getCanonicalPath();
            dir += File.separator + fileDir;
            if (!FileUtil.isFolderExist(dir,true)){
                throw  new FileNotFoundException(dir);
            }
            File file = new File(dir + File.separator + fileName);
            if (!file.exists()){
                boolean isCreated = file.createNewFile();
                if (!isCreated) return null;
            }
            return file.getCanonicalPath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 拨打电话
     *
     <uses-permission android:name="android.permission.CAMERA" />
     */
    public static void callPhoneNo(Context context,String phoneNo){
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:"+phoneNo));
        context.startActivity(intent);
    }

    //获取应用版本号
    public static int getVersionCode(Context ctx) {
        // 获取packagemanager的实例
        int version = 0;
        try {
            PackageManager packageManager = ctx.getPackageManager();
            PackageInfo packInfo = packageManager.getPackageInfo(ctx.getPackageName(), 0);
            version = packInfo.versionCode;
        } catch (Exception e) {
        }
        return version;
    }
    //获取应用版本名
    public static String getVersionName(Context ctx) {
        // 获取packagemanager的实例
        String version = "";
        try {
            PackageManager packageManager = ctx.getPackageManager();
            PackageInfo packInfo = packageManager.getPackageInfo(ctx.getPackageName(), 0);
            version = packInfo.versionName;
        } catch (Exception e) {
        }
        return version;
    }

    /** 检测设备是否存在Camera硬件
     * FEATURE_CAMERA：设备是否有摄像头。
     * FEATURE_CAMERA_ANY：设备至少有一个摄像头。
     * FEATURE_CAMERA_AUTOFOCUS：设备支持的摄像头是否支持自动对焦
     * FEATURE_CAMERA_FLASH：设备是否配备闪光灯。
     * FEATURE_CAMERA_FRONT：设备是否有一个前置摄像头。
     * */
    public static boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            //存在
            return true;
        } else {
            // 不存在
            return false;
        }
    }

}
