package tms.space.lbs_driver.tms_mapop.gdMap;

import android.content.Context;

import java.lang.ref.SoftReference;
import java.util.List;

/**
 * Created by Leeping on 2018/7/23.
 * email: 793065165@qq.com
 */

public abstract class IStrategy<T,D> {
    private SoftReference<Context> contextRef;

    public IStrategy(Context context) {
        this.contextRef = new SoftReference<>(context);
    }
    protected Context getContext(){
        return contextRef.get();
    }
    public abstract T config();

    public abstract List<IFilter<D>> filterList();

}
