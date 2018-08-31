package tms.space.lbs_driver.business_orders.presenterimps;

import com.hsf.framework.api.driver.DriverCompInfo;
import com.hsf.framework.api.driver.OrderInfo;
import com.hsf.framework.api.driver.sComplete;
import com.hsf.framework.api.driver.sEvaluate;
import com.hsf.framework.api.driver.sSIGN;
import com.leezp.lib.util.StrUtil;
import com.leezp.lib.util.TimeUtil;

import java.util.ArrayList;
import java.util.List;

import tms.space.lbs_driver.tms_base.beans.BasePresenter;
import tms.space.lbs_driver.tms_base.beans.DriverOrderInfo;
import tms.space.lbs_driver.tms_base.beans.DriverUser;
import tms.space.lbs_driver.tms_base.business.contracts.OrderListContract;
import tms.space.lbs_driver.tms_base.business.ice.OrderIceModel;

/**
 * Created by Leeping on 2018/7/27.
 * email: 793065165@qq.com
 * 订单业务
 */

public class OrderListPresenter extends BasePresenter<OrderListContract.View> implements OrderListContract.Presenter {

    //当前登陆的用户信息
    private DriverUser user = new DriverUser().fetch();

    private OrderListContract.Model model = new OrderIceModel();

    private final List<String> yearList = TimeUtil.getYearNowToTarget(-10);

    //当前查询的年份
    private int yearPos = yearList.size()-1;

    private int state = -1; //当前查看的状态

    private int compPos = -1;//当前选中的企业下标

    private int index = 1;//下标

    private final int range = 15;//范围

    //当前可用的所有订单对象
    private final List<DriverOrderInfo> orderList = new ArrayList<>();

    //当前可用的所有企业对象
    private DriverCompInfo[] driverComps;

    //正在请求中
    private boolean isQuerying = false;

    /**
     * 请求企业列表
     * */
    @Override
    public void queryComps() {
        if (isQuerying) return;
//        LLog.print("请求企业列表");
        view.showProgress();
        isQuerying = true;
        driverComps =  model.queryComp(user.getUserCode());
        view.hindProgress();
        isQuerying = false;
        List<String> list = new ArrayList<>();
        boolean flag = false;
        if (driverComps==null || driverComps.length==0){
            list.add("查询不到您当前的所属企业");
        }else{
            //设置企业信息
            String name;
            for (DriverCompInfo c : driverComps){
                name = (StrUtil.validate(c.fname)?c.fname:"未认证企业");//+ (StrUtil.validate(c.sname)?"("+c.sname+")":"")
                list.add(name);
                flag = true;
            }
            if (compPos>driverComps.length){
                compPos = -1;
            }
        }
        int pos = compPos;
        if (pos == -1) pos = 0;
        view.setPullDownData(list,flag,pos);
    }

    /**
     *  根据状态获取订单列表最新
     */
    @Override
    public void queryOrderList() {
//        com.orhanobut.logger.LLog.print("请求最新订单");
        if (isQuerying){
            return;
        }

        DriverCompInfo driverCompInfo = currentCompInfo();//当前公司
        if (driverCompInfo == null){
            queryComps();//请求企业信息
            return;
        }

        if (state == -1) return;

        orderList.clear();
        view.clearListData(); //清理列表
        view.updateList(); //更新列表
        index = 1; //清理分页下标

        view.showProgress(); //开启进度条
        isQuerying = true;

        //请求后台
        OrderInfo[] orderArr = model.queryOrderList(user.getUserCode(),
                driverCompInfo.compid,state,
                Integer.parseInt(yearList.get(yearPos)),
                index,
                range);

        if (orderArr!=null && orderArr.length>0){

            for (OrderInfo info : orderArr ){
                if (!verifyState(info.state)) { //检查状态是否匹配
                    view.clearListData();
                    orderList.clear();
                    break;
                }
                orderList.add(new DriverOrderInfo(user,driverCompInfo,info));
                //设置列表子项数据
                view.setListDataItem(
                        info.brief.adds,
                        info.brief.time,
                        info.brief.cargo,
                        info.brief.pay);
            }

        }
        if (orderList.size() == 0) view.setListDataItemBlank();

        view.updateList(); //更新列表
        view.hindProgress();
        isQuerying = false;
    }

    /**
     * 请求订单历史
     */
    @Override
    public void queryOrderListHistory() {
        if (index == 1){
            index++;//分页下标+1
            return;
        }

        DriverCompInfo driverCompInfo = currentCompInfo();//当前公司
        if (driverCompInfo == null){
            queryComps();//请求企业信息
            return;
        }
        if (state == -1) return;

        view.showProgress(); //开启进度条
        isQuerying = true;

        //请求后台
        OrderInfo[] orderArr = model.queryOrderList(user.getUserCode(),
                driverCompInfo.compid,state,
                Integer.parseInt(yearList.get(yearPos)),
                index,
                range);

        if (orderArr!=null && orderArr.length>0){

            for (OrderInfo info : orderArr ){
                if (!verifyState(info.state)) { //检查状态是否匹配
                    continue;
                }
                orderList.add(new DriverOrderInfo(user,driverCompInfo,info));
                //设置列表子项数据
                view.setListDataItem(
                        info.brief.adds,
                        info.brief.time,
                        info.brief.cargo,
                        info.brief.pay);
            }
            index++;//分页下标+1
        }

        view.updateList(); //更新列表
        view.hindProgress();
        isQuerying = false;

    }

    /**
     * 验证状态有效性
     * @param nState
     * @return
     */
    private boolean verifyState(int nState) {
        if (this.state == sSIGN.value){
            return (nState == this.state || nState == sEvaluate.value || nState == sComplete.value);
        }else{
            return nState == this.state;
        }
    }


    /**
     *  sGRAB.value
     *  sCLAIM.value
     *  sSIGN.value
     *  设置当前选中的订单状态
     */
    @Override
    public void setQueryOrderState(int state) {
        this.state = state;
    }

    @Override
    public void selectYear(int yearPos) {
        this.yearPos = yearPos;
    }

    @Override
    public int getYearSelect() {
        return yearPos;
    }

    @Override
    public List<String> getYearList() {
        return yearList;
    }

    //当前选中的企业
    @Override
    public void selectCompPos(int pos) {
        if (this.driverComps==null || driverComps.length == 0){
            compPos = -1;
        }
        else {
            compPos = pos;
        }
    }
    //获取当前选中的企业
    @Override
    public DriverCompInfo currentCompInfo() {
        if (compPos == -1 || driverComps == null ) return null;
        if (compPos > driverComps.length) {
            compPos = -1;
            return null;
        }
        return driverComps[compPos];
    }



    //获取当前选中的订单列表
    @Override
    public DriverOrderInfo currentOrderInfo(int pos) {
        if ( orderList.size()>0 && pos < orderList.size()) return orderList.get(pos);
        return null;
    }

}
