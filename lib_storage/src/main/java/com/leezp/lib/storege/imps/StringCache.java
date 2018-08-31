package com.leezp.lib.storege.imps;

import android.content.Context;

import com.jakewharton.disklrucache.DiskLruCache;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;

import com.leezp.lib.storege.inf.HandDiskCacheMap;
import com.leezp.lib.storege.inf.ICacheMap;
import com.leezp.lib.storege.inf.MemoryCacheMap;

/**
 * Created by Leeping on 2018/6/28.
 * email: 793065165@qq.com
 */

public class StringCache extends HandDiskCacheMap<String> {
    private StringCache(){}

    private static final class Holder{
        private static final StringCache CACHE_STRING = new StringCache();
    }
    public static StringCache get(){
        return Holder.CACHE_STRING;
    }

    //内存缓存
    private ICacheMap<String,String> memCache = new MemoryCacheMap<String,String>(10);;

    //上下文
    private Context context;
    @Override
    public void init(Context context) {
        super.init(context);
        this.context = context;
        this.memCache = new MemoryCacheMap<String,String>(10);
    }

    @Override
    public String getValue(String k) {
        if (context==null) return null;
        String v = memCache.getValue(k); //缓存取不到
        if (v == null){
            try {
                DiskLruCache.Snapshot snapShot = mDiskLruCache.get(k);
                if (snapShot != null) {
                    BufferedReader bf = new BufferedReader(new InputStreamReader(snapShot.getInputStream(0)));
                    String temp;
                    StringBuffer sb = new StringBuffer();
                    while((temp = bf.readLine()) != null)  sb.append(temp);
                    bf.close();
                    if (sb.length()>0) {
                        v = sb.toString();

                        memCache.putValue(k,v);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return v;
    }

    @Override
    public void putValue(String k, String v) {
        if (context==null) return;
        memCache.putValue(k,v);
        try {
            DiskLruCache.Editor editor = mDiskLruCache.edit(k);
            if (editor != null) {
                OutputStream outputStream = editor.newOutputStream(0);
                outputStream.write(v.getBytes("utf-8"));
                editor.commit();
                mDiskLruCache.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
