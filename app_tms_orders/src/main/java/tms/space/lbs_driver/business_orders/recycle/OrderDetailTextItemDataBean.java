package tms.space.lbs_driver.business_orders.recycle;

import android.text.SpannableString;

import com.leezp.lib.recycles.BaseViewHolderDataModel;

import tms.space.lbs_driver.business_orders.R;

/**
 * Created by Leeping on 2018/7/26.
 * email: 793065165@qq.com
 */

public class OrderDetailTextItemDataBean implements BaseViewHolderDataModel {

    public SpannableString data;

    public SpannableString data2;

    public String backColor;

    public OrderDetailTextItemDataBean(SpannableString data,String backColor) {
       this(data,null,backColor);
    }
    public OrderDetailTextItemDataBean(SpannableString data,SpannableString data2,String backColor) {
        this.data = data;
        this.data2 = data2;
        this.backColor = backColor;

    }


    @Override
    public int getViewTemplateType() {
        return R.layout.rec_item_order_detail_text;
    }

    @Override
    public OrderDetailTextItemDataBean convert() throws ClassCastException {
        return this;
    }


}
