package com.leezp.lib.singlepageapplication.base;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import com.leezp.lib.singlepageapplication.imps.SpaCommunicationDefaultImp;
import com.leezp.lib.singlepageapplication.interfaces.OnMemValueOp;
import com.leezp.lib.singlepageapplication.interfaces.OnSpaFragmentCommunication;
import com.leezp.lib.singlepageapplication.interfaces.OnSpaFragmentPageOp;
import com.leezp.lib.singlepageapplication.use.SpaIOThreadPool;

import java.io.Closeable;
import java.io.IOException;
import java.lang.ref.SoftReference;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Leeping on 2018/4/15.
 * email: 793065165@qq.com
 *
 */

public class SpaBaseHandle extends Handler implements OnSpaFragmentCommunication,OnMemValueOp,Closeable{
    //线程池
    private final SpaIOThreadPool pool = new SpaIOThreadPool();
    //所有通讯实现
    private final List<OnSpaFragmentCommunication> onSpaCommunicationImpList = new LinkedList<>();
    //activity软引用
    private final SoftReference<SpaBaseActivity> activitySoftReference;
    //内存数据传递操作
    private OnMemValueOp onMemValueOp;

    public SpaBaseHandle(SpaBaseActivity activity) {
        this.activitySoftReference = new SoftReference<>(activity);
        setOnMemValueOp(new SpaSharedMemory());//默认实现
        addOnSpaCommunication(new SpaCommunicationDefaultImp(this));//默认的消息处理实现
    }

    public void setOnMemValueOp(@NonNull OnMemValueOp onMemValueOp) {
        this.onMemValueOp = onMemValueOp;
    }

    public OnMemValueOp getOnMemValueOp() {
        return onMemValueOp;
    }

    @Override
    public void putMemVal(String key, java.lang.Object val) {
        onMemValueOp.putMemVal(key,val);
    }

    @Override
    public <T> T getMemVal(String key, T def) {
        return onMemValueOp.getMemVal(key,def);
    }

    @Override
    public <T> T removeMemVal(String key, T def) {
        return onMemValueOp.removeMemVal(key,def);
    }

    public SpaBaseActivity getActivitySoftReference() {
        return activitySoftReference.get();
    }

    protected boolean checkMainThread(){
        return Looper.myLooper() == getLooper();
    }

    /**
     * 在Ui线程执行
     * @param r
     */
    public void toUi(Runnable r){
        if (checkMainThread()){
            r.run();
        }else{
            post(r);
        }
    }

    /**
     * @param r
     * 在其他线程执行
     */
    public void toIo(Runnable r){
        if (checkMainThread()){
            pool.post(r);
        }else{
            r.run();
        }
    }

    public void toIoNew(Runnable r){
        pool.post(r);
    }

    /**
     * 添加fragment->activity消息传递回调接口实现类
     */
    public void addOnSpaCommunication(@NonNull OnSpaFragmentCommunication onSpaCommunication){
       if (onSpaCommunicationImpList.contains(onSpaCommunication)) return;
       onSpaCommunicationImpList.add(onSpaCommunication);
    }

    /**
     * fragment 消息传递到Activity
     */
    @Override
    public boolean sendSpaMessage(@NonNull SpaBaseMessage sbMsg) {
        boolean flag = false;
        for (OnSpaFragmentCommunication onSpaCommunication : onSpaCommunicationImpList){
            flag = onSpaCommunication.sendSpaMessage(sbMsg);
            if (flag) break;
        }
        sbMsg.clear();//清理消息
        return flag;
    }

    @Override
    public void close() throws IOException {
        pool.close();
    }

    public OnSpaFragmentPageOp getOnSpaFragmentPageImp() {
        return getActivitySoftReference().getOnSpaFragmentPageImp();
    }
}
