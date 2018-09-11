package tms.space.lbs_driver.tms_mapop.gdMap.filters;

import com.amap.api.location.AMapLocation;
import com.leezp.lib.util.StrUtil;
import com.leezp.lib_log.LLog;

import tms.space.lbs_driver.tms_mapop.gdMap.FilterAbs;

/**
 * Created by Leeping on 2018/8/18.
 * email: 793065165@qq.com
 */

public class LocAddressNameFilter extends FilterAbs {

    private AMapLocation prev;
    private AMapLocation prev2;

    @Override
    public boolean intercept(AMapLocation location) {
        if (prev!=null && StrUtil.validate(prev.getAddress()) && StrUtil.validate(location.getAddress()) && prev.getAddress().equals(location.getAddress())) {
            LLog.print("与前一点地址相同:"+prev.getAddress());
            return true;
        }
        if (prev2!=null && StrUtil.validate(prev2.getAddress()) && StrUtil.validate(location.getAddress()) && prev2.getAddress().equals(location.getAddress())){
            LLog.print("与前二点地址相同:"+prev2.getAddress());
            return true;
        }
        if (prev!=null && location.getTime() - prev.getTime() <= 0) {
            LLog.print("时间相同");
            return true;
        }
        if (prev2!=null && location.getTime() - prev2.getTime() <= 0) {
            LLog.print("时间相同");
            return true;
        }


        if (prev!=null) prev2 = prev;
        prev = location;
        return false;
    }
}
