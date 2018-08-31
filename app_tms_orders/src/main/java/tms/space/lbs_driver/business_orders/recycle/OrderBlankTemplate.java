package tms.space.lbs_driver.business_orders.recycle;

import android.view.View;

import com.leezp.lib.viewholder.annotations.RidClass;

import tms.space.lbs_driver.business_orders.R;
import tms.space.lbs_driver.tms_base.recycler.RecyclerViewHolderAbs;

/**
 * Created by Leeping on 2018/8/15.
 * email: 793065165@qq.com
 */
@RidClass(R.id.class)
public class OrderBlankTemplate extends RecyclerViewHolderAbs<OrderBlankItemDataBean> {

    public OrderBlankTemplate(View itemView) {
        super(itemView);
    }

    /**
     * 在 适配器中 数据与视图绑定时 调用 ,子类在此处关联数据
     *
     * @param dataModel
     */
    @Override
    protected void bindData(OrderBlankItemDataBean dataModel) {

    }
}
