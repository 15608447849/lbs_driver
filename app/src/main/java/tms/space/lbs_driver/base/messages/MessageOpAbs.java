package tms.space.lbs_driver.base.messages;

import com.leezp.lib.singlepageapplication.base.SpaBaseMessage;
import com.leezp.lib.singlepageapplication.interfaces.StateOpAbs;

import tms.space.lbs_driver.base.entrance.LbsActivity;

/**
 * Created by Leeping on 2018/7/25.
 * email: 793065165@qq.com
 */

public abstract class MessageOpAbs extends StateOpAbs<LbsActivity,SpaBaseMessage> {
    protected int what ;

    public MessageOpAbs(int what) {
        this.what = what;
    }

    @Override
    public boolean check(SpaBaseMessage sbMsg) {
        if (sbMsg.what == this.what) return true;
        return false;
    }

}
