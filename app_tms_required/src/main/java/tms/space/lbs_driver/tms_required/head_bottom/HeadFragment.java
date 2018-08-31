package tms.space.lbs_driver.tms_required.head_bottom;

import android.view.View;

import com.leezp.lib.singlepageapplication.annotations.SpaFragmentCallback;
import com.leezp.lib.singlepageapplication.annotations.SpaWorkThreadType;
import com.leezp.lib.singlepageapplication.interfaces.OnSpaFragmentBack;
import com.leezp.lib.util.AppUtil;

import tms.space.lbs_driver.tms_base.fragments.BindViewFragment;
import tms.space.lbs_driver.tms_required.head_bottom.imps.BackItem;
import tms.space.lbs_driver.tms_required.head_bottom.imps.QueryItem;
import tms.space.lbs_driver.tms_required.head_bottom.imps.ScanItem;
import tms.space.lbs_driver.tms_required.head_bottom.interfaces.ItemManage;

/**
 * Created by Leeping on 2018/7/19.
 * email: 793065165@qq.com
 */

public class HeadFragment extends BindViewFragment<HeadVh> implements View.OnClickListener{

    private ItemManage selectManage = new ItemManage(this);

    private BackItem backItem;


    @Override
    public void viewCreated() {
        vh.setListener(this);
        backItem = new BackItem(vh.back);
        selectManage.add(backItem);
        selectManage.add(new ScanItem(vh.scan));
        selectManage.add(new QueryItem(vh.query));
        elementControl(false,false,false,false,false);
    }

    @Override
    public void onClick(View v) {
        selectManage.select(v.getId());
    }

    @SpaFragmentCallback(workThread=SpaWorkThreadType.UI)
    public void showBack(Boolean flag){
        if (flag) vh.back.setVisibility(View.VISIBLE);
        else vh.back.setVisibility(View.GONE);
    }

    //子视图创建,关联返回键
    @SpaFragmentCallback(workThread=SpaWorkThreadType.UI)
    public void setBackUp(OnSpaFragmentBack onBackUp){
        backItem.setOnBackUp(onBackUp);
    }

    //子视图返回上一层将调用此方法
    @SpaFragmentCallback(workThread=SpaWorkThreadType.UI)
    public void clickBackUp(){
//        com.orhanobut.logger.Logger.e("返回上一层");
        backItem.execute(null);
    }

    @SpaFragmentCallback(workThread=SpaWorkThreadType.UI)
    public void showTitle(Boolean flag){
        if (flag) vh.title.setVisibility(View.VISIBLE);
        else vh.title.setVisibility(View.GONE);
    }

    @SpaFragmentCallback(workThread=SpaWorkThreadType.UI)
    public void setTitle(String text){
        showTitle(true);
        vh.title.setText(text);
    }


    @SpaFragmentCallback(workThread=SpaWorkThreadType.UI)
    public void showQuery(Boolean flag){
        if (flag) vh.query.setVisibility(View.VISIBLE);
        else vh.query.setVisibility(View.GONE);
    }


    @SpaFragmentCallback(workThread=SpaWorkThreadType.UI)
    public void showScan(boolean flag){
        if (flag) vh.scan.setVisibility(View.VISIBLE);
        else vh.scan.setVisibility(View.GONE);
    }
    @SpaFragmentCallback(workThread=SpaWorkThreadType.UI)
    public void showProgressBar(Boolean flag){
        if (flag) vh.progressBar.progress.setVisibility(View.VISIBLE);
        else vh.progressBar.progress.setVisibility(View.GONE);
    }

    /**根据标识显示/隐藏 头部元素
     * query 0
     * scan 1
     * title 2
     *
     *
     * */
    @SpaFragmentCallback(workThread=SpaWorkThreadType.UI)
    public void elementControl(boolean... flags){
        if (flags.length>=1) showQuery(flags[0]);
        if (flags.length>=2) showScan(flags[1]);
        if (flags.length>=3) showTitle(flags[2]);
        if (flags.length>=4) showBack(flags[3]);
        if (flags.length>=5) showProgressBar(flags[4]);
    }

    //显示toast
    @SpaFragmentCallback(workThread=SpaWorkThreadType.UI)
    public void toast(String message){
        AppUtil.showLongSnackBar(vh.coordinator,message);
    }

}
