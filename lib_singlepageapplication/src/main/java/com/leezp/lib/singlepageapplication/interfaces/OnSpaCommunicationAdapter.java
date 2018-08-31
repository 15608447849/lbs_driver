package com.leezp.lib.singlepageapplication.interfaces;

import com.leezp.lib.singlepageapplication.base.SpaBaseMessage;

/**
 * Created by Leeping on 2018/4/18.
 * email: 793065165@qq.com
 */

public abstract class OnSpaCommunicationAdapter implements OnSpaFragmentCommunication {
    @Override
    public boolean sendSpaMessage(SpaBaseMessage sbMsg) {
        return false;
    }
}
