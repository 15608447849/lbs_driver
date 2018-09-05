package tms.space.lbs_driver.tms_base.beans;

/**
 * Created by Leeping on 2018/7/27.
 * email: 793065165@qq.com
 */

public class BasePresenter<V extends IBaseView>  implements IBasePresenter<V>{

    protected V view;

    //view绑定
    @Override
    public void bindView(V view){
        if (this.view == null) this.view = view;
    }

    //view 销毁时必须调用
    @Override
    public void unbindView(){
        this.view = null;
    }

}
