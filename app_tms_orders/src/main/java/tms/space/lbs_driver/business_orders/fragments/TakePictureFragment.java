package tms.space.lbs_driver.business_orders.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.leezp.lib.util.CameraUse;
import com.leezp.lib.util.ImageUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import tms.space.lbs_driver.business_orders.R;
import tms.space.lbs_driver.business_orders.pops.TakePicturePop;
import tms.space.lbs_driver.business_orders.presenterimps.TakePicturePresenter;
import tms.space.lbs_driver.tms_base.beans.DriverOrderInfo;
import tms.space.lbs_driver.tms_base.business.contracts.OrderDetailContract;
import tms.space.lbs_driver.tms_base.fragments.CanBackUpFragmentAbs;

/**
 * Created by Leeping on 2018/7/28.
 * email: 793065165@qq.com
 */

public class TakePictureFragment extends CanBackUpFragmentAbs<TakePictureVh,OrderDetailContract.TackPicturePresenter> implements TakePicturePop.Callback, View.OnClickListener, View.OnLongClickListener,OrderDetailContract.TackPictureView, CameraUse.Callback {


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setPresenter(new TakePicturePresenter((DriverOrderInfo) getDataIn()));
    }

    //当前选中的image View
    private ImageView cImageView;
    //弹窗
    private TakePicturePop pop;
    /**图片选择*/
    private CameraUse cameraUse;

    @Override
    public void viewCreated() {
        vh.setListener(this);
        pop = new TakePicturePop(vh.getViewRoot(),this);
        cameraUse = new CameraUse(this);
        cameraUse.setCallback(this);
        setHeadTitle("请上传照片/长按删除");
    }

    //拍照获取图片
    @Override
    public void onTakePicture() {
        //打开系统拍照
        try {
            cameraUse.takePicture();
        } catch (Exception e) {
            e.printStackTrace();
            toast("不能打开照相机,请手动操作,再选择相册照片");
        }
    }


    @Override
    public void onFromAlbum() {
        //从相册获取
        try {
            cameraUse.choosePicture();
        } catch (Exception e) {
            e.printStackTrace();
            toast("不能打开相册");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        cameraUse.onActivityResult(requestCode,resultCode,data);
    }
    //拍照或选择图片回调
    @Override
    public void pictureResult(File file) {
        if (file!=null){
            cImageView.setTag(file);
            ImageUtil.setImageViewBitmap(cImageView,file);
        }else{
            toast("图片选择失败");
        }
    }

    @Override
    public void onClick(View v) {
        if (v instanceof ImageView){
            if (cImageView!=null && cImageView.getId() == v.getId() && cImageView.getTag()!=null) return;
            cImageView = (ImageView)v;
            pop.show();
        }else{
            fileUpload(vh.tb1,vh.tb2,vh.tb3);
        }
    }

    /**文件上传*/
    private void fileUpload(ImageView... ivs) {
        List<File> files = new ArrayList<>();
        File image;
        for (ImageView iv : ivs) {
            image = (File)iv.getTag();
            if (image == null){
                toast("图片缺少,请拍照或选择照片");
                return;
            }
            files.add(image);
        }
        fileUpload(files);
    }

    //文件上传-异步执行
    private void fileUpload(final List<File> files) {
      toIo(new Runnable() {
          @Override
          public void run() {
              presenter.uploadImage(getSpaHandle(),files);
          }
      });
    }

    /**长按删除*/
    @Override
    public boolean onLongClick(View v) {
        //显示弹窗 确定删除操作
        if (v.getTag()==null) return false;
        if (v.getTag()!=null){
            v.setTag(null);
            ((ImageView)v).setImageResource(R.drawable.ic_picture_add);
            toast("删除成功");
        }
        return true;
    }

    //文件上传成功
    @Override
    public void imageUpdateSuccess() {
        toast("图片上传成功,等待处理订单信息");
        presenter.orderHandle(getSpaHandle());
    }

    //取货成功
    @Override
    public void orderPickupSuccess() {
        orderSignSuccess();
    }

    //进行费用确认
    @Override
    public void confirmationFee() {
        killSelf();
        setDataOut(presenter.getOrderInfo());//向费用界面传递订单信息
        addStack("content","order_fee");//费用确认界面
    }

    @Override
    public void orderSignSuccess() {
        setDataOut(presenter.getOrderInfo());//向详情界面传递订单信息
        backUp();//返回上一层
    }


}
