package com.leezp.lib.recycles;

/**
 * Created by Leeping on 2018/4/25.
 * email: 793065165@qq.com
 * 填充 recycle item 的数据模型 (Sss)
 */
public interface BaseViewHolderDataModel {
    /**
     * recycle 适配器调用
     * @return view模板的自定义类型,如 以layout id为模板的唯一标识
     */
    int getViewTemplateType();

    /** 为了兼容多类型itemView 可以进行动态的转换
     *实现:   具体的数据模型 convert(){
     *              return this; 
     *              }
     */
    <DATA> DATA convert() throws ClassCastException;
}
