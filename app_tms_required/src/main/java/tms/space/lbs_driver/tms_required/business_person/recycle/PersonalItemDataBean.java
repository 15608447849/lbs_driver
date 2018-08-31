package tms.space.lbs_driver.tms_required.business_person.recycle;

import com.leezp.lib.recycles.BaseViewHolderDataModel;

import tms.space.lbs_driver.tms_required.R;
import tms.space.lbs_driver.tms_required.business_person.interfaces.OnClickItemHandle;

/**
 * Created by Leeping on 2018/7/25.
 * email: 793065165@qq.com
 */

public class PersonalItemDataBean implements BaseViewHolderDataModel {
    private int imageId = -1;

    private String title = "";

    private OnClickItemHandle handle;

    public PersonalItemDataBean(int imageId, String title, OnClickItemHandle handle) {
        this.imageId = imageId;
        this.title = title;
        this.handle = handle;
    }

    public int getImageId() {
        return imageId;
    }

    public String getTitle() {
        return title;
    }

    public OnClickItemHandle getHandle() {return handle;}

    @Override
    public int getViewTemplateType() {
        return R.layout.rec_item_person;
    }

    @Override
    public PersonalItemDataBean convert() throws ClassCastException {
        return this;
    }
}
