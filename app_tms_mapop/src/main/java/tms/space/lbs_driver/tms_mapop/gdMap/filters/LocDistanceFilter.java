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

    private float intervalMin = 15;

    public LocDistanceFilter setIntervalMin(float intervalMin) {
        this.intervalMin = intervalMin;
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
//                LLog.print("距离改变量不合格: "+ distance+" 米,最小距离改变:"+intervalMin+"米");
                return true;
            }
            float rangerMax = Math.min(prev.getAccuracy(),location.getAccuracy());
            if (distance> rangerMax){
                float timeDiff = (location.getTime() - prev.getTime()) / 1000.0f;
                if (timeDiff<=0) {
//                    LLog.print("距离改变量不合格: "+ distance+" 米,时间改变差:"+timeDiff);
                    return true;
                }else{
                    float v = distance/timeDiff; // m/s ,车辆速度最大值  50米/秒(m/s)=180千米/时(km/h)
                    if (v>50){
//                        LLog.print("距离改变量不合格: "+ distance+" 米,速度过大:"+v+"m/s");
                        return true;
                    }
                }

            }
        }

        prev = location;
        return false;
    }
}
