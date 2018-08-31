package com.leezp.lib.singlepageapplication.imps;

import com.leezp.lib.singlepageapplication.base.SpaBaseHandle;
import com.leezp.lib.singlepageapplication.base.SpaBaseMessage;
import com.leezp.lib.singlepageapplication.interfaces.OnSpaFragmentCommunication;
import com.leezp.lib.singlepageapplication.interfaces.StateManage;

/**
 * Created by Leeping on 2018/6/29.
 * email: 793065165@qq.com
 */

public final class SpaCommunicationDefaultImp extends StateManage<SpaBaseHandle,SpaBaseMessage> implements OnSpaFragmentCommunication {


    public SpaCommunicationDefaultImp(SpaBaseHandle handle) {
        super(handle);
    }

    @Override
    protected void init() {
        add(new InvokeImp());
    }

    @Override
    public boolean sendSpaMessage(SpaBaseMessage sbMsg) {
        return select(sbMsg);
    }


}
