package tms.space.lbs_driver.tms_required.business_login;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MotionEvent;
import android.view.View;

import com.leezp.lib.update.ApkUpdateConfig;
import com.leezp.lib.util.AppUtil;

import tms.space.lbs_driver.tms_base.business.contracts.LoginContract;
import tms.space.lbs_driver.tms_base.fragments.MVPFragment;

/**
 * Created by Leeping on 2018/6/27.
 * email: 793065165@qq.com
 */

public class LoginFragment extends MVPFragment<LoginVh,LoginContract.Presenter> implements View.OnClickListener,LoginContract.View,View.OnTouchListener {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setPresenter(new LoginPresenter());
        presenter.bindView(this);
        presenter.tryLogin();
    }


    @Override
    public void viewCreated() {

        vh.button.setOnClickListener(this);
        vh.textPhone.getEditText().setOnTouchListener(this);
        vh.textPassword.getEditText().setOnTouchListener(this);
    }

    private void cancelError() {
        if (vh.textPhone.isErrorEnabled()) vh.textPhone.setErrorEnabled(false);
        if (vh.textPassword.isErrorEnabled()) vh.textPassword.setErrorEnabled(false);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        cancelError();
        return false;
    }

    @Override
    public void onClick(View v) {
        cancelError();
        String phone = vh.textPhone.getEditText().getText().toString();
        String password = vh.textPassword.getEditText().getText().toString();
        login(phone,password);
    }

    private void login(final String phone, final String password) {
        toIo(new Runnable() {
            @Override
            public void run() {
                presenter.validateAccount(phone,password);
            }
        });
    }



    @Override
    public void toast(String msg) {
        AppUtil.showSnackBar(vh.getViewRoot(),msg);
    }

    @Override
    public void showProgress() {
        toUi(new Runnable() {
            @Override
            public void run() {
                vh.progressBar.progress.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void hindProgress() {
        toUi(new Runnable() {
            @Override
            public void run() {
                vh.progressBar.progress.setVisibility(View.INVISIBLE);
            }
        });
    }

    @Override
    public void showPhoneError(final String msg) {
        toUi(new Runnable() {
            @Override
            public void run() {
                vh.textPhone.setError(msg);
            }
        });
    }

    @Override
    public void showPasswordError(final String msg) {
        toUi(new Runnable() {
            @Override
            public void run() {
                vh.textPassword.setError(msg);
            }
        });
    }

    @Override
    public void entry() {
        //移除登陆
        remove("content","login");
        //显示头部菜单
        skip("head","head");
        //显示tab菜单
        skip("bottom","bottom");
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
         ApkUpdateConfig.autoCheckUpdate(); //尝试更新
    }
}
