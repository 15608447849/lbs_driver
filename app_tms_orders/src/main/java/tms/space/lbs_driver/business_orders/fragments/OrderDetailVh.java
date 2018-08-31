package tms.space.lbs_driver.business_orders.fragments;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.leezp.lib.viewholder.annotations.OnClicked;
import com.leezp.lib.viewholder.annotations.RidClass;
import com.leezp.lib.viewholder.annotations.RidName;

import tms.space.lbs_driver.business_orders.R;
import tms.space.lbs_driver.tms_base.recycler.FragmentRecycleViewHolderAbs;
import tms.space.lbs_driver.tms_base.viewholder.base.IncRecyclerView;

/**
 * Created by Leeping on 2018/7/26.
 * email: 793065165@qq.com
 */
@RidClass(R.id.class)
public class OrderDetailVh extends FragmentRecycleViewHolderAbs{
    @RidName("frg_order_detail_iv")
    public ImageView iv;

    @RidName("frg_order_detail_tv")
    public TextView tv;

    public IncRecyclerView list;

    @OnClicked
    @RidName("inc_frg_order_detail_btn_pickup")
    public Button pickup;

    @OnClicked
    @RidName("inc_frg_order_detail_btn_unload")
    public Button unload;

    @OnClicked
    @RidName("inc_frg_order_detail_btn_sign")
    public Button sign;




    public OrderDetailVh(Context context) {
        super(context,R.layout.frg_order_details);

    }

    @Override
    public RecyclerView getRecyclerView() {
        return list.recycler;
    }
}
