package com.leezp.lib.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.PowerManager;
import android.provider.Settings;

/**
 * Created by Leeping on 2018/5/2.
 * email: 793065165@qq.com
 */

public class PowerUse {
    private String tag;
    private PowerManager.WakeLock wakeLock;
    private PowerManager powerManager;
    public PowerUse(Context context, String tag) {
        this.tag = tag;
        powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
    }



    //获取唤醒锁-保持CUP运转
    public void startPowerWakeLockByCPU() {
        if (wakeLock==null){
            wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, tag);
            wakeLock.acquire(24*60*60*1000L /*24H*/);
        }
    }
    //解除唤醒锁
    public void stopPowerWakeLock() {
        if (wakeLock!=null){
            wakeLock.release();
            wakeLock = null;
        }
    }

    //获取唤醒锁 点亮屏幕
    public void startPowerWakeLockByScreen(){
        if (wakeLock==null){
            wakeLock = powerManager.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.ON_AFTER_RELEASE, tag);
            wakeLock.acquire();
        }
        stopPowerWakeLock();
    }

}
