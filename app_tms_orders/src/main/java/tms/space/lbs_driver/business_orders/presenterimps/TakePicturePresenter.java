package tms.space.lbs_driver.business_orders.presenterimps;

import com.hsf.framework.api.driver.sCLAIM;
import com.hsf.framework.api.driver.sGRAB;
import com.hsf.framework.api.driver.sSIGN;
import com.leezp.lib.singlepageapplication.base.SpaBaseHandle;
import com.leezp.lib.util.HttpUtil;
import com.leezp.lib.util.ImageUtil;
import com.leezp.lib_log.LLog;

import java.io.File;
import java.util.List;

import tms.space.lbs_driver.tms_base.beans.DriverOrderInfo;
import tms.space.lbs_driver.tms_base.business.contracts.OrderDetailContract;
import tms.space.lbs_driver.tms_base.business.ice.OrderIceModel;

/**
 * Created by Leeping on 2018/8/2.
 * email: 793065165@qq.com
 */

public class TakePicturePresenter extends OrderOptionPresenterImp<OrderDetailContract.TackPictureView> implements OrderDetailContract.TackPicturePresenter{


    public TakePicturePresenter(DriverOrderInfo orderInfo) {
        super(new OrderIceModel());
        setOrderInfo(orderInfo);
    }

    //图片上传回调
    private final HttpUtil.CallbackAbs onImageUploadResult = new HttpUtil.CallbackAbs(){
        @Override
        public void onResult(HttpUtil.Response response) {
            if (view!=null) view.hindProgress(); //关闭进度条
            isExecute = false;
            if (response.isSuccess()){
                LLog.print("图片上传信息:" + response.getMessage());
                if (view!=null) {
                    view.toast("图片上传成功,等待处理订单信息");
                    view.imageUpdateSuccess();
                }
            }else{
                if (view!=null) view.toast("上传失败,请重试");
            }
        }
    };

    //view -> 上传图片
    @Override
    public void uploadImage(SpaBaseHandle handle,List<File> fileList) {
        if (isExecute) {
            if (view!=null) view.toast("上传中,请等待");
            return;
        }
        isExecute = true;
        if (view!=null) view.showProgress(); //打开进度条
        if (view!=null) view.toast("正在进行图片处理,请稍后");
        for (File file : fileList){
           detectionFile(file);
        }
        //图片压缩处理
        fileList = ImageUtil.imageCompression(fileList,1024);
        if (view!=null) view.toast("开始上传图片");
        model.uploadImage(
                getOrderInfo().getUser().getUserCode(),
                getOrderInfo().getComp().compid,
                getOrderInfo().getInfo().orderNo,
                getOrderInfo().getInfo().state,
                fileList,onImageUploadResult);
    }

    //检查文件流是否有效
    private void detectionFile(File file) {
        if (file.length()==0 ) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException ignored) {}
            detectionFile(file);
        }
    }
    @Override
    public void orderHandle(SpaBaseHandle handle) {
        if ( getOrderInfo().getInfo().state == sGRAB.value){ //取货成功
            changeOrderState(sCLAIM.value);//改变订单状态->取货
            if ( getOrderInfo().getInfo().state == sCLAIM.value){
                //取货成功 , 开始记录轨迹
                addTrackRecode(handle);
                if ( getOrderInfo().getInfo().complex.isOnline){
                    //线上支付 - 进入费用确认
                    if (view!=null) view.confirmationFee();
                }else{
                    if (view!=null)  view.orderPickupSuccess();
                }
            }else{
                if (view!=null) view.toast("订单操作异常");
            }
        }
        else if ( getOrderInfo().getInfo().state == sCLAIM.value){
            changeOrderState(sSIGN.value);//改变订单状态->签收
            if ( getOrderInfo().getInfo().state == sSIGN.value){
                //签收成功,关闭轨迹
                removeTrackRecode(handle);
                if (view!=null) view.orderSignSuccess();
            }else{
                if (view!=null) view.toast("订单操作异常");
            }
        }
    }
}
