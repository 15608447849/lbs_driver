package tms.space.lbs_driver.base.init;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Toast;

import com.leezp.lib.update.ApkUpdateConfig;
import com.leezp.lib.util.AppUtil;
import com.leezp.lib.util.CrashHandler;
import com.leezp.lib.util.ErrorUtil;
import com.leezp.lib.util.GlideUtil;
import com.leezp.lib.util.ImageUtil;
import com.leezp.lib.util.LeeApplicationAbs;
import com.leezp.lib.zerocice.IceIo;
import com.leezp.lib_gdmap.GdMapUtils;
import com.leezp.lib_log.LLog;

import tms.space.lbs_driver.tms_base.storage.DbStore;
import tms.space.lbs_driver.tms_mapop.server.TrackTransferService;


/**
 * Created by Leeping on 2018/6/27.
 * email: 793065165@qq.com
 */
public class LbsApplication extends LeeApplicationAbs {
    //打包使用 0测试环境 1准生产环境 2正式环境
    private static int envFlag = 1;

    /**
     * 所有进程需要的初始化操作
     */
    @Override
    protected void onCreateByAllProgress(String processName) {
        super.onCreateByAllProgress(processName);
        //异常捕获
        catCrash();
        //初始化服务器参数信息
        initServiceParams();
        //k-v SqlLite3
        DbStore.get().init(getApplicationContext());
        //高德地图工具库
        GdMapUtils.get().init(getApplicationContext());
    }

    //异常捕获
    private void catCrash() {
        CrashHandler.getInstance().init(getApplicationContext(), new CrashHandler.Callback() {
            @Override
            public void crash(String crashFilePath, Throwable ex) {
                LLog.print(ErrorUtil.printExceptInfo(ex),"\n",crashFilePath);
                if (envFlag!=0) System.exit(-1);
                //发送日志到服务器 pass
                Toast.makeText(getApplicationContext(),"捕获到未处理的异常信息:\n"+ErrorUtil.printExceptInfo(ex),Toast.LENGTH_SHORT).show();
            }
        });
    }

    //服务器信息
    private void initServiceParams() {
        String host = null;
        int port = 0;
        String fileServerUpload = null;
        String fileServerDownload = null;

        if (envFlag==0) {
            host = "192.168.1.241";
            port = 7061;
            fileServerUpload = "http://"+host+":8090/fileUploadApp";
            fileServerDownload = "http://"+host+":8080/wlq";
        }
        if (envFlag==1) {
            host = "39.108.85.159";
            port = 4061;
            fileServerUpload = "http://"+host+":8090/fileUploadApp";
            fileServerDownload = "http://"+host+":80/wlq";
        }
        if (envFlag==2) {
            host = "119.23.203.132";
            port = 4061;
            fileServerUpload = "http://"+host+":8090/fileUploadApp";
            fileServerDownload = "http://"+host+":80/wlq";
        }

        IceIo.get().init("WLQ", host, port);
        IceIo.get().addParams("fileServerUpload", fileServerUpload);
        IceIo.get().addParams("fileServerDownload", fileServerDownload);
        //添加网络状态过滤器
        IceIo.get().addFilter(new IceIo.IFilter() {
            @Override
            public void filter() throws Exception {
                if (!AppUtil.isNetworkAvailable(getApplicationContext()))
                    throw new IllegalStateException("网络无效.");
            }
        });
    }

    /**
     * 主包名进程 初始化创建
     */
    @Override
    protected void onCreateByApplicationMainProgress(String processName) {
        initUI();
        startTraceServer();
        initAppUpdate();
    }

    //UI主进程使用
    private void initUI() {
        //碎片页面注册
        FragmentReg.init();
        //图片工具
        ImageUtil.initAppContext(getApplicationContext());
        //glide加载
        GlideUtil.initAppContext(getApplicationContext());
    }

    //APP 自动升级
    private void initAppUpdate() {
        if (envFlag==0) {
            ApkUpdateConfig.init(getApplicationContext(), "cc893a1f7d");//测试
        }
        if (envFlag==1) {
            ApkUpdateConfig.init(getApplicationContext(), "e7895e9bd9");//准生产
        }
        if (envFlag==2) {
            ApkUpdateConfig.init(getApplicationContext(), "198d7b8d74");//正式
        }
    }
    //打开轨迹服务
    private void startTraceServer() {
        startService(new Intent(getApplicationContext(), TrackTransferService.class));
    }

    /**
     * 其他包名进程 初始化创建
     */
    @Override
    protected void onCreateByApplicationOtherProgress(String processName) {

    }

    /**
     * activity 启动设置
     */
    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        super.onActivityCreated(activity, savedInstanceState);
        //竖屏锁定
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //标题栏隐藏
//        activity.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //全屏
//        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //应用运行时，保持屏幕高亮,不锁屏
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        //设定软键盘的输入法模式 覆盖在图层上 不会改变布局
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        //设定软键盘的输入法模式: 确保当前输入焦点是可见的 - https://blog.csdn.net/achellies/article/details/7034756
//        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    }


}
