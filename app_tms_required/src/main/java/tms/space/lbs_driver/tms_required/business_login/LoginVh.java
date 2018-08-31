package tms.space.lbs_driver.tms_required.business_login;

import android.content.Context;
import android.widget.Button;
import android.widget.TextView;

import com.leezp.lib.util.AppUtil;
import com.leezp.lib.util.StrUtil;
import com.leezp.lib.viewholder.annotations.RidClass;
import com.leezp.lib.viewholder.annotations.RidName;
import com.leezp.lib.viewholder.interfaces.ViewHolderAbs;

import tms.space.lbs_driver.tms_base.viewholder.base.IncHorizontalProgressBar;
import tms.space.lbs_driver.tms_required.R;


/**
 * Created by Leeping on 2018/6/27.
 * email: 793065165@qq.com
 * 登陆界面视图
 */

@RidClass(R.id.class)
public class LoginVh extends ViewHolderAbs{
    //进度条
    public IncHorizontalProgressBar progressBar;

    @RidName("tms_login_frg_login_title_tv")
    public TextView version;
    //手机号码输入框
    @RidName("inc_input_phone")
    public android.support.design.widget.TextInputLayout textPhone;
    //密码输入框
    @RidName("inc_input_password")
    public android.support.design.widget.TextInputLayout textPassword;
    //登陆按钮
    @RidName("inc_input_btn_sure")
    public Button button;

    public LoginVh(Context context) {
        super(context, R.layout.frg_login);
        version.setText(StrUtil.stringFormat("%s %s",context.getString(R.string.app_name),AppUtil.getVersionName(context)));

    }

}
