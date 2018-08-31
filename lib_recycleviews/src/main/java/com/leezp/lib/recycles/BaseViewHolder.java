package com.leezp.lib.recycles;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Leeping on 2018/4/25.
 * email: 793065165@qq.com
 * 列表子项view模板持有者
 */

public abstract class BaseViewHolder<T extends BaseViewHolderDataModel> extends RecyclerView.ViewHolder {
    public BaseViewHolder(View itemView) {
        super(itemView);
    }
    /**
     * 在 适配器中 数据与视图绑定时 调用 ,子类在此处关联数据
     * */
    protected abstract void bindData(T dataModel);
}
