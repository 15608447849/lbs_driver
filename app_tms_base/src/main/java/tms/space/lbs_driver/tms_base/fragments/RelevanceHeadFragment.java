package tms.space.lbs_driver.tms_base.fragments;

import android.app.Fragment;

import com.leezp.lib.singlepageapplication.base.SpaBaseMessage;
import com.leezp.lib.viewholder.interfaces.ViewHolderAbs;

import tms.space.lbs_driver.tms_base.beans.IBasePresenter;

/**
 * Created by Leeping on 2018/7/26.
 * email: 793065165@qq.com
 * 关联头部 - 组fragment
 */

public abstract class RelevanceHeadFragment<VH extends ViewHolderAbs,P extends IBasePresenter> extends MVPFragment<VH,P>{

    //头部标题元素
    private String titleInfo;

    @Override
    public void onResume() {
        super.onResume();
        if (isVisible()){
            //如果当前处于显示状态,关联底部
            correlationBottomElement();
            //关联head 自动隐藏所有
            correlationHeadElements();
        }
    }

    private void correlationBottomElement() {

        Fragment fragment = query("bottom","bottom");
        if(fragment == null) return;

        if (getPrev() == null){ //组视图
            if (fragment.isVisible()) return;
            //打开底部
            skip("bottom","bottom");
        }else{
            if (!fragment.isVisible()) return;
            //栈顶子视图
            //隐藏底部
            hidden("bottom","bottom");
        }
    }

    private void correlationHeadElements() {
        //自动隐藏头部所有元素
        invokeSpaFragmentMethod("head",
                new SpaBaseMessage().invokeTargetMethod(
                        "elementControl",
                        showHeadElement)
        );
        //如果标题存在
        if (titleInfo!=null) {
            invokeSpaFragmentMethod("head",new SpaBaseMessage().invokeTargetMethod("setTitle",titleInfo));
        }
    }

    //默认全部不显示
    private boolean[] showHeadElement = new boolean[]{false,false,false,false,false};

    protected void setShowHeadElement(boolean[] flags){
        int size = Math.min(showHeadElement.length,flags.length);
        for (int i = 0; i< size;i++){
            showHeadElement[i] = flags[i];
        }
    }

    protected void setHeadTitle(String title){
        this.titleInfo = title;
    }

    //头部显示Toast
    private void headToast(String msg){
        invokeSpaFragmentMethod("head",new SpaBaseMessage().invokeTargetMethod("toast",msg));
    }
    //头部显示进度条
    private void showProgressBar(boolean flag){
        invokeSpaFragmentMethod("head",new SpaBaseMessage().invokeTargetMethod("showProgressBar",flag));
    }

    @Override
    public void toast(String msg) {
        headToast(msg);
    }
    @Override
    public void showProgress() {
        showProgressBar(true);
    }
    @Override
    public void hindProgress() {
        showProgressBar(false);
    }




}
