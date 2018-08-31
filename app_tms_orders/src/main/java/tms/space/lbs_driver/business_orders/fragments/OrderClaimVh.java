package tms.space.lbs_driver.business_orders.fragments;

import android.content.Context;

import com.leezp.lib.viewholder.annotations.RidClass;
import com.leezp.lib.viewholder.annotations.RidName;
import com.leezp.lib.viewholder.interfaces.ViewHolderAbs;

import tms.space.lbs_driver.business_orders.R;

/**
 * Created by Leeping on 2018/7/28.
 * email: 793065165@qq.com
 */
@RidClass(R.id.class)
public class OrderClaimVh extends ViewHolderAbs{

    @RidName("frg_order_claim_verificationcodeview")
    public com.leezp.lib.cusview.vc.VerificationCodeView verificationCodeView;

    public OrderClaimVh(Context context) {
        super(context,R.layout.frg_order_claim);
    }
}
