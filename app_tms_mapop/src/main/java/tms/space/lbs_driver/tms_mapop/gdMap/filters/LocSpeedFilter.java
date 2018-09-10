package tms.space.lbs_driver.tms_mapop.gdMap.filters;

import com.amap.api.location.AMapLocation;
import com.leezp.lib_log.LLog;

import tms.space.lbs_driver.tms_mapop.gdMap.FilterAbs;
import tms.space.lbs_driver.tms_mapop.gdMap.IFilter;

/**
 * Created by Leeping on 2018/8/18.
 * email: 793065165@qq.com
 */

public class LocSpeedFilter extends FilterAbs {
    //速度过滤
    private float speed = 0.0f;

    public LocSpeedFilter setSpeed(float speed) {
        this.speed = speed;
        return this;
    }
    @Override
    public boolean intercept(AMapLocation location) {
        //速度小于或者等于指定速度
        if (location.getSpeed() <= speed) {
            LLog.print("速度不合格: "+ location.getSpeed()+",最小速度:"+speed);
            return true;
        }
        return false;
    }
}
