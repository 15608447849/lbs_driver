package com.leezp.lib.util.camera;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.leezp.lib.util.R;

/**
 * Created by Leeping on 2018/12/24.
 * email: 793065165@qq.com
 * 照相机 / 图片选择器
 *
 */
public class PhotoPickActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_photo_pick);
    }

}
