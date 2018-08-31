package tms.space.lbs_driver.tms_base.activitys;

import android.app.Activity;
import android.view.View;

import com.leezp.lib.util.AppUtil;

/**
 * Created by Leeping on 2018/8/22.
 * email: 793065165@qq.com
 */

public abstract class BaseActivityAbs extends Activity {
    protected <V extends View> V $(int rid){
        return findViewById(rid);
    }

    protected void $ui(Runnable run){
        if (AppUtil.checkUIThread()) {
            run.run();
        }else{
            runOnUiThread(run);
        }
    }
}
