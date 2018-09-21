package com.leezp.lib.util;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.leezp.lib_log.LLog;

/**
 * Created by Leeping on 2018/5/2.
 * email: 793065165@qq.com
 * 心跳服务
 */

public abstract class HearServer extends Service implements Runnable{
    private PendingIntent pendingIntentOp; //闹钟使用
    private PowerUse power; //电源管理
    private FrontNotification notification;//前台通知栏
    private Thread thread;
    private volatile boolean isRun = true;
    private long interval = 20 * 1000L;

    @Override
    public void onCreate() {
        power = new PowerUse(getApplicationContext(),getClass().getSimpleName());
        notification = createNotification();
        notification.showNotification();
        initCreate();
        thread = new Thread(this);
        thread.setDaemon(true);
        thread.start();
    }

    protected void initCreate(){}

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        unlockSelf();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        isRun = false;
        unlockSelf();
        notification.cancelNotification();
    }

    private FrontNotification createNotification(){
        return new FrontNotification.Build(getApplicationContext(),getNotificationId())
                .setIntent(getOpenActivityClass())
                .setFlags(new int[]{Notification.FLAG_FOREGROUND_SERVICE,Notification.FLAG_NO_CLEAR})
                .setGroup(getNotificationGroupKey())
                .autoGenerateNotification(
                        getNotificationTitle(),
                        getNotificationContent(),
                        getNotificationInfo(),
                        getNotificationIcon(),
                        Notification.DEFAULT_ALL);
    }

    protected abstract int getNotificationIcon();

    protected abstract String getNotificationGroupKey();

    protected abstract Class<?> getOpenActivityClass();

    protected abstract int getNotificationId();

    @Override
    public void run() {

        while (isRun){
            power.startPowerWakeLockByCPU();
            try {
                executeTask();
            } catch (Exception e) {
                e.printStackTrace();
            }
            power.stopPowerWakeLock();
            startAlarmManagerHeartbeat();//开始闹钟
            lockSelf();
        }

    }

    protected abstract void executeTask();

    public void startAlarmManagerHeartbeat() {
        if (pendingIntentOp==null){
            Intent intent = new Intent();
            intent.setClass(getApplicationContext(), this.getClass());
            pendingIntentOp = PendingIntent.getService(getApplicationContext(), 0, intent, 0);
        }
        AlarmManager am = (AlarmManager) getApplicationContext().getSystemService(ALARM_SERVICE);
        assert am!=null;
        am.setExact(AlarmManager.RTC_WAKEUP, getNextTime(),  pendingIntentOp);
    }
    private long getNextTime() {
        long now = System.currentTimeMillis();
//        private boolean isFirst = true;
//        if (isFirst){
//            isFirst = false;
//            return now + 60 * 1000 - now % (60 * 1000);
//        }
        return now + interval;
    }

    /**
     *
     * @param i 秒
     */
    public void setInterval(int i) {
        interval = i * 1000L;
    }

    private void lockSelf() {
        synchronized (HearServer.this){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    private void unlockSelf(){
        synchronized (HearServer.this){
                notify();
        }
    }


    protected String getNotificationTitle() {
        return "心跳服务";
    }

    protected String getNotificationContent() {
        return "连接存活";
    }

    protected String getNotificationInfo() {
        return "请勿关闭";
    }
}
