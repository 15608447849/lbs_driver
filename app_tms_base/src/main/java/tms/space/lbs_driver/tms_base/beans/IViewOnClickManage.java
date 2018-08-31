package tms.space.lbs_driver.tms_base.beans;

import java.util.LinkedList;

/**
 * Created by Leeping on 2018/8/1.
 * email: 793065165@qq.com
 */

public class IViewOnClickManage<IV extends IViewOnClickAction> {
    public LinkedList<IV> list = new LinkedList<>();
    public void add(IV... vList){
        for (IV v : vList) add(v);
    }
    public void add(IV v){
        list.add(v);
    }
    public IV select(int vid){
        for (IV v : list){
            if (v.check(vid)) {
                v.onAction();
                return v;
            }
        }
        return null;
    }
}
