package tms.space.lbs_driver.tms_mapop.gdMap.manage;

import android.content.Context;

import com.amap.api.trace.LBSTraceClient;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;

import tms.space.lbs_driver.tms_base.beans.DriverUser;
import tms.space.lbs_driver.tms_mapop.db.TrackDb;
import tms.space.lbs_driver.tms_mapop.entity.TrackCorrectResult;
import tms.space.lbs_driver.tms_mapop.entity.TrackDbBean;

/**
 * Created by Leeping on 2018/7/23.
 * email: 793065165@qq.com
 */

public class CorManage extends Thread{

    private volatile boolean isRun = true;

    //轨迹纠偏使用
    private LBSTraceClient lbsTraceClient;

    private TrackDb db;

    private TrackCorrectResult result = new TrackCorrectResult();

    public CorManage(Context context,TrackDb db) {
        this.lbsTraceClient = LBSTraceClient.getInstance(context);
        this.db = db;
        this.setDaemon(true);
        this.setName("t-"+getClass().getSimpleName());
        this.start();
    }

    //arraylist堵塞队列
    private final ArrayBlockingQueue<TrackDbBean> trackQueue = new ArrayBlockingQueue<>(1000,true);

    public void correct(DriverUser user,List<TrackDbBean> list){
        int size = list.size();
        if (size>0){
            for (int i = 0; i < size ; i ++){
                TrackDbBean b = list.get(i);
                if (b.getUserId() == user.getUserCode()){
                    //阻塞添加
                    try { trackQueue.put(b); } catch (InterruptedException ignored) { }
                }
            }
        }
    }


    @Override
    public void run() {
        while (isRun){
            try {
                //堵塞获取
                TrackDbBean b = trackQueue.take();
                result.setLbsTraceClient(lbsTraceClient).setDb(db).setBean(b).execute().clear();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void destroy() {
        isRun = false;
        result.clear();
        result = null;
        lbsTraceClient.stopTrace();//停止纠偏
        lbsTraceClient.destroy(); //轨迹纠偏销毁
        lbsTraceClient = null;
    }
}
