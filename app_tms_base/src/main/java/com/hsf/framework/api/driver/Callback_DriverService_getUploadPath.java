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
 * 获取上传文件的文件夹路径
 **/

public abstract class Callback_DriverService_getUploadPath
    extends IceInternal.TwowayCallback implements Ice.TwowayCallbackArg1<String>
{
    public final void __completed(Ice.AsyncResult __result)
    {
        DriverServicePrxHelper.__getUploadPath_completed(this, __result);
    }
}
