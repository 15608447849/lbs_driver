package com.leezp.lib.singlepageapplication.base;

import com.leezp.lib.singlepageapplication.interfaces.OnMemValueOp;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Leeping on 2018/4/16.
 * email: 793065165@qq.com
 * activity之间的fragment 共享内存块 - 可以传递大数据
 */

public class SpaSharedMemory implements OnMemValueOp {


    private final Map<String,Object> map;

    public SpaSharedMemory() {
        this.map = new HashMap();
    }

    public SpaSharedMemory(Map<String, Object> map) {
        this.map = map;
    }

    public void put(String k,Object v){
        map.put(k,v);
    }

    public <T> T get(String k,T def){
        Object o = map.get(k);
        if (o==null) return def;
        else return (T)o;
    }

    public Object remove(String k) {
        if (map.containsKey(k))  return map.remove(k);
        else return null;
    }
    public<K> K remove(String k,K def) {
        if (map.containsKey(k))  return (K)map.remove(k);
        else return def;
    }

    @Override
    public void putMemVal(String key, Object val) {
        put(key,val);
    }



    @Override
    public <T> T getMemVal(String key, T def) {
        return get(key,def);
    }

    @Override
    public <T> T removeMemVal(String key, T def) {
        return remove(key,def);
    }

    @Override
    public String toString() {
        StringBuffer stringBuffer = new StringBuffer("内存共享数据块:\n{");
        Iterator<Map.Entry<String,Object>> iterator =  map.entrySet().iterator();
        Map.Entry<String,Object> entry;
        while (iterator.hasNext()){
            entry = iterator.next();
            stringBuffer.append("\n\t")
            .append(entry.getKey())
            .append(" = ")
            .append(entry.getValue());
        }
        stringBuffer.append("\n").append("}");
        return stringBuffer.toString();
    }
}
