package tms.space.lbs_driver.business_orders.presenterimps;

import android.annotation.SuppressLint;

import com.hsf.framework.api.driver.OrderInfo;
import com.hsf.framework.api.driver.sCLAIM;
import com.hsf.framework.api.driver.sComplete;
import com.hsf.framework.api.driver.sEvaluate;
import com.hsf.framework.api.driver.sGRAB;
import com.hsf.framework.api.driver.sSIGN;
import com.hsf.framework.api.driver.sUnload;
import com.leezp.lib.singlepageapplication.base.SpaBaseActivity;
import com.leezp.lib.singlepageapplication.base.SpaBaseHandle;
import com.leezp.lib.util.AppUtil;

import java.util.HashMap;

import tms.space.lbs_driver.business_orders.converts.ConvertClaim;
import tms.space.lbs_driver.business_orders.converts.ConvertGrab;
import tms.space.lbs_driver.business_orders.converts.ConvertSign;
import tms.space.lbs_driver.business_orders.converts.OrderDetailConvert;
import tms.space.lbs_driver.tms_base.business.contracts.OrderDetailContract;
import tms.space.lbs_driver.tms_base.business.ice.OrderIceModel;

/**
 * Created by Leeping on 2018/8/1.
 * email: 793065165@qq.com
 */

public class OrderDetailPresenter extends OrderOptionPresenterImp<OrderDetailContract.OrderDetailView> implements OrderDetailContract.OrderDetailPresenter {
    @SuppressLint("UseSparseArrays")
    private HashMap<Integer,OrderDetailConvert> map = new HashMap<>();

    public OrderDetailPresenter() {
        super(new OrderIceModel());
        map.put(sGRAB.value,new ConvertGrab());
        map.put(sCLAIM.value,new ConvertClaim());
        map.put(sUnload.value,new ConvertClaim());
        map.put(sSIGN.value,new ConvertSign());
        map.put(sEvaluate.value,new ConvertSign());
        map.put(sComplete.value,new ConvertSign());
    }

    private OrderDetailConvert getConvert(int state) {
        return map.get(state);
    }


    //更新订单
    @Override
    public void updateOrder(SpaBaseActivity context) {
        if (AppUtil.checkUIThread()) throw new IllegalStateException("无法在主线程更新订单");

        if (!getOrderInfo().checkValid()){
            queryOrderInfo();//向后台查询最新订单详情
            if (!getOrderInfo().checkValid()){
                if (view!=null) view.clearList();
                if (view!=null) view.setListDataItemBlank();
                if (view!=null) view.updateList();
                return;
            }
        }

        OrderInfo info = getOrderInfo().getInfo();
        int enterpriseid = getOrderInfo().getComp().compid;
        String imagePath = null;
        if (info.state == sCLAIM.value){
            addTrackRecode(context.getSpaBaseHandle());
        }
        if ( !(info.state == sGRAB.value || info.state == sCLAIM.value) ){
            imagePath = model.imagePath(enterpriseid,info.orderNo);
        }
        //根据状态获取 显示策略
        OrderDetailConvert convert = getConvert(info.state);
        if (convert!=null) {
            //清理列表
            if (view!=null) view.clearList();
            if (view!=null) convert.convert(context,view,info,info.complex,imagePath);
            if (view!=null) view.updateList();
        }
        //更新按钮状态
        if (view!=null) view.updateState(info.state);

    }

    //签收操作
    @Override
    public void signOp() {

        if (getOrderInfo().getInfo().complex.isOnline){
            if (getOrderInfo().getInfo().complex.isPay){ //客户支付成功
                if (view!=null) view.allowSign();
            }else{
                //获取最新订单信息
                queryOrderInfo();
                if (!getOrderInfo().getInfo().complex.isPay){
                    if (view!=null) view.toast("客户未支付费用,无法签收,请与货主确认后再次尝试");
                }
            }
        }else{
            if (view!=null) view.allowSign();
        }
    }

    @Override
    public void unloadOp(SpaBaseHandle spaHandle) {
        //尝试改变订单状态
        changeOrderState(sUnload.value);
        if (getOrderInfo().getInfo().state == sUnload.value){
            //停止轨迹
            removeTrackRecode(spaHandle);
            if (view!=null) view.allowUnload();
        }else{
            if (view!=null) view.toast("无法进行卸载操作");
        }
    }

}
