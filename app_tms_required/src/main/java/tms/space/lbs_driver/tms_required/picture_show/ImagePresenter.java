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

        void openImageFile(File image);
        void closeImageFile();
        void imageError();
    }



    //图片下载回调
    private final HttpUtil.CallbackAbs fileDownloadCallback = new HttpUtil.CallbackAbs(){
        @Override
        public void onResult(HttpUtil.Response response) {
            if (view!=null) view.hindProgress();
            if (response.isSuccess()){
                loadImageFile((File)response.getData());
            }else{
                if (response.isError()){
                    response.getException().printStackTrace();
                }else{
                    LLog.print(response.getMessage());
                }
                if (view!=null) view.imageError();
            }
        }
    };

    public void loadImageUrl(String imageUrl) {
        if (view!=null) view.showProgress();
//        LLog.print("图片URL :"+imageUrl);
        File imageFile = new File(Environment.getExternalStorageDirectory(), MD5Util.encryption(imageUrl));
        HttpUtil.Request request = new HttpUtil.Request(imageUrl,fileDownloadCallback);
        request.setStream().setDownloadFileLoc(imageFile).download();
        new Thread(request).start();//下载
    }


    public void loadImageFile(File imageFile) {
//        LLog.print("加载图片文件 :"+ imageFile);
        if (view!=null) view.openImageFile(imageFile);
    }

    @Override
    public void unbindView() {
        if (view!=null) view.closeImageFile();
        super.unbindView();
    }
}
