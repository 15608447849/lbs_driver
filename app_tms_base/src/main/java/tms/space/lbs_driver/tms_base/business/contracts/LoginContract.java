package tms.space.lbs_driver.tms_base.business.contracts;

import com.hsf.framework.api.driver.DriverBase;

import tms.space.lbs_driver.tms_base.beans.IBasePresenter;
import tms.space.lbs_driver.tms_base.beans.IBaseView;

/**
 * Created by Leeping on 2018/7/27.
 * email: 793065165@qq.com
 */

public class LoginContract {
    //模型层
    public interface Model{
        //回调给presenter
        interface Callback{
            //失败回调
            void onFailed(String cause);
            //成功回调
            void onSuccess(DriverBase driverBase);
        }
        //登陆服务器
        void loginServer(String phone,String password,Callback callback);
    }
    //视图层
    public interface View extends IBaseView<Presenter>{
        void showPhoneError(String msg); //账号错误
        void showPasswordError(String msg);//密码错误
        void entry();
    }
    //业务层
    public interface Presenter extends IBasePresenter<View>,Model.Callback{
        //登陆验证
        void validateAccount(String phone,String password);
       //尝试登陆
       void tryLogin();
    }

}
