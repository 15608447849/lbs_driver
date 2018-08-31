package com.leezp.lib.util;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

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
        int id;
        Notification notification;
        NotificationManager notificationManager;
        Intent intent;//点击通知栏-跳转到这里.
        int[] flags;
        String groupKey = "default";
        public Build(Context context,int id){
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

        public FrontNotification.Build setIntent( Class<?> destCls){
            if (destCls!=null){
                intent = new Intent(context, destCls);
            }

            return this;
        }

        public FrontNotification.Build setIntent(String action,String scheme){
            intent = new Intent(action, Uri.parse(scheme));
            return this;
        }

        public FrontNotification.Build setIntent(Intent intent){
          this.intent = intent;
          return this;
        }

        public FrontNotification.Build setFlags(int[] flags){
            this.flags = flags;
            return this;
        }

        public FrontNotification.Build setGroup(String groupKey){
            this.groupKey = groupKey;
            return this;
        }

        public FrontNotification autoGenerateNotification(String title,String content,String info,int icon,int defaults){
            PendingIntent pIntent = null;
            if (intent!=null){
                pIntent = PendingIntent.getActivity(context,0,intent,0);
            }

            notification = geneNotify(pIntent,title,content,info,icon,defaults);

            if (flags!=null && flags.length>0){
                for (int flag : flags){
                    notification.flags |= flag;
                }
            }

            return new FrontNotification(this);
        }

        private Notification geneNotify(PendingIntent pIntent,String title,String content,String info,int icon,int defaults) {
            return new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
                     .setSmallIcon(icon)
                     .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),icon))
                     .setPriority(Notification.PRIORITY_MAX)
                     .setContentTitle(title)
                     .setContentText(content)
                     .setContentInfo(info)
                     .setContentIntent(pIntent)
                     .setDefaults(defaults)
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


}
