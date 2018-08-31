package tms.space.lbs_driver.tms_base.business.contracts;

import com.leezp.lib.singlepageapplication.base.SpaBaseHandle;

import tms.space.lbs_driver.tms_base.beans.DriverOrderInfo;
import tms.space.lbs_driver.tms_base.beans.IBasePresenter;
import tms.space.lbs_driver.tms_base.beans.IBaseView;
import tms.space.lbs_driver.tms_base.beans.IStackOption;

/**
 * Created by Leeping on 2018/8/3.
 * email: 793065165@qq.com
 */

public class ScanQrContract {

    public interface Model extends OrderDetailContract.Model{
        //识别码二维内容
        boolean identificationCode(DriverOrderInfo info , String code);
    }

    public interface View extends IBaseView<Presenter>{
        //不识别的二维码或错误信息
        void popErrDialog(String msg);
        //对二维码处理成功-取货
        void toClaim(DriverOrderInfo orderInfo);
        //对二维码处理成功-再次运输
        void toCarriage(DriverOrderInfo orderInfo);
    }

    public interface Presenter extends IBasePresenter<View>,IStackOption{
        void handleOr(SpaBaseHandle handle,String code);
    }
}
