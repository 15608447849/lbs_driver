package com.leezp.lib.singlepageapplication.use;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;

import com.leezp.lib.singlepageapplication.base.SpaBaseFragment;
import com.leezp.lib.singlepageapplication.imps.SpaFragmentOpDefaultImp;
import com.leezp.lib.singlepageapplication.interfaces.SpaFragmentOperationInterface;

import java.util.Iterator;
import java.util.List;

/**
 * Created by Leeping on 2018/4/15.
 * email: 793065165@qq.com
 *
 * activity 实现 SpaActivity
 * 设置SpaAttr实现( 关于所有的Fragment的相关信息等 )
 *
 *
 */

public final class SpaManage implements SpaFragmentOperationInterface {

    /**
     * fragment 操作具体实现
     */
    private SpaFragmentOperationInterface spaFragmentOperation = new SpaFragmentOpDefaultImp();

    private SpaManage() {}

    @Override
    public SpaBaseFragment queryFragmentByTag(SpaFragmentPageHolder pageHolder, SpaFragmentRegisterAttribute gAttribute) {
        if (gAttribute==null) return null;
        return  spaFragmentOperation.queryFragmentByTag(pageHolder,gAttribute);
    }

    /**
     * 判断当前栈顶是否是指定目标
     */
    @Override
    public boolean checkTargetIsStackTop(SpaFragmentPageHolder pageHolder, SpaFragmentRegisterAttribute gAttribute) {

        if (pageHolder.getCurrentGroupPage()!=null && pageHolder.getCurrentGroupPage().equals(gAttribute)) {
            return spaFragmentOperation.checkTargetIsStackTop(pageHolder, gAttribute);
        }
        return false;
    }

    /**
     * 1.创建添加显示组fragment
     * 2.如果存在,显示组或者栈顶fragment
     * 3.设置当前组碎片
     */
    @Override
    public boolean showGroupFragment(SpaFragmentPageHolder pageHolder, SpaFragmentRegisterAttribute gAttribute) {
        if (gAttribute == null) return false;
        boolean flag = spaFragmentOperation.showGroupFragment(pageHolder,gAttribute);
        pageHolder.setCurrentGroupPage(gAttribute);
        if (flag){
            SpaPrt.print(this+ " , 显示组碎片 : "+gAttribute.getTagName());
        }
        return flag;
    }

    /**
     * 向组fragment添加下一个fragment并显示(添加进回退栈操作)
     */
    @Override
    public void showGroupFragmentByOnlyStackTop(SpaFragmentPageHolder pageHolder, SpaFragmentRegisterAttribute gAttribute, SpaFragmentRegisterAttribute cAttribute) {
        if (gAttribute==null || cAttribute==null || gAttribute.equals(cAttribute)) return; //避免重复添加组fragment
        SpaPrt.print(this+ " , 向组中添加栈顶元素 : 组碎片"+gAttribute.getTagName()+" - 等待添加的栈顶碎片"+ cAttribute.getTagName());
        spaFragmentOperation.showGroupFragmentByOnlyStackTop(pageHolder,gAttribute,cAttribute);
    }

    /**
     * 1.隐藏组或栈顶fragment
     */
    @Override
    public boolean hindGroupFragment(SpaFragmentPageHolder pageHolder, SpaFragmentRegisterAttribute gAttribute) {
        if (gAttribute==null) return false;
        boolean flag = spaFragmentOperation.hindGroupFragment(pageHolder,gAttribute);
        if (flag) SpaPrt.print(this+ " , 隐藏组碎片 : "+gAttribute.getTagName());
        return flag;
    }

    /**
     * 1.移除组fragment,包括此组栈中所有fragment
     */
    @Override
    public void removeGroupFragment(SpaFragmentPageHolder pageHolder, SpaFragmentRegisterAttribute gAttribute) {
        if (gAttribute==null) return;
        SpaPrt.print(this + " ,移除组碎片  "+gAttribute.getTagName());
        spaFragmentOperation.removeGroupFragment(pageHolder,gAttribute);
        if (pageHolder.getCurrentGroupPage()!=null && pageHolder.getCurrentGroupPage().equals(gAttribute)) {
            pageHolder.setCurrentGroupPage(null);
        }else{
            pageHolder.removeGroupPage(gAttribute);
        }
    }

    /**
     * 1.如果存在栈,移除指定组存在的一个栈顶fragment,显示上一个栈顶(回退操作)
     */
    @Override
    public void removeGroupFragmentByOnlyStackTop(SpaFragmentPageHolder pageHolder, SpaFragmentRegisterAttribute attribute) {
        if (attribute==null) return;
        SpaPrt.print(this+ " ,移除组栈顶  "+attribute.getTagName());
        spaFragmentOperation.removeGroupFragmentByOnlyStackTop(pageHolder,attribute);
    }

    /**
     * 移除当前活动的所有fragment
     */
    @Override
    public void removeGroupFragmentByActiveAll(SpaFragmentPageHolder pageHolder) {
        SpaPrt.print(this+ " ,移除全部活动碎片");
        spaFragmentOperation.removeGroupFragmentByActiveAll(pageHolder);
        pageHolder.removeCurrentGroupAll();
    }
    /**还原fragment的状态*/
    public void recovery(SpaFragmentPageHolder spaFragmentPageHolder, List<SpaBaseFragment> recoveryFragments) {
        SpaPrt.print(this+ " recovery() ,  还原状态 "+spaFragmentPageHolder.getTag());
        FragmentManager fm = spaFragmentPageHolder.getFm();
        SpaBaseFragment temp;
        for (SpaBaseFragment fragment : recoveryFragments){
            if (!SpaFragmentRegisterAttribute.parsePageTag(fragment.getTag()).equals(spaFragmentPageHolder.getTag())){
                continue;
            }
            if (fragment.getNextTag()!=null && fragment.getNext()==null){
                temp = (SpaBaseFragment) fm.findFragmentByTag(fragment.getNextTag());
                fragment.setNext(temp);
                temp.setPrev(fragment);
                SpaPrt.print(this+ ",关联回退栈关系: "+fragment.getTag()+ " -> "+ temp.getTag());
            }

            if (fragment.getPrevTag()!=null && fragment.getPrev()==null){
                temp = (SpaBaseFragment) fm.findFragmentByTag(fragment.getPrevTag());
                fragment.setPrev(temp);
                temp.setNext(fragment);
                SpaPrt.print(this+ " ,关联回退栈关系: "+temp.getTag()+" -> "+fragment.getTag());
            }
        }
        FragmentTransaction ft = fm.beginTransaction();
        //隐藏所有
        for (Fragment f : recoveryFragments){
            ft.hide(f);
        }
        ft.commit();

        Iterator<SpaBaseFragment> iterator = recoveryFragments.iterator();
        SpaFragmentRegisterAttribute pageAttr;

        while (iterator.hasNext()){
            temp = iterator.next();
            if (!SpaFragmentRegisterAttribute.parsePageTag(temp.getTag()).equals(spaFragmentPageHolder.getTag())){
                continue;
            }
            iterator.remove();

            SpaPrt.print(this+ " : "+ temp +" - isHide = " + temp.isRecoverHide());

            if (temp.isRecoverHide()){

               /* if (temp.isVisible()){
                    SpaPrt.print(this+ ", 隐藏碎片 "+temp.getTag()+" visible = "+temp.isVisible());
                    ft = fm.beginTransaction();
                    ft.hide(temp);
                    ft.commit();
                    while (temp.isVisible());
                }
*/
            }else{
               // SpaPrt.print(this+ ", 显示碎片 "+temp.getTag() + " visible = "+ temp.isVisible() );
//                if (!temp.isVisible()){
                    ft = fm.beginTransaction();
                    ft.show(temp);
                    ft.commit();
//                    while (!temp.isVisible());
//                }

                //寻找组碎片
                temp = findGroupFragment(temp);
                pageAttr = spaFragmentPageHolder.getPage(SpaFragmentRegisterAttribute.parseFragmentTag(temp.getTag()));
                spaFragmentPageHolder.setCurrentGroupPage(pageAttr);
                SpaPrt.print(this+ ", 设置当前界面显示的组碎片 "+ pageAttr.getTagName());

            }
        }
    }



    /**查询组fragment*/
    private SpaBaseFragment findGroupFragment(SpaBaseFragment fragment){
        if (fragment.getPrev()!=null){
            return findGroupFragment(fragment.getPrev());
        }
        return fragment;
    }


    private static class Holder{
        private static SpaManage INSTANCE = new SpaManage();
    }
    public static SpaManage getInstance() {
        return Holder.INSTANCE;
    }

    public SpaManage(SpaFragmentOperationInterface spaFragmentOperation) {}

}
