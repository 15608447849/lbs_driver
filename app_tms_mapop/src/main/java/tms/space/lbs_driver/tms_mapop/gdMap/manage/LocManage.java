package tms.space.lbs_driver.tms_mapop.gdMap.manage;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationListener;

import java.util.concurrent.ConcurrentLinkedQueue;

import tms.space.lbs_driver.tms_mapop.gdMap.ILocationAbs;
import tms.space.lbs_driver.tms_mapop.gdMap.IStrategy;
import tms.space.lbs_driver.tms_mapop.gdMap.filters.LocBaseFilter;

/**
 * Created by Leeping on 2018/7/23.
 * email: 793065165@qq.com
 */

public class LocManage extends ILocationAbs<AMapLocationClient,AMapLocationListener,AMapLocation> implements AMapLocationListener,Runnable{

    //并发无界限线程安全队列
    private final ConcurrentLinkedQueue<AMapLocation> locQueue = new ConcurrentLinkedQueue<>();

    //取数据线程
    private Thread t;

    private volatile boolean isRun = true;

    private LocBaseFilter baseFilter = new LocBaseFilter();//基础过滤器

    public LocBaseFilter getBaseFilter() {
        return baseFilter;
    }

    public LocManage() {
        this.t = new Thread(this);
        this.t.setName("t-"+getClass().getSimpleName());
        this.t.setDaemon(true);
        this.t.start();
    }


    @Override
    public void create(IStrategy<AMapLocationClient,AMapLocation> configStrategy) {
        super.create(configStrategy);
        mLocationClient.setLocationListener(this);
    }


    @Override
    public void destroy() {
        super.destroy();
        isRun = false;
        t = null;
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
        if (baseFilter.filter(aMapLocation)){
            //startLoc(); //重新启动坐标
            return;
        }
//        LLog.print("添加队列:"+ aMapLocation.toStr(3));
        locQueue.offer(aMapLocation);
    }

    @Override
    public void run() {
        while (isRun){
            AMapLocation aMapLocation = locQueue.poll();
            if (baseFilter.filter(aMapLocation)) {
                try { Thread.sleep(1000);} catch (InterruptedException ignored) {}
                continue;
            }
            // 1. 过滤 如果被拦截,true
            int size = filters.size();
            boolean isFilter = false;
            if (size>0) {
                for (int i = 0 ; i < size ; i++){
                    if (filters.get(i).filter(aMapLocation)) {
                        isFilter = true;
                        break;
                    };
                }
            }
            if (isFilter) continue;

            // 2.分配
            size = listeners.size();
            if (size>0){
                for (int i = 0; i<listeners.size(); i++){
                    listeners.get(i).onLocationChanged(aMapLocation);
                }
            }
            try { Thread.sleep(200);} catch (InterruptedException ignored) {}
        }
    }
}
