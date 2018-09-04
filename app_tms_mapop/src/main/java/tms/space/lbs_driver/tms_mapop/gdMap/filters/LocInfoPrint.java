package tms.space.lbs_driver.tms_mapop.gdMap.filters;

import com.amap.api.location.AMapLocation;
import com.leezp.lib_log.LLog;

import tms.space.lbs_driver.tms_mapop.gdMap.IFilter;

/**
 * Created by Leeping on 2018/7/24.
 * email: 793065165@qq.com
 */

public class LocInfoPrint implements IFilter<AMapLocation> {
    @Override
    public boolean filter(AMapLocation location) {
        String string =
                "经纬度:(" + location.getLongitude()  + "," + location.getLatitude() + ")," +
                "卫星数:" + location.getSatellites() + ",强度:" + location.getGpsAccuracyStatus() + "," +
                "精度:" + location.getAccuracy() + "m," +
                "速度:" + location.getSpeed() + "m/s," +
                "角度:" + location.getBearing() + "°," +
                "海拔"+location.getAltitude()+"m"+
                (null!=location.getLocationQualityReport().getAdviseMessage() ? ",建议:" + location.getLocationQualityReport().getAdviseMessage():"");
        LLog.print(string);
        return false;
    }
}