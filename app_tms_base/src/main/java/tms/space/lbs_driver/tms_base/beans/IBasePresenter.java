package tms.space.lbs_driver.tms_base.beans;

/**
 * Created by Leeping on 2018/8/2.
 * email: 793065165@qq.com
 */

public interface IBasePresenter<V extends IBaseView> {
    void bindView(V view);
    void unbindView();
}
