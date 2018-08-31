package tms.space.lbs_driver.business_orders.converts;

import android.content.Context;

import com.hsf.framework.api.driver.OrderComplex;
import com.hsf.framework.api.driver.OrderInfo;

import tms.space.lbs_driver.tms_base.business.contracts.OrderDetailContract;

/**
 * Created by Leeping on 2018/8/12.
 * email: 793065165@qq.com
 * 运输中
 */

public class ConvertClaim extends OrderDetailConvert {
    @Override
    public void convert(Context context, OrderDetailContract.OrderDetailView view, OrderInfo info, OrderComplex complex, String imagePath) {
        super.convert(context,view,info,complex,imagePath);
        //送达地址
        deliverAddress(view,info,complex);
        //收货人
        takeInfo(context,view,info,complex);
        //到货时间
        append(colon("送达时间")).append(complex.arrivalTime);
        view.setListItemData(createSpannable(fontStyle,contentSize),backColor);
        //分割线
        view.setListItemData(1);
        //订单费用
        feeInfo(view,info,complex);
        view.setListItemData(append("").createSpannable(),backColor);
        //间隔线
        view.setListItemData(2);
        //取货地址
        claimAddress(view,info,complex);
        //货物信息
        append(colon("货物信息")).append(complex.cargoInfo);
        view.setListItemData(createSpannable(fontStyle,contentSize),backColor);
        //费用信息
        append(colon("费用信息")).append(complex.feeInfo);
        view.setListItemData(createSpannable(fontStyle,contentSize),backColor);
        //联系方式
        contactInformation(context,view,info,complex);
        //间隔线
        view.setListItemData(2);
        //间隔线
        view.setListItemData(2);
        //时间
        time(view,complex);

    }
}
