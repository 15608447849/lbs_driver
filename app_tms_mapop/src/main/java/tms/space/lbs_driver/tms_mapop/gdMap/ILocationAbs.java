package tms.space.lbs_driver.tms_mapop.gdMap;

import java.util.LinkedList;
import java.util.List;

import tms.space.lbs_driver.tms_mapop.server.TrackTransferService;

/**
 * Created by Leeping on 2018/7/23.
 * email: 793065165@qq.com
 */

public abstract class ILocationAbs<T,L,D> implements ILocation<T,L,D>{

    protected final LinkedList<L> listeners = new LinkedList<>();
    protected IFilter<D> baseFilter;
    protected T mLocationClient;



    @Override
    public void create(IStrategy<T,D> configStrategy) {
        if (mLocationClient==null){
            mLocationClient = configStrategy.config();
            setLocationFilter(configStrategy.getFilter());
            List<IFilter<D>> filterList = configStrategy.filterList();
            if (baseFilter!=null && filterList!=null && filterList.size() > 0) {
                for (IFilter<D> filter : filterList) baseFilter.setNext(filter);
            }
        }
    }

    @Override
    public void destroy() {
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
    public void setLocationFilter(IFilter<D> filter) {
        this.baseFilter = filter;
    }

    @Override
    public T getLocClient() {
        return mLocationClient;
    }

    @Override
    public void setFilterError(IFilterError<D> filterError) {
        if (baseFilter!=null) baseFilter.setFilterError(filterError);
    }
}
