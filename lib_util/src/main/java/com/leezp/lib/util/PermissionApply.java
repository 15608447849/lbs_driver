package com.leezp.lib.util;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.PowerManager;
import android.provider.Settings;

import com.leezp.lib_log.LLog;

import java.util.Arrays;

/**
 * Created by Leeping on 2018/6/20.
 * email: 793065165@qq.com
 */

public class PermissionApply {
     public interface Callback{
         void onPermissionsGranted();
     }

    private Activity activity;

    private boolean isPermissionsDenied = true ;//假设权限被拒绝

    private final int SDK_PERMISSION_REQUEST = 127;

    private final int SDK_POWER_REQUEST = 128;

    private final int OVERLAY_PERMISSION_REQ_CODE = 129;

    private final String[] permissions ;

    private final PermissionApply.Callback callback;

    public PermissionApply(Activity activity, PermissionApply.Callback callback, String[] permissions) {
        this.activity = activity;
        this.permissions = permissions;
        this.callback = callback;
        check();
    }

    public void destroy(){
        this.activity = null;
    }


    //检测
    private void check() {

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (getPermissions()){ //  isIgnoreBatteryOption() &&  && askFloatWindowPermission()
                callback.onPermissionsGranted();
            }
        }else{
            callback.onPermissionsGranted();
        }

    }
    /**
     * 请求用户给予悬浮窗的权限
     */
    @TargetApi(23)
    private boolean askFloatWindowPermission() {
        if (!Settings.canDrawOverlays(activity)) {
            final DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                    if (which == DialogInterface.BUTTON_POSITIVE){
                        openFloatWindowPower();
                    }else if (which == DialogInterface.BUTTON_NEGATIVE){
                        System.exit(0);
                    }
                }
            };
            openAppDetails(listener);
            return false;
        }
        return true;
    }

    private void openFloatWindowPower() {
        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:" + activity.getPackageName()));
        activity.startActivityForResult(intent, OVERLAY_PERMISSION_REQ_CODE);
    }

    /**
     * 针对M以上的Doze模式
     * 忽略电池的优化
     <!--可以直接弹出一个系统对话框让用户直接添加app到白名单-->
     <uses-permission android:name="android.permission.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS" />
     */
    @TargetApi(23)
    private boolean isIgnoreBatteryOption() {

        PowerManager pm = (PowerManager)activity. getSystemService(Context.POWER_SERVICE);
        if (pm == null) return false;
        if (!pm.isIgnoringBatteryOptimizations(activity.getPackageName())) {
            Intent intent = new Intent();
            intent.setAction(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
            intent.setData( Uri.parse("package:" + activity.getPackageName()));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            activity.startActivityForResult(intent,SDK_POWER_REQUEST);
            LLog.print("权限", "isIgnoreBatteryOption");
            return false;
        }
        return true;
    }

    //获取权限
    @TargetApi(23)
    private boolean getPermissions() {

        isPermissionsDenied = false;
        for (int i = 0; i < permissions.length; i++){
            if (activity.checkSelfPermission(permissions[i]) == PackageManager.PERMISSION_DENIED){
                isPermissionsDenied = true;
                break;
            }
        }

        if(isPermissionsDenied){
            activity.requestPermissions(permissions, SDK_PERMISSION_REQUEST);
            LLog.print("权限","需要请求权限");
            return false;
        }
        return true;
    }

    /** activity 权限申请回调 */
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){
        LLog.print("权限", "结果:\n"+Arrays.toString(permissions)+"\n"+Arrays.toString(grantResults) );
        if (requestCode == SDK_PERMISSION_REQUEST) {
            isPermissionsDenied = false; //假设授权没有被拒绝
            for (int result : grantResults) {
                if (result == PackageManager.PERMISSION_DENIED) {
                    isPermissionsDenied = true;//发现有一个权限未授予,则无权限访问
                    break;
                }
            }
            if (isPermissionsDenied) {
                final DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        if (which == DialogInterface.BUTTON_POSITIVE){
                            startSysSettingActivity();
                        }else if (which == DialogInterface.BUTTON_NEGATIVE){
                            System.exit(0);
                        }
                    }
                };
                openAppDetails(listener);
            } else {
                callback.onPermissionsGranted();
            }
        }
    }

    //提示框 >> 打开系统应用
    @SuppressLint("WrongConstant")
    private void openAppDetails(DialogInterface.OnClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("应用授权") ;//设置标题
        builder.setMessage("您拒绝了相关权限,将无法正常使用,请手动授予") ;//设置内容
        PackageManager pkm = activity.getPackageManager();
        Drawable mAppicon = null;
        try {
            mAppicon = pkm.getActivityInfo(activity.getComponentName(), ActivityInfo.FLAG_STATE_NOT_NEEDED).loadIcon(pkm);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        builder.setIcon(mAppicon);//设置图标，

        builder.setPositiveButton("手动授权",listener);
        builder.setNegativeButton("退出应用",listener);
        builder.setCancelable(false);
        builder.create().show();
    }
    //打开系统应用
    private void  startSysSettingActivity() {
        Intent intent = new Intent();
        intent.setAction( Settings.ACTION_APPLICATION_DETAILS_SETTINGS) ;
        intent.setData(Uri.parse("package:" + activity.getPackageName()));
        intent.addCategory(Intent.CATEGORY_DEFAULT);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
//        intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
//        activity.startActivity(intent);
        activity.startActivityForResult(intent,SDK_POWER_REQUEST);
    }

    /**
     * activity 回调
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if (requestCode == SDK_POWER_REQUEST || requestCode == OVERLAY_PERMISSION_REQ_CODE){
            check();
        }
    }
}
