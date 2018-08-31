package tms.space.lbs_driver.business_orders.fragments;

import android.content.Context;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.leezp.lib.viewholder.annotations.RidClass;
import com.leezp.lib.viewholder.annotations.RidName;
import com.leezp.lib.viewholder.interfaces.ViewHolderAbs;

import tms.space.lbs_driver.business_orders.R;

/**
 * Created by Leeping on 2018/7/29.
 * email: 793065165@qq.com
 */

@RidClass(R.id.class)
public class OrderFeeVh extends ViewHolderAbs {

    @RidName("frg_order_fee_tv")
    public TextView tv;

    @RidName("frg_order_fee_ed")
    public EditText et;

    @RidName("frg_order_fee_btn")
    public Button button;

    public OrderFeeVh(Context context) {
        super(context, R.layout.frg_order__fee);
        initEt();
    }

    private void initEt() {
        et.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_CLASS_NUMBER);
        //设置字符过滤
        et.setFilters(new InputFilter[]{new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                if (source.equals(".") && dest.toString().length() == 0) {
                    return "0.";
                }
                if (dest.toString().contains(".")) {
                    int index = dest.toString().indexOf(".");
                    int length = dest.toString().substring(index).length();
                    if (length == 3) {
                        return "";
                    }
                }
                return null;
            }
        }});
    }
}
