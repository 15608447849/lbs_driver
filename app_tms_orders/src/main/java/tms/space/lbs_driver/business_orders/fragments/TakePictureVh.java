package tms.space.lbs_driver.business_orders.fragments;

import android.content.Context;
import android.widget.Button;
import android.widget.ImageView;

import com.leezp.lib.viewholder.annotations.OnClicked;
import com.leezp.lib.viewholder.annotations.OnLongClicked;
import com.leezp.lib.viewholder.annotations.RidClass;
import com.leezp.lib.viewholder.annotations.RidName;
import com.leezp.lib.viewholder.interfaces.ViewHolderAbs;

import tms.space.lbs_driver.business_orders.R;

/**
 * Created by Leeping on 2018/7/28.
 * email: 793065165@qq.com
 */

@RidClass(R.id.class)
public class TakePictureVh extends ViewHolderAbs{
    @OnClicked
    @OnLongClicked
    @RidName("frg_take_picture_fl_iv_1")
    public ImageView tb1;

    @OnClicked
    @OnLongClicked
    @RidName("frg_take_picture_fl_iv_2")
    public ImageView tb2;

    @OnClicked
    @OnLongClicked
    @RidName("frg_take_picture_fl_iv_3")
    public ImageView tb3;

    @OnClicked
    @RidName("frg_take_picture_update")
    public Button update;


    public TakePictureVh(Context context) {
        super(context, R.layout.frg_take_picture);
    }



}
