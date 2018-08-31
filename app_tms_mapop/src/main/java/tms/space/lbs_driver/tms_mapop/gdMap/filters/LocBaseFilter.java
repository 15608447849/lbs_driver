package tms.space.lbs_driver.tms_mapop.gdMap.filters;

import com.amap.api.location.AMapLocation;
import com.leezp.lib_log.LLog;

import tms.space.lbs_driver.tms_mapop.gdMap.IFilter;
import tms.space.lbs_driver.tms_mapop.gdMap.IFilterError;

/**
 * Created by Leeping on 2018/7/23.
 * email: 793065165@qq.com
 */

public class LocBaseFilter implements IFilter<AMapLocation> {

    private IFilterError<AMapLocation> filterError;

    public void setFilterError(IFilterError<AMapLocation> filterError) {
        this.filterError = filterError;
    }

    @Override
    public boolean filter(AMapLocation aMapLocation) {
        if (aMapLocation == null){
            return true;
        }
        if (aMapLocation.getErrorCode() != 0){
            LLog.print("错误码: " +aMapLocation.getErrorInfo());
            if (filterError!=null){
                filterError.onFilterError(aMapLocation);
            }
            return true;
        }
        return false;
    }
}
