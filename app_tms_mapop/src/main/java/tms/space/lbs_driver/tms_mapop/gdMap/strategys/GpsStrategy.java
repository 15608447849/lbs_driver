package tms.space.lbs_driver.tms_mapop.gdMap.strategys;

import android.content.Context;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;

import java.util.LinkedList;
import java.util.List;

import tms.space.lbs_driver.tms_mapop.gdMap.IFilter;
import tms.space.lbs_driver.tms_mapop.gdMap.IStrategy;
import tms.space.lbs_driver.tms_mapop.gdMap.filters.LocAccuracyFilter;
import tms.space.lbs_driver.tms_mapop.gdMap.filters.LocBearingFilter;
import tms.space.lbs_driver.tms_mapop.gdMap.filters.LocDistanceFilter;
import tms.space.lbs_driver.tms_mapop.gdMap.filters.LocInfoPrint;
import tms.space.lbs_driver.tms_mapop.gdMap.filters.LocSatellitesFilter;
import tms.space.lbs_driver.tms_mapop.gdMap.filters.LocSpeedFilter;
import tms.space.lbs_driver.tms_mapop.gdMap.filters.LocTypeFilter;

/**
 * Created by Leeping on 2018/7/23.
 * email: 793065165@qq.com
 */

public class GpsStrategy extends IStrategy<AMapLocationClient,AMapLocation> {

    public GpsStrategy(Context context) {
        super(context);
    }

    @Override
    public AMapLocationClient config() {
            AMapLocationClientOption mLocationOption = new AMapLocationClientOption();
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Device_Sensors);//定位模式,gps
            mLocationOption.setInterval(2000);//间隔
            mLocationOption.setHttpTimeOut(30000);
            mLocationOption.setSensorEnable(false);//不使用手机传感器定位角度
            mLocationOption.setLocationCacheEnable(false);//不使用定位缓存
            mLocationOption.setNeedAddress(false);//不用返回地理信息
            mLocationOption.setWifiScan(false);//降低耗电不自动刷新wifi
            mLocationOption.setMockEnable(false);//设置是否允许模拟位置
            mLocationOption.setLocationPurpose(null);//不需要场景
            AMapLocationClient mLocationClient = new AMapLocationClient(getContext());//定位客户端
            mLocationClient.setLocationOption(mLocationOption);
            return mLocationClient;
    }

    @Override
    public List<IFilter<AMapLocation>> filterList() {
        List<IFilter<AMapLocation>> list = new LinkedList<>();
        list.add(new LocTypeFilter());//类型过滤
        list.add(new LocSatellitesFilter());//卫星数过滤
        list.add(new LocAccuracyFilter());//精度范围过滤
        list.add(new LocSpeedFilter());//速度过滤
        list.add(new LocBearingFilter());//角度过滤
        list.add(new LocDistanceFilter());//距离过滤
        list.add(new LocInfoPrint());//信息打印
        return list;
    }
}
