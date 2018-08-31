package com.leezp.lib.zerocice;

import java.io.Closeable;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


public class IOThreadPool implements Closeable {
    private ThreadPoolExecutor executor;
    public IOThreadPool() {
        executor = createIoExecutor();
        createIoExecutor();
    }
    //核心线程数,最大线程数,非核心线程空闲时间,存活时间单位,线程池中的任务队列
    private ThreadPoolExecutor createIoExecutor() {

         return new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors(),
                200,
                30L,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<Runnable>(500),
                new ThreadFactory(){

                    @Override
                    public Thread newThread(Runnable r) {
                        Thread thread = new Thread(r);
                        thread.setName("t-pio#-"+thread.getId());
                        return thread;
                    }
                },
                new RejectedExecutionHandler(){
                    @Override
                    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                        //超过IO线程池处理能力的任务, 丢弃

                    }
                }
        );
    }
    public void post(Runnable runnable){
        executor.execute(runnable);
    }
    @Override
    public void close(){
        if (executor!=null) executor.shutdownNow();
    }
}
