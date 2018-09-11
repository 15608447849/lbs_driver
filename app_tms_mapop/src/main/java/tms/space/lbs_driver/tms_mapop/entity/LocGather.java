package tms.space.lbs_driver.tms_mapop.entity;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.LatLng;
import com.amap.api.trace.TraceLocation;
import com.google.gson.reflect.TypeToken;
import com.leezp.lib.util.JsonUti;
import com.leezp.lib.util.StrUtil;
import com.leezp.lib_log.LLog;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import tms.space.lbs_driver.tms_mapop.db.TrackDb;
import tms.space.lbs_driver.tms_mapop.gdMap.filters.LocNullFilter;

import static java.lang.Float.NaN;

/**
 * Created by Leeping on 2018/7/23.
 * email: 793065165@qq.com
 */

public class LocGather implements AMapLocationListener {
    private final TrackDb db;

    public LocGather(TrackDb db) {
        this.db = db;
    }


    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        //获取数据库存在的数据
        List<TrackDbBean> list = db.queryAll();
        int size = list.size();

        if (size>0){
            for (int i = 0 ; i < size ; i++ ){
                handleTrack(list.get(i),aMapLocation);
            }
        }
    }

    private void handleTrack(TrackDbBean bean, AMapLocation aMapLocation) {
        if (bean.getState() > 0) {
            return; //不收集轨迹数据
        }
        List<TraceLocation> path = null;
        String json = bean.getTrack();
        if (StrUtil.validate(json)){
            path = JsonUti.jsonToJavaBean(json,new TypeToken<List<TraceLocation>>(){}.getType());
        }
        if (path==null) path = new LinkedList<>();

        TraceLocation traceLocation = new TraceLocation(
                aMapLocation.getLatitude(),//径度
                aMapLocation.getLongitude(),//维度
                aMapLocation.getSpeed(),//速度
                aMapLocation.getBearing(),//角度
                aMapLocation.getTime()//时间
        );
        path.add(traceLocation);//添加轨迹点

        //对规矩点进行筛选
        filterPath(path);

        //设置等待轨迹纠偏次数+1
        bean.settCode(bean.gettCode() + 1);
        bean.setTrack(JsonUti.javaBeanToJson(path));
        int result = db.updateTrack(bean);
        if (result == 0){
            LLog.print("记录原始轨迹点成功,当前数量:"+path.size());
        }else{
            LLog.print("数据库记录轨迹点失败");
        }

    }

    private void filterPath(List<TraceLocation> path) {
        //没三个点一组    pre cur bak  两点间速度<10m/s 认为是静止状态
        TraceLocation location;
        LatLng pre,cur,bak;
        long preTime,curTime,bakTime;
        long difPre,difBak;//时间改变量
        float toPre,toBak;//距离改变量
        float vPre,vBak;//速度
        float aver;//速度平均值
        boolean flag;
        List<Integer> delIndex = new ArrayList<>();
        for (int i = 0; i < path.size(); i++){
            flag = false;
            if (i+2>=path.size()) break;

            location = path.get(i);
            pre = new LatLng(location.getLatitude(),location.getLongitude());
            preTime = location.getTime();
            location = path.get(i+1);
            cur = new LatLng(location.getLatitude(),location.getLongitude());
            curTime = location.getTime();
            location = path.get(i+2);
            bak = new LatLng(location.getLatitude(),location.getLongitude());
            bakTime = location.getTime();

            toPre = AMapUtils.calculateLineDistance(pre,cur);//距离改变量,单位米
            difPre = Math.abs(( curTime - preTime ) / 1000L);

            toBak = AMapUtils.calculateLineDistance(cur,bak);//距离改变量,单位米
            difBak = Math.abs(( bakTime - curTime ) / 1000L);


            vPre = (difPre*1.0f)/toPre;
            vBak = (difBak*1.0f)/toBak;
            aver = (vPre + vBak)/2;

//            LLog.print("当前: "+ i+" A-B: 时间/距离=速度 => "+ difPre+"/"+toPre+"="+vPre);
//            LLog.print("当前: "+ i+" B-C: 时间/距离=速度 => "+ difBak+"/"+toBak+"="+vBak);
            LLog.print("当前速度平均值:"+aver );

            if (Double.isNaN(vPre)  ||Double.isInfinite(vPre)){
                flag = true;
            }else if (Double.isNaN(vBak) || Double.isInfinite(vBak)){
                flag = true;
            }else if ( aver < 15 ){
                flag = true;
            }else if (aver > 315){
                flag = true;
            }
            if (flag) delIndex.add(i+1);
        }
        int delCount = 0;
        for (int index : delIndex){
            location = path.remove(index-delCount);
            delCount++;
            LLog.print("删除: "+ JsonUti.javaBeanToJson(location));
        }
        if (path.size() == 2) {
            location = path.get(0);
            pre = new LatLng(location.getLatitude(),location.getLongitude());
            location = path.get(1);
            bak = new LatLng(location.getLatitude(),location.getLongitude());
            if (AMapUtils.calculateLineDistance(pre,bak)<30) path.remove(1);
        }
        if (delCount>0) filterPath(path);


    }
}
