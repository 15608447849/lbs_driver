package tms.space.lbs_driver.tms_mapop.gdMap.manage;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationListener;
import com.leezp.lib_gdmap.GdMapUtils;

import java.lang.ref.WeakReference;
import java.util.concurrent.ConcurrentLinkedQueue;

import tms.space.lbs_driver.tms_mapop.gdMap.IFilter;
import tms.space.lbs_driver.tms_mapop.gdMap.ILocationAbs;
import tms.space.lbs_driver.tms_mapop.gdMap.IStrategy;
import tms.space.lbs_driver.tms_mapop.gdMap.filters.LocAccuracyFilter;
import tms.space.lbs_driver.tms_mapop.gdMap.filters.LocDistanceFilter;
import tms.space.lbs_driver.tms_mapop.gdMap.filters.LocErrorCodeFilter;
import tms.space.lbs_driver.tms_mapop.gdMap.filters.LocInfoPrint;
import tms.space.lbs_driver.tms_mapop.gdMap.filters.LocNullFilter;

/**
 * Created by Leeping on 2018/7/23.
 * email: 793065165@qq.com
 */

public class LocManage extends ILocationAbs<AMapLocationClient,AMapLocationListener,AMapLocation> implements AMapLocationListener,Runnable{


    private static class Loc extends LocNullFilter{
        AMapLocation location;
        int filterType = 1; //默认基础过滤
        private Loc(AMapLocation location, int filterType) {
            this.location = location;
            this.filterType = filterType;
            if (intercept(location)) throw new NullPointerException();
        }
    }

    //间隔采集网络定位
    private static class LoopNetWorkLocation extends Thread {
        private long oldTime = System.currentTimeMillis();
        private volatile boolean flag = true;
        private final long interval =  3 * 60 * 1000L; // 3分钟一次
        private WeakReference<LocManage> locManageWeakReference;
        private IFilter<AMapLocation> filter = new LocErrorCodeFilter();
        LoopNetWorkLocation(LocManage locManage) {
            filter.setNext(new LocAccuracyFilter().setAccuracy(100));
            filter.setNext(new LocDistanceFilter().setIntervalMin(10));
            filter.setNext(new LocInfoPrint());
            this.locManageWeakReference = new WeakReference<>(locManage);
            setName("t-loop-net-location");
            start();
        }

        void stopLoop(){
            this.flag = false;
        }

        @Override
        public void run() {
            long difference ;
            while (flag){
                if (locManageWeakReference==null || locManageWeakReference.get()==null) stopLoop();
                difference = System.currentTimeMillis() - oldTime;
                if ( difference < interval){
                    synchronized (this){
                        try {  this.wait( interval-difference );} catch (InterruptedException ignored) { }
                    }
                }
                locManageWeakReference.get().addLocationToQueue(GdMapUtils.get().getCurrentLocation(),2);
                oldTime = System.currentTimeMillis();
            }
        }

        void locOnce() {
            synchronized (this){
                this.notify();
            }
        }
    };

    //并发无界限线程安全队列
    private final ConcurrentLinkedQueue<Loc> locQueue = new ConcurrentLinkedQueue<>();

    //取数据线程
    private Thread t;

    private LoopNetWorkLocation t2 = new LoopNetWorkLocation(this);

    private volatile boolean isRun = true;

    public LocManage() {
        this.t = new Thread(this);
        this.t.setName("t-"+getClass().getSimpleName());
        this.t.setDaemon(true);
        this.t.start();
    }

    @Override
    public void create(IStrategy<AMapLocationClient, AMapLocation> configStrategy) {
        super.create(configStrategy);
        mLocationClient.setLocationListener(this);
    }

    @Override
    public void destroy() {
        super.destroy();
        isRun = false;
        t = null;
        t2.stopLoop();
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
        return mLocationClient.isStarted();
    }

    void addLocationToQueue(AMapLocation location,int type){
        try {
            boolean isAdd = locQueue.offer(new Loc(location,type));
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
                if(t2.filter.chainIntercept(loc.location)){
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
            if (t2!=null) t2.locOnce();
        }
    }
}
