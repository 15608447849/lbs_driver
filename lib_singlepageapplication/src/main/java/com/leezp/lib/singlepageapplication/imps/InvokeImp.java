package com.leezp.lib.singlepageapplication.imps;

import com.leezp.lib.singlepageapplication.annotations.SpaFragmentCallback;
import com.leezp.lib.singlepageapplication.annotations.SpaWorkThreadType;
import com.leezp.lib.singlepageapplication.base.SpaBaseHandle;
import com.leezp.lib.singlepageapplication.base.SpaBaseMessage;
import com.leezp.lib.singlepageapplication.interfaces.StateOpAbs;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * Created by Leeping on 2018/7/22.
 * email: 793065165@qq.com
 */

public class InvokeImp extends StateOpAbs<SpaBaseHandle,SpaBaseMessage> {

    @Override
    public boolean check(SpaBaseMessage sbMsg) {
        if (sbMsg.what == SpaBaseMessage.INVOKE_METHOD) return true;
        return false;
    }
    @Override
    public void execute(SpaBaseHandle handle, SpaBaseMessage sbMsg) {
            try{
                    final SpaBaseMessage.Callback callback = sbMsg.callback;
                    final java.lang.Object holder  = sbMsg.target;
                    final String methodName = sbMsg.methodName;
                    final Class[] paramClasses = sbMsg.argsTypes ;
                    final java.lang.Object[] args = sbMsg.args;

                    final Method method = holder.getClass().getDeclaredMethod(methodName, paramClasses);
                    method.setAccessible(true);
                    int fieldValue = method.getModifiers();// 获取字段的修饰符
                    if (!Modifier.isPublic(fieldValue)) throw new IllegalAccessException("method is not public, permission denied."); //非公开 拒绝
                    SpaFragmentCallback annotation = method.getAnnotation(SpaFragmentCallback.class);
                    if (annotation == null) throw new IllegalAccessException("annotation 'SpaFragmentCallback' not find.");
                    if (!annotation.allow()) throw new IllegalAccessException("annotation 'SpaFragmentCallback' permission denied.");
                    final Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
                            try {
                                java.lang.Object result = method.invoke(holder, args);
                                if (callback!=null) callback.onCallback(result);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    SpaWorkThreadType threadType = annotation.workThread();
                    if (threadType == SpaWorkThreadType.IO){
                        handle.toIo(runnable);
                    }else if (threadType == SpaWorkThreadType.UI) {
                        handle.toUi(runnable);
                    }

            }catch (Exception e){
                e.printStackTrace();
            }
    }
}
