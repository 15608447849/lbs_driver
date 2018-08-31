package tms.space.lbs_driver.tms_base.beans;

import com.hsf.framework.api.driver.DriverCompInfo;
import com.hsf.framework.api.driver.OrderInfo;

/**
 * Created by Leeping on 2018/8/15.
 * email: 793065165@qq.com
 */

public class DriverOrderInfo implements java.io.Serializable{
    private DriverUser user;
    private DriverCompInfo comp;
    private OrderInfo info;

    public DriverOrderInfo(DriverUser user) {
        this.user = user;
    }

    public DriverOrderInfo(DriverUser user, DriverCompInfo comp, OrderInfo info) {
        this.user = user;
        this.comp = comp;
        this.info = info;
    }

    public DriverUser getUser() {
        return user;
    }

    public void setUser(DriverUser user) {
        this.user = user;
    }

    public DriverCompInfo getComp() {
        return comp;
    }

    public void setComp(DriverCompInfo comp) {
        this.comp = comp;
    }

    public OrderInfo getInfo() {
        return info;
    }

    public void setInfo(OrderInfo info) {
        this.info = info;
    }

    public boolean checkValid(){
        if (info == null || info.complex == null || !info.complex.isValid){
            return false;
        }
        return true;
    }
}
