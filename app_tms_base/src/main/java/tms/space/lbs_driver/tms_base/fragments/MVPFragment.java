package tms.space.lbs_driver.tms_base.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.leezp.lib.viewholder.interfaces.ViewHolderAbs;

import tms.space.lbs_driver.tms_base.beans.IBasePresenter;
import tms.space.lbs_driver.tms_base.beans.IBaseView;

/**
 * Created by Leeping on 2018/8/2.
 * email: 793065165@qq.com
 */

public abstract class MVPFragment<VH extends ViewHolderAbs,P extends IBasePresenter> extends BindViewFragment<VH> implements IBaseView<P> {
    protected P presenter;
    public void setPresenter(P presenter) {
        this.presenter = presenter;
        if (presenter !=null) presenter.bindView(this);
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (presenter !=null) presenter.bindView(this);
    }

    @Override
    public void onDestroyView() {
        if (presenter !=null) presenter.unbindView();
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (presenter !=null) presenter=null;
    }
}
