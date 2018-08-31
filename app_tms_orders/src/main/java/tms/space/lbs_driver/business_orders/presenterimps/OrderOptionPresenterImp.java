package tms.space.lbs_driver.business_orders.presenterimps;

import com.hsf.framework.api.driver.OrderComplex;
import com.leezp.lib.singlepageapplication.base.SpaBaseHandle;

import tms.space.lbs_driver.tms_base.beans.BasePresenter;
import tms.space.lbs_driver.tms_base.beans.DriverOrderInfo;
import tms.space.lbs_driver.tms_base.beans.StackOptionImp;
import tms.space.lbs_driver.tms_base.business.contracts.OrderDetailContract;

/**
 * Created by Leeping on 2018/8/13.
 * email: 793065165@qq.com
 */ //基类处理订单实现
public class OrderOptionPresenterImp<V extends OrderDetailContract.View> extends BasePresenter<V> implements OrderDetailContract.Presenter<V> {

    protected boolean isExecute;

    protected final OrderDetailContract.Model model;

    private StackOptionImp stackOptionImp = new StackOptionImp();

    //当前订单对象
    private DriverOrderInfo orderInfo;

    protected OrderOptionPresenterImp(OrderDetailContract.Model model) {
        this.model = model;
    }

    //获取当前订单
    @Override
    public DriverOrderInfo getOrderInfo() {
        return orderInfo;
    }

    //设置当前订单
    @Override
    public void setOrderInfo(DriverOrderInfo orderInfo) {
        this.orderInfo = orderInfo;
        stackOptionImp.setOrderInfo(this.orderInfo);
    }

    //查询订单详情
    @Override
    public void queryOrderInfo() {
        if (orderInfo == null || isExecute) return;
        view.showProgress();
        isExecute = true;
        OrderComplex complex = model.queryOrderInfo(
                orderInfo.getComp().compid,
                orderInfo.getInfo().orderNo);
        if (complex!=null){
            orderInfo.getInfo().complex = complex;
        }else{
            view.toast("无法获取订单信息");
        }
        view.hindProgress();
        isExecute = false;
    }

    //改变订单状态
    @Override
    public void changeOrderState(int state) {
        if (isExecute) return;
        view.showProgress();
        isExecute = true;

        boolean flag =model.changeOrderState(
                orderInfo.getUser().getUserCode(),
                orderInfo.getComp().compid,
                orderInfo.getInfo().orderNo,
                 state);
        if (flag) {
                orderInfo.getInfo().state = state;
                //状态改变,生成节点信息
                model.addStackText(
                        orderInfo.getUser().getName(),
                        orderInfo.getUser().getPhone(),
                        orderInfo.getUser().getUserCode(),
                        orderInfo.getComp().compid,
                        orderInfo.getInfo().orderNo,
                        orderInfo.getInfo().state
                       );
        }
        view.hindProgress();
        isExecute = false;
    }

    //添加轨迹记录
    @Override
    public void addTrackRecode(SpaBaseHandle handler) {
        stackOptionImp.addTrackRecode(handler);
    }

    //移除轨迹记录
    @Override
    public void removeTrackRecode(SpaBaseHandle handler) {
        stackOptionImp.removeTrackRecode(handler);
    }



}
