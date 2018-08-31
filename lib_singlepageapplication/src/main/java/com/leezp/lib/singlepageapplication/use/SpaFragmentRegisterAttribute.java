package com.leezp.lib.singlepageapplication.use;

import android.os.Bundle;

import com.leezp.lib.singlepageapplication.base.SpaBaseFragment;

/**
 * Created by Leeping on 2018/4/15.
 * email: 793065165@qq.com
 * 用于反射构建Fragment
 */

public final class SpaFragmentRegisterAttribute {
    /**
     * 主容器标识
     */
    private String pageTag = "";
    /**
     * fragment 类路径
     */
    private String classPath;
    /**
     * fragment 唯一标识
     */
    private String tagName;
    /**
     * 初始化时携带的参数
     */
    private Bundle args;

    SpaFragmentRegisterAttribute(String classPath, String tagName, Bundle args) {
        if (classPath==null || classPath.length()==0 || tagName==null || tagName.length()==0 ) throw new IllegalArgumentException("无法绑定一个fragment属性");
        this.classPath = classPath;
        this.tagName = tagName;
        this.args = args;
    }
    SpaFragmentRegisterAttribute(String classPath, String tagName) {
        this(classPath,tagName,null);
    }
    public <T extends SpaBaseFragment> SpaFragmentRegisterAttribute(Class<T> cls, String tagName) {
        this(cls.getName(),tagName,null);
    }


    public SpaFragmentRegisterAttribute(String classPath) {
        this(classPath,classPath);
    }

    public String getClassPath() {
        return classPath;
    }

    public String getTagName() {
        return pageTag+separator+tagName;
    }

    public static final String separator = "&";

    public static String parsePageTag(String spaFragmentTag){
        String[] sArr = spaFragmentTag.split(separator);
        if (sArr.length==2) return sArr[0];
        return  null;
    }
    public static String parseFragmentTag(String spaFragmentTag){
        String[] sArr = spaFragmentTag.split(separator);
        if (sArr.length==2) return sArr[1];
        return  null;
    }

    public Bundle getArgs() {
        return args;
    }



    @Override
    public int hashCode() {
        return getTagName().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj!=null && obj instanceof SpaFragmentRegisterAttribute){
            SpaFragmentRegisterAttribute o = (SpaFragmentRegisterAttribute)obj;
            if (o.getTagName().equals(getTagName())) return true;
        }
        return false;
    }

    public String getPageTag() {
        return pageTag;
    }

    public void setPageTag(String pageTag) {
        this.pageTag = pageTag;
    }

    @Override
    public String toString() {
        return "{" +
                "pageTag='" + pageTag + '\'' +

                ", tagName='" + tagName + '\'' +
                '}';
    }

    public boolean isSame(String tag) {

        return getTagName().equals(pageTag+separator+tag);
    }
}
