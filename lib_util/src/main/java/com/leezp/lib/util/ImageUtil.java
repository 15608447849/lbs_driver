package com.leezp.lib.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import top.zibin.luban.Luban;

/**
 * Created by Leeping on 2018/7/29.
 * email: 793065165@qq.com
 */

public class ImageUtil {

    private static final String TAG = ImageUtil.class.getSimpleName();

    private static Context appContext;

    public static void initAppContext(Context context){
        ImageUtil.appContext = context;
    }

    public static boolean setImageViewBitmap(ImageView iv,File image){
        int w = iv.getWidth();
        int h = iv.getHeight();
        Bitmap bitmap = imageFileToBitmap(image,w * h);
        if (bitmap!=null) {
        iv.setImageBitmap(bitmap);
        return true;
        }
        return false;
    }

    /**
     *
     * @param options
     * @param minSideLength 最小边长 -1
     * @param maxNumOfPixels  最大像素 如 1920*1080
     * @return
     */
    private static int computeSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {

        int initialSize = computeInitialSampleSize(options, minSideLength, maxNumOfPixels);

        int roundedSize;
        if (initialSize <= 8) {
            roundedSize = 1;
            while (roundedSize < initialSize) {
                roundedSize <<= 1;
            }
        } else {
            roundedSize = (initialSize + 7) / 8 * 8;
        }
        return roundedSize;
    }
    private static int computeInitialSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
        double w = options.outWidth;
        double h = options.outHeight;
        //Log.d(TAG,"原图片大小: "+ w+"*"+h+"="+(w * h) +"\t容器最大像素 : "+ maxNumOfPixels);
        int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math.sqrt(w * h / maxNumOfPixels));
        int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(Math.floor(w / minSideLength), Math.floor(h / minSideLength));
        if (upperBound < lowerBound) {
            // return the larger one when there is no overlapping zone.
            return lowerBound;
        }
        if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
            return 1;
        } else if (minSideLength == -1) {
            return lowerBound;
        } else {
            return upperBound;
        }
    }


    /**
     *
     * @param image 图片文件
     * @param containerPixels 容器大小
     * @return
     */
    public static Bitmap imageFileToBitmap(File image, int containerPixels){
        Bitmap bitmap = null;
        try {
            FileInputStream is = new FileInputStream(image);
            FileDescriptor fd = is.getFD();

            BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inJustDecodeBounds = true;//设置压缩比例
            bitmap = BitmapFactory.decodeFileDescriptor(is.getFD(), null, opts);
            opts.inSampleSize = computeSampleSize(opts, -1,containerPixels);
            opts.inJustDecodeBounds = false;
            opts.inPreferredConfig = Bitmap.Config.ARGB_8888;
            opts.inDither = false; //是否开启抖动

            opts.inTempStorage = new byte[10 * 1024];
            bitmap = BitmapFactory.decodeFileDescriptor(is.getFD(),
                    null, opts);
//            Log.d(TAG,"bitmap size = "+ bitmap.getByteCount()+"byte");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    /**
     * 图片压缩
     */
    public static List<File> imageCompression(List<File> imageList,int thresholdValue){

        List<File> fileList = null;
        try {
//            for (File file : imageList){
//                Log.w(TAG,"原图片:"+file.getCanonicalPath()+" , "+ file.length());
//            }
            fileList = Luban
                    .with(appContext)
                    .load(imageList)
                    .ignoreBy(thresholdValue)
                    .setTargetDir(appContext.getCacheDir()
                    .getCanonicalPath())
                    .get();
//            for (File file : fileList){
//                Log.w(TAG,"压缩图片:"+file.getCanonicalPath()+" , "+ file.length());
//            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileList;
    }
}




    /*  //            opts.inPurgeable = true;   过时, 配合使用在5.0以前 可以回收native的内存
     //            opts.inInputShareable = true;
     //            inBitmap参数
     //            这个参数用来实现Bitmap内存的复用，但复用存在一些限制，具体体现在：在Android 4.4之前只能重用相同大小的Bitmap的内存，而Android 4.4及以后版本则只要后来的Bitmap比之前的小即可。

     //            opts.inDensity; //即上文我们提到的inDensity 图片在drawable-xhdpi文件夹下，那么inDensity值就为320；设备的屏幕密度为240dpi，因而inTargetDensity的值就为240
     //            opts.inTargetDensity = density; //目标屏幕密度
     //            opts.inDither = false;
     //            opts.inScaled = false; //是否支持缩放 默认支持 ,根据 inTargetDensity/inDensity 计算*/