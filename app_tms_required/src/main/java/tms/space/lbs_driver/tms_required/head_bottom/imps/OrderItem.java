package tms.space.lbs_driver.tms_required.head_bottom.imps;

import android.view.View;

import com.leezp.lib.singlepageapplication.base.SpaBaseFragment;

import tms.space.lbs_driver.tms_required.head_bottom.interfaces.ItemAbs;

/**
 * Created by Leeping on 2018/7/18.
 * email: 793065165@qq.com
 * 选中订单
 */

public class OrderItem extends ItemAbs {
    public OrderItem(View view) {
        super(view);
    }

    @Override
    public void execute(SpaBaseFragment fragment) {
        fragment.skip("content","order");
    }
}
