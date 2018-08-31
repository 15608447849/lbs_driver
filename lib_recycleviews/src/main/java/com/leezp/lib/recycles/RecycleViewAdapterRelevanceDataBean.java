package com.leezp.lib.recycles;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Leeping on 2018/4/23.
 * email: 793065165@qq.com
 * recycle 适配器
 * 自动关联 view 和 data
 */
public abstract class RecycleViewAdapterRelevanceDataBean<D extends BaseViewHolderDataModel> extends RecyclerView.Adapter<BaseViewHolder<D>>{

    protected Context mContext;;

    //数据列表
    private List<D> dataList;

    public interface OnItemClickListener<D extends BaseViewHolderDataModel>{
        void onItemClick(BaseViewHolder<D> vh,D data, int position);
    }

    private OnItemClickListener<D> itemClickListener;

    public void setItemClickListener(OnItemClickListener<D> itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public RecycleViewAdapterRelevanceDataBean(Context context) {
        this(context,new ArrayList<D>());
    }

    public RecycleViewAdapterRelevanceDataBean(Context mContext,List<D> mDatas) {
        this.mContext = mContext;
        this.dataList = mDatas;
    }

    //适配获取数据需要绑定的view模板类型
    @Override
    public int getItemViewType(int position) {
        return getData(position).getViewTemplateType();
    }

    @Override
    public void onBindViewHolder(final BaseViewHolder<D> holder, final int position) {

        final D data = getData(position);

        //view - data 关联
        holder.bindData(data);

        //子项点击监听回调
        if (itemClickListener!=null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClickListener.onItemClick(holder,data,position);
                }
            });

        }
    }


    @Override
    public int getItemCount() {
        return dataList.size();
    }

    /**
     * 获取数据列表
     */
    public List<D> getDataList(){
        return dataList;
    }

    /**
     * 获取指定下标的数据
     */
    public D getData(int pos){
        if (dataList.size()>pos) return dataList.get(pos);
        else return null;
    }
    /**
     * 添加数据
     */
    public void addData(D data){
        dataList.add(data);
    }
    public interface ObjectFlat<T>{
        boolean flat(T oldObj,T newObj);
    }
    /**
     * 添加数据
     */
    public void addData(D data,ObjectFlat<D> fun){
        for (D d: dataList){
            if (fun.flat(d,data)){
                return ;
            }
        }
        dataList.add(data);
    }
    /**
     * 添加数据
     */
    public void addData(int pos,D data){
        dataList.add(pos,data);
    }
    /**
     * 清理所有数据
     */
    public void clearAll(){
        dataList.clear();
    }
    /**
     * 添加列表
     */
    public void addDataList(List<D> list){
        addDataList(list,false);
    }
    /**
     * 添加列表
     */
    public void addDataList(List<D> list,boolean isClear){
        if (isClear) clearAll();
        dataList.addAll(list);
    }



}
