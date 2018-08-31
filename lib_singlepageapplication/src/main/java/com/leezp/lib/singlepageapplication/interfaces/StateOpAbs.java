package com.leezp.lib.singlepageapplication.interfaces;

/**
 * Created by Leeping on 2018/7/22.
 * email: 793065165@qq.com
 */

public abstract class StateOpAbs<T,D> {
    public abstract boolean check(D d);
    public abstract void execute(T t, D d);
}
