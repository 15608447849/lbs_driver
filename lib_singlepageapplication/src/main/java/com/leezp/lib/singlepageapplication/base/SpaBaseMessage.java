package com.leezp.lib.singlepageapplication.base;

import android.content.Intent;
import android.support.annotation.NonNull;

/**
 * Created by Leeping on 2018/6/13.
 * email: 793065165@qq.com
 * 用于在fragmen <-> acticity 之间 传递消息
 * 请自由扩展
 */

public final class SpaBaseMessage {
    //消息工厂
    public final static class Factory {}
    public static final int INVOKE_METHOD = -127;

    public interface Callback{
        void  onCallback(java.lang.Object result);
        Intent i = new Intent();
    }

    public SpaBaseMessage() {}
    public SpaBaseMessage(int what) {
        this.what = what;
    }
    public SpaBaseMessage(java.lang.Object data) {
        setData(data);
    }


    public int what;
    public String message;
    public java.lang.Object data;
    public java.lang.Object target;//反射调用方法所属对象
    public String methodName;//反射方法名
    public Class<?>[] argsTypes;//反射方法参数类型
    public java.lang.Object[] args;//反射方法参数

    public Callback callback;

    public SpaBaseMessage setData(java.lang.Object data) {
        this.data = data;
        return this;
    }
    public <T> T getData() {
        return (T)data;
    }


    public SpaBaseMessage setInvokeTarget(@NonNull java.lang.Object object){
        this.target = object;
        return this;
    }

    //碎片反射调用目标的方法
    public SpaBaseMessage invokeTargetMethod(
                                           @NonNull String methodName,
                                           Class<?>[] argsTypes,
                                           java.lang.Object[] args){

        this.methodName = methodName;
        this.argsTypes = (null == argsTypes? new Class[]{}: argsTypes);
        this.args = (null == args? new java.lang.Object[]{} : args);
        this.what = INVOKE_METHOD;
        return this;
    }

    //碎片反射调用目标方法
    public SpaBaseMessage invokeTargetMethod(
                                           @NonNull String methodName){
        return invokeTargetMethod(methodName,null,null);
    }


    //碎片反射调用目标方法
    public SpaBaseMessage invokeTargetMethod(
                                             @NonNull String methodName,
                                             @NonNull java.lang.Object... args){
        Class[] argsTypes = new Class[args.length];
        for (int i = 0 ; i < args.length ; i++){
            argsTypes[i] = args[i].getClass();
        }
        return invokeTargetMethod(methodName,argsTypes,args);
    }

    public void clear(){
        if (message!=null) message = null;
        if (data!=null) data = null;
        if (target!=null) target = null;
        if (methodName!=null) methodName = null;
        if (argsTypes!=null) argsTypes = null;
        if (args!=null) args = null;
    }

}
