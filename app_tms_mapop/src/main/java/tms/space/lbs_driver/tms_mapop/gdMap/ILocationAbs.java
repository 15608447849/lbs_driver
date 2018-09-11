package tms.space.lbs_driver.tms_mapop.gdMap;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Leeping on 2018/7/23.
 * email: 793065165@qq.com
 */

public abstract class ILocationAbs<T,L,D> implements ILocation<T,L,D>{

    protected final LinkedList<L> listeners = new LinkedList<>();

    protected T mLocationClient;

    @Override
    public void create(IStrategy<T> configStrategy) {
        if (mLocationClient==null){
            mLocationClient = configStrategy.config();
        }
    }

    @Override
    public void onDestroy() {
        if (mLocationClient!=null){
            stopLoc();
            listeners.clear();
            mLocationClient = null;
        }
    }

    @Override
    public void addLocationListener(L listener) {
        listeners.add(listener);
    }

    @Override
    public T getLocClient() {
        return mLocationClient;
    }


}
