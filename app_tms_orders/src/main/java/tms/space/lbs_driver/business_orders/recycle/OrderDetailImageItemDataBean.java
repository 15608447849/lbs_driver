package tms.space.lbs_driver.business_orders.recycle;

import com.leezp.lib.recycles.BaseViewHolderDataModel;
import com.leezp.lib.singlepageapplication.base.SpaBaseHandle;

import tms.space.lbs_driver.business_orders.R;

public class OrderDetailImageItemDataBean implements BaseViewHolderDataModel {

    private String url1;
    private String url2;
    private String url3;
    private SpaBaseHandle handle;

    public OrderDetailImageItemDataBean(SpaBaseHandle handle,String url1, String url2, String url3) {
        this.handle = handle;
        this.url1 = url1;
        this.url2 = url2;
        this.url3 = url3;
    }

    public String getUrl1() {
        return url1;
    }

    public String getUrl2() {
        return url2;
    }

    public String getUrl3() {
        return url3;
    }

    public SpaBaseHandle getHandle(){
        return handle;
    }


    @Override
    public int getViewTemplateType() {
        return R.layout.rec_item_order_detail_image;
    }

    @Override
    public <DATA> DATA convert() throws ClassCastException {
        return null;
    }
}
