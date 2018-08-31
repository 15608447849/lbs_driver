package com.leezp.lib.recycles.more_view_adapter;

import android.view.View;

import com.leezp.lib.recycles.BaseViewHolder;
import com.leezp.lib.recycles.BaseViewHolderDataModel;

/**
 * Created by Leeping on 2018/7/24.
 * email: 793065165@qq.com
 *
 */
public abstract class AutomaticViewBaseViewHolder<T extends BaseViewHolderDataModel> extends BaseViewHolder<T> {

    public AutomaticViewBaseViewHolder(View itemView) {
        super(itemView);
        try {
            automaticView(itemView);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    /** 子类实现 对视图 view init赋值绑定 */
    protected abstract void automaticView(View rootView);
}
