package tms.space.lbs_driver.business_orders.recycle;

import android.view.View;
import android.widget.ImageView;

import com.leezp.lib.singlepageapplication.base.SpaBaseHandle;
import com.leezp.lib.singlepageapplication.base.SpaBaseMessage;
import com.leezp.lib.util.GlideUtil;
import com.leezp.lib.viewholder.annotations.RidClass;
import com.leezp.lib.viewholder.annotations.RidName;
import com.leezp.lib_log.LLog;

import tms.space.lbs_driver.business_orders.R;
import tms.space.lbs_driver.tms_base.recycler.RecyclerViewHolderAbs;

@RidClass(R.id.class)
public class OrderDetailImageTemplate extends RecyclerViewHolderAbs<OrderDetailImageItemDataBean> {

    @RidName("rec_item_order_picture_iv1")
    public ImageView iv1;

    @RidName("rec_item_order_picture_iv2")
    public ImageView iv2;

    @RidName("rec_item_order_picture_iv3")
    public ImageView iv3;


    public OrderDetailImageTemplate(View itemView) {
        super(itemView);
    }

    @Override
    protected void bindData(final OrderDetailImageItemDataBean dataModel) {
        setImageUri(iv1,dataModel.getUrl1(),dataModel.getHandle());
        setImageUri(iv2,dataModel.getUrl2(),dataModel.getHandle());
        setImageUri(iv3,dataModel.getUrl3(),dataModel.getHandle());
    }

    private void setImageUri(ImageView iv, final String url, final SpaBaseHandle handle) {
        GlideUtil.loadImageByHttp(iv,url);
        if (handle==null) return;
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handle.sendSpaMessage(new SpaBaseMessage(12).setData(url));
            }
        });
    }

}
