package com.leezp.lib.util.tuples;



/**
 * Created by Leeping on 2018/4/8.
 * email: 793065165@qq.com
 */

public class Tuple3<A,B,C> extends Tuple2<A,B> implements Tuple.IValue2<C> {
    public Tuple3(A a, B b,C c) {
        super(new Object[]{a,b,c});
    }
    @Override
    public C getValue2() {
        return (C)getValue(2);
    }
}
