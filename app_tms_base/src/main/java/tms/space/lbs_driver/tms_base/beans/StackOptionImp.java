package tms.space.lbs_driver.tms_base.beans;

import com.leezp.lib.singlepageapplication.base.SpaBaseHandle;
import com.leezp.lib.singlepageapplication.base.SpaBaseMessage;

/**
 * Created by Leeping on 2018/8/16.
 * email: 793065165@qq.com
 */

public class StackOptionImp implements IStackOption {

    private DriverOrderInfo orderInfo;

    public void setOrderInfo(DriverOrderInfo orderInfo) {
        this.orderInfo = orderInfo;
    }

    /**
     * 添加轨迹记录
     *
     * @param handler
     */
    @Override
    public void addTrackRecode(SpaBaseHandle handler) {
        if (orderInfo==null) return;

        //添加一条记录
        SpaBaseMessage sbMsg = new SpaBaseMessage(10);
        sbMsg.args = new java.lang.Object[]{ orderInfo.getInfo().orderNo,
                orderInfo.getUser().getUserCode(),
                orderInfo.getComp().compid
        };
//        com.orhanobut.logger.Logger.i("轨迹添加 - "+ Arrays.toString(sbMsg.args));
        handler.sendSpaMessage(sbMsg);
    }

    /**
     * 移除轨迹记录
     * @param handler
     */
    @Override
    public void removeTrackRecode(SpaBaseHandle handler) {
        if (orderInfo==null) return;
        //删除一条记录- 通过订单号
        SpaBaseMessage sbMsg = new SpaBaseMessage(11);
        sbMsg.args = new java.lang.Object[]{orderInfo.getInfo().orderNo};
//        com.orhanobut.logger.Logger.i("轨迹添移除 - "+ Arrays.toString(sbMsg.args));
        handler.sendSpaMessage(sbMsg);
    }
}
