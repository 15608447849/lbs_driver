package tms.space.lbs_driver.tms_required.business_person;

import android.view.View;

import com.hsf.framework.api.driver.DriverServicePrx;
import com.leezp.lib.util.MD5Util;
import com.leezp.lib.util.StrUtil;
import com.leezp.lib.zerocice.IceIo;

import tms.space.lbs_driver.tms_base.beans.DriverUser;
import tms.space.lbs_driver.tms_base.beans.IBasePresenter;
import tms.space.lbs_driver.tms_base.fragments.CanBackUpFragmentAbs;

/**
 * Created by Leeping on 2018/7/25.
 * email: 793065165@qq.com
 */

public class ChangePasswordFragment extends CanBackUpFragmentAbs<ChangePasswordVh,IBasePresenter> implements View.OnClickListener {



    @Override
    public void viewCreated() {
        vh.setListener(this);
        setHeadTitle("修改密码");
    }

    @Override
    public void onClick(View v) {
        tryChange();
    }

    //尝试修改密码
    private void tryChange() {
        final String originalPassword = vh.originalPw.getText().toString();
        final String newPassword = vh.newPw.getText().toString();
        final String surePassword = vh.surePw.getText().toString();

        if (!StrUtil.validateGroup(originalPassword, newPassword, surePassword)) {
            toast("请输入完整密码");
            return;
        }

        if (!newPassword.equals(surePassword)) {
            toast("两次输入密码不一致");
            return;
        }

        if (originalPassword.equals(surePassword)) {
            toast("原始密码和新密码相同");
            return;
        }

        mHandle.toIo(new Runnable() {
            @Override
            public void run() {
                try {
                    DriverUser user = new DriverUser().fetch();
                    DriverServicePrx driverServicePrx = IceIo.get().getIceClient().getServicePrx(DriverServicePrx.class);
                    int result = driverServicePrx.driverChangePw(
                            user.getPhone(),
                            MD5Util.encryption(originalPassword),
                            MD5Util.encryption(surePassword));
                    if (result == 0) {
                        toast("修改成功");
                    } else {
                        throw new IllegalArgumentException("原密码不正确");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    if (e instanceof IllegalArgumentException){
                        toast(e.getMessage());
                    }else{
                        toast("修改失败,服务器异常");
                    }

                }
            }
        });
    }

}
