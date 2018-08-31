package tms.space.lbs_driver.business_orders.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RadioGroup;

import com.hsf.framework.api.driver.sCLAIM;
import com.hsf.framework.api.driver.sGRAB;
import com.hsf.framework.api.driver.sSIGN;
import com.leezp.lib.recycles.BaseViewHolder;
import com.leezp.lib.recycles.BaseViewHolderDataModel;
import com.leezp.lib.recycles.PullOnLoad;
import com.leezp.lib.recycles.more_view_adapter.ItemViewTemplateAttribute;
import com.leezp.lib.recycles.more_view_adapter.ItemViewTemplateManage;
import com.leezp.lib.util.PickerUtil;

import java.util.List;

import tms.space.lbs_driver.business_orders.R;
import tms.space.lbs_driver.business_orders.presenterimps.OrderListPresenter;
import tms.space.lbs_driver.business_orders.recycle.OrderBlankItemDataBean;
import tms.space.lbs_driver.business_orders.recycle.OrderBlankTemplate;
import tms.space.lbs_driver.business_orders.recycle.OrderListItemDataBean;
import tms.space.lbs_driver.business_orders.recycle.OrderListTemplate;
import tms.space.lbs_driver.tms_base.beans.DriverOrderInfo;
import tms.space.lbs_driver.tms_base.beans.IViewOnClickAction;
import tms.space.lbs_driver.tms_base.beans.IViewOnClickManage;
import tms.space.lbs_driver.tms_base.business.contracts.OrderListContract;
import tms.space.lbs_driver.tms_base.fragments.RelevanceHeadFragment;

/**
 * Created by Leeping on 2018/7/25.
 * email: 793065165@qq.com
 *
 *
 */

public class OrderListFragment extends RelevanceHeadFragment<OrderListVh,OrderListContract.Presenter> implements AdapterView.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener,RadioGroup.OnCheckedChangeListener,OrderListContract.View, View.OnClickListener, PullOnLoad.RecyclerViewLoadMoreListener {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setPresenter(new OrderListPresenter());
    }

    //list适配器使用的模板类型
    @Override
    protected ItemViewTemplateManage getMoreRecItemManage() {
        return new ItemViewTemplateManage(){
            @Override
            public void initItemLayout() {
                addAttr(new ItemViewTemplateAttribute(OrderListTemplate.class, R.layout.rec_item_order_list));
                //空白
                addAttr(new ItemViewTemplateAttribute(OrderBlankTemplate.class, R.layout.rec_item_order_list_black));
            }
        };
    }


    private IViewOnClickManage tabManage = new IViewOnClickManage();

    //初始化控件
    @Override
    public void viewCreated() {

        vh.incRefresh.refresh.setOnRefreshListener(this);
        vh.incSpinner.spinner.addOnItemClickListener(this);
        vh.rg.setOnCheckedChangeListener(this);
        vh.spanRefresh.setOnClickListener(this);
        vh.pullOnLoad = new PullOnLoad(vh.getRecyclerView());
        vh.pullOnLoad.setRecyclerViewLoadMoreListener(this);
        vh.yearBtn.setOnClickListener(this);
        initTabSelectAction();

        setHeadTitle("转运订单");
        setShowHeadElement(new boolean[]{false,true});//头部显示扫码
        vh.claim.toggle(); //点击可取货订单
        vh.yearBtn.setText(presenter.getYearList().get(presenter.getYearSelect())+"年");
    }

    @Override
    public void onResume() {
        super.onResume();
       if (isVisible()){
           queryOrder(true); //请求订单
       }
    }
    //请求订单信息
    private void queryOrder(final boolean flag) {
        toIo(new Runnable() {
            @Override
            public void run() {
                if (flag) presenter.queryOrderList();
                else  presenter.queryOrderListHistory();
            }
        });
    }

    /**下拉框选中*/
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        presenter.selectCompPos(position); //设置选中的企业
        queryOrder(true); //请求订单信息
    }

    /**tab 单选框选中回调*/
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        tabManage.select(checkedId);
    }

    // 点击TAB刷新列表
    private void initTabSelectAction() {
        tabManage.add(
                //获取取货列表
                new IViewOnClickAction(vh.claim) {
                    @Override protected void onAction() {
                    presenter.setQueryOrderState(sGRAB.value);
                    queryOrder(true);
                    }},
                //获取运输列表
                new IViewOnClickAction(vh.transfer) {
                    @Override protected void onAction() {
                    presenter.setQueryOrderState(sCLAIM.value);
                    queryOrder(true);
                    }},
                //获取完成列表
                new IViewOnClickAction(vh.complete) {
                    @Override protected void onAction() {
                    presenter.setQueryOrderState(sSIGN.value);
                    queryOrder(true);
                    }}
        );
    }

    /**list 点击子项 */
    @Override
    public void onItemClick(BaseViewHolder<BaseViewHolderDataModel> vh,BaseViewHolderDataModel dataModel, int position) {
        if (dataModel.convert() instanceof OrderBlankItemDataBean){
            queryOrder(true);
            return ;
        }
        DriverOrderInfo orderInfo = presenter.currentOrderInfo(position);
        if (orderInfo == null) {
            toast("未知错误");
            queryOrder(true);
            return;
        }
        setDataOut(orderInfo);//传递到下一个页面的信息-订单详情信息,公司信息
        addStack("order_detail"); //进入订单详情
    }
    /** 下拉刷新列表*/
    @Override
    public void onRefresh() {
        queryOrder(true);
    }

    @Override
    public void showProgress() {

        toUi(new Runnable() {
            @Override
            public void run() {
                vh.incRefresh.refresh.setRefreshing(true);
                vh.pullOnLoad.setLoadMoreEnable(false);//设置不可下拉加载
            }
        });
    }

    @Override
    public void hindProgress() {
        toUi(new Runnable() {
            @Override
            public void run() {
                vh.incRefresh.refresh.setRefreshing(false);
                vh.pullOnLoad.setLoadMoreEnable(true);//设置允许下拉加载
            }
        });
    }

    //设置下拉数据
    @Override
    public void setPullDownData(final List<String> list, final boolean flag, final int index) {
        toUi(new Runnable() {
            @Override
            public void run() {
                vh.incSpinner.spinner.attachDataSource(list);
                if (flag) {
                    vh.incSpinner.spinner.setSelectedIndex(index);//选中第一个下标
                }
            }
        });
    }

    //清理适配器数据
    @Override
    public void clearListData() {
        recycleAdapter.clearAll();
    }

    //设置适配器数据子项
    @Override
    public void setListDataItem(String leftTop,String rightTop,String leftBottom,String rightBottom) {
        recycleAdapter.addData(new OrderListItemDataBean(leftTop,leftBottom,rightTop,rightBottom));
    }

    @Override
    public void setListDataItemBlank() {
        recycleAdapter.addData(new OrderBlankItemDataBean());
    }

    //更新列表
    @Override
    public void updateList() {
        toUi(new Runnable() {
            @Override
            public void run() {
                recycleAdapter.notifyDataSetChanged();
            }
        });
    }

    //刷新企业信息
    @Override
    public void onClick(View v) {
        if (v.getId() == vh.yearBtn.getId()){

            List<String> list = presenter.getYearList();
            int index = presenter.getYearSelect();
            PickerUtil.alertSinglePicker(getSpaActivity(),
                    list,
                    list.get(index),
                    new PickerUtil.ItemCallback() {
                @Override
                public void callback(int index,String item) {
                    vh.yearBtn.setText(item+"年");
                    presenter.selectYear(index);
                    queryOrder(true);
                }
            });
        }else if (v.getId() == vh.spanRefresh.getId()){
            toIo(new Runnable() {
                @Override
                public void run() {
                    presenter.queryComps();
                }
            });
        }

    }

    @Override
    public void onLoadMore() {
        queryOrder(false);
    }
}
