package tms.space.lbs_driver.tms_required.head_bottom.imps;

import android.view.View;

import com.leezp.lib.singlepageapplication.base.SpaBaseFragment;
import com.leezp.lib.singlepageapplication.interfaces.OnSpaFragmentBack;

import tms.space.lbs_driver.tms_required.head_bottom.interfaces.ItemAbs;

/**
 * Created by Leeping on 2018/7/20.
 * email: 793065165@qq.com
 *
 */

public class BackItem extends ItemAbs {
    private View view;
    private OnSpaFragmentBack onBackUp;

    public BackItem(View view) {
        super(view);
        this.view  = view;
    }

    public void setOnBackUp(OnSpaFragmentBack onBackUp) {
        if (onBackUp!=null) {
            view.setVisibility(View.VISIBLE);
            this.onBackUp = onBackUp;
        }
    }

    //点击返回键将被调用
    @Override
    public void execute(SpaBaseFragment fragment) {
        if (onBackUp!=null){
               if (fragment!=null) {
                   onBackUp.backUp(); //用户点击返回键而不是应用视图主动退出
               }
                onBackUp = null;
               view.setVisibility(View.GONE);//隐藏头部返回按钮
        }
    }
}
