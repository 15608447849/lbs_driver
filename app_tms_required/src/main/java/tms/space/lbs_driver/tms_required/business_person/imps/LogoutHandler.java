package tms.space.lbs_driver.tms_required.business_person.imps;

import com.leezp.lib.singlepageapplication.base.SpaBaseFragment;

import tms.space.lbs_driver.tms_base.beans.DriverUser;
import tms.space.lbs_driver.tms_required.business_person.interfaces.OnClickItemHandle;

/**
 * Created by Leeping on 2018/7/25.
 * email: 793065165@qq.com
 * 点击登出操作
 */

public class LogoutHandler implements OnClickItemHandle {
    @Override
    public void onClick(SpaBaseFragment spaBaseFragment) {
        //移除头部
        spaBaseFragment.remove("head","head");
        //移除底部
        spaBaseFragment.remove("bottom","bottom");
        //删除登陆用户数据
        new DriverUser().fetch().remove();
        //清空回退栈
        spaBaseFragment.clearAll();
        //显示登陆界面
        spaBaseFragment.skip("content","login");
    }
}
