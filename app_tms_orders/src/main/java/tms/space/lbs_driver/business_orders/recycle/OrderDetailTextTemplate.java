package tms.space.lbs_driver.business_orders.recycle;

import android.graphics.Color;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.TextView;

import com.leezp.lib.viewholder.annotations.RidClass;
import com.leezp.lib.viewholder.annotations.RidName;

import tms.space.lbs_driver.business_orders.R;
import tms.space.lbs_driver.tms_base.recycler.RecyclerViewHolderAbs;

/**
 * Created by Leeping on 2018/7/26.
 * email: 793065165@qq.com
 */
@RidClass(R.id.class)
public class OrderDetailTextTemplate extends RecyclerViewHolderAbs<OrderDetailTextItemDataBean> {

    @RidName("rec_item_order_detail_tv")
    public TextView tv;

    @RidName("rec_item_order_detail_tv2")
    public TextView tv2;

    public OrderDetailTextTemplate(View itemView) {
        super(itemView);
        tv.setMovementMethod(LinkMovementMethod.getInstance());
    }

    @Override
    protected void bindData(OrderDetailTextItemDataBean d) {
        tv.setText(d.data);
        if (d.data2!=null) {
            tv2.setText(d.data2);
        } else {
            tv2.setText("");
        }
        if (d.backColor!=null) {
            itemView.setBackgroundColor(Color.parseColor(d.backColor));
        }else{
            itemView.setBackgroundColor(Color.TRANSPARENT);
        }
    }

}
