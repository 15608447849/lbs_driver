package tms.space.lbs_driver.tms_mapop.gdMap.manage;

import android.content.Context;

import com.amap.api.trace.LBSTraceClient;

import java.util.List;

import tms.space.lbs_driver.tms_base.beans.DriverUser;
import tms.space.lbs_driver.tms_mapop.db.TrackDb;
import tms.space.lbs_driver.tms_mapop.entity.TrackCorrectResult;
import tms.space.lbs_driver.tms_mapop.entity.TrackDbBean;

/**
 * Created by Leeping on 2018/7/23.
 * email: 793065165@qq.com
 */

public class CorManage{

    //轨迹纠偏使用
    private LBSTraceClient lbsTraceClient;
    private TrackCorrectResult result = new TrackCorrectResult();

    public CorManage(Context context) {
        this.lbsTraceClient = LBSTraceClient.getInstance(context);
    }

    public void correct(DriverUser user,List<TrackDbBean> list, final TrackDb db){
        int size = list.size();
        if (size>0){
            for (int i = 0; i < size ; i ++){
                TrackDbBean b = list.get(i);
                if (b.getUserId() == user.getUserCode()){
                    result.setLbsTraceClient(lbsTraceClient).setDb(db).setBean(b).execute().clear();
                }
            }
        }
    }

    public void stop() {
        result.clear();
        result = null;
        lbsTraceClient.stopTrace();//停止纠偏
        lbsTraceClient.destroy(); //轨迹纠偏销毁
        lbsTraceClient = null;
    }
}
