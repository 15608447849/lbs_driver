package tms.space.lbs_driver.tms_mapop.gdMap.filters;

import com.amap.api.location.AMapLocation;
import com.leezp.lib_log.LLog;

import tms.space.lbs_driver.tms_mapop.gdMap.FilterAbs;
import tms.space.lbs_driver.tms_mapop.gdMap.IFilter;

/**
 * Created by Leeping on 2018/8/18.
 * email: 793065165@qq.com
 */

public class LocSatellitesFilter  extends FilterAbs {
    //最小卫星数
    private int satellites = 4;
    public LocSatellitesFilter setSatellites(int satellites) {
        this.satellites = satellites;
        return this;
    }
    @Override
    public boolean intercept(AMapLocation location) {
        //卫星数过滤
        if (location.getLocationType() == AMapLocation.LOCATION_TYPE_GPS && location.getSatellites() < satellites){
//            LLog.print("卫星数量不合格: "+ location.getSatellites()+",最小卫星数:"+satellites);
            return true;
        }
        return false;
    }
}
