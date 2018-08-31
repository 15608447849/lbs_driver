package com.leezp.lib.util.tuples;



/**
 * Created by Leeping on 2018/4/8.
 * email: 793065165@qq.com
 */

public class Tuple2<A,B> extends Tuple implements Tuple.IValue0<A>,Tuple.IValue1<B> {
    public Tuple2(A a,B b) {
        super(new Object[]{a,b});
    }
    protected Tuple2(Object[] obj) {
        super(obj);
    }

    @Override
    public A getValue0() {
        return (A)getValue(0);
    }

    @Override
    public B getValue1() {
        return (B)getValue(1);
    }
}
