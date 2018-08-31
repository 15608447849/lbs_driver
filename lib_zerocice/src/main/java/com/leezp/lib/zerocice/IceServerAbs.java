package com.leezp.lib.zerocice;

/**
 * Created by Leeping on 2018/7/13.
 * email: 793065165@qq.com
 */

public abstract class IceServerAbs<P extends Ice.ObjectPrx> {

    public Class<P> cls;

    public IceServerAbs(Class<P> cls) {
        this.cls = cls;
    }

    private IceClient getIce(){
        return IceIo.get().getIceClient();
    }

    protected P getProxy() throws Exception{
        IceIo.get().executeFilter();
        return getIce().getServicePrx(cls); }

    protected void printParam(Object... params){
        IceIo.get().println(params);
    }

    protected String getParam(String k){
        return IceIo.get().getParams(k);
    }
}
