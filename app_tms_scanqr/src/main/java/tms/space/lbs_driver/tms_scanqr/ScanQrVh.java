package tms.space.lbs_driver.tms_scanqr;

import android.content.Context;
import android.widget.ToggleButton;

import com.leezp.lib.viewholder.annotations.RidClass;
import com.leezp.lib.viewholder.annotations.RidName;
import com.leezp.lib.viewholder.interfaces.ViewHolderAbs;

/**
 * Created by Leeping on 2018/7/19.
 * email: 793065165@qq.com
 */
@RidClass(R.id.class)
public class ScanQrVh extends ViewHolderAbs {

    @RidName("frg_scanqr_zbarview")
    public cn.bingoogolapple.qrcode.zbar.ZBarView mQRCodeView;

    @RidName("frg_scanqr_btn_flash")
    public ToggleButton toggleButton;

    public ScanQrVh(Context context) {
        super(context, R.layout.frg_scanqr);
    }

}
