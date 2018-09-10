package tms.space.lbs_driver.business_orders.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.SpannableString;

import com.hsf.framework.api.driver.DriverCompInfo;
import com.hsf.framework.api.driver.sCLAIM;
import com.hsf.framework.api.driver.sGRAB;
import com.leezp.lib.recycles.more_view_adapter.ItemViewTemplateAttribute;
import com.leezp.lib.recycles.more_view_adapter.ItemViewTemplateManage;
import com.leezp.lib.util.DialogUtil;
import com.leezp.lib.util.GlideUtil;

import tms.space.lbs_driver.business_orders.R;
import tms.space.lbs_driver.business_orders.interfaces.OrderDetailButtonStateAbs;
import tms.space.lbs_driver.business_orders.interfaces.OrderDetailButtonStateOption;
import tms.space.lbs_driver.business_orders.presenterimps.OrderDetailPresenter;
import tms.space.lbs_driver.business_orders.recycle.OrderBlankItemDataBean;
import tms.space.lbs_driver.business_orders.recycle.OrderBlankTemplate;
import tms.space.lbs_driver.business_orders.recycle.OrderDetailImageItemDataBean;
import tms.space.lbs_driver.business_orders.recycle.OrderDetailImageTemplate;
import tms.space.lbs_driver.business_orders.recycle.OrderDetailSeparateItemDataBean;
import tms.space.lbs_driver.business_orders.recycle.OrderDetailSeparateTemplate;
import tms.space.lbs_driver.business_orders.recycle.OrderDetailTextItemDataBean;
import tms.space.lbs_driver.business_orders.recycle.OrderDetailTextTemplate;
import tms.space.lbs_driver.tms_base.beans.DriverOrderInfo;
import tms.space.lbs_driver.tms_base.business.contracts.OrderDetailContract;
import tms.space.lbs_driver.tms_base.fragments.CanBackUpFragmentAbs;

/**
 * Created by Leeping on 2018/7/26.
 * email: 793065165@qq.com
 * 订单详情页面
 */

public class OrderDetailFragment extends CanBackUpFragmentAbs<OrderDetailVh,OrderDetailContract.OrderDetailPresenter> implements OrderDetailContract.OrderDetailView {

    /** 按钮状态操作 */
    private OrderDetailButtonStateOption buttonOption = new OrderDetailButtonStateOption();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setPresenter(new OrderDetailPresenter());
    }

    //可用list item 模板
    @Override
    protected ItemViewTemplateManage getMoreRecItemManage() {
        return  new ItemViewTemplateManage(){
            @Override
            public void initItemLayout() {
                addAttr(new ItemViewTemplateAttribute(OrderDetailTextTemplate.class, R.layout.rec_item_order_detail_text));
                addAttr(new ItemViewTemplateAttribute(OrderDetailImageTemplate.class, R.layout.rec_item_order_detail_image));
                //间隔线
                addAttr(new ItemViewTemplateAttribute(OrderDetailSeparateTemplate.class, R.layout.rec_item_order_detail_cutoff));
                //分隔线
                addAttr(new ItemViewTemplateAttribute(OrderDetailSeparateTemplate.class, R.layout.rec_item_order_detail_line));
                //空白
                addAttr(new ItemViewTemplateAttribute(OrderBlankTemplate.class, R.layout.rec_item_order_list_black));
            }
        };
    }


    //初始化空间
    @Override
    public void viewCreated() {
        initButtonState(); //初始化按钮状态及事件
        setHeadTitle("订单详情");
    }

    //初始化按钮状态
    private void initButtonState() {
        buttonOption.add(
                //取货
                new OrderDetailButtonStateAbs(vh.pickup, sGRAB.value) {
                    @Override
                    protected void onAction() {
                        //传递订单数据到下一个页面
                        setDataOut(presenter.getOrderInfo());
                        //打开取货码界面
                        addStack("order_claim");
                    }
                },
                //卸载
                new OrderDetailButtonStateAbs(vh.unload, sCLAIM.value) {
                    @Override
                    protected void onAction() {
                        //改变订单状态为 已卸货
                        toIo(new Runnable() {
                            @Override
                            public void run() {
                                presenter.unloadOp(getSpaHandle());
                            }
                        });
                    }
                },
                //签收
                new OrderDetailButtonStateAbs(vh.sign, sCLAIM.value) {
                    @Override
                    protected void onAction() {
                        toIo(new Runnable() {
                            @Override
                            public void run() {
                                presenter.signOp();
                            }
                        });
                    }
                }
        );
    }

    /** 显示 */
    @Override
    public void onResume() {
        super.onResume();
        if (isVisible()){
            setCurrentOrderInfo(getDataIn());
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (!isVisible()){
            clearList();
            updateList();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private void setCurrentOrderInfo(Object o) {
        if (o!=null){
            if (o instanceof DriverOrderInfo){
                DriverOrderInfo info = (DriverOrderInfo) o;
                presenter.setOrderInfo(info);
                setCompInfo(info.getComp());
                updateOrder();
                }
            }
    }

    private void updateOrder(){
        toIo(new Runnable() {
            @Override
            public void run() {
                //通知状态跟更新
                presenter.updateOrder(getSpaActivity());
            }
        });
    }


    @Override
    public void clearList() {
        recycleAdapter.clearAll();
    }

    @Override
    public void updateList() {
        toUi(new Runnable() {
            @Override
            public void run() {
                recycleAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void setListItemData(SpannableString span,String backColor) {
        recycleAdapter.addData(new OrderDetailTextItemDataBean(span,backColor));
    }

    @Override
    public void setListItemData(SpannableString span, SpannableString span2,String backColor) {
        recycleAdapter.addData(new OrderDetailTextItemDataBean(span,span2,backColor));
    }

    @Override
    public void setListItemData(int type) {
        recycleAdapter.addData(new OrderDetailSeparateItemDataBean(type));
    }

    @Override
    public void setListItemData(String... urls) {
        if (urls.length>=3){
            recycleAdapter.addData(new OrderDetailImageItemDataBean(getSpaHandle(),urls[0],urls[1],urls[2]));
        }
    }

    @Override
    public void setListDataItemBlank() {
        recycleAdapter.addData(new OrderBlankItemDataBean());
    }

    @Override
    public void updateState(final int state) {
        toUi(new Runnable() {
                 @Override
                 public void run() {
                     buttonOption.notifyState(state); //通知当前按钮订单状态
                 }
             }
        );
    }
    //允许签收
    @Override
    public void allowSign() {
        toUi(new Runnable() {
            @Override
            public void run() {
                //传递订单数据到下一个页面
                setDataOut(presenter.getOrderInfo());
                //打开拍照页面
                addStack("take_picture");
            }
        });
    }
    //允许卸货
    @Override
    public void allowUnload() {
        updateOrder();
        toUi(new Runnable() {
            @Override
            public void run() {
                DialogUtil.dialogSimple(getSpaActivity(),
                        "成功卸货",
                        "确定并返回",
                        new DialogUtil.Action0() {
                            @Override
                            public void onAction0() {
                                backUp();
                            }
                        });
            }
        });
    }
    //设置公司信息
    public void setCompInfo(DriverCompInfo compInfo) {
        GlideUtil.loadImageByHttp(vh.iv,compInfo.address);
        vh.tv.setText(compInfo.fname);
    }

}
