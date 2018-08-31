package tms.space.lbs_driver.tms_required.head_bottom.imps;

import android.view.View;

import com.leezp.lib.singlepageapplication.base.SpaBaseFragment;

import tms.space.lbs_driver.tms_required.head_bottom.interfaces.ItemAbs;

/**
 * Created by Leeping on 2018/7/19.
 * email: 793065165@qq.com
 */

public class ScanItem extends ItemAbs {
    public ScanItem(View view) {
        super(view);
    }

    @Override
    public void execute(final SpaBaseFragment fragment) {

        //进入扫码fragment,添加到当前组回退栈
        fragment.addStack("content","scan");
    }
}
