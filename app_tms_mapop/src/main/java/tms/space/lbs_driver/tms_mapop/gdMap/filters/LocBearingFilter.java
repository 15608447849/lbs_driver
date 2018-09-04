package tms.space.lbs_driver.tms_mapop.gdMap.filters;

import com.amap.api.location.AMapLocation;
import com.leezp.lib_log.LLog;

import tms.space.lbs_driver.tms_mapop.gdMap.IFilter;

/**
 * Created by Leeping on 2018/8/18.
 * email: 793065165@qq.com
 */

public class LocBearingFilter implements IFilter<AMapLocation> {
    //角度过滤
    private float bearing = 0.0f;

    public LocBearingFilter setBearing(float bearing) {
        this.bearing = bearing;
        return this;
    }

    @Override
    public boolean filter(AMapLocation location) {
        //角度小于或等于指定角度
        if (location.getBearing() <= bearing) {
            LLog.print("角度不合格: "+ location.getBearing()+",最小角度:"+bearing);
            return true;
        }
        return false;
    }
}