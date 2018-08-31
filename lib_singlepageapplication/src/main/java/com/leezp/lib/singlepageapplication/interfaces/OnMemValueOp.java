package com.leezp.lib.singlepageapplication.interfaces;

/**
 * Created by Leeping on 2018/4/28.
 * email: 793065165@qq.com
 */

public interface OnMemValueOp {
    void putMemVal(String key,Object val);
    <T> T getMemVal(String key,T def);
    <T> T removeMemVal(String key,T def);
}
