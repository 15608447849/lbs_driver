package tms.space.lbs_driver.tms_base.beans;

import com.leezp.lib.singlepageapplication.base.SpaBaseHandle;

/**
 * Created by Leeping on 2018/8/16.
 * email: 793065165@qq.com
 */

public interface IStackOption {
    /**添加轨迹记录*/
    void addTrackRecode(SpaBaseHandle handler);
    /**移除轨迹记录*/
    void removeTrackRecode(SpaBaseHandle handler);
}
