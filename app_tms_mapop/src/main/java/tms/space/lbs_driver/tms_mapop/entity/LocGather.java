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
        //没三个点一组    pre cur bak
        // 三个点之间速度<4m/s 认为是静止状态
        // 三个点之间速度>40m/s 认为是超速状态

        TraceLocation location;
        LatLng pre,cur,bak;
        long preTime,curTime,bakTime;
        float difPre,difBak;//时间改变量
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
//
            toPre = AMapUtils.calculateLineDistance(pre,cur);//距离改变量,单位米
            difPre = ( curTime - preTime ) / 1000.0f;//时间改变量

            toBak = AMapUtils.calculateLineDistance(cur,bak);//距离改变量,单位米
            difBak = ( bakTime - curTime ) / 1000.0f;//时间改变量

            if (toPre<15){
                LLog.print("当前: "+ i+"->"+(i+1)+" 距离过短, "+ toPre);
                delIndex.add(i+1);
                continue;
            }
            if (toBak<15){
                LLog.print("当前: "+ (i+1)+"->"+(i+2)+" 距离过短, "+toBak);
                delIndex.add(i+2);
                continue;
            }


            if (difPre<=0){
                LLog.print("当前: "+ i+"->"+(i+1)+" 时间线异常, "+ difPre);
                delIndex.add(i+1);
                continue;
            }

            if (difBak<=0){
                LLog.print("当前: "+ (i+1)+"->"+(i+2)+" 时间线异常, "+difBak);
                delIndex.add(i+2);
                continue;
            }

            if (difPre > (30*60) || difBak > (30*60)){
                LLog.print("当前: "+ (i)+"->"+(i+2)+" 其中一个时间线过长 "+difPre+" , "+ difBak);
                if (AMapUtils.calculateLineDistance(pre,bak)<100){
                    delIndex.add(i+1);
                }
                continue;
            }

            vPre = toPre/difPre;
            vBak = toBak/difBak;
            aver = (vPre + vBak)/2;

            LLog.print("当前: "+ i+"->"+(i+1)+" 速度 => "+ toPre+"/"+difPre+"="+vPre);
            LLog.print("当前: "+ (i+1)+"->"+(i+2)+" 速度 => "+ toBak+"/"+difBak+"="+vBak);
            LLog.print("当前距离速度平均值:"+aver );

            if (Double.isNaN(vPre)  ||Double.isInfinite(vPre)){
                flag = true;
            }else if (Double.isNaN(vBak) || Double.isInfinite(vBak)){
                flag = true;
            }else if ( aver < 0.65 ){
                flag = true;
            }
            if (flag) delIndex.add(i+1);
        }
        int delCount = 0;
        for (int index : delIndex){
            location = path.remove(index-delCount);
            delCount++;
            LLog.print("删除: index="+index +" - " + JsonUti.javaBeanToJson(location));
        }
        if (path.size() == 2) {
            location = path.get(0);
            pre = new LatLng(location.getLatitude(),location.getLongitude());
            location = path.get(1);
            bak = new LatLng(location.getLatitude(),location.getLongitude());
            if (AMapUtils.calculateLineDistance(pre,bak)<=100 && (location.getBearing() == 0 && location.getSpeed() == 0) ) path.remove(1);
        }
        if (delCount>0) filterPath(path);


    }
}
