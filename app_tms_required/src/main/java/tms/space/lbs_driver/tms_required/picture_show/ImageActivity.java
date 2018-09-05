package tms.space.lbs_driver.tms_required.picture_show;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.leezp.lib.util.DialogUtil;
import com.leezp.lib.util.TouchSlidingHandle;

import java.io.File;

import tms.space.lbs_driver.tms_base.activitys.BaseActivityAbs;
import tms.space.lbs_driver.tms_required.R;

/**
 * Created by Leeping on 2018/8/22.
 * email: 793065165@qq.com
 */

public class ImageActivity extends BaseActivityAbs implements ImagePresenter.View, SubsamplingScaleImageView.OnImageEventListener, View.OnClickListener, TouchSlidingHandle.Callback {

    public static String URL = "IMAGE_URL_ADDRESS";
    private ProgressBar progressBar;
    private com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView imageView;
    private android.support.design.widget.FloatingActionButton button;
    private ImagePresenter presenter = new ImagePresenter();
    private TouchSlidingHandle touchSlidingHandle = new TouchSlidingHandle();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        if (intent == null){
            finish();
            return;
        }
        String imageUrl = intent.getStringExtra(URL);
        if (imageUrl == null){
            finish();
            return;
        }

        setContentView(R.layout.act_image);
        setDialogWindowSize();

        progressBar = $(R.id.inc_layout_horizontal_progress_bar);
        imageView = $(R.id.act_image_subsamplingscaleimageview);

        button = $(R.id.act_image_floatbtn);
        button.setOnClickListener(this);
        imageView.setOnImageEventListener(this);
        presenter.bindView(this);
        presenter.loadImageUrl(imageUrl);

    }

    private void setDialogWindowSize() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        WindowManager.LayoutParams layoutParams =  getWindow().getAttributes(); //获取对话框当前的参数值
        layoutParams.height = (int) (dm.heightPixels * 0.95);   //高度设置为屏幕的0.5
        layoutParams.width = (int) (dm.widthPixels * 0.95);    //宽度设置为屏幕的0.8
        layoutParams.alpha = 1.0f;      //设置本身透明度
        layoutParams.dimAmount = 0.5f;      //设置黑暗度
        getWindow().setAttributes(layoutParams);

    }

    @Override
    protected void onDestroy() {
        presenter.unbindView();
        super.onDestroy();
    }

    @Override
    public void toast(final String msg) {
        $ui(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(ImageActivity.this,msg,Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void showProgress() {
        $ui(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void hindProgress() {
        $ui(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void openImageFile(final File image) {
        $ui(new Runnable() {
            @Override
            public void run() {
                imageView.setImage(ImageSource.uri(image.getAbsolutePath()));
            }
        });

    }

    @Override
    public void closeImageFile() {
        imageView.recycle();
    }

    @Override
    public void imageError() {
        $ui(new Runnable() {
            @Override
            public void run() {
                DialogUtil.dialogSimple(ImageActivity.this,
                        "无法显示图片",
                        "关闭",
                        new DialogUtil.Action0() {
                            @Override
                            public void onAction0() {
                                finish();
                            }
                });
            }
        });

    }

    @Override
    public void onReady() {
        showProgress();
    }
    @Override
    public void onImageLoaded() {
        hindProgress();
    }
    @Override
    public void onPreviewLoadError(Exception e) {
        hindProgress();
        imageError();
    }
    @Override
    public void onImageLoadError(Exception e) {
        hindProgress();
        imageError();
    }
    @Override
    public void onTileLoadError(Exception e) {
        hindProgress();
        imageError();
    }
    @Override
    public void onPreviewReleased() {
        hindProgress();
        imageError();
    }

    @Override
    public void onClick(View v) {
        finish();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        boolean flag = touchSlidingHandle.onTouchEvent(event,this);
        if (!flag){
            return super.dispatchTouchEvent(event);
        }
        return flag;
    }

    @Override
    public boolean leftSlide() {
        finish();
        return true;
    }

    @Override
    public boolean rightSlide() {
        finish();
        return true;
    }

    @Override
    public boolean topSlide() {
        finish();
        return true;
    }

    @Override
    public boolean bottomSlide() {
        finish();
        return true;
    }
}
