package tms.space.lbs_driver.tms_mapop.entity;

import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.LatLng;
import com.amap.api.trace.LBSTraceClient;
import com.amap.api.trace.TraceListener;
import com.amap.api.trace.TraceLocation;
import com.google.gson.reflect.TypeToken;
import com.leezp.lib.util.JsonUti;
import com.leezp.lib.util.StrUtil;
import com.leezp.lib_gdmap.GdMapUtils;
import com.leezp.lib_log.LLog;

import java.util.Iterator;
import java.util.List;

import tms.space.lbs_driver.tms_mapop.db.TrackDb;

/**
 * Created by Leeping on 2018/7/23.
 * email: 793065165@qq.com
 */

public class TrackCorrectResult implements TraceListener {

    private static final boolean isCorrect = false;

    private LBSTraceClient lbsTraceClient;
    private TrackDb db;
    private TrackDbBean bean;
    private  int errorCount = 0;

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
//        LLog.print("纠偏失败 id="+id+" ,原因:\n"+cause);
        //获取原始轨迹->纠偏点
        bean.setCorrect(convertTrace(bean.getTrack()));
        if (isCorrect){
            errorCount++ ;
            if (errorCount > 10){
                dataUpdate(true);
                errorCount = 0;
            }else {
                dataUpdate(false);
            }
        }else{
            dataUpdate(true);
        }

        threadRouse();
    }

    private String convertTrace(String track) {
        if (StrUtil.validate(track)){
            List<TraceLocation> path = JsonUti.jsonToJavaBean(track,new TypeToken<List<TraceLocation>>(){}.getType());
           return JsonUti.javaBeanToJson(GdMapUtils.get().convertTracePointToLatLng(path));
        }
        return null;
    }

    @Override
    public void onTraceProcessing(int i, int i1, List<LatLng> list) {

    }

    @Override
    public void onFinished(int id, List<LatLng> list, int distance, int waitingtime) {
        //LLog.print("纠偏成功 id=" +id+ " 距离 = "+distance+" m,停留时间 = "+ waitingtime+" s");
        //轨迹纠偏完成
        String json = JsonUti.javaBeanToJson(list);
        bean.setCorrect(json);
        dataUpdate(true);
        threadRouse();
    }

    private void dataUpdate(boolean flag) {
        int count = bean.getcCode() + 1;
        if (count > bean.gettCode()) count = bean.gettCode();
        if (flag) bean.setcCode(count);//已纠偏次数
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
                    if (path!=null){
//                        preformPath(path);
                        if (isCorrect){
                            if (path.size() >= 2){
                                lbsTraceClient.queryProcessedTrace(
                                        bean.getId(),
                                        path,
                                        LBSTraceClient.TYPE_AMAP,
                                        this);
                                threadWait();
                            }else{
                                onRequestFailed(bean.getId(),"轨迹点过少,无法执行纠偏操作,当前有效轨迹数量:"+path.size());
                            }
                        }else{
                            onRequestFailed(bean.getId(),"禁止纠偏操作");
                        }

                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }
    //对队列进行一些准备工作
    private void preformPath(List<TraceLocation> path) {
        Iterator<TraceLocation> iterator = path.iterator();
        TraceLocation  cTrace;
        LatLng oLatLng = null;
        LatLng cLatLng;

        while (iterator.hasNext()){
            cTrace = iterator.next();
            if (oLatLng==null) {
                oLatLng = new LatLng(cTrace.getLatitude(),cTrace.getLongitude());
            }else{
                cLatLng = new LatLng(cTrace.getLatitude(),cTrace.getLongitude());
                float distance = AMapUtils.calculateLineDistance(oLatLng,cLatLng);//距离改变量,单位米
                if (distance<10){
                    //认为是一个点
                    iterator.remove();
                }else{
                    oLatLng = cLatLng;
                }
            }
        }
    }

    public void clear(){
        this.db = null;
        this.bean = null;
        this.lbsTraceClient = null;
    }
}
