package com.leezp.lib.viewholder.util;


import android.content.Context;
import android.view.View;

import com.leezp.lib.viewholder.annotations.OnClicked;
import com.leezp.lib.viewholder.annotations.OnLongClicked;
import com.leezp.lib.viewholder.annotations.Rid;
import com.leezp.lib.viewholder.annotations.RidClass;
import com.leezp.lib.viewholder.annotations.RidName;
import com.leezp.lib.viewholder.interfaces.ViewHolderAbs;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;


/**
 * Created by Leeping on 2018/4/16.
 * email: 793065165@qq.com
 */

public class ReflectionTool {
    private ReflectionTool(){}

    private static void setViewValue(Object holder,Field f ,int rid,View viewRoot) throws Exception{

        f.setAccessible(true);
        View view = viewRoot.findViewById(rid);
        if (view==null) throw new RuntimeException(viewRoot+" findViewById not find");
//        Log.w("反射创建视图","视图ID:"+rid+" "+f.getName()+" 设置 "+ view);
        f.set(holder,view);
    }

    private static void setViewHolderAbsValue(Object holder, Field f, Class<? extends ViewHolderAbs> vh, View viewRoot) throws Exception{
        Constructor cons = vh.getConstructor(View.class);//获取有参构造
        f.setAccessible(true);
        f.set(holder, cons.newInstance(viewRoot));
    }

    public static <VH extends ViewHolderAbs> VH createViewHolder(Class vh, Context context) throws Exception {
        Constructor cons = vh.getConstructor(Context.class);//获取有参构造
        return (VH) cons.newInstance(context);
    }
    /*
     * getFields()：获得某个类的所有的公共（public）的字段，包括父类中的字段。
     getDeclaredFields()：获得某个类的所有声明的字段，即包括public、private和proteced，但是不包括父类的申明字段。
     */
    public static void autoViewValue(Object holder,View viewRoot) throws Exception{
        Field[] fields = holder.getClass().getDeclaredFields();
        Class c;
        RidClass idClass = holder.getClass().getAnnotation(RidClass.class);
        Rid id;
        RidName idName;
        int rid;
        for (Field field : fields) {
            int fieldValue = field.getModifiers();// 获取字段的修饰符
            if (!Modifier.isPublic(fieldValue)) continue; //非公开 跳过
            c = field.getType();
            if (View.class.isAssignableFrom(c)) {
                //获取注解
                idName = field.getAnnotation(RidName.class);
                if (idName!=null && idClass!=null){
                    rid = getRidByName(idClass.value(),idName.value());
                    setViewValue(holder, field,rid,viewRoot);
                    continue;
                }
                id = field.getAnnotation(Rid.class);
                if (id!=null){
                    setViewValue(holder, field,id.value(), viewRoot);
                }
            } else if (ViewHolderAbs.class.isAssignableFrom(c)) {
                setViewHolderAbsValue(holder, field, c, viewRoot);
            }
        }

    }

    //根据name获取id 值
    private static int getRidByName(Class rClass, String idName) throws Exception {
//        Log.w("反射Rid","类名:"+rClass.getName()+" 属性名:"+idName);
        Field field = Class.forName(rClass.getName()).getField(idName);
        field.setAccessible(true); // 设置些属性是可以访问的
        return field.getInt(rClass);
    }

    //自动把值设置为null
    public static void autoViewValueNull(ViewHolderAbs holder) {
        try{
            Field[] fields = holder.getClass().getDeclaredFields();
            Class c;
            for (Field field : fields) {
                int fieldValue = field.getModifiers();// 获取字段的修饰符
                if (!Modifier.isPublic(fieldValue)) continue; //非公开 跳过
                field.setAccessible(true);
                Object value = field.get(holder);
                if (value==null) continue;
                c = field.getType();
                setValueIsNull(holder,field);
                if (ViewHolderAbs.class.isAssignableFrom(c)) {
                    autoViewValueNull((ViewHolderAbs) value);
                }

            }
        }catch (Exception ignored){}
    }

    private static void setValueIsNull(Object holder, Field field) {
        try {
            field.setAccessible(true);
            field.set(holder,null);
        } catch (IllegalAccessException ignored) {
        }
    }

    /**
     * 自动设置点击事件
     */
    public static void autoViewListener(ViewHolderAbs holder, Object listener) throws Exception {
        Field[] fields = holder.getClass().getDeclaredFields();
        Class c;
        for (Field field : fields) {
            int fieldValue = field.getModifiers();// 获取字段的修饰符
            if (!Modifier.isPublic(fieldValue)) continue; //非公开 跳过
            c = field.getType();
            if (View.class.isAssignableFrom(c)) {
                //获取注解
                if (field.getAnnotation(OnClicked.class)!=null && listener instanceof View.OnClickListener) setViewOnClickListener(holder,field, (View.OnClickListener)listener);

                if (field.getAnnotation(OnLongClicked.class)!=null && listener instanceof View.OnLongClickListener) setViewOnLongClickListener(holder,field, (View.OnLongClickListener)listener);
            } else if (ViewHolderAbs.class.isAssignableFrom(c)) {
                callViewHolderOnclickListener (holder,field,listener);
            }
        }

    }

    private static void callViewHolderOnclickListener(ViewHolderAbs holder, Field field, Object listener) throws Exception{
        field.setAccessible(true);
        ViewHolderAbs h = (ViewHolderAbs) field.get(holder);
        h.setListener(listener);
    }

    private static void setViewOnClickListener(ViewHolderAbs holder, Field field, View.OnClickListener listener) throws IllegalAccessException {
            field.setAccessible(true);
            View v = (View) field.get(holder);
            v.setOnClickListener(listener);
    }

    private static void setViewOnLongClickListener(ViewHolderAbs holder, Field field, View.OnLongClickListener listener) throws IllegalAccessException{
        field.setAccessible(true);
        View v = (View) field.get(holder);
        v.setOnLongClickListener(listener);
    }

    private static String[] baseTypes = {
            "java.lang.Integer",
            "java.lang.Double",
            "java.lang.Float",
            "java.lang.Long",
            "java.lang.Short",
            "java.lang.Byte",
            "java.lang.Boolean",
            "java.lang.Character",
            "java.lang.String",
            "int","double","long","short","byte","boolean","char","float"};

    private static StringBuffer addTabs(int index,StringBuffer sb){
        for (int i=0;i<index;i++){
            sb.append("\t");
        }
        return sb;
    }

    public static String reflectionObjectFields(Object object){
        StringBuffer stringBuffer = new StringBuffer();
        reflectionObjectFields(object,stringBuffer,1);
        return stringBuffer.toString();
    }

    //打印对象
    private static void reflectionObjectFields(Object object,StringBuffer sb,int level) {
        if (object==null) return;
        Class cla= object.getClass();

        if (cla.isArray()){
            addTabs(level,sb).append("[\n");
            Object[] objects = (Object[]) object;
            if (objects.length == 0 ) return;
            for (Object  obj : objects){
                reflectionObjectFields(obj,sb,level+1);
            }
            addTabs(level,sb).append("]\n");
        }else{
            addTabs(level,sb).append(cla.getName()).append("#").append("{\n");
            Field[] fields=cla.getDeclaredFields();
            for(Field fd : fields){
                if (Modifier.isStatic(fd.getModifiers())) continue;
                try {
                    boolean isBase = false;
                    fd.setAccessible(true);
                    for(String str : baseTypes) {
                        if (fd.getType().getName().equals(str)){
                            addTabs(level, sb).append("(").append(fd.getType().getName()).append(")").append(fd.getName())
                                    .append(" -> ").append(fd.get(object))
                                    .append("\n");
                            isBase = true;
                            break;
                        }
                    }

                    if (!isBase) {
                        addTabs(level,sb).append("(").append(fd.getType().getName()).append(")").append(fd.getName())
                                .append(":");
                        sb.append("\n");
                        reflectionObjectFields(fd.get(object),sb,level+2);
                    }

                } catch (IllegalAccessException e) {
                    addTabs(level,sb).append(e).append("\n");
                }
            }
            addTabs(level,sb).append("}\n");
        }

    }


}
