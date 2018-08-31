package com.leezp.lib.singlepageapplication.use;

import android.app.FragmentManager;
import android.content.Context;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

/**
 * Created by Leeping on 2018/4/15.
 * email: 793065165@qq.com
 */

public final class SpaFragmentPageHolder implements Closeable{

    private final String tag;
    //当前存在的所有fragment
    private final ArrayList<SpaFragmentRegisterAttribute> activeGroupPages = new ArrayList<>();
    //上下文
    private Context context = null;
    //fragmentManager
    private FragmentManager fm = null;
    //容器布局id
    private int containerRid = -1;
    //关联的所有可用fragment的属性及编号
    private final HashSet<SpaFragmentRegisterAttribute> pages;

    public SpaFragmentPageHolder(Context context,String tag, FragmentManager fm, int containerRid, HashSet<SpaFragmentRegisterAttribute> pages ) {
        this.tag = tag;
        this.pages = pages;
        this.fm = fm;
        this.containerRid = containerRid;
        this.context = context;
    }
    public ArrayList<SpaFragmentRegisterAttribute> getActiveGroupPages() {
        return activeGroupPages;
    }

    //正在当前栈顶显示的 fragment属性
    private SpaFragmentRegisterAttribute currentGroupPage = null;

    public SpaFragmentRegisterAttribute getCurrentGroupPage() {
        return currentGroupPage;
    }

    //设置当前页面 ,如果为Null 则表示当前无显示
    public void setCurrentGroupPage(SpaFragmentRegisterAttribute page) {
        if (page == null) {
            if (currentGroupPage !=null){
                activeGroupPages.remove(currentGroupPage);
//                SpaPrt.print(this+ " , 移除页面: "+currentGroupPage);
            }
        }else{
            if (page!=currentGroupPage)
                activeGroupPages.add(page);
//                SpaPrt.print(this+" 添加页面: "+page);
        }
        currentGroupPage = page;
    }

    public void removeGroupPage(SpaFragmentRegisterAttribute page){
        activeGroupPages.remove(page);
//        SpaPrt.print(this+" , 移除页面: "+page);
    }
    //移除全部页面
    public void removeCurrentGroupAll(){
        Iterator<SpaFragmentRegisterAttribute> iterator = getActiveGroupPages().iterator();
        SpaFragmentRegisterAttribute entry;
        while (iterator.hasNext()){
            entry = iterator.next();
            iterator.remove();
//            SpaPrt.print(this+" , 移除页面: "+entry);
        }
        this.currentGroupPage = null;
    }
    //获取fragment管理器
    public FragmentManager getFm() {
        return fm;
    }
    //根据一个指定的标识获取一个fragment属性
    public SpaFragmentRegisterAttribute getPage(String tag) {

        Iterator<SpaFragmentRegisterAttribute> it = pages.iterator();
        SpaFragmentRegisterAttribute entry;
        while (it.hasNext()){
            entry = it.next();
            if (entry.isSame(tag)) return entry;
        }
        return null;
    }
    //获取容器层id
    public int getContainerRid() {
        return containerRid;
    }
    //获取上下文
    public Context getContext() {
        return context;
    }

    public String getTag() {
        return tag;
    }

    @Override
    public void close() throws IOException {
        context = null;
        fm = null;
        containerRid = -1;
    }
}
