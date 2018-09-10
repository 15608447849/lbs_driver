package tms.space.lbs_driver.tms_mapop.gdMap.filters;

import com.amap.api.location.AMapLocation;
import com.leezp.lib_log.LLog;

import tms.space.lbs_driver.tms_mapop.gdMap.FilterAbs;

public class LocNullFilter extends FilterAbs {
    @Override
    public boolean intercept(AMapLocation aMapLocation) {
        if (null == aMapLocation){
            return true;
        }
        return false;
    }
}
