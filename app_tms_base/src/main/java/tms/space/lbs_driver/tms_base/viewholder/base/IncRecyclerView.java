package tms.space.lbs_driver.tms_base.viewholder.base;

import android.support.v4.widget.NestedScrollView;
import android.view.View;

import com.leezp.lib.util.AppUtil;
import com.leezp.lib.viewholder.annotations.RidClass;
import com.leezp.lib.viewholder.annotations.RidName;
import com.leezp.lib.viewholder.interfaces.ViewHolderAbs;

import tms.space.lbs_driver.tms_base.R;

/**
 * Created by Leeping on 2018/4/23.
 * email: 793065165@qq.com
 * list view
 */
@RidClass(R.id.class)
public class IncRecyclerView extends ViewHolderAbs {    //recycleView

    @RidName("inc_base_recycler")
    public android.support.v7.widget.RecyclerView recycler;

    public IncRecyclerView(View viewRoot) {
        super(viewRoot);
    }

    @Override
    protected void initViewParam() {
        boolean flag = AppUtil.checkViewRootExistTargetViewClassType(getViewRoot(), NestedScrollView.class);
        if (flag) {
            recycler.setNestedScrollingEnabled(false);
            recycler.setFocusableInTouchMode(false);
        }
    }
}
