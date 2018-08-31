// **********************************************************************
//
// Copyright (c) 2003-2016 ZeroC, Inc. All rights reserved.
//
// This copy of Ice is licensed to you under the terms described in the
// ICE_LICENSE file included in this distribution.
//
// **********************************************************************
//
// Ice version 3.6.3
//
// <auto-generated>
//
// Generated from file `driver.ice'
//
// Warning: do not edit this file.
//
// </auto-generated>
//

package com.hsf.framework.api.driver;

public final class OrderInfoSeqHelper
{
    public static void
    write(IceInternal.BasicStream __os, OrderInfo[] __v)
    {
        if(__v == null)
        {
            __os.writeSize(0);
        }
        else
        {
            __os.writeSize(__v.length);
            for(int __i0 = 0; __i0 < __v.length; __i0++)
            {
                OrderInfo.__write(__os, __v[__i0]);
            }
        }
    }

    public static OrderInfo[]
    read(IceInternal.BasicStream __is)
    {
        OrderInfo[] __v;
        final int __len0 = __is.readAndCheckSeqSize(44);
        __v = new OrderInfo[__len0];
        for(int __i0 = 0; __i0 < __len0; __i0++)
        {
            __v[__i0] = OrderInfo.__read(__is, __v[__i0]);
        }
        return __v;
    }
}
