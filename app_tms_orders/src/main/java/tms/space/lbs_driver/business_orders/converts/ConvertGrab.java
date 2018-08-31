package tms.space.lbs_driver.business_orders.converts;

import android.content.Context;

import com.hsf.framework.api.driver.OrderComplex;
import com.hsf.framework.api.driver.OrderInfo;

import tms.space.lbs_driver.tms_base.business.contracts.OrderDetailContract;

/**
 * Created by Leeping on 2018/8/12.
 * email: 793065165@qq.com
 * 待取货
 */

public class ConvertGrab extends OrderDetailConvert {
    @Override
    public void convert(Context context,OrderDetailContract.OrderDetailView view, OrderInfo info, OrderComplex complex, String imagePath) {
        super.convert(context,view,info,complex,imagePath);
        //取货地址
        claimAddress(view,info,complex);
        //送达地址
        deliverAddress(view,info,complex);
        //货物信息
        append(colon("货物信息")).append(complex.cargoInfo);
        view.setListItemData(createSpannable(fontStyle,contentSize),backColor);
        //车辆信息
        append(colon("车辆要求")).append(complex.carInfo);
        view.setListItemData(createSpannable(fontStyle,contentSize),backColor);
        //费用信息
        append(colon("费用信息")).append(complex.feeInfo);
        view.setListItemData(createSpannable(fontStyle,contentSize),backColor);
        //取货时间
        append(colon("取货时间")).append(complex.pickTime);
        view.setListItemData(createSpannable(fontStyle,contentSize),backColor);

        //联系方式
        contactInformation(context,view,info,complex);
        //间隔线
        view.setListItemData(2);

        //收货人
        takeInfo(context,view,info,complex);
        //到货时间
        append(colon("送达时间")).append(complex.arrivalTime);
        view.setListItemData(createSpannable(fontStyle,contentSize),backColor);
        //间隔线
        view.setListItemData(2);
        //支付信息 无背景
        feeInfo(view,info,complex);
        //间隔线
        view.setListItemData(2);
        //间隔线
        view.setListItemData(2);

        //发单时间  字体相对小
        //抢单时间  字体相对小
        time(view,complex);


    }



}
