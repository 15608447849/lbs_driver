package tms.space.lbs_driver.base.track;

import com.leezp.lib.singlepageapplication.base.SpaBaseMessage;
import com.leezp.lib_log.LLog;

import tms.space.lbs_driver.base.entrance.LbsActivity;
import tms.space.lbs_driver.base.messages.MessageOpAbs;

/**
 * Created by Leeping on 2018/7/22.
 * email: 793065165@qq.com
 */

public class AddTrack extends MessageOpAbs {
    public AddTrack(int what) {
        super(what);
    }

    @Override
    public void execute(LbsActivity lbsActivity, SpaBaseMessage sbMsg) {
        try {
            long t = System.currentTimeMillis();

            TrackServerConnect serverConn = lbsActivity.getTrackServerConnect();
//            Logger.i("添加轨迹记录 : " +sbMsg.what+" ,参数"+ Arrays.toString(sbMsg.args));
            java.lang.Object[] objArr = sbMsg.args;
            long orderId = (long) objArr[0];
            int userId = (int) objArr[1];
            int enterpriseId  = (int) objArr[2];

            if (serverConn!=null && serverConn.getStub()!= null){
               int result = serverConn.getStub().addTrack(userId,orderId,enterpriseId);
               if (sbMsg.callback!=null) sbMsg.callback.onCallback(result);
            }

            LLog.print("添加轨迹时间"+ (System.currentTimeMillis() - t) );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
