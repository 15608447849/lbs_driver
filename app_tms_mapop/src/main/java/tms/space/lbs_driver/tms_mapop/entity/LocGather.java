package tms.space.lbs_driver.tms_mapop.entity;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.LatLng;
import com.amap.api.trace.TraceLocation;
import com.google.gson.reflect.TypeToken;
import com.leezp.lib.util.JsonUti;
import com.leezp.lib.util.StrUtil;
import com.leezp.lib.util.TimeUtil;
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

    private void handleTrack(TrackDbBean bean, final AMapLocation aMapLocation) {
        if (bean.getState() > 0) {
            return; //不收集轨迹数据
        }
//        LLog.print("\n"+bean.getId()+" 开始处理原始轨迹点");
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

        //对点进行筛选
        filterPath(path);
        //设置等待轨迹纠偏次数+1
        bean.settCode(bean.gettCode() + 1);
        bean.setTrack(JsonUti.javaBeanToJson(path));
        int result = db.updateTrack(bean);
        if (result == 0){
//            LLog.print(bean.getId()+" 记录原始轨迹点成功,当前数量:"+path.size()+"\n");
        }else{
            LLog.print(bean.getId()+" 数据库记录轨迹点失败");
        }

    }



    private void filterPath(List<TraceLocation> path) {

        // 1. 3个点之间时间线递增
        // 2. 3个点距离平均值 大于 30米

        TraceLocation loc1,loc2,loc3;
        LatLng pre,cur,bak;
        long preTime,curTime,bakTime; //时间
        float preToCur,curToBak,aver;//两段距离改变量和平均值
        //需要删除的点下标
        List<Integer> delIndex = new ArrayList<>();

        for (int i = 0; i < path.size(); i++){

            if (i+2>=path.size()) break; //三个循环一次

            loc1 = path.get(i);
            pre = new LatLng(loc1.getLatitude(),loc1.getLongitude());
            preTime = loc1.getTime();

            loc2 = path.get(i+1);
            cur = new LatLng(loc2.getLatitude(),loc2.getLongitude());
            curTime = loc2.getTime();

            loc3 = path.get(i+2);
            bak = new LatLng(loc3.getLatitude(),loc3.getLongitude());
            bakTime = loc3.getTime();

            preToCur = AMapUtils.calculateLineDistance(pre,cur);//A-B距离改变量,单位米
            curToBak = AMapUtils.calculateLineDistance(cur,bak);//B-C距离改变量,单位米
            aver = (preToCur+curToBak)/2.0f;

            //打印
//            LLog.print(i+" -> "+(i+1) +" " +
//                    ",距离: "+ preToCur+ "米," +
//                    "["+ TimeUtil.formatUTC(preTime,"HH:mm:ss")+" - "+ TimeUtil.formatUTC(curTime,"HH:mm:ss")+"]," +
//                    "时间差: "+(curTime - preTime)/1000L+"秒" );
//
//            LLog.print((i+1)+" -> "+(i+2) +" " +
//                    ",距离: "+ curToBak+ "米," +
//                    "["+ TimeUtil.formatUTC(curTime,"HH:mm:ss")+" - "+ TimeUtil.formatUTC(bakTime,"HH:mm:ss")+"]," +
//                    "时间差: "+(bakTime - curTime)/1000L+"秒" );

            if (preTime>curTime){
                delIndex.add(i+1);
                LLog.print((i+1) + " 时间异常");
                break;
            }
            if (curTime>bakTime){
                delIndex.add(i+2);
                LLog.print((i+2) + " 时间异常");
                break;
            }
//            LLog.print(i+" -> "+(i+2) +" 平均距离:"+aver);
            if (aver<5){
                delIndex.add(i+1);
                delIndex.add(i+2);
                LLog.print("同一点");
                loc1.setTime(loc3.getTime());

                break;
            }
            if (aver<35){
                if (preToCur < aver){
                    delIndex.add(i+1);
                    LLog.print((i+1) + " 距离过小");
                    break;
                }else if (curToBak<aver){
                    delIndex.add(i+2);
                    LLog.print((i+2) + " 距离过小");
                    break;
                }
            }
            if (aver>80 && aver<500){
                if (preToCur > aver){
                    if (loc2.getSpeed() < 1.5  && loc2.getBearing() == 0){
                        delIndex.add(i+1);
                        LLog.print((i+1) + " 距离非法");
                        break;
                    }else {
                        LLog.print(JsonUti.javaBeanToJson(loc2));
                    }
                }else if (curToBak > aver){
                    if (loc3.getSpeed() < 1.5 && loc3.getBearing() == 0){
                        delIndex.add(i+2);
                        LLog.print((i+2) + " 距离非法");
                        break;
                    }else {
                        LLog.print(JsonUti.javaBeanToJson(loc3));
                    }

                }
            }

        }

        int delCount = 0;
        for (int index : delIndex){
            path.remove(index-delCount);
            delCount++;
        }

        if (delCount>0) filterPath(path);
    }
}
