package com.leezp.lib.recycles.more_view_adapter;

import java.util.LinkedList;

/**
 * Created by Leeping on 2018/7/24.
 * email: 793065165@qq.com
 */

public class ItemViewTemplateManage {

    private LinkedList<ItemViewTemplateAttribute> attrList = new LinkedList<>();

    public ItemViewTemplateManage() {
        initItemLayout();
    }

    public void initItemLayout(){

    }
    public void addAttr(ItemViewTemplateAttribute attr){
        if (attrList.contains(attr)) return;
        attrList.add(attr);
    }

    public void addAttr(ItemViewTemplateAttribute... attrList){
        for (ItemViewTemplateAttribute attr : attrList) addAttr(attr);
    }

    public LinkedList<ItemViewTemplateAttribute> getAttrList() {
        return attrList;
    }

}
