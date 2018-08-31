package tms.space.lbs_driver.base.image;

import android.content.Intent;

import com.leezp.lib.singlepageapplication.base.SpaBaseMessage;

import tms.space.lbs_driver.base.entrance.LbsActivity;
import tms.space.lbs_driver.base.messages.MessageOpAbs;
import tms.space.lbs_driver.tms_required.picture_show.ImageActivity;

/**
 * Created by Leeping on 2018/8/22.
 * email: 793065165@qq.com
 */

public class ShowImage extends MessageOpAbs {
    public ShowImage(int what) {
        super(what);
    }

    @Override
    public void execute(LbsActivity lbsActivity, SpaBaseMessage sbMsg) {
        try {
            String uri = sbMsg.getData();
            if (uri == null) return;
            Intent intent = new Intent(lbsActivity,ImageActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(ImageActivity.URL,uri);
            lbsActivity.startActivity(intent);
        } catch (Exception e) {
        }
    }
}
