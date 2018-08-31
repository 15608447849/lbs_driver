package com.leezp.lib.recycles.more_view_adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.leezp.lib.recycles.BaseViewHolder;
import com.leezp.lib.recycles.BaseViewHolderDataModel;
import com.leezp.lib.recycles.RecycleViewAdapterRelevanceDataBean;
import com.leezp.lib.recycles.RecyclerUtil;

/**
 * Created by Leeping on 2018/7/24.
 * email: 793065165@qq.com
 */

public class MultiTypeAdapter extends RecycleViewAdapterRelevanceDataBean<BaseViewHolderDataModel> {

    //可以适配的view模板管理
    private final ItemViewTemplateManage moreRecItemManage;

    public MultiTypeAdapter(Context context, ItemViewTemplateManage moreRecItemManage) {
        super(context);
        this.moreRecItemManage = moreRecItemManage;
    }

    @Override
    public BaseViewHolder<BaseViewHolderDataModel> onCreateViewHolder(ViewGroup parent, int viewType) {
        int size = moreRecItemManage.getAttrList().size();
        if (size>0){
            ItemViewTemplateAttribute attr;
            for (int i = 0 ; i < size ; i++ ){
                attr = moreRecItemManage.getAttrList().get(i);
                if (viewType == attr.getViewType()){
                    return RecyclerUtil.createHolder(mContext, attr.getViewHolder(),attr.getLayoutId() ,parent);
                }
            }
        }

        throw new IllegalArgumentException("找不到合适的视图持有者");
//        return null;
    }
}
