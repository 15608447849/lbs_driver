package com.leezp.lib.update;

import android.content.Context;
import android.util.Log;

import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;
import com.tencent.bugly.beta.UpgradeInfo;

/**
 * Created by Leeping on 2018/8/15.
 * email: 793065165@qq.com
 */
public class ApkUpdateConfig {
    private static String TAG = ApkUpdateConfig.class.getSimpleName();

    public static void init(Context appContext,String appid){
        Beta.autoInit = true;//true表示app启动自动初始化升级模块
        Beta.autoCheckUpgrade = false;//false表示不会自动检查升级
        Beta.autoDownloadOnWifi = true;
        Beta.initDelay = 1000;
        Beta.upgradeCheckPeriod = 5 * 1000;
        Beta.showInterruptedStrategy = false;
        Bugly.init(appContext, appid, false);
    }

    public static void autoCheckUpdate(){
        /**
         * @param isManual  用户手动点击检查，非用户点击操作请传false
         * @param isSilence 是否显示弹窗等交互，[true:没有弹窗和toast] [false:有弹窗或toast]
         */
//        Beta.checkUpgrade(false,true);
//        uploadInfo();
        userCheckUpdate();
    }
    public static void userCheckUpdate(){
        Beta.checkUpgrade(true,false);
        uploadInfo();
    }

    private static void uploadInfo(){
        UpgradeInfo upgradeInfo = Beta.getUpgradeInfo();
        if (upgradeInfo == null) {
            return;
        }
        StringBuilder info = new StringBuilder();
        info.append("id: ").append(upgradeInfo.id).append("\n");
        info.append("标题: ").append(upgradeInfo.title).append("\n");
        info.append("升级说明: ").append(upgradeInfo.newFeature).append("\n");
        info.append("versionCode: ").append(upgradeInfo.versionCode).append("\n");
        info.append("versionName: ").append(upgradeInfo.versionName).append("\n");
        info.append("发布时间: ").append(upgradeInfo.publishTime).append("\n");
        info.append("安装包Md5: ").append(upgradeInfo.apkMd5).append("\n");
        info.append("安装包下载地址: ").append(upgradeInfo.apkUrl).append("\n");
        info.append("安装包大小: ").append(upgradeInfo.fileSize).append("\n");
        info.append("弹窗间隔（ms）: ").append(upgradeInfo.popInterval).append("\n");
        info.append("弹窗次数: ").append(upgradeInfo.popTimes).append("\n");
        info.append("发布类型（0:测试 1:正式）: ").append(upgradeInfo.publishType).append("\n");
        info.append("弹窗类型（1:建议 2:强制 3:手工）: ").append(upgradeInfo.upgradeType);
        Log.d(TAG,info.toString());
    }
}
