package com.leezp.lib.util;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

/**
 * Created by user on 2018/3/8.
 * 用于连接后台服务
 */

public class ServiceConnectionEnc implements ServiceConnection {
    private static final String TAG = "MyServiceConnection";

    protected Class<?> serviceClassType;

    private boolean isConnected;

    private IBinder binder;

    private ConnectedListener listener;

    public <S extends Service> ServiceConnectionEnc(Context context, Class<S> clazz) {
        this.serviceClassType = clazz;
        startServer(context);
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        Log.d(TAG,"onServiceConnected ,"+name+" ,"+service);
        isConnected = true;
        binder = service;
        if (listener!=null){
            listener.onServiceConnected(binder);
        }
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        Log.d(TAG,"onServiceDisconnected ,"+name);
        isConnected = false;
        binder = null;
    }

    @Override
    public void onBindingDied(ComponentName name) {
        Log.d(TAG,"onBindingDied ,"+name);
        binder = null;
    }

    public void bind(Context context,ConnectedListener listener){
        if (!isConnected){
            setListener(listener);
            context.bindService(new Intent(context,serviceClassType),this, Context.BIND_ABOVE_CLIENT);
        }
    }
    public void unbind(Context context){
        if (isConnected){
            context.unbindService(this);
            setListener(null);
            isConnected = false;
        }
    }

    public IBinder getBinder() {
        return binder;
    }

    private void setListener(ConnectedListener listener) {
        this.listener = listener;
    }

    private void startServer(Context context) {
        context.startService(new Intent(context,serviceClassType)); //打开服务
    }

    public void stopServer(Context context) {
        unbind(context);
        context.stopService(new Intent(context,serviceClassType)); //关闭服务
    }

    /**
     * 连接成功监听
     *
     */
    public interface ConnectedListener{
        void onServiceConnected(IBinder ibinder);
    }
}
