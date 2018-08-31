package tms.space.lbs_driver.tms_required.business_login;

import com.hsf.framework.api.driver.DriverBase;
import com.leezp.lib.util.MD5Util;

import tms.space.lbs_driver.tms_base.beans.BasePresenter;
import tms.space.lbs_driver.tms_base.beans.DriverUser;
import tms.space.lbs_driver.tms_base.business.contracts.LoginContract;
import tms.space.lbs_driver.tms_base.business.ice.PersonalIceModel;
import tms.space.lbs_driver.tms_base.util.BusinessUtil;


/**
 * Created by Leeping on 2018/6/28.
 * email: 793065165@qq.com
 */

public class LoginPresenter extends BasePresenter<LoginContract.View> implements LoginContract.Presenter {

    private boolean isAccess;

    private DriverUser user = new DriverUser().fetch();

    private final LoginContract.Model model = new PersonalIceModel();


    @Override
    public void onFailed(String cause) {
        isAccess = false;
        view.hindProgress();
        view.toast(cause);
    }

    @Override
    public void onSuccess(DriverBase driverBase) {
        isAccess = false;
        user = new DriverUser();
        user.setUserCode(driverBase.oid);
        user.setName(driverBase.urealname);
        user.setPhone(driverBase.uphone+"");
        user.setName(driverBase.urealname);
        user.save(); //存入数据库
        view.hindProgress();
        view.toast("登陆成功");
        tryLogin();
    }
   //登陆成功 尝试登陆
    public void tryLogin(){
       if (user!=null && view!=null){
              view.entry();
       }
    }
    //登陆验证
    @Override
    public void validateAccount(String phone, String password) {
        if (isAccess) view.toast("登录中,请稍等片刻");
        if (user == null) {
            if (!BusinessUtil.validatePhone(phone)) {
                view.showPhoneError("手机号码不正确");
                return;
            }
            if (!BusinessUtil.validatePassword(password)) {
                view.showPasswordError("密码不符合要求");
                return;
            }
            view.showProgress();
            isAccess = true;
            model.loginServer(phone, MD5Util.encryption(password), this);
        }
    }

}
