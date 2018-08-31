package tms.space.lbs_driver.tms_required.head_bottom;

import android.content.Context;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.leezp.lib.viewholder.annotations.RidClass;
import com.leezp.lib.viewholder.annotations.RidName;
import com.leezp.lib.viewholder.interfaces.ViewHolderAbs;

import tms.space.lbs_driver.tms_required.R;


/**
 * Created by Leeping on 2018/7/18.
 * email: 793065165@qq.com
 */
@RidClass(R.id.class)
public class BottomMenuVh extends ViewHolderAbs {

    //单选框组
    @RidName("frg_bottom_menu_rb")
    public RadioGroup rb;

    @RidName("frg_bottom_menu_rb_order")
    public RadioButton order;
    @RidName("frg_bottom_menu_rb_personal")
    public RadioButton personal;


    public BottomMenuVh(Context context) {
        super(context, R.layout.frg_bottom_menu);
    }
}
