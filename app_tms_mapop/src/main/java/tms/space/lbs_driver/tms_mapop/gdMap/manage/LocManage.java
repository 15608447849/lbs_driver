package tms.space.lbs_driver.tms_mapop.gdMap.manage;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationListener;
import com.leezp.lib_gdmap.GdMapUtils;
import com.leezp.lib_log.LLog;

import java.lang.ref.WeakReference;
import java.util.concurrent.ConcurrentLinkedQueue;

import tms.space.lbs_driver.tms_mapop.gdMap.ILocationAbs;
import tms.space.lbs_driver.tms_mapop.gdMap.IStrategy;
import tms.space.lbs_driver.tms_mapop.gdMap.filters.LocNullFilter;

/**
 * Created by Leeping on 2018/7/23.
 * email: 793065165@qq.com
 */

public class LocManage extends ILocationAbs<AMapLocationClient,AMapLocationListener,AMapLocation> implements AMapLocationListener,Runnable{


    //间隔采集网络定位
    private static class LoopNetWorkLocation extends Thread {
        private long oldTime = System.currentTimeMillis();
        private volatile boolean flag = true;
        private final long interval =  15 * 60 * 1000L; // 15分钟一次
        private WeakReference<LocManage> locManageWeakReference;

        public LoopNetWorkLocation(LocManage locManage) {
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
                locManageWeakReference.get().onCollection( GdMapUtils.get().getCurrentLocation());
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
    private final ConcurrentLinkedQueue<AMapLocation> locQueue = new ConcurrentLinkedQueue<>();

    //取数据线程
    private Thread t;

    private LoopNetWorkLocation t2 = new LoopNetWorkLocation(this);

    private volatile boolean isRun = true;

    private LocNullFilter nullFilter = new LocNullFilter();

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


    /**
     * 定位回调
     */
    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        //当前回调在main线程  方法: 添加队列 放入队列中 ,执行异步线程取点
        if (nullFilter.intercept(aMapLocation)){
            return;
        }
        locQueue.offer(aMapLocation);
        synchronized (this){
           this.notify();
        }
    }

    @Override
    public void run() {
        while (isRun){

            AMapLocation aMapLocation = locQueue.poll();
            if (nullFilter.intercept(aMapLocation)) {
                synchronized (this){
                    try {
                        this.wait();
                    } catch (InterruptedException ignored) {}
                }
                continue;
            }
            if (baseFilter.chainIntercept(aMapLocation)) {
                continue;
            }
            onCollection(aMapLocation);
        }
    }

    public synchronized void onCollection(AMapLocation aMapLocation) {
        if (nullFilter.intercept(aMapLocation)) return;
        // 2.分配
        int size = listeners.size();
        if (size>0){
            for (int i = 0; i< listeners.size(); i++){
                listeners.get(i).onLocationChanged(aMapLocation);
            }
        }
    }

    public void networkLocationOnce(){

        if (t2!=null) {
            t2.locOnce();
        }
    }
}
