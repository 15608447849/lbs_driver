package tms.space.lbs_driver.tms_mapop.gdMap.filters;

import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.leezp.lib.util.StrUtil;
import com.leezp.lib.util.TimeUtil;
import com.leezp.lib_log.LLog;

import tms.space.lbs_driver.tms_mapop.gdMap.FilterAbs;

/**
 * Created by Leeping on 2018/7/24.
 * email: 793065165@qq.com
 */

public class LocInfoPrint extends FilterAbs {
    private String tag;

    public LocInfoPrint setTag(String tag) {
        this.tag = tag;
        return this;
    }

    @Override
    public boolean intercept(AMapLocation location) {
        String string = tag==null?"":tag +
                //"经纬度:(" + location.getLongitude()  + "," + location.getLatitude() + ")," +
                "卫星数:" + location.getSatellites() + ",强度:" + location.getGpsAccuracyStatus() + "," +
                "精度:" + location.getAccuracy() + "m," +
                (location.getSpeed()>0?"速度:" + location.getSpeed() + "m/s,":"") +
                (location.getBearing()>0?"角度:" + location.getBearing() + "°,":"") +
                "时间:" + TimeUtil.formatUTC(location.getTime(),"[HH:mm:ss]") + "," +
                //"海拔"+location.getAltitude()+"m"+
                //(StrUtil.validate(location.getLocationQualityReport().getAdviseMessage())? ",建议:" + location.getLocationQualityReport().getAdviseMessage():"")+
                (StrUtil.validate(location.getAddress())? ",地址:" + location.getAddress():"");
        LLog.print(string);
//        Log.d("坐标",string);
        return false;
    }
}
