package tms.space.lbs_driver.tms_required.head_bottom.interfaces;

import com.leezp.lib.singlepageapplication.base.SpaBaseFragment;

import tms.space.lbs_driver.tms_base.beans.IViewOnClickManage;

/**
 * Created by Leeping on 2018/7/18.
 * email: 793065165@qq.com
 */

public class ItemManage extends IViewOnClickManage<ItemAbs> {

    private final SpaBaseFragment fragment;

    public ItemManage(SpaBaseFragment fragment) {
        this.fragment = fragment;
    }

    public ItemAbs select(int vid){
        ItemAbs tabItem = super.select(vid);
        if (tabItem!=null) tabItem.execute(fragment);
        return null;
    }

}
