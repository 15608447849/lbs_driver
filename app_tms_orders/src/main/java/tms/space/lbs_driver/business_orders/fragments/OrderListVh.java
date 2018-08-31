package tms.space.lbs_driver.business_orders.fragments;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.leezp.lib.recycles.PullOnLoad;
import com.leezp.lib.viewholder.annotations.RidClass;
import com.leezp.lib.viewholder.annotations.RidName;

import tms.space.lbs_driver.business_orders.R;
import tms.space.lbs_driver.tms_base.recycler.FragmentRecycleViewHolderAbs;
import tms.space.lbs_driver.tms_base.viewholder.base.IncRefreshRecyclerView;
import tms.space.lbs_driver.tms_base.viewholder.base.IncSpinner;

/**
 * Created by Leeping on 2018/7/26.
 * email: 793065165@qq.com
 */
@RidClass(R.id.class)
public class OrderListVh extends FragmentRecycleViewHolderAbs {

    @RidName("inc_order_list_year_select")
    public Button yearBtn;
    //下拉
    public IncSpinner incSpinner;
    //下拉刷新+list
    public IncRefreshRecyclerView incRefresh;

    @RidName("inc_order_list_tab_rg")
    public RadioGroup rg;

    @RidName("inc_order_list_tab_rg_claim")
    public RadioButton claim;

    @RidName("inc_order_list_tab_rg_transfer")
    public RadioButton transfer;

    @RidName("inc_order_list_tab_rg_complete")
    public RadioButton complete;

    @RidName("inc_order_list_span_refresh")
    public ImageButton spanRefresh;

    public PullOnLoad pullOnLoad;

    public OrderListVh(Context context) {
        super(context, R.layout.frg_order_list);
    }

    @Override
    public RecyclerView getRecyclerView() {
        return incRefresh.list.recycler;
    }

}
