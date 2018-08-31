package tms.space.lbs_driver.tms_required.business_person;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.leezp.lib.viewholder.annotations.RidClass;

import tms.space.lbs_driver.tms_base.recycler.FragmentRecycleViewHolderAbs;
import tms.space.lbs_driver.tms_base.viewholder.base.IncRecyclerView;
import tms.space.lbs_driver.tms_required.R;

/**
 * Created by Leeping on 2018/7/25.
 * email: 793065165@qq.com
 */
@RidClass(R.id.class)
public class PersonalVh extends FragmentRecycleViewHolderAbs {

    public IncRecyclerView list;
    public PersonalVh(Context context) {
        super(context, R.layout.frg_person);
    }

    @Override
    public RecyclerView getRecyclerView() {
        return list.recycler;
    }
}
