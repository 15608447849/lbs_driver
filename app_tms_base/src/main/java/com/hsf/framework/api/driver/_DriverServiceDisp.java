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
public abstract class _DriverServiceDisp extends Ice.ObjectImpl implements DriverService
{
    protected void
    ice_copyStateFrom(Ice.Object __obj)
        throws java.lang.CloneNotSupportedException
    {
        throw new java.lang.CloneNotSupportedException();
    }

    public static final String[] __ids =
    {
        "::Ice::Object",
        "::driver::DriverService"
    };

    public boolean ice_isA(String s)
    {
        return java.util.Arrays.binarySearch(__ids, s) >= 0;
    }

    public boolean ice_isA(String s, Ice.Current __current)
    {
        return java.util.Arrays.binarySearch(__ids, s) >= 0;
    }

    public String[] ice_ids()
    {
        return __ids;
    }

    public String[] ice_ids(Ice.Current __current)
    {
        return __ids;
    }

    public String ice_id()
    {
        return __ids[1];
    }

    public String ice_id(Ice.Current __current)
    {
        return __ids[1];
    }

    public static String ice_staticId()
    {
        return __ids[1];
    }

    /**
     * 司机修改密码
     * phone:司机手机号码
     * opw:旧密码(MD5加密)
     * npw:新密码(MD5加密)
     **/
    public final int driverChangePw(String phone, String opw, String npw)
    {
        return driverChangePw(phone, opw, npw, null);
    }

    /**
     * 司机登录
     * phone: 司机登录电话号码
     * pw:司机密码(MD5加密)
     * 请判断角色码
     **/
    public final DriverBase driverLogin(String phone, String pw)
    {
        return driverLogin(phone, pw, null);
    }

    /**
     * 对订单进行状态修改操作
     * userid 用户码 - 在取货时需要与用户码绑定
     * enterpriseid 企业号
     * info 订单号
     * state 需要修改的状态
     * 操作成功返回true
     **/
    public final boolean driverOrderStateOp(int userid, int enterpriseid, long orderNo, int state)
    {
        return driverOrderStateOp(userid, enterpriseid, orderNo, state, null);
    }

    /**
     * 根据 用户ID 查询其 所属全部企业列表信息
     * userid 用户ID
     **/
    public final DriverCompInfo[] driverQueryCompList(int userid)
    {
        return driverQueryCompList(userid, null);
    }

    /**
     * 查询单个订单的详情信息
     * enterpriseid 企业号
     * info 订单对象
     **/
    public final OrderComplex driverQueryOrderInfo(int userid, int enterpriseid, long orderNo)
    {
        return driverQueryOrderInfo(userid, enterpriseid, orderNo, null);
    }

    /**
     * 根据 用户码 ,企业码 ,指定的订单状态 , 获取订单列表 - 简单信息
     * userid 用户码
     * enterpriseid 企业码
     * state 查询的状态值
     * return 订单对象数组 (包含订单简略信息)
     **/
    public final OrderInfo[] driverQueryOrderList(int userid, int enterpriseid, int state, int year, int index, int range)
    {
        return driverQueryOrderList(userid, enterpriseid, state, year, index, range, null);
    }

    /**
     * 查询单个订单的状态
     * enterpriseid 企业号
     * info 订单对象
     **/
    public final int driverQueryOrderState(int enterpriseid, long orderNo)
    {
        return driverQueryOrderState(enterpriseid, orderNo, null);
    }

    /**
     * 通知订单 修改金额
     * enterpriseid 企业号
     * info 订单对象
     * 操作成功返回true
     **/
    public final boolean driverSureOrderFee(int enterpriseid, long orderNo, double fee)
    {
        return driverSureOrderFee(enterpriseid, orderNo, fee, null);
    }

    /**
     * 根据用户订单号 ,用户码 ,企业编号 ,上传用户 纠偏行驶轨迹点 JSON 数据 0成功
     **/
    public final int driverUploadCorrect(int userid, int enterpriseid, long orderid, String json)
    {
        return driverUploadCorrect(userid, enterpriseid, orderid, json, null);
    }

    /**
     * 根据用户订单号 ,用户码 ,企业编号 ,上传用户 走货节点信息 JSON 数据 0成功
     **/
    public final int driverUploadNode(int userid, int enterpriseid, long orderid, String node)
    {
        return driverUploadNode(userid, enterpriseid, orderid, node, null);
    }

    /**
     * 根据  用户码 ,企业编号,订单号,上传用户 原始行驶轨迹点 JSON 数据 0成功
     **/
    public final int driverUploadOriginal(int userid, int enterpriseid, long orderid, String json)
    {
        return driverUploadOriginal(userid, enterpriseid, orderid, json, null);
    }

    /**
     * 获取上传文件的文件夹路径
     **/
    public final String getUploadPath(String compid, String orderno)
    {
        return getUploadPath(compid, orderno, null);
    }

    public static Ice.DispatchStatus ___driverLogin(DriverService __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Normal, __current.mode);
        IceInternal.BasicStream __is = __inS.startReadParams();
        String phone;
        String pw;
        phone = __is.readString();
        pw = __is.readString();
        __inS.endReadParams();
        DriverBase __ret = __obj.driverLogin(phone, pw, __current);
        IceInternal.BasicStream __os = __inS.__startWriteParams(Ice.FormatType.DefaultFormat);
        DriverBase.__write(__os, __ret);
        __inS.__endWriteParams(true);
        return Ice.DispatchStatus.DispatchOK;
    }

    public static Ice.DispatchStatus ___driverChangePw(DriverService __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Normal, __current.mode);
        IceInternal.BasicStream __is = __inS.startReadParams();
        String phone;
        String opw;
        String npw;
        phone = __is.readString();
        opw = __is.readString();
        npw = __is.readString();
        __inS.endReadParams();
        int __ret = __obj.driverChangePw(phone, opw, npw, __current);
        IceInternal.BasicStream __os = __inS.__startWriteParams(Ice.FormatType.DefaultFormat);
        __os.writeInt(__ret);
        __inS.__endWriteParams(true);
        return Ice.DispatchStatus.DispatchOK;
    }

    public static Ice.DispatchStatus ___driverQueryCompList(DriverService __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Idempotent, __current.mode);
        IceInternal.BasicStream __is = __inS.startReadParams();
        int userid;
        userid = __is.readInt();
        __inS.endReadParams();
        DriverCompInfo[] __ret = __obj.driverQueryCompList(userid, __current);
        IceInternal.BasicStream __os = __inS.__startWriteParams(Ice.FormatType.DefaultFormat);
        DriverCompInfoSeqHelper.write(__os, __ret);
        __inS.__endWriteParams(true);
        return Ice.DispatchStatus.DispatchOK;
    }

    public static Ice.DispatchStatus ___driverQueryOrderList(DriverService __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Idempotent, __current.mode);
        IceInternal.BasicStream __is = __inS.startReadParams();
        int userid;
        int enterpriseid;
        int state;
        int year;
        int index;
        int range;
        userid = __is.readInt();
        enterpriseid = __is.readInt();
        state = __is.readInt();
        year = __is.readInt();
        index = __is.readInt();
        range = __is.readInt();
        __inS.endReadParams();
        OrderInfo[] __ret = __obj.driverQueryOrderList(userid, enterpriseid, state, year, index, range, __current);
        IceInternal.BasicStream __os = __inS.__startWriteParams(Ice.FormatType.DefaultFormat);
        OrderInfoSeqHelper.write(__os, __ret);
        __inS.__endWriteParams(true);
        return Ice.DispatchStatus.DispatchOK;
    }

    public static Ice.DispatchStatus ___driverQueryOrderInfo(DriverService __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Idempotent, __current.mode);
        IceInternal.BasicStream __is = __inS.startReadParams();
        int userid;
        int enterpriseid;
        long orderNo;
        userid = __is.readInt();
        enterpriseid = __is.readInt();
        orderNo = __is.readLong();
        __inS.endReadParams();
        OrderComplex __ret = __obj.driverQueryOrderInfo(userid, enterpriseid, orderNo, __current);
        IceInternal.BasicStream __os = __inS.__startWriteParams(Ice.FormatType.DefaultFormat);
        OrderComplex.__write(__os, __ret);
        __inS.__endWriteParams(true);
        return Ice.DispatchStatus.DispatchOK;
    }

    public static Ice.DispatchStatus ___driverQueryOrderState(DriverService __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Idempotent, __current.mode);
        IceInternal.BasicStream __is = __inS.startReadParams();
        int enterpriseid;
        long orderNo;
        enterpriseid = __is.readInt();
        orderNo = __is.readLong();
        __inS.endReadParams();
        int __ret = __obj.driverQueryOrderState(enterpriseid, orderNo, __current);
        IceInternal.BasicStream __os = __inS.__startWriteParams(Ice.FormatType.DefaultFormat);
        __os.writeInt(__ret);
        __inS.__endWriteParams(true);
        return Ice.DispatchStatus.DispatchOK;
    }

    public static Ice.DispatchStatus ___driverOrderStateOp(DriverService __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Normal, __current.mode);
        IceInternal.BasicStream __is = __inS.startReadParams();
        int userid;
        int enterpriseid;
        long orderNo;
        int state;
        userid = __is.readInt();
        enterpriseid = __is.readInt();
        orderNo = __is.readLong();
        state = __is.readInt();
        __inS.endReadParams();
        boolean __ret = __obj.driverOrderStateOp(userid, enterpriseid, orderNo, state, __current);
        IceInternal.BasicStream __os = __inS.__startWriteParams(Ice.FormatType.DefaultFormat);
        __os.writeBool(__ret);
        __inS.__endWriteParams(true);
        return Ice.DispatchStatus.DispatchOK;
    }

    public static Ice.DispatchStatus ___driverSureOrderFee(DriverService __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Normal, __current.mode);
        IceInternal.BasicStream __is = __inS.startReadParams();
        int enterpriseid;
        long orderNo;
        double fee;
        enterpriseid = __is.readInt();
        orderNo = __is.readLong();
        fee = __is.readDouble();
        __inS.endReadParams();
        boolean __ret = __obj.driverSureOrderFee(enterpriseid, orderNo, fee, __current);
        IceInternal.BasicStream __os = __inS.__startWriteParams(Ice.FormatType.DefaultFormat);
        __os.writeBool(__ret);
        __inS.__endWriteParams(true);
        return Ice.DispatchStatus.DispatchOK;
    }

    public static Ice.DispatchStatus ___driverUploadOriginal(DriverService __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Normal, __current.mode);
        IceInternal.BasicStream __is = __inS.startReadParams();
        int userid;
        int enterpriseid;
        long orderid;
        String json;
        userid = __is.readInt();
        enterpriseid = __is.readInt();
        orderid = __is.readLong();
        json = __is.readString();
        __inS.endReadParams();
        int __ret = __obj.driverUploadOriginal(userid, enterpriseid, orderid, json, __current);
        IceInternal.BasicStream __os = __inS.__startWriteParams(Ice.FormatType.DefaultFormat);
        __os.writeInt(__ret);
        __inS.__endWriteParams(true);
        return Ice.DispatchStatus.DispatchOK;
    }

    public static Ice.DispatchStatus ___driverUploadCorrect(DriverService __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Normal, __current.mode);
        IceInternal.BasicStream __is = __inS.startReadParams();
        int userid;
        int enterpriseid;
        long orderid;
        String json;
        userid = __is.readInt();
        enterpriseid = __is.readInt();
        orderid = __is.readLong();
        json = __is.readString();
        __inS.endReadParams();
        int __ret = __obj.driverUploadCorrect(userid, enterpriseid, orderid, json, __current);
        IceInternal.BasicStream __os = __inS.__startWriteParams(Ice.FormatType.DefaultFormat);
        __os.writeInt(__ret);
        __inS.__endWriteParams(true);
        return Ice.DispatchStatus.DispatchOK;
    }

    public static Ice.DispatchStatus ___driverUploadNode(DriverService __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Normal, __current.mode);
        IceInternal.BasicStream __is = __inS.startReadParams();
        int userid;
        int enterpriseid;
        long orderid;
        String node;
        userid = __is.readInt();
        enterpriseid = __is.readInt();
        orderid = __is.readLong();
        node = __is.readString();
        __inS.endReadParams();
        int __ret = __obj.driverUploadNode(userid, enterpriseid, orderid, node, __current);
        IceInternal.BasicStream __os = __inS.__startWriteParams(Ice.FormatType.DefaultFormat);
        __os.writeInt(__ret);
        __inS.__endWriteParams(true);
        return Ice.DispatchStatus.DispatchOK;
    }

    public static Ice.DispatchStatus ___getUploadPath(DriverService __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Normal, __current.mode);
        IceInternal.BasicStream __is = __inS.startReadParams();
        String compid;
        String orderno;
        compid = __is.readString();
        orderno = __is.readString();
        __inS.endReadParams();
        String __ret = __obj.getUploadPath(compid, orderno, __current);
        IceInternal.BasicStream __os = __inS.__startWriteParams(Ice.FormatType.DefaultFormat);
        __os.writeString(__ret);
        __inS.__endWriteParams(true);
        return Ice.DispatchStatus.DispatchOK;
    }

    private final static String[] __all =
    {
        "driverChangePw",
        "driverLogin",
        "driverOrderStateOp",
        "driverQueryCompList",
        "driverQueryOrderInfo",
        "driverQueryOrderList",
        "driverQueryOrderState",
        "driverSureOrderFee",
        "driverUploadCorrect",
        "driverUploadNode",
        "driverUploadOriginal",
        "getUploadPath",
        "ice_id",
        "ice_ids",
        "ice_isA",
        "ice_ping"
    };

    public Ice.DispatchStatus __dispatch(IceInternal.Incoming in, Ice.Current __current)
    {
        int pos = java.util.Arrays.binarySearch(__all, __current.operation);
        if(pos < 0)
        {
            throw new Ice.OperationNotExistException(__current.id, __current.facet, __current.operation);
        }

        switch(pos)
        {
            case 0:
            {
                return ___driverChangePw(this, in, __current);
            }
            case 1:
            {
                return ___driverLogin(this, in, __current);
            }
            case 2:
            {
                return ___driverOrderStateOp(this, in, __current);
            }
            case 3:
            {
                return ___driverQueryCompList(this, in, __current);
            }
            case 4:
            {
                return ___driverQueryOrderInfo(this, in, __current);
            }
            case 5:
            {
                return ___driverQueryOrderList(this, in, __current);
            }
            case 6:
            {
                return ___driverQueryOrderState(this, in, __current);
            }
            case 7:
            {
                return ___driverSureOrderFee(this, in, __current);
            }
            case 8:
            {
                return ___driverUploadCorrect(this, in, __current);
            }
            case 9:
            {
                return ___driverUploadNode(this, in, __current);
            }
            case 10:
            {
                return ___driverUploadOriginal(this, in, __current);
            }
            case 11:
            {
                return ___getUploadPath(this, in, __current);
            }
            case 12:
            {
                return ___ice_id(this, in, __current);
            }
            case 13:
            {
                return ___ice_ids(this, in, __current);
            }
            case 14:
            {
                return ___ice_isA(this, in, __current);
            }
            case 15:
            {
                return ___ice_ping(this, in, __current);
            }
        }

        assert(false);
        throw new Ice.OperationNotExistException(__current.id, __current.facet, __current.operation);
    }

    protected void __writeImpl(IceInternal.BasicStream __os)
    {
        __os.startWriteSlice(ice_staticId(), -1, true);
        __os.endWriteSlice();
    }

    protected void __readImpl(IceInternal.BasicStream __is)
    {
        __is.startReadSlice();
        __is.endReadSlice();
    }

    public static final long serialVersionUID = 0L;
}
