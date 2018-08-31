package com.leezp.lib.util;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;

/**
 * Created by Leeping on 2018/8/7.
 * email: 793065165@qq.com
 *
 */

public class CameraUse implements Closeable{
    private static final String TAG = CameraUse.class.getSimpleName();
    //拍照回调
    private static final int TAKE_PICTURE= 198;
    //选择照片回调
    private static final int CHOOSE_PICTURE = 199;

    //存在内存泄露,需要及时关闭
    private Activity activity;
    private Fragment fragment;
    //android 7.0 使用
    private final String authority;

    private boolean isChoosePhotosError = false;

    private File takePictureFile;

    public CameraUse(Fragment fragment) {
        this(fragment.getActivity());
        this.fragment = fragment;
    }

    public CameraUse(Activity activity) {
        if (activity==null) throw new NullPointerException("activity is null");
        this.activity = activity;
        this.authority = activity.getApplication().getPackageName()+".fileprovider";
//        Log.w(TAG,"authority = "+ authority);
//        Log.w(TAG,"android sdk : "+Build.VERSION.SDK_INT);
    }

    //关闭
    @Override
    public void close() throws IOException {
        this.activity = null;
        this.fragment = null;
    }

    private void startActivityForResult(Intent intent, int requestCode){
            if (fragment!=null) fragment.startActivityForResult(intent,requestCode);
            else activity.startActivityForResult(intent,requestCode);
    }

    /**选择相片/拍照 回调*/
    public void onActivityResult(int requestCode, int resultCode, Intent data){
//        LLog.print(requestCode,resultCode,"data="+data);

        try {
            if (requestCode == CHOOSE_PICTURE){
                choosePictureResult(resultCode,data);
            }else if (requestCode == TAKE_PICTURE){
                takePictureResult(resultCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (callback!=null) callback.pictureResult(null);
        }
    }

    //选择图片
    public void choosePicture(){
        Intent intent = new Intent();
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.N){
            intent.setAction(Intent.ACTION_PICK); //ACTION_GET_CONTENT
            if (isChoosePhotosError) intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
        }else{
//            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
        }

        intent.setDataAndType(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intent, CHOOSE_PICTURE);
    }

    //拍照
    public void takePicture(){
        String filePath = AppUtil.getExternalStorageTempFilePath(activity,Environment.DIRECTORY_DCIM,null);
        takePictureFile = new File(filePath);
        //从文件中创建uri
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            uri = FileProvider.getUriForFile(activity, authority, takePictureFile);
        }else{
            uri =  Uri.fromFile(takePictureFile);
        }
        Intent intent = new Intent();
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
//        intent.addCategory(intent.CATEGORY_DEFAULT);
        startActivityForResult(intent, TAKE_PICTURE);
    }

    //图片选择回调
    private void choosePictureResult(int resultCode, Intent data) {
        if (data == null) isChoosePhotosError = true;
        File image = null;
        if(resultCode == Activity.RESULT_OK){
            Uri uri = data.getData();
            String filePath = AppUtil.getExternalStorageTempFilePath(activity,Environment.DIRECTORY_DCIM,null);
            image = IoUtil.imageUriToFile(activity,uri,filePath);
        }

        if (callback!=null) callback.pictureResult(image);
    }
    //拍照选择回调
    private void takePictureResult(int resultCode) {
        if (resultCode==Activity.RESULT_OK){
            File file = takePictureFile;
            takePictureFile = null;
            if (callback!=null) callback.pictureResult(file);

        }else{
            if (callback!=null) callback.pictureResult(null);
        }

    }




    /**android 7.0 图片选择*/
    /*private void choosePictureSDK_N(Intent intent) {
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.N){
            // choosePictureSDK_N(intent);
            //            intent.setAction(Intent.ACTION_GET_CONTENT);
//            intent.addCategory(Intent.CATEGORY_OPENABLE);
//            intent.setType("image/*");
        }
        String filePath = AppUtil.getExternalStorageTempFilePath(activity, Environment.DIRECTORY_DCIM,null);
        if (filePath ==null) throw new NullPointerException();
        //临时文件
        private File temp;temp = new File(filePath);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(activity, authority,temp));
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
    }*/








    private Callback callback;

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public interface Callback{
        void pictureResult(File file);
    }
}
  /*
       if(requestCode == TAKE_PICTURE_TAKE)
        {
            if (resultCode == Activity.RESULT_OK){
                File file = (File) cImageView.getTag();
                if (!ImageUtil.setImageViewBitmap(cImageView,file)){
                    cImageView.setTag(null);
                    toast("无效照片");
                }
            }else{
                cImageView.setTag(null);
                toast("拍照失败");
            }
        }
        else if(requestCode == TAKE_PICTURE_ALBUM && resultCode == Activity.RESULT_OK)
        {
            Uri uri = data.getData();
            String filePath = AppUtil.getExternalStorageTempFilePath(getSpaActivity(),Environment.DIRECTORY_DCIM,null);

            File image = IoUtil.imageUriToFile(getSpaActivity(),uri,filePath);
            if (image!=null && ImageUtil.setImageViewBitmap(cImageView,image)){
                cImageView.setTag(image);
            }else{
                toast("获取失败");
            }
        }
        */