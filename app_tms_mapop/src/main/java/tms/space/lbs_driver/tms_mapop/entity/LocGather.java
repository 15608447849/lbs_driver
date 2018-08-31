package tms.space.lbs_driver.tms_mapop.entity;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.trace.TraceLocation;
import com.google.gson.reflect.TypeToken;
import com.leezp.lib.util.JsonUti;
import com.leezp.lib.util.StrUtil;
import com.leezp.lib_log.LLog;

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
        if (bean.getState() > 0) return; //不收集轨迹数据
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
}
