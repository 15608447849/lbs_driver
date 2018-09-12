package tms.space.lbs_driver.tms_mapop.gdMap.filters;

import com.amap.api.location.AMapLocation;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.LatLng;
import com.leezp.lib_log.LLog;

import tms.space.lbs_driver.tms_mapop.gdMap.FilterAbs;

/**
 * Created by Leeping on 2018/7/24.
 * email: 793065165@qq.com
 * 距离过滤
 */

public class LocDistanceFilter extends FilterAbs {


    //小于 两点距离最小值 , 无效
    private float intervalMin = 15;

    //大于 两点距离最大值 不判断
    private float intervalMax = 1000;

    public LocDistanceFilter setIntervalMin(float intervalMin) {
        this.intervalMin = intervalMin;
        return this;
    }

    public LocDistanceFilter setIntervalMax(float intervalMax) {
        this.intervalMax = intervalMax;
        return this;
    }

    private AMapLocation prev;

    @Override
    public boolean intercept(AMapLocation location) {

        if (prev!=null){
            LatLng s =  new LatLng(prev.getLatitude(), prev.getLatitude());
            LatLng d = new LatLng(location.getLatitude(), location.getLatitude());
            float distance = AMapUtils.calculateLineDistance(s,d);//距离改变量,单位米

            if (distance <= intervalMin ) {
                LLog.print("距离改变量过小: "+ distance+" 米");
                return true;
            }

            if (distance < intervalMax){
                float time = (location.getTime() - prev.getTime()) / 1000.0f; //两点的时间差
                if (time <= 0) {
                    return true;
                }else{
                    float speed = distance/time; // m/s ,车辆速度最大值  50米/秒(m/s)=180千米/时(km/h)
                    if (speed<1.5){
                        LLog.print("速度过小: "+ speed+" 米/秒");
                    }else if (speed > 50){
                        LLog.print("速度过大: "+ speed+" 米/秒");
                        return true;
                    }
                }
            }
        }

        prev = location;
        return false;
    }
}
