package tms.space.lbs_driver.tms_base.viewholder.base;

import android.view.View;

import com.leezp.lib.viewholder.annotations.RidClass;
import com.leezp.lib.viewholder.annotations.RidName;
import com.leezp.lib.viewholder.interfaces.ViewHolderAbs;

import tms.space.lbs_driver.tms_base.R;

/**
 * Created by Leeping on 2018/7/26.
 * email: 793065165@qq.com
 * 下拉框
 */
@RidClass(R.id.class)
public class IncSpinner extends ViewHolderAbs {

    @RidName("inc_base_spinner")
    public com.leezp.lib.cusview.nicespanner.NiceSpinner spinner;

    public IncSpinner(View viewRoot) {
        super(viewRoot);
    }
}

