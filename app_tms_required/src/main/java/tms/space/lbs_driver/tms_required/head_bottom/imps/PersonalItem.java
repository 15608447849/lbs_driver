package tms.space.lbs_driver.tms_required.head_bottom.imps;

import android.view.View;

import com.leezp.lib.singlepageapplication.base.SpaBaseFragment;

import tms.space.lbs_driver.tms_required.head_bottom.interfaces.ItemAbs;

/**
 * Created by Leeping on 2018/7/18.
 * email: 793065165@qq.com
 */

public class PersonalItem extends ItemAbs {
    public PersonalItem(View view) {
        super(view);
    }

    @Override
    public void execute(SpaBaseFragment fragment) {
        //跳转到个人中心
        fragment. skip("content","personal");
    }
}
