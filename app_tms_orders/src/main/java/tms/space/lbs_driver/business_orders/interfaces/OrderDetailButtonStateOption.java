package tms.space.lbs_driver.business_orders.interfaces;

import android.view.View;

import tms.space.lbs_driver.tms_base.beans.IViewOnClickManage;

/**
 * Created by Leeping on 2018/8/1.
 * email: 793065165@qq.com
 */

public final class OrderDetailButtonStateOption extends IViewOnClickManage<OrderDetailButtonStateAbs> implements View.OnClickListener{

    @Override
    public void add(OrderDetailButtonStateAbs v) {
        super.add(v);
        v.button.setOnClickListener(this);
    }

    //通知状态改变
    public void notifyState(int state){
        for (OrderDetailButtonStateAbs s : list){
            s.onState(state);
        }
    }
    @Override
    public void onClick(View v) {
        select(v.getId());
    }
}