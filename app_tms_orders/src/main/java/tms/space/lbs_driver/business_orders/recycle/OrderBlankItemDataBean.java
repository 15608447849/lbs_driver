package tms.space.lbs_driver.business_orders.recycle;

import com.leezp.lib.recycles.BaseViewHolderDataModel;

import tms.space.lbs_driver.business_orders.R;

public class OrderBlankItemDataBean implements BaseViewHolderDataModel {

    @Override
    public int getViewTemplateType() {
        return R.layout.rec_item_order_list_black;
    }

    @Override
    public OrderBlankItemDataBean convert() throws ClassCastException {
        return this;
    }
}
