package tms.space.lbs_driver.tms_base.business.ice;

import com.hsf.framework.api.driver.DriverBase;
import com.hsf.framework.api.driver.DriverServicePrx;
import com.leezp.lib.zerocice.IceServerAbs;

import IceInternal.BasicStream;
import tms.space.lbs_driver.tms_base.business.contracts.LoginContract;

/**
 * Created by Leeping on 2018/7/13.
 * email: 793065165@qq.com
 */

public class PersonalIceModel extends IceServerAbs<DriverServicePrx> implements LoginContract.Model  {
    //登陆
    @Override
    public void loginServer(String phone, String password, Callback callback) {
//        callback.onSuccess(DATA.user());
        try {
            printParam("登录",phone,password);
            DriverBase driverBase = getProxy().driverLogin(phone,password);
            if (driverBase.code==0){
                callback.onSuccess(driverBase);
            }else{
                callback.onFailed(driverBase.msg);
            }
        } catch (Exception e) {
            e.printStackTrace();
            callback.onFailed("网络不可用或服务器连接失败");
        }
    }
}
