package tms.space.lbs_driver.tms_mapop.server;


import android.app.Notification;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.amap.api.location.AMapLocation;
import com.leezp.lib.util.AppUtil;
import com.leezp.lib.util.FrontNotification;
import com.leezp.lib.util.HearServer;
import com.leezp.lib.util.TimeUtil;
import com.leezp.lib_log.LLog;

import java.util.List;

import tms.space.lbs_driver.tms_base.beans.DriverUser;
import tms.space.lbs_driver.tms_base.business.ice.TrackTransferIce;
import tms.space.lbs_driver.tms_mapop.R;
import tms.space.lbs_driver.tms_mapop.db.TrackDb;
import tms.space.lbs_driver.tms_mapop.display.MapActivity;
import tms.space.lbs_driver.tms_mapop.entity.LocGather;
import tms.space.lbs_driver.tms_mapop.entity.TrackDbBean;
import tms.space.lbs_driver.tms_mapop.gdMap.IFilterError;
import tms.space.lbs_driver.tms_mapop.gdMap.manage.CorManage;
import tms.space.lbs_driver.tms_mapop.gdMap.manage.LocManage;
import tms.space.lbs_driver.tms_mapop.gdMap.strategys.NetStrategy;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;


/**
 * Created by Leeping on 2018/7/20.
 * email: 793065165@qq.com
 *
 * 轨迹收集数据库:
 *          库名:   TMS_TRACK_DB
 *          表名:   TMS_TRACK_TABLE
 *          字段:   订单号order_id  主键唯一 ,
 *          司机手机号码driver_phone ,
 *          所属企业编号enterprise_id ,
 *          原始轨迹段track_path ,
 *          纠偏轨迹段 correct_path ,
 *          状态(是否有效) state ,
 *          更新时间 update_time
 *
 *
 * 功能说明:
 * 0. 初始化本地sqlite3 , 初始化后台ICE服务
 *
 * 1. 开启轨迹收集服务进程 ,执行高德GPS定位采集坐标 ,有效坐标将 更新数据库每一条记录.
 * 3. 开启轨迹纠偏服务进程,间隔 获取数据库数据 ,对一定长度轨迹进行轨迹纠偏并保存轨迹,更新记录
 * 4. 开放远程进程调用接口:
 *        a. 数据库添加一条轨迹收集数据( 订单号 => 是否新增)
 *
 *        b. 数据库修改一条轨迹收集数据状态 ( 订单号 => 可删除 )
 *
 *  5. 间隔拉取数据库记录尝试轨迹传输 , 如状态可删除,判断更新记录是否最新 , 如果间隔大于指定值 , 将移除此记录.
 *
 *
 *
 */

public class TrackTransferService extends HearServer implements IFilterError<AMapLocation> {


    //sqlite3 数据库
    private TrackDb db;

    private LocManage locManage;

    private CorManage corManage;

    private FrontNotification gpsNotify;

    private TrackTransferIce iceServer = new TrackTransferIce();

    @Override
    protected void initCreate() {

        setInterval(10); //10s
        db = new TrackDb(getApplicationContext());
        gpsNotify = createGpsNotify();
        corManage = new CorManage(getApplicationContext());
        locManage = new LocManage();
//        locManage.create(new GpsStrategy(getApplicationContext()));
        locManage.create(new NetStrategy(getApplicationContext()));
        locManage.addLocationListener(new LocGather(db));//坐标点采集实现
        locManage.getBaseFilter().setFilterError(this);
        locManage.startLoc();
    }




    @Override
    public void onDestroy() {
        super.onDestroy();
        corManage.stop();
        locManage.destroy();
    }

    //创建GPS 通知栏
    private FrontNotification createGpsNotify() {
        FrontNotification.Build build = new FrontNotification.Build(getApplicationContext(),400);
        Intent intent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
        // 打开GPS设置界面
        build.setIntent(intent);
        build.setFlags(new int[]{Notification.FLAG_INSISTENT,Notification.FLAG_AUTO_CANCEL});
        build.setGroup(getNotificationGroupKey());
        return build.autoGenerateNotification(
                getNotificationTitle(),
                "请在设置中打开定位功能",
                getNotificationInfo(),
                getNotificationIcon(),
                Notification.DEFAULT_ALL);
    }

    private FrontNotification createInfoNotify(String error) {
        FrontNotification.Build build = new FrontNotification.Build(getApplicationContext(),500);
        // 打开GPS设置界面
        build.setFlags(new int[]{Notification.FLAG_INSISTENT,Notification.FLAG_AUTO_CANCEL});
        build.setGroup(getNotificationGroupKey());
        return build.autoGenerateNotification(
                getNotificationTitle(),
                TimeUtil.formatUTCByCurrent()+"\t"+error,
                getNotificationInfo(),
                getNotificationIcon(),
                Notification.DEFAULT_ALL);
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
//        Logger.i("绑定客户端 : "+ intent);
        return new TraceServiceAIDLImp(db);
    }

    @Override
    public boolean onUnbind(Intent intent) {
//        Logger.i("解除绑定客户端 : "+ intent);
        return super.onUnbind(intent);
    }

    @Override
    public String getNotificationTitle() {
        return  "空间折叠物流科技有限公司";
    }

    @Override
    public String getNotificationContent() {
        return getApplication().getString(R.string.app_name)+" 正在为您服务";
    }
    protected String getNotificationInfo() {
        return "谢谢使用";
    }

    @Override
    protected int getNotificationIcon() {
        return R.mipmap.ic_launcher;
    }

    @Override
    protected String getNotificationGroupKey() {
        return getClass().getSimpleName();
    }

    @Override
    protected Class<?> getOpenActivityClass() {
        return MapActivity.class;
    }

    @Override
    protected int getNotificationId() {
        return 100;
    }

    @Override
    protected void executeTask() {

        DriverUser user = new DriverUser().fetch();
        if (user != null){
            checkGps();//检查GPS
            launchClient();//启动定位服务
            locDataCorrection();//数据纠偏
            iceTransfer(user);//数据传输
        }
    }

    /** 检查GPS */
    private void checkGps() {
        if (!AppUtil.isOenGPS(getApplicationContext())){
            gpsNotify.showNotification();
        }else{
            gpsNotify.cancelNotification();
        }
    }

    /**启动客户端*/
    private void launchClient() {
        if (!locManage.isLaunch()){
            locManage.startLoc();
        }

    }

    /** 停止客户端 */
    private void stopClient() {
        if (locManage.isLaunch()){
            locManage.stopLoc();//关闭定位
        }
    }

    /**定位数据坐标点纠偏*/
    private void locDataCorrection() {
        //判断网络是否有效
        if (AppUtil.isNetworkAvailable(getApplicationContext())){
            //获取数据库存在的数据
            List<TrackDbBean> list = db.queryAll();
            corManage.correct(list,db);
        }
    }

    /**ice 传输*/
    private void iceTransfer(DriverUser user) {
        //获取数据库存在的数据
        List<TrackDbBean> list = db.queryAll();

        for (TrackDbBean b : list){
            if (b.getUserId() != user.getUserCode()) continue; //判断当前登陆用户
            //上传数据到后台
            int result = trackImp(b);
            //判断删除
            checkDel(b,result);
        }
    }

    //上传数据到后台
    private int trackImp(TrackDbBean b) {
        return iceServer.transferCorrect(b.getOrderId(),b.getUserId(),b.getEnterpriseId(),b.getCorrect());
    }

    /**判断是否删除数据*/
    private void checkDel(TrackDbBean b, int result) {

        if (b.getState() > 0 && result == 0 && b.gettCode() == b.getcCode()){ //
            int del = db.deleteTrack(b.getId());
            if (del == 0) LLog.print("订单:"+b.getOrderId()+ " 删除成功");
        }
    }

    @Override
    public void onFilterError(AMapLocation location) {
        try {
            String error = location.getErrorInfo().split("\\s+")[0];
            createInfoNotify(error).showNotification();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

