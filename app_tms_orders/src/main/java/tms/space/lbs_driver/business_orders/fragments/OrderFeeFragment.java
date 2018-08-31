package tms.space.lbs_driver.business_orders.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import tms.space.lbs_driver.business_orders.presenterimps.OrderFeePresenter;
import tms.space.lbs_driver.tms_base.beans.DriverOrderInfo;
import tms.space.lbs_driver.tms_base.business.contracts.OrderDetailContract;
import tms.space.lbs_driver.tms_base.fragments.CanBackUpFragmentAbs;

/**
 * Created by Leeping on 2018/7/29.
 * email: 793065165@qq.com
 * 确认收费金额
 */

public class OrderFeeFragment extends CanBackUpFragmentAbs<OrderFeeVh,OrderDetailContract.OrderFeePresenter> implements View.OnClickListener,OrderDetailContract.OrderFeeView {


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setPresenter( new OrderFeePresenter((DriverOrderInfo) getDataIn()));

    }

    @Override
    public void viewCreated() {
        vh.button.setOnClickListener(this);
        vh.tv.setText("请对运费金额确认,可进行修改,如有异议请与货主协商");
        vh.et.setText(String.valueOf(presenter.getOrderInfo().getInfo().complex.fee));
        setHeadTitle("请确认费用信息");
    }

    @Override
    public void onClick(View v) {
        //确认收款成功
        final String fee = vh.et.getText().toString().trim();
        //调用后台接口 发送金额
        toIo(new Runnable() {
            @Override
            public void run() {
                presenter.updateOrderFee(Double.parseDouble(fee));
            }
        });

    }

    @Override
    public void callback() {
        //已确认金额
        setDataOut(presenter.getOrderInfo());
        backUp();
    }


}
