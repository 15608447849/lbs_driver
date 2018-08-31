package tms.space.lbs_driver.tms_required.business_person;

import android.content.Context;
import android.widget.Button;
import android.widget.EditText;

import com.leezp.lib.viewholder.annotations.OnClicked;
import com.leezp.lib.viewholder.annotations.RidClass;
import com.leezp.lib.viewholder.annotations.RidName;
import com.leezp.lib.viewholder.interfaces.ViewHolderAbs;

import tms.space.lbs_driver.tms_required.R;

/**
 * Created by Leeping on 2018/7/25.
 * email: 793065165@qq.com
 */
@RidClass(R.id.class)
public class ChangePasswordVh extends ViewHolderAbs {


    @RidName("frg_change_password_et_original")
    public EditText originalPw;

    @RidName("frg_change_password_et_new")
    public EditText newPw;

    @RidName("frg_change_password_et_sure")
    public EditText surePw;

    @OnClicked
    @RidName("frg_change_password_btn_done")
    public Button sureBtn;

    public ChangePasswordVh(Context context) {
        super(context, R.layout.frg_change_password);
    }
}
