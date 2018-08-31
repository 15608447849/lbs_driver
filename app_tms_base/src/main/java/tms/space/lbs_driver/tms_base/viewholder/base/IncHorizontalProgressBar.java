package tms.space.lbs_driver.tms_base.viewholder.base;

import android.view.View;
import android.widget.ProgressBar;

import com.leezp.lib.viewholder.annotations.RidClass;
import com.leezp.lib.viewholder.annotations.RidName;
import com.leezp.lib.viewholder.interfaces.ViewHolderAbs;

import tms.space.lbs_driver.tms_base.R;

/**
 * Created by Leeping on 2018/7/28.
 * email: 793065165@qq.com
 * 进度条
 */
@RidClass(R.id.class)
public class IncHorizontalProgressBar extends ViewHolderAbs {

    @RidName("inc_layout_horizontal_progress_bar")
    public ProgressBar progress;

    public IncHorizontalProgressBar(View viewRoot) {
        super(viewRoot);
    }

    @Override
    protected void initViewParam() {
        progress.setIndeterminate(true);
    }
}
