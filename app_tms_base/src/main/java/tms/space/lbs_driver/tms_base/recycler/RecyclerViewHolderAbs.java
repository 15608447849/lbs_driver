package tms.space.lbs_driver.tms_base.recycler;

import android.view.View;

import com.leezp.lib.recycles.BaseViewHolderDataModel;
import com.leezp.lib.recycles.more_view_adapter.AutomaticViewBaseViewHolder;
import com.leezp.lib.viewholder.util.ReflectionTool;

/**
 * Created by Leeping on 2018/7/25.
 * email: 793065165@qq.com
 */

public abstract class RecyclerViewHolderAbs<T extends BaseViewHolderDataModel> extends AutomaticViewBaseViewHolder<T> {
    public RecyclerViewHolderAbs(View itemView) {
        super(itemView);
    }
    /**
     * 子类实现 对视图 view 赋值
     *
     * @param rootView
     */
    @Override
    protected void automaticView(View rootView) {

        try {
            ReflectionTool.autoViewValue(this,rootView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
