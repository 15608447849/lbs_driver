package com.leezp.lib.recycles;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

/**
 * Created by Leeping on 2018/4/23.
 * email: 793065165@qq.com
 * 装饰者模式
 * 添加 页头 页尾
 */

public class RecycleViewAdapterHeadAndFooter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    public static final int SHOW_HEADER=1;

    public static final int SHOW_FOOTER=2;

    private RecyclerView.Adapter mAdapter;

    //页头 视图持有者
    private BaseViewHolder mHeaderViewHolder;

    //页尾 视图持有者
    private BaseViewHolder mFooterViewHolder;


    public RecycleViewAdapterHeadAndFooter(RecyclerView.Adapter mAdapter) {
        this.mAdapter = mAdapter;
    }

    public void setHeaderViewHolder(BaseViewHolder headerViewHolder) {
        this.mHeaderViewHolder = headerViewHolder;
    }

    public void setFooterViewHolder(BaseViewHolder footerViewHolder) {
        this.mFooterViewHolder = footerViewHolder;
    }

    private int getHeadersCount() {
        return mHeaderViewHolder==null?0:1;
    }

    private int getFootersCount() {
        return mFooterViewHolder==null?0:1;
    }

    @Override
    public int getItemViewType(int position) {
        int numHeaders = getHeadersCount();
        //如果存在head视图 且 当前传递过来的下标 == 0
        if (position < numHeaders) {
            return SHOW_HEADER;
        }
        //如果内容适配器不等于空
        if (mAdapter != null) {
            final int adjPosition = position - numHeaders;
            int adapterCount = mAdapter.getItemCount();
            if (adjPosition < adapterCount) {
                return mAdapter.getItemViewType(adjPosition);
            }
        }

        return getFootersCount()==1?SHOW_FOOTER:super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        if (mAdapter != null) {
            return getFootersCount() + getHeadersCount() + mAdapter.getItemCount();
        } else {
            return getFootersCount() + getHeadersCount();
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == SHOW_HEADER){
            return mHeaderViewHolder;
        }
        if(viewType == SHOW_FOOTER){
            return mFooterViewHolder;
        }
        return mAdapter.onCreateViewHolder(parent,viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        //head
        int numHeaders = getHeadersCount();
        if (position < numHeaders) {
            return;
        }
        //adapter body

        if (mAdapter != null) {
            final int adjPosition = position - numHeaders;
            int adapterCount = 0;
            adapterCount = mAdapter.getItemCount();
            if (adjPosition < adapterCount) {
                mAdapter.onBindViewHolder(holder, adjPosition);
                return;
            }
        }
        //footer

    }
}
