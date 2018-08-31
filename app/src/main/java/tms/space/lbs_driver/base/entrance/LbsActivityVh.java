package tms.space.lbs_driver.base.entrance;

import android.content.Context;
import android.widget.FrameLayout;

import com.leezp.lib.singlepageapplication.annotations.SpaCreatePageHolder;
import com.leezp.lib.viewholder.annotations.RidClass;
import com.leezp.lib.viewholder.annotations.RidName;
import com.leezp.lib.viewholder.interfaces.ViewHolderAbs;

import tms.space.lbs_driver.R;


/**
 * Created by Leeping on 2018/6/27.
 * email: 793065165@qq.com
 *
 */
@RidClass(R.id.class)
public class LbsActivityVh extends ViewHolderAbs {

    //头部容器
    @SpaCreatePageHolder("head")
    @RidName("act_container_head")
    public FrameLayout headFrame;

    //页面容器
    @SpaCreatePageHolder("content")
    @RidName("act_container_content")
    public FrameLayout contentFrame;

    //底部容器
    @SpaCreatePageHolder("bottom")
    @RidName("act_container_bottom")
    public FrameLayout bottomFrame;

    public LbsActivityVh(Context context) {
        super(context, R.layout.act_lbs);
    }
}
