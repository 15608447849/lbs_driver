package tms.space.lbs_driver.tms_base.business.contracts;

import android.text.SpannableString;

import com.hsf.framework.api.driver.OrderComplex;
import com.leezp.lib.singlepageapplication.base.SpaBaseActivity;
import com.leezp.lib.singlepageapplication.base.SpaBaseHandle;
import com.leezp.lib.util.HttpUtil;

import java.io.File;
import java.util.List;

import tms.space.lbs_driver.tms_base.beans.DriverOrderInfo;
import tms.space.lbs_driver.tms_base.beans.IBasePresenter;
import tms.space.lbs_driver.tms_base.beans.IBaseView;
import tms.space.lbs_driver.tms_base.beans.IStackOption;

/**
 * Created by Leeping on 2018/8/1.
 * email: 793065165@qq.com
 */

public class OrderDetailContract {

    //模型
    public interface Model{

        //获取订单详情
        OrderComplex queryOrderInfo(int enterpriseid,long orderid);
        //查询订单状态
        int queryOrderState(int enterpriseid,long orderid);
        //修改订单状态
        boolean changeOrderState(int userid, int enterpriseid,long orderid, int state);
        //修改订单价格
        boolean updateOrderFee(int enterpriseid,long orderId, double fee);
        //上传取货/签收 图片
        void uploadImage(int userid, int enterpriseid, long orderid,int state,List<File> files,HttpUtil.Callback callback);
        //获取取货/签收 文件存放的文件夹路径
        String imagePath(int enterpriseid,long orderid);
        //添加走货信息
        void addStackText(String name,String phone,int userid, int enterpriseid, long orderid, int state);

    }
    public interface View<P extends Presenter> extends IBaseView<P>{

    }


    public interface Presenter<V extends View> extends IBasePresenter<V>,IStackOption{
        /**获取当前订单*/
        DriverOrderInfo getOrderInfo();
        /**设置当前订单*/
        void setOrderInfo(DriverOrderInfo orderInfo);
        /**查询订单详情*/
        void queryOrderInfo();
        /**改变订单状态*/
        void changeOrderState(int state);
    }

    //订单详情界面
    public interface OrderDetailView extends View<OrderDetailPresenter>{
        //清理
        void clearList();
        //更新
        void updateList();

        //设置订单详情列表子项,文字行
        void setListItemData(SpannableString span,String backColor);
        void setListItemData(SpannableString span,SpannableString span2,String backColor);
        //设置订单详情列表子项 分割线
        void setListItemData(int type);
        //设置订单详情列表子项,图片链接
        void setListItemData(String... urls);
         void setListDataItemBlank();
        //通知按钮状态
        void updateState(int state);
        //允许签收
        void allowSign();
        //允许卸货
        void allowUnload();
    }

    //订单详情逻辑处理
    public interface OrderDetailPresenter extends Presenter<OrderDetailView> {
        //更新订单
        void updateOrder(SpaBaseActivity context);
        //签收操作
        void signOp();
        //卸货操作
        void unloadOp(SpaBaseHandle spaHandle);
    }

    //取货/签收 图片上传
    public interface TackPictureView extends View<TackPicturePresenter>{
        void imageUpdateSuccess();
        void orderPickupSuccess();
        void confirmationFee();
        void orderSignSuccess();
    }

    //图片上传逻辑处理
    public interface TackPicturePresenter  extends Presenter<TackPictureView>{
        // 上传取货/收货图片
        void uploadImage(SpaBaseHandle handle,List<File> fileList);
        void orderHandle(SpaBaseHandle handle);
    }

    //订单费用界面
    public interface OrderFeeView extends View<OrderFeePresenter>{
        void callback();
    }
    //订单费用逻辑处理
    public interface OrderFeePresenter extends Presenter<OrderFeeView>{
        //修改订单金额
        void updateOrderFee(double fee);
    }

}
