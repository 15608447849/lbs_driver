package com.leezp.lib.singlepageapplication.use;

import android.view.View;

import com.leezp.lib.singlepageapplication.annotations.SpaCreatePageHolder;
import com.leezp.lib.singlepageapplication.annotations.SpaViewHolder;
import com.leezp.lib.singlepageapplication.base.SpaBaseActivity;
import com.leezp.lib.singlepageapplication.base.SpaBaseFragment;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Leeping on 2018/6/13.
 * email: 793065165@qq.com
 * fragment 注册中心
 */

public class SpaRegisterCentre {
    static Map<String,HashSet<SpaFragmentRegisterAttribute>> map = new LinkedHashMap<>();

    /** 注册单个fragment到指定容器*/
    public static void register(String containerTag, SpaFragmentRegisterAttribute fragmentAttr){
        fragmentAttr.setPageTag(containerTag);//设置主容器标识
        HashSet<SpaFragmentRegisterAttribute> hashSet = map.get(containerTag);
        if (hashSet == null){
            hashSet = new HashSet<>();
            map.put(containerTag,hashSet);
        }
        hashSet.add(fragmentAttr);

    }
    /** 注册多个fragment到指定容器*/
    public static void register(String containerTag, SpaFragmentRegisterAttribute... fragmentAttrs){
       for (SpaFragmentRegisterAttribute attr : fragmentAttrs){
           register(containerTag,attr);
       }
    }
    public static <T extends SpaBaseFragment> void register(String containerTag, Class<T> cls,String tagName){
        register(containerTag,new SpaFragmentRegisterAttribute(cls,tagName));
    }

    public static HashSet<SpaFragmentRegisterAttribute> getFragmentPage(String tag){
        return map.get(tag);
    }
    /**根据注解自动创建容器
     *   * 1. 获取此对象所有属性
     * 2. 遍历此对象是否存在注解 @CreatePageHolder(name)
     * 3. 遍历是否存在注解 @SpaViewHolder() ,存在-> 遍历此对象是否存在 @CreatePageHolder
     *
     *
     * */
    public static void autoCreatePageHolder(SpaBaseActivity activity) throws Exception{
        //获取实体属性列表
        Class cls = activity.getClass();
        Field[] fieldArr = cls.getDeclaredFields();
        Field field;
        SpaViewHolder svh;
        SpaCreatePageHolder scph;
        int size = fieldArr.length;
        for (int i = 0; i < size; i++) {
            field  = fieldArr[i];
            field.setAccessible(true); //设置些属性是可以访问的
            svh = field.getAnnotation(SpaViewHolder.class);
            scph = field.getAnnotation(SpaCreatePageHolder.class);

            if (svh!=null){
                foreachViewHolder(activity,field.get(activity));
            }
            if (scph!=null) execCreatePageHolder(activity,activity,scph.value(),field);
        }
    }

    private static void foreachViewHolder(SpaBaseActivity activity, Object holder) throws Exception{
        Class cls = holder.getClass();
        Field[] fieldArr = cls.getDeclaredFields();
        int size = fieldArr.length;
        Field field;
        SpaCreatePageHolder scph;
        for (int i=0 ; i<size;i++){
            field  = fieldArr[i];
            field.setAccessible(true); //设置些属性是可以访问的
            scph = field.getAnnotation(SpaCreatePageHolder.class);
            if (scph!=null) execCreatePageHolder(activity,holder,scph.value(),field);
        }
    }

    private static void execCreatePageHolder(SpaBaseActivity activity, Object FieldHolder,String pageTag, Field field) throws Exception{
        if (null == pageTag || "".equals(pageTag)) return;
        if (!View.class.isAssignableFrom(field.getType())) return;
        View v = (View) field.get(FieldHolder);
        activity.createPageHolder(pageTag,v.getId());
    }

}
