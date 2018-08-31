package tms.space.lbs_driver.tms_mapop.server;

import android.os.RemoteException;

import tms.space.lbs_driver.tms_mapop.ITrackService;
import tms.space.lbs_driver.tms_mapop.db.TrackDb;
import tms.space.lbs_driver.tms_mapop.entity.TrackDbBean;

/**
 * Created by Leeping on 2018/7/21.
 * email: 793065165@qq.com
 */

class TraceServiceAIDLImp extends ITrackService.Stub{

    private final TrackDb db;

    public TraceServiceAIDLImp(TrackDb db) {
        if (db == null) throw new RuntimeException("The remote agent cannot be initialized because the database cannot operate");
        this.db = db;
    }



    @Override
    public int addTrack(int userid, long orderId, int enterpriseId) throws RemoteException {
        return db.inset(new TrackDbBean(orderId,userid,enterpriseId));
    }

    @Override
    public int updateState(long orderId, int state) throws RemoteException {
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
