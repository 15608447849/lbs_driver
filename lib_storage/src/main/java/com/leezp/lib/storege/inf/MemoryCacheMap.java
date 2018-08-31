package com.leezp.lib.storege.inf;

import android.content.Context;
import android.util.LruCache;

/**
 * Created by user on 2017/1/13.
 *  内存缓存
 */

public class MemoryCacheMap<K,V> implements ICacheMap<K,V>{

    @Override
    public void init(Context context) {

    }

    @Override
    public V getValue(K k) {
        return get(k);
    }

    @Override
    public void putValue(K k, V v) {
        put(k,v);
    }

    @Override
    public void removeKey(K k) {
        remove(k);
    }

    public interface CacheStoreListener<K,V>{
        void onDelete(K key, V value);
    }

    private LruCache<K,V> lruCache;
    private CacheStoreListener<K,V> listener;

    public void setListener(CacheStoreListener<K, V> listener) {
        this.listener = listener;
    }



    /**
     * 设置最大可用内存的百分比
     * @param percentage
     */
    public MemoryCacheMap(int percentage) {
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        maxMemory = maxMemory/percentage;

        lruCache = new LruCache<K,V>(maxMemory){
            @Override
            protected void entryRemoved(boolean evicted, K key, V oldValue, V newValue) {
//                super.entryRemoved(evicted, key, oldValue, newValue);//默认空实现
                //当item被回收或者删掉时调用。该方法当value被回收释放存储空间时被remove调用， 或者替换item值时put调用.
                //true: 为释放空间被删除；
                // false: put或remove导致
                    if (listener!=null){
                        listener.onDelete(key,oldValue);
                    }
            }
        };
    }

    //添加
    public synchronized boolean put(K key,V value){
        if (key!=null && value!=null){
           if (lruCache.get(key) == null){
               lruCache.put(key,value);
               return true;
           }
        }
        return false;
    }
    //获取
    public synchronized V get(K key){
        if (key == null){
            return  null;
        }
        return lruCache.get(key);
    }

    //移除
    public synchronized V remove(K key){
        if (key == null){
            return  null;
        }
        return lruCache.remove(key);
    }

    //获取大小
    public int size(){
        return lruCache.size();
    }

    public synchronized void clearAll(){
        //清除所有
        if (lruCache.size() > 0) {
            lruCache.evictAll();
        }
    }

}
