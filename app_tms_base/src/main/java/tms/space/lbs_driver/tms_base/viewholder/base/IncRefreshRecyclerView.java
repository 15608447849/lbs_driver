package tms.space.lbs_driver.tms_base.viewholder.base;

import android.view.View;

import com.leezp.lib.viewholder.annotations.RidClass;
import com.leezp.lib.viewholder.annotations.RidName;
import com.leezp.lib.viewholder.interfaces.ViewHolderAbs;

import tms.space.lbs_driver.tms_base.R;

/**
 * Created by Leeping on 2018/7/26.
 * email: 793065165@qq.com
 * 下拉刷新list view
 */
@RidClass(R.id.class)
public class IncRefreshRecyclerView extends ViewHolderAbs {
    @RidName("inc_base_refresh")
    public android.support.v4.widget.SwipeRefreshLayout refresh;

    public IncRecyclerView list;

    public IncRefreshRecyclerView(View viewRoot) {
        super(viewRoot);
    }
}
