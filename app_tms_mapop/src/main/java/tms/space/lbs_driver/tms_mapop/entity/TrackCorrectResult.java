package tms.space.lbs_driver.tms_mapop.entity;

import com.amap.api.maps.model.LatLng;
import com.amap.api.trace.LBSTraceClient;
import com.amap.api.trace.TraceListener;
import com.amap.api.trace.TraceLocation;
import com.google.gson.reflect.TypeToken;
import com.leezp.lib.util.JsonUti;
import com.leezp.lib.util.StrUtil;
import com.leezp.lib_log.LLog;

import java.util.List;

import tms.space.lbs_driver.tms_mapop.db.TrackDb;

/**
 * Created by Leeping on 2018/7/23.
 * email: 793065165@qq.com
 */

public class TrackCorrectResult implements TraceListener {

    private LBSTraceClient lbsTraceClient;
    private TrackDb db;
    private TrackDbBean bean;

    public TrackCorrectResult setLbsTraceClient(LBSTraceClient lbsTraceClient) {
        this.lbsTraceClient = lbsTraceClient;
        return this;
    }

    public TrackCorrectResult setDb(TrackDb db) {
        this.db = db;
        return this;
    }

    public TrackCorrectResult setBean(TrackDbBean bean) {
        this.bean = bean;
        return this;
    }

    private void threadWait(){
        synchronized (this){
            try {
                this.wait();
            } catch (InterruptedException ignored) {}
        }
    }
    private void threadRouse(){
        synchronized (this){
            this.notify();
        }
    }

    @Override
    public void onRequestFailed(int id, String cause) {
        LLog.print("纠偏失败",id+ " 原因:"+cause);
        //获取原始轨迹->纠偏点
        bean.setCorrect(bean.getTrack());
        dataUpdate(false);
        threadRouse();
    }

    @Override
    public void onTraceProcessing(int i, int i1, List<LatLng> list) {

    }

    @Override
    public void onFinished(int id, List<LatLng> list, int distance, int waitingtime) {
        LLog.print("纠偏成功", id+" 距离 = "+distance+" ,停留时间 = "+ waitingtime);
        //轨迹纠偏完成
        String json = JsonUti.javaBeanToJson(list);
        bean.setCorrect(json);
        dataUpdate(true);
        threadRouse();
    }

    private void dataUpdate(boolean flag) {
        if (flag) bean.setcCode(bean.getcCode() + 1);//已纠偏次数
        db.updateCorrect(bean);
    }

    /**
     * 执行纠偏
     */
    public TrackCorrectResult execute() {
        try {
            //tCode = 等待纠偏次数(每多收集一个坐标点 +1 )
            //cCode = 已纠偏次数(每执行纠偏一次+1 无论成功或失败)
            if ( bean.gettCode() > bean.getcCode()){
                String json = bean.getTrack();//原始轨迹
                if (StrUtil.validate(json)){
                    List<TraceLocation> path = JsonUti.jsonToJavaBean(json,new TypeToken<List<TraceLocation>>(){}.getType());
                    if (path!=null && path.size()>2 ){
                        LLog.print("纠偏,原始轨迹点数量:"+path.size());
                        lbsTraceClient.queryProcessedTrace(
                                bean.getId(),
                                path,
                                LBSTraceClient.TYPE_AMAP,
                                this);
                        threadWait();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }

    public void clear(){
        this.db = null;
        this.bean = null;
        this.lbsTraceClient = null;
    }
}
