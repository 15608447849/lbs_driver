package com.leezp.lib.singlepageapplication.interfaces;

import com.leezp.lib.singlepageapplication.base.SpaBaseFragment;
import com.leezp.lib.singlepageapplication.use.SpaFragmentPageHolder;
import com.leezp.lib.singlepageapplication.use.SpaFragmentRegisterAttribute;

/**
 * Created by Leeping on 2018/4/15.
 * email: 793065165@qq.com
 */

public interface SpaFragmentOperationInterface {
    /** 查询一个fragment对象*/
    SpaBaseFragment queryFragmentByTag(SpaFragmentPageHolder pageHolder, SpaFragmentRegisterAttribute gAttribute);

    /**检测栈顶是否与目标相同*/
    boolean checkTargetIsStackTop(SpaFragmentPageHolder pageHolder, SpaFragmentRegisterAttribute gAttribute);

    /**
     * 1.创建添加显示组fragment
     * 2.如果存在,显示组或者栈顶fragment
     */
    boolean showGroupFragment(SpaFragmentPageHolder pageHolder, SpaFragmentRegisterAttribute gAttribute);

    /**
     * 向组fragment添加下一个fragment并显示(添加进回退栈操作)
     */
    void showGroupFragmentByOnlyStackTop(SpaFragmentPageHolder pageHolder,SpaFragmentRegisterAttribute gAttribute,SpaFragmentRegisterAttribute cAttribute);



    /**
     * 1.隐藏组或栈顶fragment
     */
    boolean hindGroupFragment(SpaFragmentPageHolder pageHolder, SpaFragmentRegisterAttribute gAttribute);

    /**
     * 1.移除组fragment,包括此组栈中所有fragment
     */
     void removeGroupFragment(SpaFragmentPageHolder pageHolder, SpaFragmentRegisterAttribute gAttribute);

    /**
     *
     * 1.如果存在栈,移除指定组存在的一个栈顶fragment,显示上一个栈顶(回退操作)
     */
    void removeGroupFragmentByOnlyStackTop(SpaFragmentPageHolder pageHolder,SpaFragmentRegisterAttribute attribute);



    /**
     * 移除当前活动的所有fragment
     * */
    void removeGroupFragmentByActiveAll(SpaFragmentPageHolder pageHolder);




}

