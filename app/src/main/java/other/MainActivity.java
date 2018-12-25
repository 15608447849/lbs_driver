package other;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.leezp.lib.util.PermissionApply;
import com.leezp.lib.util.camera.CameraActivity;

import tms.space.lbs_driver.R;

/**
 * Created by Leeping on 2018/12/24.
 * email: 793065165@qq.com
 */
public class MainActivity extends Activity implements PermissionApply.Callback{

    private String[] permissionArray = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE, // 写sd卡
            Manifest.permission.CAMERA, // 相机和闪光灯
    };
    private PermissionApply permissionApply =  new PermissionApply(this,permissionArray,this);;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_mian);
        permissionApply.permissionCheck();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (permissionApply != null) permissionApply.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (permissionApply != null) permissionApply.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            Log.d("返回数据",data.getData()+"");
        }
    }

    @Override
    public void onPermissionsGranted() {
        //pass
    }

    public void phonePick(View view){
        Log.d("自定义activity","打开相机");
        startActivityForResult(new Intent(this,CameraActivity.class),255);
    }
}
