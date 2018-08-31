package com.leezp.lib.recycles.more_view_adapter;

import android.support.annotation.NonNull;

import com.leezp.lib.recycles.BaseViewHolder;

/**
 * Created by Leeping on 2018/7/24.
 * email: 793065165@qq.com
 */

public class ItemViewTemplateAttribute {
    private Class<? extends BaseViewHolder> viewHolder;
    private int layoutId ;
    private int viewType;

    public ItemViewTemplateAttribute(@NonNull Class<? extends BaseViewHolder> viewHolder, int layoutId) {
       this(viewHolder,layoutId,layoutId);
    }
    public ItemViewTemplateAttribute(@NonNull Class<? extends BaseViewHolder> viewHolder, int layoutId, int viewType) {
        this.viewHolder = viewHolder;
        this.layoutId = layoutId;
        this.viewType = viewType;
    }

    public Class<? extends BaseViewHolder> getViewHolder() {
        return viewHolder;
    }

    public int getLayoutId() {
        return layoutId;
    }

    public int getViewType() {
        return viewType;
    }


    @Override
    public int hashCode() {
        return viewType;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ItemViewTemplateAttribute){
            ItemViewTemplateAttribute o = (ItemViewTemplateAttribute) obj;
            return o.getViewType() == this.getViewType();
        }
        return super.equals(obj);
    }
}
