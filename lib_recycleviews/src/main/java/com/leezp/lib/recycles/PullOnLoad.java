package com.leezp.lib.recycles;

import android.support.v7.widget.RecyclerView;

/**
 * Created by Leeping on 2018/8/18.
 * email: 793065165@qq.com
 */

public class PullOnLoad {
    private boolean enable = true;
    private RecyclerView mRecyclerView;
    private RecyclerViewLoadMoreListener mRecyclerViewLoadMoreListener;

    public PullOnLoad(RecyclerView mRecyclerView) {
        this.mRecyclerView = mRecyclerView;
    }

    public void setRecyclerViewLoadMoreListener(RecyclerViewLoadMoreListener listener) {
        this.mRecyclerViewLoadMoreListener = listener;
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (newState == RecyclerView.SCROLL_STATE_IDLE && isBottom(mRecyclerView)) {
                    if (enable && mRecyclerViewLoadMoreListener != null) {
                        mRecyclerViewLoadMoreListener.onLoadMore();
                    }
                }
            }
        });
    }

    public void setLoadMoreEnable(boolean enable) {
        this.enable = enable;
    }

    /*private boolean isBottom(RecyclerView recyclerView) {
        if (recyclerView == null) return false;

        if (recyclerView.computeVerticalScrollExtent() + recyclerView.computeVerticalScrollOffset() >= recyclerView.computeVerticalScrollRange()) return true;

        return false;
    }*/
    private boolean isBottom(RecyclerView recyclerView) {
        return !recyclerView.canScrollVertically(1);
    }

    public interface RecyclerViewLoadMoreListener {
        /**上滑加载更多*/
        public void onLoadMore();
    }
}
