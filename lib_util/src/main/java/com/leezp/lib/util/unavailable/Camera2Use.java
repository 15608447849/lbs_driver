package com.leezp.lib.util.unavailable;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.ImageFormat;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CaptureResult;
import android.hardware.camera2.TotalCaptureResult;
import android.media.Image;
import android.media.ImageReader;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.ImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;

import static android.content.Context.CAMERA_SERVICE;

/**
 * Created by Leeping on 2018/7/28.
 * email: 793065165@qq.com
 */

public class Camera2Use  {

//    private Semaphore mCameraOpenCloseLock = new Semaphore(1);
private static final int STATE_PREVIEW = 0;
    private static final int STATE_WAITING_LOCK = 1;
    private static final int STATE_WAITING_PRECAPTURE = 2;
    private static final int STATE_WAITING_NON_PRECAPTURE = 3;
    private static final int STATE_PICTURE_TAKEN = 4;

    private int state = STATE_PREVIEW;


    private String mCameraId = "" + CameraCharacteristics.LENS_FACING_FRONT;

    private Context context;
    private Handler handler;
    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;
    private Surface surface;

    //摄像头管理器
    private CameraManager cameraManager;
    //系统摄像头
    private CameraDevice cameraDevice;
    //当程序需要预览、拍照时，都需要先通过该类的实例创建Session

    //控制预览的方法为setRepeatingRequest(),控制拍照的方法为capture()
    private CameraCaptureSession cameraCaptureSession;

    private CaptureRequest.Builder builder;

    private CaptureRequest mPreviewRequest;

    private File imageFile;

    private ImageReader mImageReader = null;

    private final ImageReader.OnImageAvailableListener onImageAvailableListener = new ImageReader.OnImageAvailableListener() {
        @Override
        public void onImageAvailable(ImageReader reader) {
            handler.post(new ImageSaver(reader.acquireNextImage(), imageFile));
        }
    };


    public Camera2Use(SurfaceView mSurfaceView,Handler handler) {
        this.context = mSurfaceView.getContext();
        this.surfaceView = mSurfaceView;
        this.surfaceHolder = mSurfaceView.getHolder();
        this.surfaceHolder.addCallback(callback);
        this.surface = this.surfaceHolder.getSurface();
        this.handler = handler;
        this.imageFile = new
                 File(context.getCacheDir()+File.separator+"capture.jpeg");
        cameraManager = (CameraManager) this.context.getSystemService(CAMERA_SERVICE);
    }

    private SurfaceHolder.Callback callback = new SurfaceHolder.Callback() {
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            mImageReader = ImageReader.newInstance(surfaceView.getWidth(), surfaceView.getHeight(),
                    ImageFormat.JPEG, 2);
            mImageReader.setOnImageAvailableListener(onImageAvailableListener, handler);
            tryOpenCamera();
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            if (cameraDevice!=null) {
                cameraDevice.close();
                cameraDevice = null;
            }
        }
    };
    //尝试打开相机
    @SuppressLint("MissingPermission")
    private void tryOpenCamera() {
        try {
            //打开的摄像头ID, 监听摄像头的状态,  执行callback的Handler -如果程序希望直接在当前线程中执行callback
            cameraManager.openCamera(mCameraId, stateCallback, handler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }


    private CameraDevice.StateCallback stateCallback = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(@NonNull CameraDevice camera) {
           // 当摄像头被打开之后，程序即可获取CameraDevice
            cameraDevice = camera;
            createCameraPreviewSession();
        }

        @Override
        public void onDisconnected(@NonNull CameraDevice camera) {
            camera.close();
            cameraDevice = null;
        }

        @Override
        public void onError(@NonNull CameraDevice camera, int error) {
            camera.close();
            cameraDevice = null;
        }
    };

    private void createCameraPreviewSession() {
        try {
            builder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            builder.addTarget(surface);
             /*
                List集合，封装了所有需要从该摄像头获取图片的Surface
                用于监听CameraCaptureSession的创建过程
                执行callback的Handler
                */
            List<Surface> list = Arrays.asList(surface,mImageReader.getSurface());
            cameraDevice.createCaptureSession(list,stateCallback2,handler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private CameraCaptureSession.StateCallback stateCallback2 = new CameraCaptureSession.StateCallback() {
        @Override
        public void onConfigured(@NonNull CameraCaptureSession session) {
            cameraCaptureSession = session;
            //相机的自动对焦
            builder.set(CaptureRequest.CONTROL_AF_MODE,
                    CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
            //闪光灯自动
            builder.set(CaptureRequest.CONTROL_AE_MODE,
                    CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH);
            mPreviewRequest = builder.build();//创建预览请求
            try {
                //开始预览
                cameraCaptureSession.setRepeatingRequest(mPreviewRequest,
                        captureCallback, handler);
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onConfigureFailed(@NonNull CameraCaptureSession session) {

        }
    };


    private CameraCaptureSession.CaptureCallback captureCallback = new CameraCaptureSession.CaptureCallback() {
        @Override
        public void onCaptureProgressed(@NonNull CameraCaptureSession session,
                                        @NonNull CaptureRequest request,
                                        @NonNull CaptureResult partialResult) {
//            Log.w("拍照","onCaptureProgressed");
            process(partialResult);
        }

        @Override
        public void onCaptureCompleted(@NonNull CameraCaptureSession session,
                                       @NonNull CaptureRequest request,
                                       @NonNull TotalCaptureResult result) {
//            Log.w("拍照","onCaptureCompleted");
            process(result);
        }
    };


    private void process(CaptureResult result) {
        switch (state) {
            case STATE_PREVIEW: {
                // We have nothing to do when the camera preview is working normally.
                break;
            }
            case STATE_WAITING_LOCK: {
                Integer afState = result.get(CaptureResult.CONTROL_AF_STATE);
                if (afState == null) {
                    captureStillPicture();
                } else if (CaptureResult.CONTROL_AF_STATE_FOCUSED_LOCKED == afState || CaptureResult.CONTROL_AF_STATE_NOT_FOCUSED_LOCKED == afState) {
                    // CONTROL_AE_STATE can be null on some devices
                    Integer aeState = result.get(CaptureResult.CONTROL_AE_STATE);
                    if (aeState == null || aeState == CaptureResult.CONTROL_AE_STATE_CONVERGED) {
                        state = STATE_PICTURE_TAKEN;
                        captureStillPicture();
                    } else {
                        runPrecaptureSequence();
                    }
                }
                break;
            }
            case STATE_WAITING_PRECAPTURE: {
                // CONTROL_AE_STATE can be null on some devices
                Integer aeState = result.get(CaptureResult.CONTROL_AE_STATE);
                if (aeState == null ||
                        aeState == CaptureResult.CONTROL_AE_STATE_PRECAPTURE ||
                        aeState == CaptureRequest.CONTROL_AE_STATE_FLASH_REQUIRED) {
                    state = STATE_WAITING_NON_PRECAPTURE;
                }
                break;
            }
            case STATE_WAITING_NON_PRECAPTURE: {
                // CONTROL_AE_STATE can be null on some devices
                Integer aeState = result.get(CaptureResult.CONTROL_AE_STATE);
                if (aeState == null || aeState != CaptureResult.CONTROL_AE_STATE_PRECAPTURE) {
                    state = STATE_PICTURE_TAKEN;
                    captureStillPicture();
                }
                break;
            }
        }
    }


    private void runPrecaptureSequence() {
        try {
            // This is how to tell the camera to trigger.
            builder.set(CaptureRequest.CONTROL_AE_PRECAPTURE_TRIGGER,
                    CaptureRequest.CONTROL_AE_PRECAPTURE_TRIGGER_START);
            // Tell #mCaptureCallback to wait for the precapture sequence to be set.
            state = STATE_WAITING_PRECAPTURE;

            cameraCaptureSession.capture(builder.build(), captureCallback,
                    handler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void captureStillPicture() {
        try {

            final CaptureRequest.Builder captureBuilder =
                    cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
            captureBuilder.addTarget(mImageReader.getSurface());

            // Use the same AE and AF modes as the preview.
            captureBuilder.set(CaptureRequest.CONTROL_AF_MODE,
                    CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
            builder.set(CaptureRequest.CONTROL_AE_MODE,
                    CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH);



            CameraCaptureSession.CaptureCallback CaptureCallback
                    = new CameraCaptureSession.CaptureCallback() {

                @Override
                public void onCaptureCompleted(@NonNull CameraCaptureSession session,
                                               @NonNull CaptureRequest request,
                                               @NonNull TotalCaptureResult result) {
                    Log.d("拍照" ,imageFile+"");
                    unlockFocus();
                }
            };

            cameraCaptureSession.stopRepeating();
            cameraCaptureSession.abortCaptures();
            cameraCaptureSession.capture(captureBuilder.build(), CaptureCallback, null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void unlockFocus() {
        try {
            // Reset the auto-focus trigger
            builder.set(CaptureRequest.CONTROL_AF_TRIGGER,
                    CameraMetadata.CONTROL_AF_TRIGGER_CANCEL);
            builder.set(CaptureRequest.CONTROL_AE_MODE,
                    CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH);
            cameraCaptureSession.capture(builder.build(), captureCallback,
                    handler);
            // After this, the camera will go back to the normal state of preview.
            state = STATE_PREVIEW;
            cameraCaptureSession.setRepeatingRequest(mPreviewRequest, captureCallback,
                    handler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }




    public void capture() {
        lockFocus();
    }

    private void lockFocus() {
        //相机锁定焦点
        builder.set(CaptureRequest.CONTROL_AF_TRIGGER,
                CameraMetadata.CONTROL_AF_TRIGGER_START);
        state = STATE_WAITING_LOCK;
        try {

            cameraCaptureSession.capture(builder.build(), captureCallback, handler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }


    private static class ImageSaver implements Runnable {

        /**
         * The JPEG image
         */
        private final Image mImage;
        /**
         * The file we save the image into.
         */
        private final File mFile;

        ImageSaver(Image image, File file) {
            mImage = image;
            mFile = file;
        }

        @Override
        public void run() {
            ByteBuffer buffer = mImage.getPlanes()[0].getBuffer();
            byte[] bytes = new byte[buffer.remaining()];
            buffer.get(bytes);
            FileOutputStream output = null;
            try {
                output = new FileOutputStream(mFile);
                output.write(bytes);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                mImage.close();
                if (null != output) {
                    try {
                        output.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

    }

}
