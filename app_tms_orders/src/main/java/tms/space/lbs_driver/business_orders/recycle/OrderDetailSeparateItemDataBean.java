package tms.space.lbs_driver.business_orders.recycle;

import com.leezp.lib.recycles.BaseViewHolderDataModel;

import tms.space.lbs_driver.business_orders.R;

/**
 * Created by Leeping on 2018/8/11.
 * email: 793065165@qq.com
 */


public class OrderDetailSeparateItemDataBean implements BaseViewHolderDataModel {

    private int rid;

    public OrderDetailSeparateItemDataBean(int type) {
        if (type == 1) rid = R.layout.rec_item_order_detail_line;
        else if (type == 2) rid = R.layout.rec_item_order_detail_cutoff;
        else throw new IllegalArgumentException("type no match layout id");
    }

    @Override
    public int getViewTemplateType() {
        return rid;
    }

    @Override
    public <DATA> DATA convert() throws ClassCastException {
        return null;
    }
}