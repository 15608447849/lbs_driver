package tms.space.lbs_driver.tms_base.beans;

import android.view.View;

/**
 * Created by Leeping on 2018/8/1.
 * email: 793065165@qq.com
 */

public abstract class IViewOnClickAction {
    private final int vid;
    public IViewOnClickAction(View view) {
        this.vid = view.getId();
    }
    boolean check(int vid){
        return this.vid == vid;
    }
    protected abstract void onAction();

}
