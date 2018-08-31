package com.leezp.lib.storege.inf;

import android.content.Context;
import android.content.pm.PackageInfo;

import com.jakewharton.disklrucache.DiskLruCache;

import java.io.File;
import java.io.IOException;

/**
 * Created by Leeping on 2018/4/24.
 * email: 793065165@qq.com
 *
 * https://blog.csdn.net/guolin_blog/article/details/28863651 磁盘缓存使用
 */

public abstract class HandDiskCacheMap<V> implements ICacheMap<String,V>{

    public static class Builder{
        private String uniqueName = "hand-disk-def";
        private long maxSize = 10 * 1024 * 1024L; //默认10M;

        public String getUniqueName() {
            return uniqueName;
        }

        public HandDiskCacheMap.Builder setUniqueName(String uniqueName) {
            this.uniqueName = uniqueName;
            return this;
        }

        public long getMaxSize() {
            return maxSize;
        }

        public HandDiskCacheMap.Builder setMaxSize(long maxSize) {
            this.maxSize = maxSize;
            return this;
        }
    }

    private HandDiskCacheMap.Builder builder;

    protected DiskLruCache mDiskLruCache;

    public HandDiskCacheMap() {
        this(new HandDiskCacheMap.Builder());
    }

    public HandDiskCacheMap(HandDiskCacheMap.Builder builder) {
      this.builder = builder;
    }

    @Override
    public void init(Context context) {
        if (mDiskLruCache!=null) return;
        final File directory = getCacheFile(context,builder.getUniqueName());
        final int appVersion = getVersion(context);
        final int valueCount = 1;
        final long maxSize = builder.getMaxSize();
        /**
         * File directory, 第一个参数指定的是数据的缓存地址
         * int appVersion, 第二个参数指定当前应用程序的版本号
         * int valueCount, 指定同一个key可以对应多少个缓存文件,基本都是传1
         * long maxSize 参数指定最多可以缓存多少字节的数据
         * */
        try {
            mDiskLruCache = DiskLruCache.open(directory,appVersion,valueCount,maxSize);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //获取缓存文件目录
    private File getCacheFile(Context context,String uniqueName) {
        return new File(context.getCacheDir().getPath() + File.separator + uniqueName);
    }
    //获取App版本
    private int getVersion(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return info.versionCode;
        } catch (Exception e) {
        }
        return 1;
    }
    //移除
    @Override
    public void removeKey(String k) {
        try {
            mDiskLruCache.remove(k);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
