package tms.space.lbs_driver.business_orders.recycle;

import com.leezp.lib.recycles.BaseViewHolderDataModel;

import tms.space.lbs_driver.business_orders.R;

/**
 * Created by Leeping on 2018/7/26.
 * email: 793065165@qq.com
 */

public class OrderListItemDataBean implements BaseViewHolderDataModel {

    public String leftTop = null;
    public String leftBottom = null;
    public String rightTop = null;
    public String rightBottom = null;

    public OrderListItemDataBean(String leftTop, String leftBottom, String rightTop, String rightBottom) {
        this.leftTop = leftTop;
        this.leftBottom = leftBottom;
        this.rightTop = rightTop;
        this.rightBottom = rightBottom;
    }

    @Override
    public int getViewTemplateType() {
        return R.layout.rec_item_order_list;
    }

    @Override
    public OrderListItemDataBean convert() throws ClassCastException {
        return this;
    }
}
