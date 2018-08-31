package tms.space.lbs_driver.business_orders.presenterimps;

import tms.space.lbs_driver.tms_base.beans.DriverOrderInfo;
import tms.space.lbs_driver.tms_base.business.contracts.OrderDetailContract;
import tms.space.lbs_driver.tms_base.business.ice.OrderIceModel;

/**
 * Created by Leeping on 2018/8/2.
 * email: 793065165@qq.com
 *
 */

public class OrderFeePresenter extends OrderOptionPresenterImp<OrderDetailContract.OrderFeeView> implements  OrderDetailContract.OrderFeePresenter {
    public OrderFeePresenter(DriverOrderInfo orderInfo) {
        super(new OrderIceModel());
        setOrderInfo(orderInfo);
    }
    @Override
    public void updateOrderFee(double fee) {
        if (isExecute) {
            view.toast("确认费用中,请等候");
            return;
        };
        if ( getOrderInfo().getInfo().complex.fee != fee) {
            isExecute = true;
            view.showProgress();
            //修改费用
            boolean flag = model.updateOrderFee(
                    getOrderInfo().getComp().compid,
                    getOrderInfo().getInfo().orderNo,
                    fee);

            view.hindProgress();
            isExecute= false;
            if (!flag){
                view.toast("修改失败");
                return;
            }
        };
        view.callback();
    }
}
