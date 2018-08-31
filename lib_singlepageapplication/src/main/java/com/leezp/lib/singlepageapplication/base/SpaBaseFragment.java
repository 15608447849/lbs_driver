package com.leezp.lib.singlepageapplication.base;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.view.View;

import com.leezp.lib.singlepageapplication.interfaces.OnMemValueOp;
import com.leezp.lib.singlepageapplication.interfaces.OnSpaFragmentCommunication;
import com.leezp.lib.singlepageapplication.interfaces.OnSpaFragmentPageOp;
import com.leezp.lib.singlepageapplication.use.SpaFragmentRegisterAttribute;
import com.leezp.lib.singlepageapplication.use.SpaPrt;

import java.io.Serializable;

/**
 * Created by Leeping on 2018/4/15.
 * email: 793065165@qq.com
 */

public class SpaBaseFragment extends Fragment implements OnMemValueOp,OnSpaFragmentPageOp,OnSpaFragmentCommunication{

    //下层碎片
    private SpaBaseFragment prev;

    private String prevTag;
    //上层碎片
    private SpaBaseFragment next;

    private String nextTag;

    public SpaBaseFragment getPrev() {
        return prev;
    }

    public void setPrev(SpaBaseFragment prev) {
        this.prev = prev;
        if (this.prev == null){
            setPrevTag(null);
        } else{
            setPrevTag(this.prev.getTag());
        }
    }

    public SpaBaseFragment getNext() {
        return next;
    }

    public void setNext(SpaBaseFragment next) {
        this.next = next;
        if (this.next == null){
            setNextTag(null);
        } else{
            setNextTag(this.next.getTag());
        }
    }

    public void setPrevTag(String prevTag) {
        this.prevTag = prevTag;
    }

    public void setNextTag(String nextTag) {
        this.nextTag = nextTag;
    }

    public String getPrevTag() {
        return prevTag;
    }

    public String getNextTag() {
        return nextTag;
    }

    private boolean isKillSelf = false;

    public void killSelf() {
        killSelf(true);
    }

    public void killSelf(boolean flag) {
        this.isKillSelf = flag;
    }

    public boolean isKillSelf() {
        return isKillSelf;
    }

    //提供给所有子类使用
    protected SpaBaseHandle mHandle;

    public SpaBaseHandle getSpaHandle(){return mHandle;}

    public SpaBaseActivity getSpaActivity(){
        if (mHandle!=null) return mHandle.getActivitySoftReference();
        return null;
    }

    private boolean recoverHide;

    public boolean isRecoverHide() {
        return recoverHide;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            init(activity);
        }
    }

    @TargetApi(23)
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        init(context);
    }

    private void init(Context context){
        SpaPrt.print(this+" - onAttach");
        if (context instanceof SpaBaseActivity){
            SpaBaseActivity spaBaseActivity  = (SpaBaseActivity)context;
            mHandle = spaBaseActivity.getSpaBaseHandle();
            if (mHandle!=null) mHandle.getActivitySoftReference().addSpaFragment(this);
        }else{
            throw new RuntimeException("activity does not extends 'com.leezp.lib.singlepageapplication.base.SpaBaseActivity'.");
        }
    }

    private static final String STATE_SAVE_HIDDEN = "STATE_SAVE_HIDDEN";
    private static final String STRUCT_SAVE_PREV = "STRUCT_SAVE_PREV";
    private static final String STRUCT_SAVE_NEXT = "STRUCT_SAVE_NEXT";
    private static final String DATA_SAVE_IN = "DATA_SAVE_IN";
    private static final String DATA_SAVE_OUT = "DATA_SAVE_OUT";
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) return;
        SpaPrt.print(this+" - onCreate ,savedInstanceState");
        setPrevTag(savedInstanceState.getString(STRUCT_SAVE_PREV,null));
        setNextTag(savedInstanceState.getString(STRUCT_SAVE_NEXT,null));
        recoverHide = savedInstanceState.getBoolean(STATE_SAVE_HIDDEN);
        dataIn = savedInstanceState.getSerializable(DATA_SAVE_IN);
        if (dataIn==null) dataIn = savedInstanceState.getParcelable(DATA_SAVE_IN);
        dataOut = savedInstanceState.getSerializable(DATA_SAVE_OUT);
        if (dataOut==null) dataOut = savedInstanceState.getParcelable(DATA_SAVE_OUT);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        outState.putBoolean(STATE_SAVE_HIDDEN, isHidden());
        outState.putString(STRUCT_SAVE_PREV,getPrevTag());
        outState.putString(STRUCT_SAVE_NEXT,getNextTag());
        if (dataIn!=null){
            if ( dataIn instanceof Serializable){
                outState.putSerializable(DATA_SAVE_IN, (Serializable) dataIn);
            }
            if (dataIn instanceof Parcelable){
                outState.putParcelable(DATA_SAVE_IN, (Parcelable) dataIn);
            }
        }
        if (dataOut!=null){
            if ( dataOut instanceof Serializable){
                outState.putSerializable(DATA_SAVE_OUT, (Serializable) dataIn);
            }
            if (dataOut instanceof Parcelable){
                outState.putParcelable(DATA_SAVE_OUT, (Parcelable) dataIn);
            }
        }

        super.onSaveInstanceState(outState);
        SpaPrt.print(this+" - onSaveInstanceState");

    }



    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState==null) return;
        SpaPrt.print(this+" - onViewStateRestored");
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SpaPrt.print(this+", onViewCreated"
//                +" view = "+view
//                +" getView() = "+getView()
                +" getView().getWindowToken() = "+ getView().getWindowToken()
//                +" getView().visibility = "+ (getView().getVisibility() == View.VISIBLE)
        );

    }

    @Override
    public void onStart() {
        super.onStart();
        SpaPrt.print(this+" - onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        SpaPrt.print(this +" - onResume\n"
                +" isVisible-"+isVisible()
                +" isAdded-" + isAdded()
                +" isHidden-" + isHidden()
                +" isResumed-" + isResumed()
//                +" isDetached-" + isDetached()
//                +" isRemoving-" + isRemoving()
//                +" isInLayout-" + isInLayout()
//                +" getUserVisibleHint-"+getUserVisibleHint()
        );
//       if (isVisible() == false && isHidden() == false){
//           SpaPrt.print(this+" 显示状态有异常!!!!!!!!!!!!!!!");
//       }
    }

    @Override
    public void onPause() {
        super.onPause();
        SpaPrt.print(this+" - onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        SpaPrt.print(this+" - onStop");
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        SpaPrt.print(this +" - onHiddenChanged - hidden:" + hidden);
        if (!hidden){
            if (isResumed())  onResume();
        }else{
            if (isResumed()) onPause();
        }
    }
    //viewpage 调用
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        SpaPrt.print(this+" - setUserVisibleHint - " + isVisibleToUser);
        if (isVisibleToUser) {
            //相当于Fragment的onResume
            if (isResumed()) onResume();
        } else {
            //相当于Fragment的onPause
            if (isResumed()) onPause();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        SpaPrt.print(this+" - onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        SpaPrt.print(this+" - onDestroy");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        SpaPrt.print(this+" - onDetach");
        mHandle = null;
    }


    /**
     * 发送消息
     */
    @Override
    public boolean sendSpaMessage(SpaBaseMessage sbMsg) {
       return mHandle.sendSpaMessage(sbMsg);
    }

    public boolean sendSpaMessageToActivity(SpaBaseMessage sbMsg) { return getSpaActivity().sendSpaMessage(sbMsg); }

    @Override
    public void putMemVal(String key, java.lang.Object val) {
        if (mHandle!=null){
            mHandle.putMemVal(key,val);
        }
    }

    @Override
    public <T> T getMemVal(String key,T def){
        if (mHandle!=null){
            return mHandle.getMemVal(key,def);
        }
        return def;
    }

    @Override
    public <T> T removeMemVal(String key, T def) {
        if (mHandle!=null){
            return mHandle.removeMemVal(key,def);
        }
        return def;
    }

    public void skip(String gPageTag) {
        skip(SpaFragmentRegisterAttribute.parsePageTag(getTag()),gPageTag);
    }

    @Override
    public SpaBaseFragment query(String pageHolderTag, String gPageTag) {
       return mHandle.getOnSpaFragmentPageImp().query(pageHolderTag,gPageTag);
    }

    @Override
    public void skip(String pageHolderTag, String gPageTag) {
       mHandle.getOnSpaFragmentPageImp().skip(pageHolderTag,gPageTag);
    }

    public void hidden(String gPageTag) {
        hidden(SpaFragmentRegisterAttribute.parsePageTag(getTag()),gPageTag);
    }

    @Override
    public void hidden(String pageHolderTag, String gPageTag) {
        mHandle.getOnSpaFragmentPageImp().hidden(pageHolderTag,gPageTag);
    }

    public void removeSelf(){
        remove(SpaFragmentRegisterAttribute.parseFragmentTag(getTag()));
    }

    public void remove(String gPageTag) {
        remove(SpaFragmentRegisterAttribute.parsePageTag(getTag()),gPageTag);
    }
    @Override
    public void remove(String pageHolderTag, String gPageTag) {
        mHandle.getOnSpaFragmentPageImp().remove(pageHolderTag,gPageTag);
    }

    public void addStack(String cPageTag) {
        addStack(SpaFragmentRegisterAttribute.parsePageTag(getTag()),cPageTag);
    }
    @Override
    public void addStack(String pageHolderTag, String cPageTag) {
        mHandle.getOnSpaFragmentPageImp().addStack(pageHolderTag,cPageTag);
    }

    public void back() {
        back(SpaFragmentRegisterAttribute.parsePageTag(getTag()));
    }
    @Override
    public void back(String pageHolderTag) {
        mHandle.getOnSpaFragmentPageImp().back(pageHolderTag);
    }

    public void clearAll() {
        clearAll(SpaFragmentRegisterAttribute.parsePageTag(getTag()));
    }

    @Override
    public void clearAll(String pageHolderTag) {
        mHandle.getOnSpaFragmentPageImp().clearAll(pageHolderTag);
    }

    //反射调用方法
    protected void invokeTargetMethod(java.lang.Object object , SpaBaseMessage... sbMsgList){
        for (SpaBaseMessage sbMsg : sbMsgList){
            sbMsg.setInvokeTarget(object);
            sendSpaMessage(sbMsg);
        }
    }

    protected void invokeSpaFragmentMethod(String pageHolderTag, String pageTag, SpaBaseMessage... sbMsgList){
        SpaBaseFragment target = query("head","head");
        if (target!=null){
            invokeTargetMethod(target,sbMsgList);
        }
    }

    protected void invokeSpaFragmentMethod(String tag, SpaBaseMessage... sbMsgList){
        invokeSpaFragmentMethod(tag,tag,sbMsgList);
    }

    public void toIo(Runnable r){
        if (mHandle!=null){
            mHandle.toIo(r);
        }
    }

    public void toUi(Runnable r){
        if (mHandle!=null){
            mHandle.toUi(r);
        }
    }

    private Object dataIn; //传进来
    private Object dataOut;  //传出去

    //获取传递进这个页面的数据
    public final Object getDataIn() {
        return dataIn;
    }

    //传出去这个页面的数据
    public final void setDataOut(Object dataOut){this.dataOut = dataOut;}

    /**向其他页面传递数据 */
    public final Object transmitData() {
        return dataOut;
    }

    /**接收来自其他页面的数据*/
    public final void receiveData(Object dataIn) {
            this.dataIn = dataIn;
    }




}
