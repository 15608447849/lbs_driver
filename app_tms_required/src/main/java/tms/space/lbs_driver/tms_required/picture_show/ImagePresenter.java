package tms.space.lbs_driver.tms_required.picture_show;

import android.os.Environment;

import com.leezp.lib.util.HttpUtil;
import com.leezp.lib.util.MD5Util;
import com.leezp.lib.util.StrUtil;
import com.leezp.lib_log.LLog;

import java.io.File;
import java.text.NumberFormat;

import tms.space.lbs_driver.tms_base.beans.BasePresenter;
import tms.space.lbs_driver.tms_base.beans.IBaseView;

/**
 * Created by Leeping on 2018/8/22.
 * email: 793065165@qq.com
 */

class ImagePresenter extends BasePresenter<ImagePresenter.View> {



    public interface View extends IBaseView<ImagePresenter>{
        //设置需要展示image View 文件
        void setProgressPercent(int percent);
        void openImageFile(File image);
        void closeImageFile();
        void imageError();
    }



    //图片下载回调
    private final HttpUtil.CallbackAbs fileDownloadCallback = new HttpUtil.CallbackAbs(){
        @Override
        public void onProgress(File file, long progress, long total) {

            NumberFormat nt = NumberFormat.getPercentInstance();
            String str = StrUtil.stringFormat("当前进度: $s",nt.format((float)progress/total));
//            view.setProgressPercent();
        }

        @Override
        public void onResult(HttpUtil.Response response) {
            view.hindProgress();
            if (response.isSuccess()){
                loadImageFile((File)response.getData());
            }else{
                if (response.isError()){
                    response.getException().printStackTrace();
                }else{
                    LLog.print(response.getMessage());
                }
                view.imageError();
            }
        }
    };

    public void loadImageUrl(String imageUrl) {
        view.showProgress();
        LLog.print("图片URL :"+imageUrl);
        File imageFile = new File(Environment.getExternalStorageDirectory(), MD5Util.encryption(imageUrl));
        HttpUtil.Request request = new HttpUtil.Request(imageUrl,fileDownloadCallback);
        request.setStream().setDownloadFileLoc(imageFile).download();
        new Thread(request).start();//下载
    }


    public void loadImageFile(File imageFile) {
        LLog.print("加载图片文件 :"+ imageFile);
        view.openImageFile(imageFile);
    }

    @Override
    public void unbindView() {
        view.closeImageFile();
        super.unbindView();
    }
}
