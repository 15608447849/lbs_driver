package tms.space.lbs_driver.tms_mapop.gdMap;

import com.amap.api.location.AMapLocation;

public abstract class FilterAbs implements IFilter<AMapLocation>{

    private IFilter<AMapLocation> next;

    protected IFilterError<AMapLocation> filterError;

    @Override
    public void setFilterError(IFilterError<AMapLocation> filterError) {
        this.filterError = filterError;
        if (this.next!=null){
            this.next.setFilterError(filterError);
        }
    }

    @Override
    public void setNext(IFilter<AMapLocation> iFilter) {

        if (this.next==null){
                this.next = iFilter;
        }else{
                this.next.setNext(iFilter);
        }
    }

    @Override
    public boolean chainIntercept(AMapLocation aMapLocation) {
        if (intercept(aMapLocation)){
            return true;
        }
        else{
            if (next!=null) return next.chainIntercept(aMapLocation);
        }
        return false;
    }
}
