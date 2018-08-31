package tms.space.lbs_driver.tms_required.head_bottom.interfaces;

import android.view.View;

import com.leezp.lib.singlepageapplication.base.SpaBaseFragment;

import tms.space.lbs_driver.tms_base.beans.IViewOnClickAction;

/**
 * Created by Leeping on 2018/7/18.
 * email: 793065165@qq.com
 */

public abstract class ItemAbs extends IViewOnClickAction {
    public ItemAbs(View view) {
        super(view);
    }
    public void onAction(){ }

    public abstract  void execute(SpaBaseFragment fragment);
}
