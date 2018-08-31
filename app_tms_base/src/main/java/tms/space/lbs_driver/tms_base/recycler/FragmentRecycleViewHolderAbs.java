package tms.space.lbs_driver.tms_base.recycler;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.leezp.lib.viewholder.interfaces.ViewHolderAbs;

/**
 * Created by Leeping on 2018/7/26.
 * email: 793065165@qq.com
 */

public abstract class FragmentRecycleViewHolderAbs extends ViewHolderAbs {


    public FragmentRecycleViewHolderAbs(Context context, int layoutId) {
        super(context, layoutId);
    }

    public FragmentRecycleViewHolderAbs(Context context) {
        super(context);
    }

    public abstract RecyclerView getRecyclerView();

}
