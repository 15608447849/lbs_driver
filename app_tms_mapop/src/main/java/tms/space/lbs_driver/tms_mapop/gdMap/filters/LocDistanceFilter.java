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

    private float intervalMax = 200; //距离改变量最大值  *如果在短期内 距离改变量很大 ,认为在做加速运动

    public LocDistanceFilter setIntervalMin(float intervalMin) {
        this.intervalMin = intervalMin;
        return this;
    }

    private AMapLocation prev;

    @Override
    public boolean intercept(AMapLocation location) {
        boolean flag = false;
        if (prev!=null){
            LatLng s =  new LatLng(prev.getLatitude(), prev.getLatitude());
            LatLng d = new LatLng(location.getLatitude(), location.getLatitude());
            float distance = AMapUtils.calculateLineDistance(s,d);//距离改变量,单位米
            LLog.print("距离: "+ distance+" 米" +" - 两点精度最小值: "+ Math.min(prev.getAccuracy(),location.getAccuracy()));

            if (distance <= intervalMin ) {
                LLog.print("距离改变量不合格: "+ distance+" 米,最小距离改变:"+intervalMin+"米");
                flag = true;
            }
        }
        if (prev == null) flag = true;
        prev = location;
        return flag;
    }
}
