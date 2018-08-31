package tms.space.lbs_driver.business_orders.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.leezp.lib.cusview.vc.VerificationCodeView;
import com.leezp.lib.util.AppUtil;

import tms.space.lbs_driver.tms_base.beans.DriverOrderInfo;
import tms.space.lbs_driver.tms_base.beans.IBasePresenter;
import tms.space.lbs_driver.tms_base.fragments.CanBackUpFragmentAbs;

/**
 * Created by Leeping on 2018/7/28.
 * email: 793065165@qq.com
 * 订单确认收款码
 */

public class OrderClaimFragment extends CanBackUpFragmentAbs<OrderClaimVh,IBasePresenter> implements  VerificationCodeView.OnCodeFinishListener {

    private DriverOrderInfo orderInfo;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        orderInfo = (DriverOrderInfo)getDataIn();
    }


    @Override
    public void viewCreated() {
        vh.verificationCodeView.setOnCodeFinishListener(this);
        setHeadTitle("请输入取货码");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        vh.verificationCodeView.clearFocus();
        AppUtil.hideSoftInputFromWindow(getSpaActivity());
    }

    //输入完成
    @Override
    public void onComplete(final String content) {
        verification(content);
    }

    //验证码验证
    private void verification(String content) {
        try {
            if (content.equals(orderInfo.getInfo().complex.claimCode)){
                //结束自己
                killSelf();
                //传送数据到下一个界面
                setDataOut(orderInfo);
                //进入拍照界面
                addStack("take_picture");
            }else {
                toast("取货码错误");
                vh.verificationCodeView.clearContent();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
