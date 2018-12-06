package com.leezp.lib.util;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

/**
 * Created by Leeping on 2018/5/2.
 * email: 793065165@qq.com
 */

public class FrontNotification {

    private FrontNotification.Build build;

    private FrontNotification(FrontNotification.Build build){
        this.build = build;
    }
    private static final String NOTIFICATION_CHANNEL_ID = "leePing.foreground.notification";
    private static final String NOTIFICATION_CHANNEL_NAME = "ANDROID CHANNEL";


    public static class Build{
        Context context;
        int id = 1000;
        Notification notification;
        NotificationManager notificationManager;
        Intent activityIntent;//点击打开指定Activity
        int[] flags = new int[]{Notification.FLAG_FOREGROUND_SERVICE,Notification.FLAG_NO_CLEAR};
        int defaults = Notification.DEFAULT_LIGHTS;
        String groupKey = "default";
        Intent serviceIntent;//点击打开指定服务
        int smallIcon = -1;
        int bigIcon = -1;
        RemoteViews view;
        public Build(Context context){
            this.context = context;
            this.id = id;
            this.notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            //android 8.0
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                NotificationChannel notificationChannel = new NotificationChannel(
                        NOTIFICATION_CHANNEL_ID,
                        NOTIFICATION_CHANNEL_NAME,
                        NotificationManager.IMPORTANCE_HIGH);
                notificationChannel.setDescription("android 8.0 消息通知");
                notificationChannel.setShowBadge(true); //是否在久按桌面图标时显示此渠道的通知
                notificationChannel.enableLights(true);//是否在桌面icon右上角展示小圆点
                notificationChannel.setLightColor(Color.RED);//小圆点颜色
                notificationChannel.enableVibration(true);//允许震动
                notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
                notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
                notificationManager.createNotificationChannel(notificationChannel);
            }
        }

        public FrontNotification.Build setId(int id){
            this.id = id;
            return this;
        }

        public FrontNotification.Build setActivityIntent(Class<?> destCls){
            if (destCls!=null){
                activityIntent = new Intent(context, destCls);
            }
            return this;
        }

        public FrontNotification.Build setActivityIntent(String action, String scheme){
            activityIntent = new Intent(action, Uri.parse(scheme));
            return this;
        }

        public FrontNotification.Build setActivityIntent(Intent intent){
            this.activityIntent = intent;
            return this;
        }

        public FrontNotification.Build setServiceIntent(Class<?> destCls){
            if (destCls!=null){
                serviceIntent = new Intent(context, destCls);
            }
            return this;
        }

        public FrontNotification.Build setServiceIntent(Intent intent){
            this.serviceIntent = intent;
            return this;
        }


        public FrontNotification.Build setFlags(int[] flags){
            this.flags = flags;
            return this;
        }

        public FrontNotification.Build setDefaults(int defaults){
            this.defaults = defaults;
            return this;
        }

        public FrontNotification.Build setGroup(String groupKey){
            this.groupKey = groupKey;
            return this;
        }

        public FrontNotification.Build setSmallIcon(int rid) {
            this.smallIcon = rid;
            return this;
        }

        public FrontNotification.Build setBigIcon(int rid) {
            this.bigIcon = rid;
            return this;
        }

        public FrontNotification.Build setIcon(int rid) {
            this.smallIcon = rid;
            this.bigIcon = rid;
            return this;
        }

        public FrontNotification.Build setView(RemoteViews view) {
            this.view = view;
            return this;
        }

        public FrontNotification autoGenerateNotification(String title, String content, String info){
            PendingIntent pIntent = null;
            if (activityIntent !=null){
                pIntent = PendingIntent.getActivity(context,0, activityIntent,0);
            }else if (serviceIntent!=null){
                pIntent = PendingIntent.getService(context,0, serviceIntent,0);
            }


            notification = geneNotify(pIntent,title,content,info,smallIcon,bigIcon,defaults);

            if (flags!=null && flags.length>0){
                for (int flag : flags){
                    notification.flags |= flag;
                }
            }

            //如果你设置的高度大于通知栏的默认高度，需要设置bigContentView模式，否则，会内容会显示不全
            if (view!=null) notification.contentView = view;

            return new FrontNotification(this);
        }

        private Notification geneNotify(PendingIntent pIntent, String title, String content, String info, int smallIcon, int icon, int defaults) {
            return new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
                     .setSmallIcon(smallIcon)
                     .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),icon))
                     .setPriority(Notification.PRIORITY_MAX)
                     .setOngoing(true)
                     .setContentTitle(title)
                     .setContentText(content)
                     .setContentInfo(info)
                     .setContentIntent(pIntent)
                     .setDefaults(defaults!=0?defaults:Notification.DEFAULT_ALL)
                     .setGroup(groupKey)
                     .build();
        }
    }


    public void showNotification() {
        build.notificationManager.notify(build.id, build.notification);
    }
    public void cancelNotification() {
        build.notificationManager.cancel(build.id);
    }
    public void startForeground(Service service){
        service.startForeground(build.id, build.notification);
    }
    public void stopForeground(Service service){
        service.stopForeground(false);
    }

}
