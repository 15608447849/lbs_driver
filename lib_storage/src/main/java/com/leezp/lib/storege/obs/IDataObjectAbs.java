package com.leezp.lib.storege.obs;

import com.leezp.lib.storege.inf.ICacheMap;

/**
 * Created by Leeping on 2018/6/28.
 * email: 793065165@qq.com
 */

public abstract class IDataObjectAbs<T> implements java.io.Serializable{

    protected abstract String getK();
    protected abstract String convert(Object object);
    protected abstract Object reverse(String data);
    protected  abstract ICacheMap<String,String> getStorage();


    public String getV() {
            return  convert(this);
    }

    public void save() {
        getStorage().putValue(getK(),getV());
    }

    public T fetch() {
        try {
            String data = getStorage().getValue(getK());
            return (T) reverse(data);
        } catch (Exception e) {
            e.printStackTrace();
            remove();
        }
        return null;
    }

    public void remove() {
        getStorage().removeKey(getK());
    }
}
