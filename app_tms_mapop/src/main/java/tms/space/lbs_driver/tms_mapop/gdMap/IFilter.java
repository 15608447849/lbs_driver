package tms.space.lbs_driver.tms_mapop.gdMap;

/**
 * Created by Leeping on 2018/7/23.
 * email: 793065165@qq.com
 */

public interface IFilter<T> {
    void setNext(IFilter<T> iFilter);
    void setFilterError(IFilterError<T> filterError);
    boolean chainIntercept(T t);
    boolean intercept(T t);

}
