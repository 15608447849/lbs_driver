package tms.space.lbs_driver.tms_mapop.gdMap.filters;

import com.amap.api.location.AMapLocation;
import com.leezp.lib_log.LLog;

import java.util.Arrays;

import tms.space.lbs_driver.tms_mapop.gdMap.FilterAbs;
import tms.space.lbs_driver.tms_mapop.gdMap.IFilter;

/**
 * Created by Leeping on 2018/8/18.
 * email: 793065165@qq.com
 */

public class LocTypeFilter extends FilterAbs {

    //可用类型
    private int[] type = new int[]{AMapLocation.LOCATION_TYPE_GPS,AMapLocation.LOCATION_TYPE_WIFI}; //默认GPS

    public LocTypeFilter setType(int... type) {
        this.type = type;
        return this;
    }

    @Override
    public boolean intercept(AMapLocation location) {
        boolean isFilter = true;
        //类型范围过滤
        for (int i = 0; i < type.length ;i++){
            if (location.getLocationType() == type[i]){
                isFilter = false;
                break;
            }
        }
//        if (isFilter) LLog.print("数据类型不合格: "+ location.getLocationType()+",可用类型:"+ Arrays.toString(type));
        return isFilter;
    }
}
