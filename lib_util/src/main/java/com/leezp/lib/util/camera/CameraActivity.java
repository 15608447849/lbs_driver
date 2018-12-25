package com.leezp.lib.util.camera;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;

import com.leezp.lib.util.AppUtil;
import com.leezp.lib.util.R;
import com.leezp.lib.util.TimeUtil;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

/**
 * Created by Leeping on 2018/12/24.
 * email: 793065165@qq.com
 * 简单照相机
 * surface 详解: http://www.jcodecraeer.com/a/anzhuokaifa/androidkaifa/2012/1201/656.html
 */
public class CameraActivity extends Activity implements View.OnClickListener ,SurfaceHolder.Callback, Camera.AutoFocusCallback, Camera.ShutterCallback, Camera.PictureCallback {

    private ImageButton backBtn;
    private ImageButton switchBtn;
    private ImageButton takeBtn;
    private ImageButton completeBtn;
    private ImageButton cancelBtn;

    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;
    private Camera camera;
    private File photoImage ;//拍摄的图片文件
    private ToneGenerator tone;//快门声
    private static final int FRONT = 1;//前置摄像头标记
    private static final int BACK = 2;//后置摄像头标记
    private int nextToCameraType = BACK;

    private boolean isReset;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_camera);
        initView();
        bindView();
        initIntent();
    }

    private void initView() {
        backBtn = findViewById(R.id.act_camera_btn_back);
        switchBtn = findViewById(R.id.act_camera_btn_switch);
        takeBtn = findViewById(R.id.act_camera_btn_take);
        completeBtn = findViewById(R.id.act_camera_btn_complete);
        cancelBtn = findViewById(R.id.act_camera_btn_cancel);
        surfaceView = findViewById(R.id.act_camera_sv);
        completeBtn.setVisibility(View.GONE);
        cancelBtn.setVisibility(View.GONE);
    }

    private void bindView() {
        backBtn.setOnClickListener(this);
        switchBtn.setOnClickListener(this);
        takeBtn.setOnClickListener(this);
        completeBtn.setOnClickListener(this);
        cancelBtn.setOnClickListener(this);
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    private void initIntent() {

        try {
            if (!AppUtil.checkCameraHardware(this)) throw new Exception("not fount camera hardware dev.");
            Intent it = getIntent();
            String imagePath = it.getStringExtra("image_path");
            if (imagePath == null) imagePath =  AppUtil.getExternalStorageTempFilePath(this,
                    Environment.DIRECTORY_DCIM,
                    TimeUtil.formatUTC(System.currentTimeMillis(),"yyyyMMdd_HHmmss")+".jpg"
           );
            Log.d("照相机","图片路径:"+ imagePath);
            photoImage = new File(imagePath);
        } catch (Exception e) {
            e.printStackTrace();
            setResult(RESULT_CANCELED);
            finish();
        }
    }

    /**
     * 按钮点击回调
     * @param view 按钮
     */
    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.act_camera_btn_back){
            //返回
            back();
        }else if (id == R.id.act_camera_btn_switch){
            //切换
            switchCamera();
        }else if (id == R.id.act_camera_btn_take){
            //拍照
            takePhoto();
        }else if (id == R.id.act_camera_btn_complete){
            //完成
            complete();
        }else if (id == R.id.act_camera_btn_cancel){
            //取消
            cancel();
        }
    }

    //打开照相机
    private void openCamera() {
        try {
            if (camera == null && surfaceHolder!=null){
                camera = setCameraType(nextToCameraType);
                //设置显示
                camera.setPreviewDisplay(surfaceHolder);

                setCameraParams(surfaceView.getWidth(),surfaceView.getHeight());
                camera.startPreview();//开始预览
//                //自动对焦拍摄
//                camera.autoFocus(this);
            }
        } catch (Exception e) {
            closeCamera();
        }
    }

    //设置摄像头类型
    private Camera setCameraType(int type) {
        int frontIndex =-1;
        int backIndex = -1;
        int cameraCount = Camera.getNumberOfCameras();
        Camera.CameraInfo info = new Camera.CameraInfo();
        for(int cameraIndex = 0; cameraIndex<cameraCount; cameraIndex++){
            Camera.getCameraInfo(cameraIndex, info);
            if(info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT){
                frontIndex = cameraIndex;
            }else if(info.facing == Camera.CameraInfo.CAMERA_FACING_BACK){
                backIndex = cameraIndex;
            }
        }
        if(type == FRONT && frontIndex != -1){
            nextToCameraType = BACK;
            return Camera.open(frontIndex);
        }else if(type == BACK && backIndex != -1){
            nextToCameraType = FRONT;
            return Camera.open(backIndex);
        }
        return null;
    }

    //设置相机参数
    private void setCameraParams(int h,int w) {
        try {
            if (camera!=null){
//                WindowManager windowManager = getWindowManager();
//                Display display = windowManager.getDefaultDisplay();
//                int screenWidth =  display.getWidth();
//                int screenHeight =  display.getHeight();
//                Log.d("照相机","预览大小:"+w+","+h+" 图片大小:"+screenWidth+","+screenHeight);

                Camera.Parameters parameters = camera.getParameters();
                // 设置照片格式
                parameters.setPictureFormat(ImageFormat.JPEG);
                // 设置预浏尺寸，注意要在摄像头支持的范围内选择
                parameters.setPreviewSize(w, h);
//                List<Camera.Size> supportedPreviewSizes = parameters.getSupportedPreviewSizes();
//                for (Camera.Size size : supportedPreviewSizes){
//                    Log.d("预览分辨率支持",size.width+" - "+ size.height);
//                }
                List<Camera.Size> supportedPictureSizes = parameters.getSupportedPictureSizes();
                for (Camera.Size size : supportedPictureSizes){
//                    Log.d("拍照分辨率支持",size.width+" - "+ size.height);
                    if (w<size.width){ //最大分辨率
                        w = size.width;
                        h = size.height;
                    }
                }
                // 设置照片分辨率，注意要在摄像头支持的范围内选择
                parameters.setPictureSize(w, h);
                parameters.setFlashMode(Camera.Parameters.FOCUS_MODE_AUTO); //自动对焦
                camera.setDisplayOrientation(90);
                // 设置照相机参数
                camera.setParameters(parameters);
            }
        } catch (Exception e) {
//            e.printStackTrace();
        }
    }

    //大小改变预览
    private void changeCamera(int w, int h) {
        try {
            camera.stopPreview();
            setCameraParams(w,h);
            camera.setPreviewDisplay(surfaceHolder);
            camera.startPreview();//开始预览
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //关闭照相机
    private void closeCamera() {
        try {
            if (camera!=null){
                camera.setPreviewCallback(null);
                camera.stopPreview();
                camera.release();
                camera = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //返回
    private void back() {
        setResult(RESULT_CANCELED);
        finish();
    }
    //完成
    private void complete(){
        if (photoImage.exists()){
            Intent intent = new Intent();
            intent.setData( Uri.fromFile(photoImage));
            setResult(RESULT_OK,intent);
            finish();
        }
    }
    //取消
    private void cancel(){

        boolean isDelete = true;
        if (photoImage.exists()){
            isDelete = photoImage.delete();
        }
        if (isDelete){
            isReset = false;
            takeBtn.setVisibility(View.VISIBLE);
            completeBtn.setVisibility(View.GONE);
            cancelBtn.setVisibility(View.GONE);
            changeCamera(surfaceView.getWidth(),surfaceView.getHeight());
        }

    }
    //切换相机
    private void switchCamera() {
        if (camera==null) return;
        if (isReset) cancel();
        closeCamera();
        openCamera();
    }
    //拍照
    private void takePhoto() {
        if (camera == null) return;
        takeBtn.setVisibility(View.GONE);
        completeBtn.setVisibility(View.VISIBLE);
        cancelBtn.setVisibility(View.VISIBLE);
        //自动对焦拍摄
        camera.autoFocus(this);
    }


    /**
     * Sureface创建
     */
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        //创建照相机
        openCamera();
    }

    /**
     *Sureface改变
     */
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        changeCamera(w,h);
    }


    /**
     * Surface销毁
     */
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        closeCamera();
    }

    //照相机自动对焦
    @Override
    public void onAutoFocus(boolean success, Camera camera) {
        if (success){
            //拍摄
            camera.takePicture(this, null, this);
        }


    }
    //拍照快门键
    @Override
    public void onShutter() {
        if(tone == null) tone = new ToneGenerator(AudioManager.STREAM_MUSIC, ToneGenerator.MAX_VOLUME);
        //发出提示用户的声音
        tone.startTone(ToneGenerator.TONE_PROP_BEEP2);
    }

    //拍照之后,被压缩图片数据
    @Override
    public void onPictureTaken(byte[] bytes, Camera camera) {
        {
            try {
                    isReset = true;
                    //存储拍照获得的图片
                    if (!photoImage.exists()){
                        photoImage.createNewFile();
                    }
                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                    Matrix matrix = new Matrix();

                    if (nextToCameraType == BACK){
                        matrix.setScale(-1, 1);//前置摄像头,镜像
                    }
                    matrix.setRotate(nextToCameraType == FRONT?90:-90);

                    bitmap = Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,true);
                    BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(photoImage));
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                    bos.flush();
                    bos.close();
                    bitmap.recycle();
            } catch (Exception e) {
                e.printStackTrace();
                setResult(RESULT_CANCELED);
                finish();
            }
        }
    }
}
