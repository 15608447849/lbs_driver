package tms.space.lbs_driver.tms_required.head_bottom;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.RadioGroup;

import tms.space.lbs_driver.tms_base.fragments.BindViewFragment;
import tms.space.lbs_driver.tms_required.head_bottom.imps.OrderItem;
import tms.space.lbs_driver.tms_required.head_bottom.imps.PersonalItem;
import tms.space.lbs_driver.tms_required.head_bottom.interfaces.ItemManage;

/**
 * Created by Leeping on 2018/7/18.
 * email: 793065165@qq.com
 */

public class BottomMenuFragment extends BindViewFragment<BottomMenuVh> implements RadioGroup.OnCheckedChangeListener{

    private ItemManage selectManage  = new ItemManage(this);;
    //是否初始化完成
    private boolean isInitialized = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState!=null){
            isInitialized = true;
        }
    }

    @Override
    public void viewCreated() {
        vh.rb.setOnCheckedChangeListener(this);
        selectManage.add(new OrderItem(vh.order));
        selectManage.add(new PersonalItem(vh.personal));
    }

    @Override
    public void onResume() {
        super.onResume();
        initLaunch();

    }

    private void initLaunch() {
        if (isInitialized) return;
        if (isVisible()) vh.order.toggle();
        isInitialized = true;

    }

    /** 底部bottom选中改变 ,默认不选中, 登陆之后将被设置首个被选中 */
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

        if (isVisible())  selectManage.select(checkedId);

    }
}
