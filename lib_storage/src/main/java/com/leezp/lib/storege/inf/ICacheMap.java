package com.leezp.lib.storege.inf;

import android.content.Context;

/**
 * Created by Leeping on 2018/6/28.
 * email: 793065165@qq.com
 */

public interface ICacheMap<K,V>  {
    void init(Context context);

    V getValue(K k);

    void putValue(K k,V v);

    void removeKey(K k);
}
