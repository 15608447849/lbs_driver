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

/**
 * 查询单个订单的详情信息
 * enterpriseid 企业号
 * info 订单对象
 **/

public abstract class Callback_DriverService_driverQueryOrderInfo
    extends IceInternal.TwowayCallback implements Ice.TwowayCallbackArg1<com.hsf.framework.api.driver.OrderComplex>
{
    public final void __completed(Ice.AsyncResult __result)
    {
        DriverServicePrxHelper.__driverQueryOrderInfo_completed(this, __result);
    }
}