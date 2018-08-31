package com.leezp.lib.recycles;

import android.content.Context;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.Constructor;

/**
 * Created by Leeping on 2018/4/25.
 * email: 793065165@qq.com
 */

public class RecyclerUtil {
    private RecyclerUtil(){}

    //创建view Holder
       public static <T> T createHolder(Context context,Class<? extends RecyclerView.ViewHolder> clzHolder,int layoutId,ViewGroup parent){
        try {
            View viewRoot = LayoutInflater.from(context).inflate(layoutId, parent, false);
            Constructor cons = clzHolder.getConstructor(View.class);//获取有参构造
            return  (T) cons.newInstance(viewRoot);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 关联适配器
     */
    public static void associationAdapter(RecyclerView recyclerView ,RecyclerView.Adapter adapter){
        recyclerView.setAdapter(adapter);
    }

    /**
     * 线性布局
     * 关于异常 Inconsistency detected. Invalid view holder adapter positionViewHolder
     * https://blog.csdn.net/lovexieyuan520/article/details/50537846
     */
    public static void LinearLayoutManagerSetting(Context context, RecyclerView recyclerView){
        recyclerView.setLayoutManager(new LinearLayoutManager(context){
            @Override
            public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
                try {
                    super.onLayoutChildren(recycler, state);
                } catch (IndexOutOfBoundsException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    /**
     * 网格布局
     * 垂直
     * 占一格
     * 不翻转
     */
    public static void gridLayoutManagerSettingVerSpan1Norev(Context context, RecyclerView recyclerView){
        recyclerView.setLayoutManager(new GridLayoutManager(context,1, LinearLayoutManager.VERTICAL,false){
            @Override
            public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
                try {
                    super.onLayoutChildren(recycler, state);
                } catch (IndexOutOfBoundsException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    //添加子项 item分割线
    public static void addDefaultItemDecoration(Context context,RecyclerView recyclerView,int orientation) {
        DividerItemDecoration decoration = new DividerItemDecoration(context,orientation);
        recyclerView.addItemDecoration(decoration);
    }
    //添加子项 item分割线
    public static void addItemDecoration(RecyclerView recyclerView,RecyclerView.ItemDecoration decoration) {
        recyclerView.addItemDecoration(decoration);
    }

    //添加anim
    public static void setItemAnimator(RecyclerView recyclerView,RecyclerView.ItemAnimator animator){
        if (animator==null){
            animator = new DefaultItemAnimator();
        }
        recyclerView.setItemAnimator(animator);
    }


}
