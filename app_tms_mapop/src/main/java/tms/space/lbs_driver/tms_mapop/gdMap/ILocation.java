package tms.space.lbs_driver.tms_mapop.gdMap;

import com.amap.api.location.AMapLocation;

/**
 * Created by Leeping on 2018/7/23.
 * email: 793065165@qq.com
 */

public interface ILocation<T,L,D>{

    void create(IStrategy<T> configStrategy);

    void onDestroy();

    void startLoc();

    void stopLoc();

    void closeClient();

    T getLocClient();

    boolean isLaunch();

    void addLocationListener(L listener);

}
