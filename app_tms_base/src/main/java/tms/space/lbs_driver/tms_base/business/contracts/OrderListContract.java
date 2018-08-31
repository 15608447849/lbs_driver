package tms.space.lbs_driver.tms_base.business.contracts;

import com.hsf.framework.api.driver.DriverCompInfo;
import com.hsf.framework.api.driver.OrderInfo;

import java.util.List;

import tms.space.lbs_driver.tms_base.beans.DriverOrderInfo;
import tms.space.lbs_driver.tms_base.beans.IBasePresenter;
import tms.space.lbs_driver.tms_base.beans.IBaseView;

/**
 * Created by Leeping on 2018/7/27.
 * email: 793065165@qq.com
 */

public class OrderListContract {
    //模型
    public interface Model  {
        // 请求用户所属企业
        DriverCompInfo[] queryComp(int userid);
        //根据状态获取订单 用户id 企业id 状态码
        OrderInfo[] queryOrderList(int userid, int enterpriseid, int state,int year,int index,int range);
    }

    //视图
    public interface View extends IBaseView<Presenter>{
        //设置下拉列表数据
        void setPullDownData(List<String> list,boolean flag,int index);
        //清理订单列表数据
        void clearListData();
        //设置订单列表子项的数据源
        void setListDataItem(String leftTop,String rightTop,String leftBottom,String rightBottom);
        //设置订单列表空白
        void setListDataItemBlank();
        //更新列表数据
        void updateList();
    }

    //业务逻辑
    public interface Presenter extends IBasePresenter<View>{
        //设置当前选中的企业下标
        void selectCompPos(int pos);
        //请求企业
        void queryComps();
        //设置选中的状态
        void setQueryOrderState(int state);
        //请求订单
        void queryOrderList();
        //请求订单历史
        void queryOrderListHistory();
        //获取当前的订单
        DriverOrderInfo currentOrderInfo(int pos);
        //获取当前选中的企业
        DriverCompInfo currentCompInfo();
        //获取可用年份列表
        List<String> getYearList();
        //设置当前查询的年份
        void selectYear(int pos);
        //获取当前年份下标
        int getYearSelect();

    }

}
