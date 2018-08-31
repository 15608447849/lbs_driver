package tms.space.lbs_driver.business_orders.recycle;

import android.graphics.Color;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
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
public class OrderListTemplate extends RecyclerViewHolderAbs<OrderListItemDataBean> {

    @RidName("rec_item_order_list_tv_left_top")
    public TextView leftTop;

    @RidName("rec_item_order_list_tv_left_bottom")
    public TextView leftBottom;

    @RidName("rec_item_order_list_tv_right_top")
    public TextView rightTop;

    @RidName("rec_item_order_list_tv_right_bottom")
    public TextView rightBottom;

    public OrderListTemplate(View itemView) {
        super(itemView);
    }

    @Override
    protected void bindData(OrderListItemDataBean d) {
        setLeftTop(d.leftTop);
        setRightTop(d.rightTop);
        setLeftBottom(d.leftBottom);
        setRightBottom(d.rightBottom);
    }
    //黑体加粗
    private void setLeftTop(String str) {
        SpannableString span = new SpannableString(str);
        span.setSpan(new StyleSpan(Typeface.BOLD_ITALIC),0,str.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        leftTop.setText(span);
    }
    //相对小 红色
    private void setRightTop(String str) {
        SpannableString span = new SpannableString(str);
        span.setSpan(new RelativeSizeSpan(0.6f),0,str.length(),Spanned.SPAN_INCLUSIVE_INCLUSIVE);
       rightTop.setText(span);
       rightTop.setTextColor(Color.RED);
    }
    // #a9a9a9
    private void setLeftBottom(String str){
        SpannableString span = new SpannableString(str);
        span.setSpan(new RelativeSizeSpan(0.8f),0,str.length(),Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        leftBottom.setText(span);
        leftBottom.setTextColor(Color.parseColor("#a9a9a9"));
    }
    //#676767
    private void setRightBottom(String str){
        SpannableString span = new SpannableString(str);
        span.setSpan(new RelativeSizeSpan(0.6f),0,str.length(),Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        rightBottom.setText(span);
        rightBottom.setTextColor(Color.parseColor("#676767"));
    }

}
