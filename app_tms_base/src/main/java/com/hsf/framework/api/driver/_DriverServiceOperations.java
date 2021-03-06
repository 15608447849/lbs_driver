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
 * 司机app服务
 **/
public interface _DriverServiceOperations
{
    /**
     * 司机登录
     * phone: 司机登录电话号码
     * pw:司机密码(MD5加密)
     * 请判断角色码
     * @param __current The Current object for the invocation.
     **/
    DriverBase driverLogin(String phone, String pw, Ice.Current __current);

    /**
     * 司机修改密码
     * phone:司机手机号码
     * opw:旧密码(MD5加密)
     * npw:新密码(MD5加密)
     * @param __current The Current object for the invocation.
     **/
    int driverChangePw(String phone, String opw, String npw, Ice.Current __current);

    /**
     * 根据 用户ID 查询其 所属全部企业列表信息
     * userid 用户ID
     * @param __current The Current object for the invocation.
     **/
    DriverCompInfo[] driverQueryCompList(int userid, Ice.Current __current);

    /**
     * 根据 用户码 ,企业码 ,指定的订单状态 , 获取订单列表 - 简单信息
     * userid 用户码
     * enterpriseid 企业码
     * state 查询的状态值
     * return 订单对象数组 (包含订单简略信息)
     * @param __current The Current object for the invocation.
     **/
    OrderInfo[] driverQueryOrderList(int userid, int enterpriseid, int state, int year, int index, int range, Ice.Current __current);

    /**
     * 查询单个订单的详情信息
     * enterpriseid 企业号
     * info 订单对象
     * @param __current The Current object for the invocation.
     **/
    OrderComplex driverQueryOrderInfo(int userid, int enterpriseid, long orderNo, Ice.Current __current);

    /**
     * 查询单个订单的状态
     * enterpriseid 企业号
     * info 订单对象
     * @param __current The Current object for the invocation.
     **/
    int driverQueryOrderState(int enterpriseid, long orderNo, Ice.Current __current);

    /**
     * 对订单进行状态修改操作
     * userid 用户码 - 在取货时需要与用户码绑定
     * enterpriseid 企业号
     * info 订单号
     * state 需要修改的状态
     * 操作成功返回true
     * @param __current The Current object for the invocation.
     **/
    boolean driverOrderStateOp(int userid, int enterpriseid, long orderNo, int state, Ice.Current __current);

    /**
     * 通知订单 修改金额
     * enterpriseid 企业号
     * info 订单对象
     * 操作成功返回true
     * @param __current The Current object for the invocation.
     **/
    boolean driverSureOrderFee(int enterpriseid, long orderNo, double fee, Ice.Current __current);

    /**
     * 根据  用户码 ,企业编号,订单号,上传用户 原始行驶轨迹点 JSON 数据 0成功
     * @param __current The Current object for the invocation.
     **/
    int driverUploadOriginal(int userid, int enterpriseid, long orderid, String json, Ice.Current __current);

    /**
     * 根据用户订单号 ,用户码 ,企业编号 ,上传用户 纠偏行驶轨迹点 JSON 数据 0成功
     * @param __current The Current object for the invocation.
     **/
    int driverUploadCorrect(int userid, int enterpriseid, long orderid, String json, Ice.Current __current);

    /**
     * 根据用户订单号 ,用户码 ,企业编号 ,上传用户 走货节点信息 JSON 数据 0成功
     * @param __current The Current object for the invocation.
     **/
    int driverUploadNode(int userid, int enterpriseid, long orderid, String node, Ice.Current __current);

    /**
     * 获取上传文件的文件夹路径
     * @param __current The Current object for the invocation.
     **/
    String getUploadPath(String compid, String orderno, Ice.Current __current);

    /**
     * 获取文件上传,文件下载前缀信息 例如: JSON: {'fileUpload':'http://ip:port/path' , 'fileLoad:':'http://ip:port/path'}
     * @param __current The Current object for the invocation.
     **/
    String getFileServer(Ice.Current __current);
}
