package tms.space.lbs_driver.tms_required.head_bottom;

import android.content.Context;
import android.widget.ImageButton;
import android.widget.TextView;

import com.leezp.lib.viewholder.annotations.OnClicked;
import com.leezp.lib.viewholder.annotations.RidClass;
import com.leezp.lib.viewholder.annotations.RidName;
import com.leezp.lib.viewholder.interfaces.ViewHolderAbs;

import tms.space.lbs_driver.tms_base.viewholder.base.IncHorizontalProgressBar;
import tms.space.lbs_driver.tms_required.R;


/**
 * Created by Leeping on 2018/7/18.
 * email: 793065165@qq.com
 */
@RidClass(R.id.class)
public class HeadVh extends ViewHolderAbs {

    //提示消息
    @RidName("inc_head_coordinator")
    public android.support.design.widget.CoordinatorLayout coordinator;

    //回退
    @OnClicked()
    @RidName("inc_head_ibtn_back")
    public ImageButton back;

    //扫码
    @OnClicked()
    @RidName("inc_head_ibtn_scan")
    public ImageButton scan;

    //搜索
    @OnClicked()
    @RidName("inc_head_ibtn_query")
    public ImageButton query;

    //标题
    @RidName("inc_head_tv_title")
    public TextView title;

    public IncHorizontalProgressBar progressBar;

    public HeadVh(Context context) {
        super(context, R.layout.frg_head);
        title.setSelected(true);//开启滚动
    }


}
