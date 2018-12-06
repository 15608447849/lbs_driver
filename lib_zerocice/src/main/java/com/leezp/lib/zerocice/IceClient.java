package com.leezp.lib.zerocice;

import java.io.Closeable;
import java.lang.reflect.Method;
import java.net.ConnectException;
import java.util.HashMap;
import java.util.Locale;

import Ice.ObjectPrx;
import Ice.Util;

public final class IceClient extends Thread implements Closeable{

    private IceClient(){
        init();
    }

    private static final class Holder{
        private static final IceClient singleton = new IceClient();
    }

    public static final IceClient getInstance(){
        return Holder.singleton;
    }

    //轮询时间
    private long loopTime = 30 * 1000L;
    //最后接入时间
    private long lastAccessTimestamp = 0L;
    //空闲时间
    private long idleTime = 5 * 60 * 1000L;
    //方法调用超时
    private int invokeTimeout = 10 * 1000;

    private String serverName = "";
    //端口号
    private int port = 0;
   //主机地址
    private String host = "127.0.0.1";

    public class Build{
        Build setLoopTime(int time){
           loopTime = time * 1000L;
           return this;
        }
        Build setIdleTime (int time){
            idleTime = time * 1000L;
            return this;
        }
        Build setInvokeTimeout(int time){
            invokeTimeout = time;
            return this;
        }
        Build setServerName(String name) {
            serverName = name;
            return this;
        }
        Build setPort (int p){
            port = p;
            return this;
        }
        Build setIp (String addr){
            host = addr;
            return this;
        }
        void reboot(){
            closeCommunicator();
        }

    }

    public Build getBuild(){
        return new Build();
    }

    public String getServerInfo() {
        return host+":"+port;
    }

    // ice 连接配置
    private String[] configStringArr() {
            return new String[] {
                    String.format(
                            Locale.getDefault(),
                            "--Ice.Default.Locator=%s/Locator:tcp -h %s -p %s", serverName,host, port),
                    "--Ice.MessageSizeMax=4096"
            };
    };

    //是否循环检测
    private boolean isLoop = true;

   //代理类模块存储
    private  HashMap<Class,Ice.ObjectPrx> cls2PrxMap = new HashMap<>();
    /**
     * ice连接
     */
    private volatile Ice.Communicator communicator = null;

   //初始化
    private void init(){
        setDaemon(true);
        start();
    }

    /**
     * 获取代理 ,强制转换类型
     */
    public <T extends Ice.ObjectPrx> T getServicePrx(Class<T> clz) throws Exception{
        lastAccessTimestamp = System.currentTimeMillis();
        T proxy = (T) cls2PrxMap.get(clz);
        if (proxy == null){
            try {
                proxy =  createIceProxy(clz);
            } catch (ConnectException e) {
                closeCommunicator();
                throw e;
            }
            cls2PrxMap.put(clz,proxy);
        }
        return proxy;
    }

    /**
     * 获取ice连接
     */
    private Ice.Communicator getIceCommunicator() {
        if (communicator == null){
            synchronized (this){
                if (communicator==null){
                    communicator = Util.initialize(configStringArr());
                }
            }
        }
        return communicator;
    }

    // 创建客户端服务代理
    private <T extends Ice.ObjectPrx> T createIceProxy(Class<T> clz) throws Exception {
       String clzName = clz.getSimpleName();
       int pos = clzName.lastIndexOf("Prx");
       if (pos <= 0) throw new IllegalArgumentException("invalid 'Ice.ObjectPrx' class ,class name must end with 'Prx'.");
       clzName = clzName.substring(0, pos);
       Ice.ObjectPrx iPrx= getIceCommunicator()
               .stringToProxy(clzName)
               .ice_timeout(invokeTimeout)
               .ice_invocationTimeout(invokeTimeout)
               .ice_twoway()
               .ice_preferSecure(true);
       Object iPrxHelper =  Class.forName(clz.getName() + "Helper").newInstance();
       Method method = iPrxHelper.getClass().getDeclaredMethod("checkedCast", ObjectPrx.class);
       return  (T) method.invoke(iPrxHelper,iPrx);
    }


    @Override
    public void run() {
        while (isLoop){
            try {
                //30秒循环一次检测
                Thread.sleep(loopTime);
            } catch (Exception e) {
                e.printStackTrace();
            }
            // 最后时间 + 空闲时间 < 当前时间 ,认为长时间未使用连接.
            if (checkTimeout() ) {
                closeCommunicator();
            }
        }
    }

   //检测超时
    private boolean checkTimeout(){
        if(lastAccessTimestamp == 0L) return false;
        return (lastAccessTimestamp + idleTime ) < System.currentTimeMillis();
    }

    /**
     * 关闭连接
     */
    private void closeCommunicator(){
        if (communicator!=null){
            synchronized (this){
                if (communicator!=null){
                    try {
                        communicator.shutdown();
                        communicator.destroy();
                    } finally {
                        communicator = null;
                        if (!cls2PrxMap.isEmpty()) cls2PrxMap.clear();
                        lastAccessTimestamp = 0L;
                    }
                }
            }
        }
    }

    @Override
    public void close(){
        isLoop = false;
        closeCommunicator();
    }


}
