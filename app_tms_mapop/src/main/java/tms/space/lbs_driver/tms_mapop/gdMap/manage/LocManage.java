package tms.space.lbs_driver.tms_mapop.gdMap.manage;

import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationListener;
import com.leezp.lib_gdmap.GdMapUtils;

import java.util.concurrent.ConcurrentLinkedQueue;

import tms.space.lbs_driver.tms_mapop.gdMap.IFilter;
import tms.space.lbs_driver.tms_mapop.gdMap.ILocationAbs;
import tms.space.lbs_driver.tms_mapop.gdMap.IStrategy;
import tms.space.lbs_driver.tms_mapop.gdMap.filters.LocAccuracyFilter;
import tms.space.lbs_driver.tms_mapop.gdMap.filters.LocBearingFilter;
import tms.space.lbs_driver.tms_mapop.gdMap.filters.LocDistanceFilter;
import tms.space.lbs_driver.tms_mapop.gdMap.filters.LocErrorCodeFilter;
import tms.space.lbs_driver.tms_mapop.gdMap.filters.LocInfoPrint;
import tms.space.lbs_driver.tms_mapop.gdMap.filters.LocNullFilter;
import tms.space.lbs_driver.tms_mapop.gdMap.filters.LocSatellitesFilter;
import tms.space.lbs_driver.tms_mapop.gdMap.filters.LocSpeedFilter;
import tms.space.lbs_driver.tms_mapop.gdMap.filters.LocTimeFilter;
import tms.space.lbs_driver.tms_mapop.gdMap.filters.LocTypeFilter;

/**
 * Created by Leeping on 2018/7/23.
 * email: 793065165@qq.com
 */

public class LocManage extends ILocationAbs<AMapLocationClient,AMapLocationListener,AMapLocation> implements AMapLocationListener,Runnable{

    //坐标过滤
    private static class Loc extends LocNullFilter{
        AMapLocation location;
        int filterType = 1; //默认基础过滤
        private Loc(AMapLocation location, int filterType) {
            if (intercept(location)) throw new NullPointerException();
            this.location = location;
            this.filterType = filterType;
        }
    }

    //30s间隔采集网络定位
    private static class LoopNetWorkLocation extends Thread {
        private final long interval =  30 * 1000L; // 30秒一次
        private LocManage locManage;

        LoopNetWorkLocation(LocManage locManage) {
            this.locManage = locManage;
            setName("t-loop-net-location");
            start();
        }

        @Override
        public void run() {
            while (locManage.isRun){
                    synchronized (this){
                        try {  this.wait( interval );} catch (InterruptedException ignored) { }
                    }

                if (locManage.isLaunch()){
                    locManage.addLocationToQueue(GdMapUtils.get().getCurrentLocation(),2);
                }
            }
        }

        void awaken() {
            synchronized (this){
                this.notify();
            }
        }
    };

    //并发无界限线程安全队列
    private final ConcurrentLinkedQueue<Loc> locQueue = new ConcurrentLinkedQueue<>();

    //取数据线程
    private Thread t;
    //循环网络坐标
    private LoopNetWorkLocation t2 = new LoopNetWorkLocation(this);

    private volatile boolean isRun = true;

    private IFilter<AMapLocation> baseFilter = createBaseFilter();
    private IFilter<AMapLocation> onceFilter = createOnceFilter();



    //创建基础坐标过滤
    private IFilter<AMapLocation> createBaseFilter() {
        LocErrorCodeFilter locErrorCodeFilter =  new LocErrorCodeFilter();
            locErrorCodeFilter.setNext(new LocTypeFilter()); //GPS和wifi类型过滤
            locErrorCodeFilter.setNext(new LocSatellitesFilter()); //卫星数过滤-4
            locErrorCodeFilter.setNext(new LocAccuracyFilter());//精度范围过滤-50
            locErrorCodeFilter.setNext(new LocSpeedFilter());//速度过滤-0
            locErrorCodeFilter.setNext(new LocBearingFilter());//角度过滤-0
            locErrorCodeFilter.setNext(new LocDistanceFilter());//距离过滤-12
            locErrorCodeFilter.setNext(new LocTimeFilter());//时间差过滤-12
            locErrorCodeFilter.setNext(new LocInfoPrint());//信息打印
        return locErrorCodeFilter;
    }


    private IFilter<AMapLocation> createOnceFilter() {
        LocErrorCodeFilter locErrorCodeFilter =  new LocErrorCodeFilter();
            locErrorCodeFilter.setNext(new LocDistanceFilter());//距离过滤-8
            locErrorCodeFilter.setNext(new LocInfoPrint());//信息打印
        return locErrorCodeFilter;
    }


    public IFilter<AMapLocation> getBaseFilter(){
        return baseFilter;
    }

    public LocManage() {
        this.t = new Thread(this);
        this.t.setName("t-"+getClass().getSimpleName());
        this.t.setDaemon(true);
        this.t.start();
    }

    @Override
    public void create(IStrategy<AMapLocationClient> configStrategy) {
        super.create(configStrategy);
        mLocationClient.setLocationListener(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isRun = false;
        t2.awaken();
        t = null;
        t2=null;
    }

    @Override
    public void startLoc() {
        stopLoc();
        mLocationClient.startLocation();
    }

    @Override
    public void stopLoc() {
        if(isLaunch()) mLocationClient.stopLocation();
    }

    @Override
    public void closeClient(){
        mLocationClient.onDestroy(); // 高德定位客户端销毁
    }

    @Override
    public boolean isLaunch() {
        if (mLocationClient!=null){
            return mLocationClient.isStarted();
        }
        return false;
    }

    void addLocationToQueue(AMapLocation location,int type){
        try {
            Loc loc = new Loc(location,type);
            boolean isAdd = locQueue.offer(loc);

            if (isAdd){
                synchronized (this){
                    this.notify();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 定位回调
     */
    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        //当前回调在main线程  方法: 添加队列 放入队列中 ,执行异步线程取点
         addLocationToQueue(aMapLocation,1);
    }

    @Override
    public void run() {
        while (isRun){
            Loc loc = locQueue.poll();
            if (loc == null){
                synchronized (this){
                    try {
                        this.wait();
                    } catch (InterruptedException ignored) {}
                }
                continue;
            }
            if (loc.filterType == 1){
                if (baseFilter.chainIntercept(loc.location)) {
                    continue;
                }
            }
            if (loc.filterType == 2){
                if(onceFilter.chainIntercept(loc.location)){
                    continue;
                }
            }

            onCollection(loc.location);

            loc.location = null;
            loc = null;
        }
    }

    // 分配坐标
    private void onCollection(AMapLocation aMapLocation) {
        int size = listeners.size();
        if (size>0){
            for (int i = 0; i< listeners.size(); i++){
                listeners.get(i).onLocationChanged(aMapLocation);
            }
        }
    }

    public void networkLocationOnce(boolean flag){
        if (flag) {
            addLocationToQueue(GdMapUtils.get().getCurrentLocation(),0);
        }else{
            if (t2!=null) t2.awaken();
        }
    }


}
