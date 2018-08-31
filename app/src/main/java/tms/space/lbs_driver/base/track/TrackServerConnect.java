package tms.space.lbs_driver.base.track;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import tms.space.lbs_driver.tms_mapop.ITrackService;
import tms.space.lbs_driver.tms_mapop.server.TrackTransferService;

import static android.content.Context.BIND_AUTO_CREATE;

/**
 * Created by Leeping on 2018/7/21.
 * email: 793065165@qq.com
 */

public class TrackServerConnect  implements ServiceConnection {
    //绑定远程轨迹服务进程
    private ITrackService stub;

    public void bindService(Context context) {
        Intent intent = new Intent(context,TrackTransferService.class);
        context.bindService(intent,this,BIND_AUTO_CREATE);
    }

    public void unbindService(Context context){
        context.unbindService(this);
    }

    public ITrackService getStub() {
        return stub;
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        stub = ITrackService.Stub.asInterface(service);
        if (stub == null){
            throw new RuntimeException("Service binding failed");
        }
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        stub = null;
    }

    @Override
    public void onBindingDied(ComponentName name) {
        stub = null;
    }

}
