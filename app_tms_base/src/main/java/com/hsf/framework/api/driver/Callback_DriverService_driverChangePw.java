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
 * 司机修改密码
 * phone:司机手机号码
 * opw:旧密码(MD5加密)
 * npw:新密码(MD5加密)
 **/

public abstract class Callback_DriverService_driverChangePw
    extends IceInternal.TwowayCallback implements Ice.TwowayCallbackInt
{
    public final void __completed(Ice.AsyncResult __result)
    {
        DriverServicePrxHelper.__driverChangePw_completed(this, __result);
    }
}
