package tms.space.lbs_driver.tms_mapop.display;

import android.content.Context;
import android.graphics.Color;

import com.amap.api.maps.AMap;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Polyline;
import com.amap.api.maps.model.PolylineOptions;
import com.amap.api.trace.LBSTraceClient;
import com.amap.api.trace.TraceLocation;
import com.amap.api.trace.TraceStatusListener;
import com.leezp.lib_gdmap.GdMapUtils;
import com.leezp.lib_log.LLog;

import java.util.List;

/**
 * Created by Leeping on 2018/9/12.
 * email: 793065165@qq.com
 */
public class MapLbs implements TraceStatusListener {

    private LBSTraceClient lbsTraceClient;
    private Polyline pathCor;
    private Polyline pathLoc;

    public MapLbs(Context context, AMap aMap) {
        lbsTraceClient = LBSTraceClient.getInstance(context);
        pathLoc = aMap.addPolyline(new PolylineOptions().width(5).color(Color.BLUE));
        pathCor = aMap.addPolyline(new PolylineOptions().width(5).color(Color.YELLOW));
    }

    @Override
    public void onTraceStatus(List<TraceLocation> locations, List<LatLng> rectifications, String errorInfo) {
        //locations 定位得到的轨迹点集，rectifications 纠偏后的点集，errorInfo 轨迹纠偏错误信息

        if (errorInfo!=null){
            LLog.print(errorInfo);
        }
        if (locations!=null){
            pathLoc.setPoints(GdMapUtils.get().convertTracePointToLatLng(locations));
        }
       if (rectifications!=null){
           pathCor.setPoints(rectifications);
       }

    }

    public void launch() {
        //开始记录轨迹，每2s记录一次轨迹，每隔5个点合并请求一次纠偏并回调
        lbsTraceClient.startTrace(this); //开始采集,需要传入一个状态回调监听
    }

    public void unLaunch() {
        lbsTraceClient.stopTrace();//在不需要轨迹纠偏时（如行程结束），可调用此接口结束纠偏
    }
}
