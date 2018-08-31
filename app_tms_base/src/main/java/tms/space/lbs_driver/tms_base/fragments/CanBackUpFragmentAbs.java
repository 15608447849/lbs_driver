package tms.space.lbs_driver.tms_base.fragments;

import com.leezp.lib.singlepageapplication.base.SpaBaseMessage;
import com.leezp.lib.singlepageapplication.interfaces.OnSpaFragmentBack;
import com.leezp.lib.viewholder.interfaces.ViewHolderAbs;

import tms.space.lbs_driver.tms_base.beans.IBasePresenter;

/**
 * Created by Leeping on 2018/7/25.
 * email: 793065165@qq.com
 * 关联头部返回按钮
 */

public abstract class CanBackUpFragmentAbs<VH extends ViewHolderAbs,P extends IBasePresenter> extends RelevanceHeadFragment<VH,P> implements OnSpaFragmentBack{


    @Override
    public void onResume() {
        super.onResume();

        //关联head 回退
        correlationHeadBack();
    }

    private void correlationHeadBack() {
        if (getPrev() == null){
            return;
        }
        if (!isVisible()) return;
        invokeSpaFragmentMethod("head",
                new SpaBaseMessage().invokeTargetMethod(
                                "setBackUp",
                                new Class[]{OnSpaFragmentBack.class},
                                new java.lang.Object[]{this}));
    }

    //用户点击头部返回按钮被调用
    @Override
    public void backUp() {
        //关闭顶部返回键
        closeBackButton();
        //返回上一层
        back();
    }

    //点击回退键
    private void closeBackButton(){
//        com.orhanobut.logger.Logger.e("关闭头部返回按钮");
        invokeSpaFragmentMethod("head",
                new SpaBaseMessage().
                        invokeTargetMethod("clickBackUp")
        );
    }

}
