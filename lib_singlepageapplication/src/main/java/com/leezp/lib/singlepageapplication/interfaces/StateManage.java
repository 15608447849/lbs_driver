package com.leezp.lib.singlepageapplication.interfaces;

import java.util.LinkedList;

/**
 * Created by Leeping on 2018/7/22.
 * email: 793065165@qq.com
 */

public class StateManage<T,D> {
    private final LinkedList<StateOpAbs<T,D>> linkedList = new LinkedList<>();

    private final T holder;

    public StateManage(T holder) {
        this.holder = holder;
        init();
    }



    protected void init(){}


    public void add(StateOpAbs<T,D> op){
        linkedList.add(op);
    }

    public boolean select(D d){
        for (StateOpAbs<T,D> op : linkedList){
            if (op.check(d)){
                op.execute(holder,d);
                return true;
            }
        }
        return false;
    }

}
