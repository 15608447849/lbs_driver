package tms.space.lbs_driver.base.messages;

import com.leezp.lib.singlepageapplication.base.SpaBaseMessage;
import com.leezp.lib.singlepageapplication.interfaces.StateManage;

import tms.space.lbs_driver.base.entrance.LbsActivity;
import tms.space.lbs_driver.base.image.ShowImage;
import tms.space.lbs_driver.base.track.AddTrack;
import tms.space.lbs_driver.base.track.DelTrack;

/**
 * Created by Leeping on 2018/7/22.
 * email: 793065165@qq.com
 */

public class FragmentMessageManage extends StateManage<LbsActivity,SpaBaseMessage> {
    public FragmentMessageManage(LbsActivity holder) {
        super(holder);
    }

    @Override
    protected void init() {
       add(new AddTrack(10));
       add(new DelTrack(11));
       add(new ShowImage(12));
    }
}
