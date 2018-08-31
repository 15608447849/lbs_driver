package com.leezp.lib.singlepageapplication.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;

import com.leezp.lib.singlepageapplication.interfaces.OnSpaFragmentCommunication;
import com.leezp.lib.singlepageapplication.interfaces.OnSpaFragmentPageOp;
import com.leezp.lib.singlepageapplication.use.SpaFragmentPageHolder;
import com.leezp.lib.singlepageapplication.use.SpaFragmentRegisterAttribute;
import com.leezp.lib.singlepageapplication.use.SpaManage;
import com.leezp.lib.singlepageapplication.use.SpaPrt;
import com.leezp.lib.singlepageapplication.use.SpaRegisterCentre;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Leeping on 2018/4/15.
 * email: 793065165@qq.com
 * 单页面应用基础activity
 */

public abstract class SpaBaseActivity extends Activity implements OnSpaFragmentCommunication,OnSpaFragmentPageOp {

    /**
     * handler 以及 管理fragment-activity通讯
     */
    private SpaBaseHandle spaBaseHandle;

    public SpaBaseHandle getSpaBaseHandle() {
        if (spaBaseHandle==null){
            spaBaseHandle = new SpaBaseHandle(this);
            spaBaseHandle.addOnSpaCommunication(this);
        }
        return spaBaseHandle;
    }

    /** 是否已经调用onCreate*/
    private boolean isCreate = false;
    private boolean isSysRecovery = false;

    public boolean isSysRecovery() {
        return isSysRecovery;
    }

    //因为系统原因 activity被杀死却保留碎片的状态而存活的fragment - 存在界面的重叠问题
    private List<SpaBaseFragment> recoveryFragments = new ArrayList<>();

    /** 被fragment调用 ,如果 activity没有创建完成却能加入到队列 说明是以前残存的碎片*/
    public final void addSpaFragment(SpaBaseFragment fragment) {
        if (!isCreate){
            recoveryFragments.add(fragment);
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState==null) return;
        SpaPrt.print(this+" , onRestoreInstanceState , savedInstanceState");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        SpaPrt.print(this+" , onSaveInstanceState");
    }

    /**
     * 所有碎片及当前显示碎片等属性管理
     */
    private final HashMap<String,SpaFragmentPageHolder> spaPageHolders = new HashMap<>();

    /** 创建容器持有者 */
    public  void createPageHolder(String tag, int containerRid){
        SpaPrt.print(this+" , 创建碎片容器: "+tag+" <-> " +containerRid);
        final HashSet<SpaFragmentRegisterAttribute> hashSet = SpaRegisterCentre.getFragmentPage(tag);
        final SpaFragmentPageHolder spaFragmentPageHolder =
                new SpaFragmentPageHolder(this,tag,getFragmentManager(),containerRid,hashSet);
        spaPageHolders.put(tag,spaFragmentPageHolder);
    }

    public SpaFragmentPageHolder getSpaPageHolders(String tag) {
        return spaPageHolders.get(tag);
    }

    public SpaManage getSpaManage() {
        return SpaManage.getInstance();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isCreate = true;
        SpaPrt.print(this+" , onCreate()");
        if (savedInstanceState!=null) isSysRecovery = true;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        SpaPrt.print(this+" , onNewIntent()");
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        autoCreatePageHolder();
    }
    //第一次显示界面
    private boolean isResumed = true;

    @Override
    protected void onResume() {
        super.onResume();
        if (isResumed){
            getSpaBaseHandle().toIo(new Runnable() {
                @Override
                public void run() {
                    SpaPrt.print(this+" , onResume "+ getWindow().getDecorView().getWindowToken());
                    while (getWindow().getDecorView().getWindowToken()==null);
                    tryRecovery();
                    getSpaBaseHandle().toUi(new Runnable() {
                        @Override
                        public void run() {
                            onInitResume();
                            if (isResumed) throw new IllegalStateException("It's already initialized");
                        }
                    });

                }
            });
        }
    }

    protected void onInitResume(){
        isResumed = false;
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
    }

    /**
     * 反射,自动创建fragment容器视图持有者
     * */
    private void autoCreatePageHolder(){
        try{
        SpaRegisterCentre.autoCreatePageHolder(this);

        }catch (Exception ignored){
        }
    }

    private void tryRecovery() {
        //系统恢复状态, 查询栈中是否存在需要恢复的fragment
        if (spaPageHolders.size()==0 || !isSysRecovery) return;
        SpaPrt.print("尝试还原activity状态");
        Iterator<SpaFragmentPageHolder> iterator = spaPageHolders.values().iterator();
        while (iterator.hasNext()){
            getSpaManage().recovery(iterator.next(),recoveryFragments);
        }
    }

    //是否允许回退按钮生效
    private boolean isAccessPressBackKey = false;

    //设置是否允许回退
    public void setAccessPressBackKey(boolean accessPressBackKey) {
        isAccessPressBackKey = accessPressBackKey;
    }
    //拦截系统回退键的点击
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            // 不允许回退键
            if (!isAccessPressBackKey){
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    // 捕获返回键的方法2
    @Override
    public void onBackPressed() {
        SpaPrt.print(this+" , onBackPressed()");
    }

    public OnSpaFragmentPageOp getOnSpaFragmentPageImp() {
        return this;
    }

    //收到fragment发送来的消息
    @Override
    public boolean sendSpaMessage(SpaBaseMessage sbMsg) {

        return false;
    }

    @Override
    public SpaBaseFragment query(String pageHolderTag, String gPageTag) {
        SpaFragmentPageHolder pageHolder = spaPageHolders.get(pageHolderTag);
        if (pageHolder==null) return null;
        return getSpaManage().queryFragmentByTag(pageHolder,pageHolder.getPage(gPageTag));
    }

    @Override
    public void skip(String pageHolderTag, String gPageTag) {
        SpaFragmentPageHolder pageHolder = spaPageHolders.get(pageHolderTag);
        if (pageHolder==null) return;
        //如果当前显示的页面和需要显示的页面一致, 不操作
        if (getSpaManage().checkTargetIsStackTop(pageHolder,pageHolder.getPage(gPageTag))) return;
        //隐藏当前页面
        getSpaManage().hindGroupFragment(pageHolder,pageHolder.getCurrentGroupPage());
        //显示一个页面
        getSpaManage().showGroupFragment(pageHolder,pageHolder.getPage(gPageTag));
    }

    //隐藏一个指定页面
    @Override
    public void hidden(String pageHolderTag, String gPageTag){
        SpaFragmentPageHolder pageHolder = spaPageHolders.get(pageHolderTag);
        if (pageHolder == null) return;
        getSpaManage().hindGroupFragment(pageHolder,pageHolder.getPage(gPageTag));
    }

    @Override
    public void remove(String pageHolderTag, String gPageTag) {
        SpaFragmentPageHolder pageHolder = spaPageHolders.get(pageHolderTag);
        if (pageHolder==null) return;
        getSpaManage().removeGroupFragment(pageHolder,pageHolder.getPage(gPageTag));
    }

    @Override
    public void addStack(String pageHolderTag, String cPageTag) {
        SpaFragmentPageHolder pageHolder = spaPageHolders.get(pageHolderTag);
        if (pageHolder==null) return;
        //添加页面在当前栈
        getSpaManage().showGroupFragmentByOnlyStackTop(pageHolder,pageHolder.getCurrentGroupPage(),pageHolder.getPage(cPageTag));
    }

    @Override
    public void back(String pageHolderTag) {
        SpaFragmentPageHolder pageHolder = spaPageHolders.get(pageHolderTag);
        if (pageHolder==null) return;
        getSpaManage().removeGroupFragmentByOnlyStackTop(pageHolder,pageHolder.getCurrentGroupPage());
    }

    @Override
    public void clearAll(String pageHolderTag) {
        SpaFragmentPageHolder pageHolder = spaPageHolders.get(pageHolderTag);
        if (pageHolder==null) return;
        getSpaManage().removeGroupFragmentByActiveAll(pageHolder);
    }



}
