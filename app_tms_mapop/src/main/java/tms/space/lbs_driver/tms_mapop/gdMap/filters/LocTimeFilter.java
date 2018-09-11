package tms.space.lbs_driver.tms_mapop.gdMap.filters;

import com.amap.api.location.AMapLocation;
import com.leezp.lib_log.LLog;

import tms.space.lbs_driver.tms_mapop.gdMap.FilterAbs;

/**
 * Created by Leeping on 2018/8/18.
 * email: 793065165@qq.com
 */

public class LocTimeFilter extends FilterAbs {
    //时间过滤
    private long timeDifference = 5 * 1000L;

    public LocTimeFilter setTimeDifference(long timeDifference) {
        this.timeDifference = timeDifference;
        return this;
    }
    private AMapLocation prev;

    @Override
    public boolean intercept(AMapLocation location) {
        boolean result = false;
        //角度小于或等于指定角度
        if (location .getLocationType() == AMapLocation.LOCATION_TYPE_GPS && prev!=null) {
            long value = location.getTime() - prev.getTime();
            if (value<= timeDifference){
                LLog.print("时间差不合格: "+ location.getBearing()+",最小时间差:"+timeDifference+"毫秒");
            }

            result = true;
        }
        if (prev == null) result = true;
        prev = location;
        return result;
    }
}
