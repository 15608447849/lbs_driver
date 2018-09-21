package tms.space.lbs_driver.base.entrance;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.leezp.lib.singlepageapplication.annotations.SpaViewHolder;
import com.leezp.lib.singlepageapplication.base.SpaBaseActivity;
import com.leezp.lib.singlepageapplication.base.SpaBaseMessage;
import com.leezp.lib.util.GlideUtil;
import com.leezp.lib.util.PermissionApply;

import tms.space.lbs_driver.base.messages.FragmentMessageManage;
import tms.space.lbs_driver.base.track.TrackServerConnect;

/**
 * Created by Leeping on 2018/6/27.
 * email: 793065165@qq.com
 *
 */
public class LbsActivity extends SpaBaseActivity implements PermissionApply.Callback{
    //权限数组
    private String[] permissionArray = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE, // 写sd卡
            Manifest.permission.CAMERA, // 相机和闪光灯
            Manifest.permission.ACCESS_FINE_LOCATION, //GPS
            Manifest.permission.ACCESS_COARSE_LOCATION, //NET LOCATION
            Manifest.permission.READ_PHONE_STATE, //获取手机信息
            Manifest.permission.CALL_PHONE //打电话权限
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (permissionApply != null) permissionApply.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (permissionApply != null) permissionApply.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onPermissionsGranted() {
        //授权成功
        skip("content","login");//跳转到登陆页面
        permissionApply.isIgnoreBatteryOption();
    }



    //视图持有
    @SpaViewHolder
    private LbsActivityVh vh;

    //权限申请
    private PermissionApply permissionApply =  new PermissionApply(this,permissionArray,this);;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vh = new LbsActivityVh(this);
        setContentView(vh.getViewRoot());
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (serverConn == null){ serverConn = new TrackServerConnect(); }
        if (messageManage == null){ messageManage = new FragmentMessageManage(this); }
//        if (permissionApply == null && !isSysRecovery()){ permissionApply = new PermissionApply(this,this,permissionArray); }
        if (serverConn != null) serverConn.bindService(this);
    }

    @Override
    protected void onInitResume() {
        super.onInitResume();
        if (!isSysRecovery()){
            permissionApply.permissionCheck();
        }
    }

    @Override
    protected void onStop() {
        if (serverConn != null) serverConn.unbindService(this);
        super.onStop();
    }

    @Override
    protected void onDestroy() {

        if (messageManage != null){
            messageManage = null;
        }

        if (serverConn != null){
            serverConn = null;
        }

        if (vh != null){
            vh.destroy();
            vh = null;
        }

        super.onDestroy();
    }

    private TrackServerConnect serverConn;

    public TrackServerConnect getTrackServerConnect() {
        return serverConn;
    }

    private FragmentMessageManage messageManage;

    /** 接受来自fragment 的消息 */
    @Override
    public boolean sendSpaMessage(SpaBaseMessage sbMsg) {
        if(messageManage!=null) return messageManage.select(sbMsg);
        return false;
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        GlideUtil.clearImageMemCache(level);
    }
}
