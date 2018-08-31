package tms.space.lbs_driver.tms_base.beans;

/**
 * Created by Leeping on 2018/7/27.
 * email: 793065165@qq.com
 */

public interface IBaseView<P extends IBasePresenter> {
    // 弹窗消息
    void toast(String msg);
    // 显示进度条
    void showProgress();
    // 隐藏进度条
    void hindProgress();
}
