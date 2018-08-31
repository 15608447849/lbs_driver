package com.leezp.lib.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

public class GlideUtil {
    //加载中占位符资源
    private static int placeholder = -1;
    //加载错误图片资源
    private static int error = -1;

    private static Context appContext;

    public static void setError(int error) {
        GlideUtil.error = error;
    }

    public static void setPlaceholder(int placeholder) {
        GlideUtil.placeholder = placeholder;
    }

    private static RequestListener<Drawable> errorListener = new RequestListener<Drawable>() {

        @Override
        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
            return false;
        }

        @Override
        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
            return false;
        }
    };


    public static final void initAppContext(Context appContext){
        GlideUtil.appContext = appContext;
    }


    @SuppressLint("VisibleForTests")
    public static final void destroy(){
        if (appContext==null) return;
        Glide.tearDown();
        GlideUtil.appContext = null;
    }

    @SuppressLint("CheckResult")
    public static void loadImageByHttp(ImageView imageView, String url){
        if (appContext==null) return;
        RequestOptions options = new RequestOptions();
        if (placeholder!= -1) options.placeholder(placeholder);
        if (error!=-1) options.error(error);
//        options.skipMemoryCache(true);
//        options.diskCacheStrategy(DiskCacheStrategy.NONE);//本地文件不缓存

        Glide.with(appContext)
                .load(url)
                .apply(options)
                .listener(errorListener)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(imageView);
    }
    public static void clearImageViewBitmap(ImageView imageView){
        if (appContext==null) return;
        Glide.with(appContext).clear(imageView);
    }

    //清除掉所有的图片的内存缓存
    public  static  void  clearImageMemCache(){
        Glide.get(appContext).clearMemory();
    }
    public  static  void  clearImageMemCache(int level){
        if (appContext==null) return;
        Glide.get(appContext).trimMemory(level);
    }
    //清除掉所有的图片的磁盘缓存
    public static void clearImageDiskCache(){
        if (appContext==null) return;
        Glide.get(appContext).clearDiskCache();
    }



}
