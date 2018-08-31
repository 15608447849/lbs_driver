package com.leezp.lib.util.tuples;

/**
 * Created by Leeping on 2018/4/8.
 * email: 793065165@qq.com
 */

public abstract class Tuple  {

//    int index = 0;
    private final Object[] valueArray;
    Tuple(Object[] objects) {
        valueArray = objects;
    }

    public Object getValue(int pos){
        if (pos>=valueArray.length) throw new ArrayIndexOutOfBoundsException();
        return valueArray[pos];
    }

    public interface IValue0<X> {
        X getValue0();
    }
    public interface IValue1<X> {
        X getValue1();
    }
    public interface IValue2<X> {
        X getValue2();
    }
}
