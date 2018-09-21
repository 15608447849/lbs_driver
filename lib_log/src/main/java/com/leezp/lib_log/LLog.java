package com.leezp.lib_log;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by Leeping on 2018/8/20.
 * email: 793065165@qq.com
 */

public class LLog{

    //消息队列
    private static final ConcurrentLinkedQueue<Object[]> locQueue = new ConcurrentLinkedQueue<>();

    //文件输出
    private static LogFileHandler fileHandler = new LogFileHandler();

    private static volatile boolean flag = true;

    private static final Runnable RUNNABLE = new Runnable() {
        @Override
        public void run() {
            fileHandler.clear(build);
            execute();
        }
    };

    private static final Thread THREAD = new Thread(RUNNABLE);

    static {
        THREAD.setName("t-logger");
        THREAD.setDaemon(true);
        THREAD.start();

    }


    private static Build build = new Build();

    public static final Build getBuild(){
        return build;
    }

    public static final void stopLogThread(){
        LLog.flag = false;
    }

    private static void waitThread(long time) {
        try {
        synchronized (LLog.class){
                LLog.class.wait(time);
        }
        } catch (InterruptedException ignored) {}
    }

    private static void notifyThread() {
        synchronized (LLog.class){
            LLog.class.notify();
        }
    }

    public static final void format(String format,Object... args){
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        Thread thread = Thread.currentThread();
        //加入队列
        locQueue.offer(
                new Object[]{thread,stackTraceElements,
                        new Object[]{String.format(Locale.getDefault(),format,args)}}
        );
        notifyThread();
        waitThread(build.threadTime);
    }




    public static final void print(Object... objects){
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        Thread thread = Thread.currentThread();
        //加入队列
        locQueue.offer(
                    new Object[]{thread,stackTraceElements, objects}
                );
        notifyThread();
    }

    private static void execute() {
        while (flag){
            Object[] objects = locQueue.poll();
            if (objects==null){
                waitThread(build.threadTime);
                continue;
            }
            try {
                execute(objects);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @NonNull static <T> T checkNotNull(@Nullable final T obj) {
        if (obj == null) {
            throw new NullPointerException();
        }
        return obj;
    }

    private static void execute(Object[] objects) throws Exception{
        checkNotNull(objects);
        if (objects.length!=3) throw new IllegalArgumentException("objects length = " + objects.length);
        Thread thread = (Thread) objects[0];
        StackTraceElement[] trace = (StackTraceElement[]) objects[1];
        Object[] messages = (Object[]) objects[2];

        StringBuffer stringBuffer = new StringBuffer();

        //获取线程信息
        threadInfo(thread,stringBuffer);
        //获取代码调用栈信息
        stackInfo(trace,stringBuffer);
        //获取日志信息
        logInfo(messages ,stringBuffer);
        //控制台输出
        consolePrint(stringBuffer);
        //文件输出
        filePrint(stringBuffer);

        stringBuffer.setLength(0);//清理
    }

    private static void threadInfo(Thread thread,StringBuffer stringBuffer) {
        if (!build.isWriteThreadInfo) return;
        ThreadGroup threadGroup = thread.getThreadGroup();
        while(threadGroup.getParent() != null){
            threadGroup = threadGroup.getParent();
        }
        int totalThread = threadGroup.activeCount();

        stringBuffer.append("线程ID=").append(thread.getId()).append(",");
        stringBuffer.append("线程名=").append(thread.getName()).append(",");
        stringBuffer.append("优先级=").append(thread.getPriority()).append(",");
        stringBuffer.append("守护线程=").append(thread.isDaemon()).append(",");
        stringBuffer.append("线程组=").append(thread.getThreadGroup().getName()).append(",");
//        stringBuffer.append("活跃数="+Thread.activeCount()+"\n");
        stringBuffer.append("总活跃数=").append(totalThread).append("\n");
    }

    private static void stackInfo(@NonNull StackTraceElement[] trace,StringBuffer stringBuffer) {
        int startIndex = getStaceStartIndex(trace);
        StackTraceElement tmp;
        for (int i = startIndex; i< startIndex + build.methodLineCount; i++){
            tmp = trace[i];
            stringBuffer.append(tmp.getClassName() + "." + tmp.getMethodName()  + "(" + tmp.getFileName() + ":" + tmp.getLineNumber() + ")");
            stringBuffer.append("\n");
        }

    }

    private static int getStaceStartIndex(StackTraceElement[] trace) {
        StackTraceElement e;
        for (int i = 0; i < trace.length; i++) {
            e = trace[i];
            if (e.getClassName().equals(LLog.class.getName())) {
                return ++i;
            }
        }
        return 0;
    }

    private static void logInfo(Object[] objects,StringBuffer stringBuffer){
        String temp;
        for (int i = 0;i<objects.length;i++){
            temp = String.valueOf(objects[i]) ;
            temp = (temp==null || temp.equals("null")) ? "" : temp;
            stringBuffer.append(temp).append("\t");
        }
        stringBuffer.deleteCharAt(stringBuffer.length() - 1);
    }

    private static void consolePrint(StringBuffer stringBuffer) {
        if (!build.isWriteConsole) return;
        Log.println(build.level,build.tag,"\n"+stringBuffer.toString());
    }

    private static void filePrint(StringBuffer stringBuffer) throws Exception {
        String head = build.dateFormat.format(new Date());
        fileHandler.handle(build, head + "\t" + stringBuffer.toString());
    }

}
