package com.leezp.lib.singlepageapplication.imps;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;

import com.leezp.lib.singlepageapplication.base.SpaBaseFragment;
import com.leezp.lib.singlepageapplication.interfaces.SpaFragmentOperationInterface;
import com.leezp.lib.singlepageapplication.use.SpaFragmentPageHolder;
import com.leezp.lib.singlepageapplication.use.SpaFragmentRegisterAttribute;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Leeping on 2018/4/16.
 * email: 793065165@qq.com
 */

public class SpaFragmentOpDefaultImp implements SpaFragmentOperationInterface {

    private class Helper{
        /**
         * 检测是否存在栈(链表结构)上fragment
         */
        private SpaBaseFragment checkFragmentNext(SpaBaseFragment spaBaseFragment) {
            if (spaBaseFragment.getNext()!=null){
                return checkFragmentNext(spaBaseFragment.getNext());
            }
            return spaBaseFragment;
        }

        /**
         * 获取栈中所有fragment
         */
        private void getFragmentStackAll(SpaBaseFragment spaBaseFragment,List<SpaBaseFragment> list) {
            list.add(spaBaseFragment);
            if (spaBaseFragment.getNext()!=null){
                getFragmentStackAll(spaBaseFragment.getNext(),list);
            }
        }
        /**提交*/
        private void commit(FragmentTransaction ft){
            synchronized (this){
                try{
                    ft.commit();
                }catch (Exception e){
                    e.printStackTrace();
                    ft.commitAllowingStateLoss();
                }
            }
        }
    }

    private Helper h = new Helper();

    /**
     * 查询一个fragment对象
     */
    @Override
    public SpaBaseFragment queryFragmentByTag(SpaFragmentPageHolder pageHolder, SpaFragmentRegisterAttribute gAttribute) {
        FragmentManager fm = pageHolder.getFm();
        Fragment fragment = fm.findFragmentByTag(gAttribute.getTagName());
        if (fragment!=null) return (SpaBaseFragment) fragment;
        return null;
    }

    /**
     * 检测栈顶是否与目标相同
     */
    @Override
    public boolean checkTargetIsStackTop(SpaFragmentPageHolder pageHolder, SpaFragmentRegisterAttribute gAttribute) {
        FragmentManager fm = pageHolder.getFm();
        Fragment fragment = fm.findFragmentByTag(gAttribute.getTagName());
        if (fragment != null){
            return fragment.isVisible() ; //显示中或者状态已经超过onresume - true || (fragment.isResumed() && !fragment.isHidden())
        }
        return false;
    }

    /**
     * 1.创建/添加 组fragment
     * 2.如果存在,显示栈顶fragment
     */
    @Override
    public boolean showGroupFragment(SpaFragmentPageHolder pageHolder, SpaFragmentRegisterAttribute gAttribute) {
        boolean flag = false;
        int containerRid = pageHolder.getContainerRid();
        FragmentManager fm = pageHolder.getFm();
        FragmentTransaction ft = fm.beginTransaction();
        Fragment fragment = fm.findFragmentByTag(gAttribute.getTagName());
        if (fragment == null){
            //不存在,创建,添加 -且为栈组fragment
            fragment = Fragment.instantiate(pageHolder.getContext(),gAttribute.getClassPath(),gAttribute.getArgs());
            ft.add(containerRid,fragment,gAttribute.getTagName());
            flag = true;
        }else{
            //存在,显示栈顶
            fragment = h.checkFragmentNext((SpaBaseFragment)fragment);
            if (!fragment.isVisible()){
                //不在显示中-> 显示
                ft.show(fragment);
                flag = true;
            }
        }
        if (flag) h.commit(ft);
        return  flag;
    }

    /**
     * 向组fragment添加下一个fragment栈顶并显示
     * (添加碎片进'回退栈'操作)
     */
    @Override
    public void showGroupFragmentByOnlyStackTop(SpaFragmentPageHolder pageHolder,
                                                SpaFragmentRegisterAttribute gAttribute,
                                                SpaFragmentRegisterAttribute cAttribute) {
        int containerRid = pageHolder.getContainerRid();
        FragmentManager fm = pageHolder.getFm();
        FragmentTransaction ft = fm.beginTransaction();
        Fragment gFragment = fm.findFragmentByTag(gAttribute.getTagName());
        if (gFragment !=null ){
            SpaBaseFragment stackTop =
                    h.checkFragmentNext((SpaBaseFragment)gFragment);

            SpaBaseFragment newStackTop =
                    (SpaBaseFragment)Fragment.instantiate(pageHolder.getContext(),cAttribute.getClassPath(),cAttribute.getArgs());

            Object data = stackTop.transmitData();//当前栈顶传送到下一个栈顶的消息
            if (data!=null) newStackTop.receiveData(data);//新栈顶接受传递的消息

            if (stackTop.isVisible()){

                if (stackTop.isKillSelf() && stackTop.getPrev() != null){ //排除组fragment
                    ft.remove(stackTop);//移除栈顶
                    stackTop = stackTop.getPrev();//指向它的前一层
                }else{
                    ft.hide(stackTop);//隐藏栈顶
                }
            }

            ft.add(containerRid,newStackTop,cAttribute.getTagName());
            //关联
            stackTop.setNext(newStackTop);
            newStackTop.setPrev(stackTop);

            h.commit(ft);
        }
    }

    /**
     * 1.隐藏组或栈顶fragment
     */
    @Override
    public boolean hindGroupFragment(SpaFragmentPageHolder pageHolder, SpaFragmentRegisterAttribute gAttribute) {
        FragmentManager fm = pageHolder.getFm();
        FragmentTransaction ft = fm.beginTransaction();
        Fragment fragment = fm.findFragmentByTag(gAttribute.getTagName());
        if (fragment !=null ){

            SpaBaseFragment f = h.checkFragmentNext((SpaBaseFragment)fragment);
            if (f.isVisible()){
                if (f.isKillSelf() && f.getNext() == null){
                    //结束组- 移除
                    ft.remove(f);
                }else{
                    //隐藏
                    ft.hide(fragment);
                }
                h.commit(ft);
                return true;
            }
        }
        return false;
    }



    /**
     * 移除组fragment,包括此组栈中所有fragment
     */
    @Override
    public void removeGroupFragment(SpaFragmentPageHolder pageHolder, SpaFragmentRegisterAttribute gAttribute) {
        FragmentManager fm = pageHolder.getFm();
        FragmentTransaction ft = fm.beginTransaction();
        Fragment fragment = fm.findFragmentByTag(gAttribute.getTagName());
        if (fragment!=null){
            List<SpaBaseFragment> list = new ArrayList<>();
            h.getFragmentStackAll((SpaBaseFragment)fragment,list);
            for (SpaBaseFragment f:list){
                ft.remove(f);
            }
            h.commit(ft);
        }
    }

    /**
     * 1.如果存在栈,移除指定组存在的一个栈顶fragment
     * 显示上一个栈顶
     * (回退操作)
     */
    @Override
    public void removeGroupFragmentByOnlyStackTop(SpaFragmentPageHolder pageHolder, SpaFragmentRegisterAttribute gAttribute) {
        FragmentManager fm = pageHolder.getFm();
        FragmentTransaction ft = fm.beginTransaction();
        Fragment fragment = fm.findFragmentByTag(gAttribute.getTagName());
        if (fragment!=null){
            SpaBaseFragment gFragment = (SpaBaseFragment) fragment;
            if (gFragment.getNext()!=null){
                SpaBaseFragment stackTop =
                        h.checkFragmentNext(gFragment.getNext());
                SpaBaseFragment prevFragment = stackTop.getPrev();

                //数据传递
                Object data = stackTop.transmitData();
                if (data!=null) prevFragment.receiveData(data);
                //解除栈元素关联
                prevFragment.setNext(null);
                stackTop.setPrev(null);
                ft.remove(stackTop);
                ft.show(prevFragment);
                h.commit(ft);
            }
        }
    }

    /**
     * 移除当前活动的所有fragment
     */
    @Override
    public void removeGroupFragmentByActiveAll(SpaFragmentPageHolder pageHolder) {
        FragmentManager fm = pageHolder.getFm();
        FragmentTransaction ft = fm.beginTransaction();
        ArrayList<SpaFragmentRegisterAttribute> activeGroupPages = pageHolder.getActiveGroupPages();
        List<SpaBaseFragment> fragmentAll = new ArrayList<>();
        Fragment temp;
        for (SpaFragmentRegisterAttribute gAttribute : activeGroupPages){
            removeGroupFragment(pageHolder,gAttribute);
            temp = fm.findFragmentByTag(gAttribute.getTagName());
            if (temp!=null)  h.getFragmentStackAll((SpaBaseFragment)temp,fragmentAll);
        }
        for (SpaBaseFragment f:fragmentAll){
            ft.remove(f);
        }
        h.commit(ft);
    }
}
