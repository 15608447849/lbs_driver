package tms.space.lbs_driver.tms_base.util;

import com.leezp.lib.util.StrUtil;

/**
 * Created by Leeping on 2018/6/27.
 * email: 793065165@qq.com
 */

public class BusinessUtil {
    //验证手机号码
    public static boolean validatePhone(String phone){
        if (!StrUtil.validate(phone)) return false;
        if (phone.length()==11){
//            final String PHONE_PATTERN = "^(13[0-9]|14[579]|15[0-3,5-9]|16[6]|17[0135678]|18[0-9]|19[89])\\d{8}$";
//            return Pattern.compile(PHONE_PATTERN).matcher(phone).matches();
            return true;
        }
        return false;
    }
    //验证密码
    public static boolean validatePassword(String password){
        return StrUtil.validate(password)  && password.length() > 5;
    }
}
