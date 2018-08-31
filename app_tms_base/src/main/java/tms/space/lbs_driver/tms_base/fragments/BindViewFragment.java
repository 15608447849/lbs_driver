package tms.space.lbs_driver.tms_base.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.leezp.lib.recycles.BaseViewHolder;
import com.leezp.lib.recycles.BaseViewHolderDataModel;
import com.leezp.lib.recycles.RecycleViewAdapterRelevanceDataBean;
import com.leezp.lib.recycles.RecyclerUtil;
import com.leezp.lib.recycles.more_view_adapter.ItemViewTemplateManage;
import com.leezp.lib.recycles.more_view_adapter.MultiTypeAdapter;
import com.leezp.lib.singlepageapplication.base.SpaBaseFragment;
import com.leezp.lib.viewholder.interfaces.ViewHolderAbs;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import tms.space.lbs_driver.tms_base.recycler.FragmentRecycleViewHolderAbs;

/**
 * Created by Leeping on 2018/7/26.
 * email: 793065165@qq.com
 *  自动关联视图fragment
 */
public abstract class BindViewFragment<VH extends ViewHolderAbs> extends SpaBaseFragment implements RecycleViewAdapterRelevanceDataBean.OnItemClickListener<BaseViewHolderDataModel> {

    /** 视图元素持有者 */
    protected VH vh;

    /** 反射创建视图元素对象实例 */
    private VH createVh(){
        ParameterizedType parameterizedType = (ParameterizedType)this.getClass().getGenericSuperclass();
        Type[] typeArr = parameterizedType.getActualTypeArguments();
        return ViewHolderAbs.createViewHolder((Class) typeArr[0],getSpaActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (vh == null){
            vh = createVh();
            initRecycleView();
            viewCreated();

        }
        return vh.getViewRoot();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (vh != null) {
            vh.destroy();
            vh = null;
        }
    }

    //列表适配器
    protected MultiTypeAdapter recycleAdapter ;

    protected ItemViewTemplateManage getMoreRecItemManage(){
        return null;
    }

    /** 视图持有者创建成功之后调用 */
    protected void viewCreated() {    }

    private void initRecycleView() {
        if (vh instanceof FragmentRecycleViewHolderAbs){
            FragmentRecycleViewHolderAbs frvh = (FragmentRecycleViewHolderAbs) vh;
            recycleAdapter = new MultiTypeAdapter(getSpaActivity(),getMoreRecItemManage());
            recycleViewConnAdapter(frvh );
        }
    }

    protected void recycleViewConnAdapter(FragmentRecycleViewHolderAbs frvh) {
        RecyclerUtil.gridLayoutManagerSettingVerSpan1Norev(getSpaActivity(),frvh.getRecyclerView());
        RecyclerUtil.associationAdapter(frvh.getRecyclerView(),recycleAdapter);
        RecyclerUtil.setItemAnimator(frvh.getRecyclerView(),new DefaultItemAnimator());
        recycleAdapter.setItemClickListener(this);
    }

    @Override
    public void onItemClick(BaseViewHolder<BaseViewHolderDataModel> vh,BaseViewHolderDataModel dataModel, int position) {

    }
}
