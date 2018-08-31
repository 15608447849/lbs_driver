package tms.space.lbs_driver.tms_scanqr;

import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.CompoundButton;

import com.hsf.framework.api.driver.scanTag;
import com.leezp.lib.util.DialogUtil;
import com.leezp.lib.util.VibratorUse;

import cn.bingoogolapple.qrcode.core.QRCodeView;
import tms.space.lbs_driver.tms_base.beans.DriverOrderInfo;
import tms.space.lbs_driver.tms_base.business.contracts.ScanQrContract;
import tms.space.lbs_driver.tms_base.fragments.CanBackUpFragmentAbs;

/**
 * Created by Leeping on 2018/7/19.
 * email: 793065165@qq.com
 */

public class ScanQrFragment extends CanBackUpFragmentAbs<ScanQrVh,ScanQrContract.Presenter> implements QRCodeView.Delegate, CompoundButton.OnCheckedChangeListener,ScanQrContract.View {
    private VibratorUse vibratorUse;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setPresenter(new ScanQrPresenter());
    }


    @Override
    public void viewCreated() {
        vibratorUse = new VibratorUse(getSpaActivity());
        vh.mQRCodeView.setDelegate(this);
        vh.mQRCodeView.setAutoFocusSuccessDelay(0);
        vh.mQRCodeView.setAutoFocusFailureDelay(0);
        vh.toggleButton.setOnCheckedChangeListener(this);
        setHeadTitle("请扫描二维码/条形码");
    }

    @Override
    public void onStart() {
        super.onStart();
        vh.mQRCodeView.startCamera();
        vh.mQRCodeView.showScanRect();
    }
    @Override
    public void onResume() {
        super.onResume();
        vh.mQRCodeView.startSpotDelay(0);
    }


    @Override
    public void onPause() {
        vh.mQRCodeView.stopSpot();
        super.onPause();
    }
    @Override
    public void onStop() {
        vh.mQRCodeView.stopCamera();
        vh.toggleButton.setChecked(false);
        super.onStop();
    }



    @Override
    public void onDestroy() {
        vibratorUse.stopVibrator();
        vh.mQRCodeView.onDestroy();
        super.onDestroy();
    }

    /**
     * 处理打开相机出错
     */
    @Override
    public void onScanQRCodeOpenCameraError() {
        DialogUtil.dialogSimple(getSpaActivity(),"打开相机出错.","关闭",new DialogUtil.Action0(){
            @Override
            public void onAction0() {
                backUp();
            }
        });
        DialogUtil.changeSystemDialogColor(
                DialogUtil.dialogSimple(getSpaActivity(),"打开相机出错.","关闭",new DialogUtil.Action0(){
            @Override
            public void onAction0() {
                backUp();
            }
        }) ,0,0, Color.RED,0,0);

    }



    /**
     * 处理扫描结果
     *
     * @param result 摄像头扫码时只要回调了该方法 result 就一定有值，不会为 null。
     *               解析本地图片或 Bitmap 时 result 可能为 null
     */
    @Override
    public void onScanQRCodeSuccess(final String result) {
        vibratorUse.startVibrator();
        toIo(new Runnable() {
            @Override
            public void run() {
                presenter.handleOr(getSpaHandle(),result);
            }
        });
    }
    //打开闪光灯
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(isChecked){
            vh.mQRCodeView.openFlashlight();
        }else{
            vh.mQRCodeView.closeFlashlight();
        }
    }


    @Override
    public void popErrDialog(final String msg) {
        toUi(new Runnable() {
            @Override
            public void run() {
                if (msg.contains(scanTag.value)){
                    dialogShow("不可操作的订单");
                }else{
                    dialogShow("扫描结果:\n" + msg);
                }

            }
        });

    }

    private void dialogShow(String msg) {
        AlertDialog dialog = DialogUtil.dialogSimple2(getSpaActivity(),
                msg ,
                "知道了",
                new DialogUtil.Action0() {
                    @Override
                    public void onAction0() {
                        backUp();
                    }
                },
                "扫一扫",
                new DialogUtil.Action0() {
                    @Override
                    public void onAction0() {
                        //重新扫码
                        vh.mQRCodeView.startSpotDelay(0);
                    }
                });
        DialogUtil.changeSystemDialogColor(dialog,Color.RED,0, Color.BLACK,Color.GRAY,0);
    }

    @Override
    public void toClaim(final DriverOrderInfo orderInfo) {
        toUi(new Runnable() {
            @Override
            public void run() {
                //killSelf();
                //传递订单数据到下一个页面
                setDataOut(orderInfo);
                //跳转的拍照界面,上传取货照片
                addStack("take_picture");
            }
        });

    }

    @Override
    public void toCarriage(final DriverOrderInfo orderInfo) {
        toUi(new Runnable() {
            @Override
            public void run() {
                dialogShow("您已成功取到企业["+orderInfo.getComp().fname+"]的转运单["+orderInfo.getInfo().orderNo +"]\n请切换到对应企业查看订单详情");
            }
        });
    }
}
