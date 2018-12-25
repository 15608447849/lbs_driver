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
// Generated from file `file_service_reg.ice'
//
// Warning: do not edit this file.
//
// </auto-generated>
//

package bottle.distributed.register.reg;

public class FSAddressConfig implements java.lang.Cloneable, java.io.Serializable
{
    public String address;

    public int priority;

    public FSAddressConfig()
    {
        address = "";
    }

    public FSAddressConfig(String address, int priority)
    {
        this.address = address;
        this.priority = priority;
    }

    public boolean
    equals(java.lang.Object rhs)
    {
        if(this == rhs)
        {
            return true;
        }
        FSAddressConfig _r = null;
        if(rhs instanceof FSAddressConfig)
        {
            _r = (FSAddressConfig)rhs;
        }

        if(_r != null)
        {
            if(address != _r.address)
            {
                if(address == null || _r.address == null || !address.equals(_r.address))
                {
                    return false;
                }
            }
            if(priority != _r.priority)
            {
                return false;
            }

            return true;
        }

        return false;
    }

    public int
    hashCode()
    {
        int __h = 5381;
        __h = IceInternal.HashUtil.hashAdd(__h, "::reg::FSAddressConfig");
        __h = IceInternal.HashUtil.hashAdd(__h, address);
        __h = IceInternal.HashUtil.hashAdd(__h, priority);
        return __h;
    }

    public FSAddressConfig
    clone()
    {
        FSAddressConfig c = null;
        try
        {
            c = (FSAddressConfig)super.clone();
        }
        catch(CloneNotSupportedException ex)
        {
            assert false; // impossible
        }
        return c;
    }

    public void
    __write(IceInternal.BasicStream __os)
    {
        __os.writeString(address);
        __os.writeInt(priority);
    }

    public void
    __read(IceInternal.BasicStream __is)
    {
        address = __is.readString();
        priority = __is.readInt();
    }

    static public void
    __write(IceInternal.BasicStream __os, FSAddressConfig __v)
    {
        if(__v == null)
        {
            __nullMarshalValue.__write(__os);
        }
        else
        {
            __v.__write(__os);
        }
    }

    static public FSAddressConfig
    __read(IceInternal.BasicStream __is, FSAddressConfig __v)
    {
        if(__v == null)
        {
             __v = new FSAddressConfig();
        }
        __v.__read(__is);
        return __v;
    }
    
    private static final FSAddressConfig __nullMarshalValue = new FSAddressConfig();

    public static final long serialVersionUID = -44176746L;
}
