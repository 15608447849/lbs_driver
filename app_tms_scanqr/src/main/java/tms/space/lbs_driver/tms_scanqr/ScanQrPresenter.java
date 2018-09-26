package tms.space.lbs_driver.tms_scanqr;

import com.hsf.framework.api.driver.OrderInfo;
import com.hsf.framework.api.driver.sCLAIM;
import com.hsf.framework.api.driver.sGRAB;
import com.hsf.framework.api.driver.sUnload;
import com.leezp.lib.singlepageapplication.base.SpaBaseHandle;

import tms.space.lbs_driver.tms_base.beans.BasePresenter;
import tms.space.lbs_driver.tms_base.beans.DriverOrderInfo;
import tms.space.lbs_driver.tms_base.beans.DriverUser;
import tms.space.lbs_driver.tms_base.beans.StackOptionImp;
import tms.space.lbs_driver.tms_base.business.contracts.ScanQrContract;
import tms.space.lbs_driver.tms_base.business.ice.OrderIceModel;

/**
 * Created by Leeping on 2018/8/3.
 * email: 793065165@qq.com
 */

public class ScanQrPresenter extends BasePresenter<ScanQrContract.View> implements ScanQrContract.Presenter {
    private ScanQrContract.Model model = new OrderIceModel();
    private boolean isHandle = false;
    private final DriverOrderInfo info = new DriverOrderInfo();
    private StackOptionImp stack = new StackOptionImp();

    public ScanQrPresenter() {
        stack.setOrderInfo(info);
    }

    @Override
    public void handleOr(SpaBaseHandle handle,String code) {
        if (isHandle) return;
        if (view!=null) view.showProgress();
        isHandle = true;
        boolean flag = model.identificationCode(info,code);
        if (view!=null) view.hindProgress();
        isHandle = false;
        if (flag) { //代表可以处理的二维码
            OrderInfo o = info.getInfo();
            if (o.state == sGRAB.value){
                //其他公司司机扫码 - 转发布 - 跳转到拍照界面
                if (view!=null) view.toClaim(info);
                return;
            }
            if (o.state == sUnload.value){
                if (view!=null) view.showProgress();
                isHandle = true;
                //同公司另外一个司机扫码 - 直接取货
                flag = model.changeOrderState(
                        info.getUser().getUserCode(),
                        info.getComp().compid,
                        info.getInfo().orderNo,
                        sCLAIM.value);
                if (view!=null) view.hindProgress();
                isHandle = false;
                if (flag){
                    if (view!=null) view.showProgress();
                    isHandle = true;
                    info.getInfo().state = sCLAIM.value;
                    //添加走货信息
                    model.addStackText(
                            info.getUser().getName(),
                            info.getUser().getPhone(),
                            info.getUser().getUserCode(),
                            info.getComp().compid,
                            info.getInfo().orderNo,
                            info.getInfo().state);
                    //添加轨迹记录
                    addTrackRecode(handle);
                    if (view!=null) view.hindProgress();
                    isHandle = false;
                    view.toCarriage(info);
                    return;
                }
            }
        }
        if (view!=null) view.popErrDialog(code);
    }

    /**
     * 添加轨迹记录
     *
     * @param handler
     */
    @Override
    public void addTrackRecode(SpaBaseHandle handler) {
        stack.addTrackRecode(handler);
    }

    /**
     * 移除轨迹记录
     *
     * @param handler
     */
    @Override
    public void removeTrackRecode(SpaBaseHandle handler) {
        stack.removeTrackRecode(handler);
    }
}
