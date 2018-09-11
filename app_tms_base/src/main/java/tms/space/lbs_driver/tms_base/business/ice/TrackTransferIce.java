package tms.space.lbs_driver.tms_base.business.ice;

import com.hsf.framework.api.driver.DriverServicePrx;
import com.leezp.lib.util.StrUtil;
import com.leezp.lib.zerocice.IceServerAbs;

/**
 * Created by Leeping on 2018/7/23.
 * email: 793065165@qq.com
 * 轨迹上传
 */

public class TrackTransferIce  extends IceServerAbs<DriverServicePrx> {

    public TrackTransferIce() {
        super(DriverServicePrx.class);
    }

    /**上传轨迹 纠偏*/
    public int transferCorrect(long orderId,int userId,int enterpriseId,String correct) {
        if (!StrUtil.validate(correct)){
           return 0;
        }
        try {
            //printParam("传输纠正轨迹",orderId,userId,enterpriseId);
            return getProxy().driverUploadCorrect(userId,enterpriseId,orderId,correct);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 1;
    }

}
