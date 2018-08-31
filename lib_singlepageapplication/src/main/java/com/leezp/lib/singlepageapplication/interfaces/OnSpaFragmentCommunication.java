package com.leezp.lib.singlepageapplication.interfaces;

import com.leezp.lib.singlepageapplication.base.SpaBaseMessage;

/**
 * Created by Leeping on 2018/4/16.
 * email: 793065165@qq.com
 * 传递消息
 */

public interface OnSpaFragmentCommunication {
    /**
     * fragment发送消息到activity 或者 一个可以对消息做出处理的实现类
     * 返回 true 则被拦截-处理
     * */
    boolean sendSpaMessage(SpaBaseMessage sbMsg);

}
