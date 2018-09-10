package tms.space.lbs_driver.tms_mapop.gdMap.filters;

import com.amap.api.location.AMapLocation;
import com.leezp.lib_log.LLog;

import tms.space.lbs_driver.tms_mapop.gdMap.FilterAbs;
import tms.space.lbs_driver.tms_mapop.gdMap.IFilter;

/**
 * Created by Leeping on 2018/8/18.
 * email: 793065165@qq.com
 * 精度过滤
 */
public class LocAccuracyFilter extends FilterAbs {
    //最大精度范围默认50
    private float accuracy = 50.0f;
    public LocAccuracyFilter setAccuracy(float accuracy) {
        this.accuracy = accuracy;
        return this;
    }
    @Override
    public boolean intercept(AMapLocation location) {
        //精度范围过滤
        if (location.getAccuracy() > accuracy){
            LLog.print("精度范围不合格: "+location.getAccuracy()+",有效范围:"+accuracy);
            return true;
        }
        return false;
    }
}
