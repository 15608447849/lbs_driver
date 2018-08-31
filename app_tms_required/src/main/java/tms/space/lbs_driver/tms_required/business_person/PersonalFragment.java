package tms.space.lbs_driver.tms_required.business_person;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.leezp.lib.recycles.BaseViewHolder;
import com.leezp.lib.recycles.BaseViewHolderDataModel;
import com.leezp.lib.recycles.more_view_adapter.ItemViewTemplateAttribute;
import com.leezp.lib.recycles.more_view_adapter.ItemViewTemplateManage;
import com.leezp.lib.util.TimeUtil;

import tms.space.lbs_driver.tms_base.beans.DriverUser;
import tms.space.lbs_driver.tms_base.beans.IBasePresenter;
import tms.space.lbs_driver.tms_base.fragments.RelevanceHeadFragment;
import tms.space.lbs_driver.tms_required.R;
import tms.space.lbs_driver.tms_required.business_person.imps.ChangePasswordHandler;
import tms.space.lbs_driver.tms_required.business_person.imps.CheckUpdateApk;
import tms.space.lbs_driver.tms_required.business_person.imps.LogoutHandler;
import tms.space.lbs_driver.tms_required.business_person.interfaces.OnClickItemHandle;
import tms.space.lbs_driver.tms_required.business_person.recycle.PersonalItemDataBean;
import tms.space.lbs_driver.tms_required.business_person.recycle.PersonalItemTemplate;

/**
 * Created by Leeping on 2018/7/24.
 * email: 793065165@qq.com
 */

public class PersonalFragment extends RelevanceHeadFragment<PersonalVh,IBasePresenter>  {

    //用户信息
    private DriverUser user = new DriverUser().fetch();

    @Override
    protected ItemViewTemplateManage getMoreRecItemManage() {
        return new ItemViewTemplateManage(){
            @Override
            public void initItemLayout() {
                addAttr(new ItemViewTemplateAttribute(PersonalItemTemplate.class, R.layout.rec_item_person));
            }
        };
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        killSelf();
    }

    @Override
    public void viewCreated() {
        initAdapter();
        setHeadTitle(TimeUtil.getTimeBucketStr()+ ","+ user.getName());

    }

    //初始化适配器
    private void initAdapter() {
        mHandle.toIo(new Runnable() {
            @Override
            public void run() {
                createItem();
                mHandle.toUi(new Runnable() {
                    @Override
                    public void run() {
                        recycleAdapter.notifyDataSetChanged();
                    }
                });
            }
        });
    }

    //创建适配子项
    private void createItem() {
        recycleAdapter.addData(new PersonalItemDataBean(R.drawable.ic_rec_item_personal_change_password,"修改密码",new ChangePasswordHandler()));
        recycleAdapter.addData(new PersonalItemDataBean(R.drawable.ic_rec_item_personal_logout,"退出登陆",new LogoutHandler()));
        recycleAdapter.addData(new PersonalItemDataBean(R.drawable.ic_rec_item_personal_update,"检查更新",new CheckUpdateApk()));

    }

    //列表子项点击回调
    @Override
    public void onItemClick(BaseViewHolder<BaseViewHolderDataModel> vh,BaseViewHolderDataModel dataModel, int position) {
        //子项点击
        try {
            PersonalItemDataBean data = dataModel.convert();
            OnClickItemHandle h = data.getHandle();
            if (h!=null){
                h.onClick(this);
            }
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
    }


}
