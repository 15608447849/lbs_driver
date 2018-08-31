package tms.space.lbs_driver.tms_required.business_person.imps;

import com.leezp.lib.singlepageapplication.base.SpaBaseFragment;
import com.leezp.lib.update.ApkUpdateConfig;

import tms.space.lbs_driver.tms_required.business_person.interfaces.OnClickItemHandle;

/**
 * Created by Leeping on 2018/8/15.
 * email: 793065165@qq.com
 */

public class CheckUpdateApk implements OnClickItemHandle {
    @Override
    public void onClick(SpaBaseFragment spaBaseFragment) {
        ApkUpdateConfig.userCheckUpdate();
    }
}
