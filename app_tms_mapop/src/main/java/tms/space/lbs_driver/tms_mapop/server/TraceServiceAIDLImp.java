package tms.space.lbs_driver.tms_mapop.server;

import android.os.RemoteException;

import com.leezp.lib_gdmap.GdMapUtils;

import tms.space.lbs_driver.tms_mapop.ITrackService;
import tms.space.lbs_driver.tms_mapop.db.TrackDb;
import tms.space.lbs_driver.tms_mapop.entity.TrackDbBean;
import tms.space.lbs_driver.tms_mapop.gdMap.manage.LocManage;

/**
 * Created by Leeping on 2018/7/21.
 * email: 793065165@qq.com
 */

class TraceServiceAIDLImp extends ITrackService.Stub{

    private final TrackDb db;
    private final LocManage locManage;
    public TraceServiceAIDLImp(LocManage locManage,TrackDb db) {
        if (db == null) throw new RuntimeException("The remote agent cannot be initialized because the database cannot operate");
        this.db = db;
        this.locManage = locManage;
    }



    @Override
    public int addTrack(int userid, long orderId, int enterpriseId) throws RemoteException {

        int result = db.inset(new TrackDbBean(orderId,userid,enterpriseId));
        if (result == 0) {
            if (this.locManage!=null) this.locManage.networkLocationOnce();
        }else{
            result = updateState(orderId,0);
        }
        return result;
    }

    @Override
    public int updateState(long orderId, int state) throws RemoteException {
        if (this.locManage!=null) this.locManage.networkLocationOnce();
        return db.updateState(orderId,state);
    }

    @Override
    public String getTrack(long orderId) throws RemoteException {
        TrackDbBean bean = db.queryByOrder(orderId);
        if (bean!=null) {
            return bean.getTrack();
        }
        return null;
    }

    @Override
    public String getCorrect(long orderId) throws RemoteException {
        TrackDbBean bean = db.queryByOrder(orderId);
        if (bean!=null) {
            return bean.getCorrect();
        }
        return null;
    }
}
