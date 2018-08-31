package com.leezp.lib.viewholder.interfaces;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.leezp.lib.viewholder.util.ReflectionTool;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;


/**
 * Created by Leeping on 2018/4/16.
 * email: 793065165@qq.com
 */

public abstract class ViewHolderAbs {
    private View viewRoot;

    public ViewHolderAbs(Context context,int layoutId){
        this(getTargetView(context,layoutId));
    }

    public ViewHolderAbs(Context context){
        this(getTargetView(context,0));
    }

    private static View getTargetView(Context context,int layoutId){
        //获取视图加载器
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        assert layoutInflater!=null;
        return layoutInflater.inflate(layoutId,null);
    }

    public ViewHolderAbs(View viewRoot) {
        this.viewRoot = viewRoot;
        autoInitView();
        initViewParam();
    }

    /**自动初始化视图*/
    private void autoInitView() {
        try {
            ReflectionTool.autoViewValue(this,viewRoot);
        } catch (Exception e) {
            throw new RuntimeException("does not create viewHolder by '"+ this +"' ,because: "+e);
        }
    }

    /**子类调用*/
    protected void initViewParam() {

    }

    /**获取根视图*/
    public View getViewRoot(){
        return viewRoot;
    }

    /**设置点击事件*/
    public void setListener(Object listener){
        try {
            ReflectionTool.autoViewListener(this,listener);
        } catch (Exception e) {
           e.printStackTrace();
           throw new RuntimeException();
        }
    }

    /** 销毁视图 - 解除绑定关系 */
    public void destroy(){
        viewRoot = null;
        //自动断开所有值关系
        ReflectionTool.autoViewValueNull(this);
    }

    public static <VH extends ViewHolderAbs> VH createViewHolder(Class vh, Context context) {
        try {
            return ReflectionTool.createViewHolder(vh, context);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
