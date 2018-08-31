package tms.space.lbs_driver.tms_required.business_person.imps;

import com.leezp.lib.singlepageapplication.base.SpaBaseFragment;

import tms.space.lbs_driver.tms_required.business_person.interfaces.OnClickItemHandle;

/**
 * Created by Leeping on 2018/7/25.
 * email: 793065165@qq.com
 * 点击跳转到密码修改
 */

public class ChangePasswordHandler implements OnClickItemHandle{
    @Override
    public void onClick(SpaBaseFragment spaBaseFragment) {
        spaBaseFragment.addStack("content","change_password");
    }
}
