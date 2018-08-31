package com.leezp.lib.singlepageapplication.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Leeping on 2018/6/29.
 * email: 793065165@qq.com
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SpaFragmentCallback {
    boolean allow() default true;
    SpaWorkThreadType workThread() default SpaWorkThreadType.IO;
}
