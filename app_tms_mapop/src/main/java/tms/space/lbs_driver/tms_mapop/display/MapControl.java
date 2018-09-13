package tms.space.lbs_driver.tms_mapop.display;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.location.Location;

import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapOptions;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.Polyline;
import com.amap.api.maps.model.PolylineOptions;
import com.amap.api.trace.TraceLocation;
import com.google.gson.reflect.TypeToken;
import com.leezp.lib.util.JsonUti;
import com.leezp.lib.util.StrUtil;
import com.leezp.lib_gdmap.GdMapUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import tms.space.lbs_driver.tms_mapop.db.TrackDb;
import tms.space.lbs_driver.tms_mapop.entity.TrackDbBean;

/**
 * Created by Leeping on 2018/8/17.
 * email: 793065165@qq.com
 */

class MapControl extends Thread implements AMap.OnMyLocationChangeListener {

    public interface Callback {
        void clearLineInfo();
        void setLineInfo(int id, int pointSize,int correctSize);
    }

    private TrackDb db;
    private Callback mapCallback;
    private Context context;
    private AMap aMap;
    private volatile boolean isStop = false;
    private boolean isLaunch = false;

    //是否第一次打开
    private boolean isFirst = true;
    //原始轨迹
    @SuppressLint("UseSparseArrays")
    private HashMap<Integer,Polyline> lineMap = new HashMap<>();
    //纠偏轨迹
    @SuppressLint("UseSparseArrays")
    private HashMap<Integer,Polyline> lineMap2 = new HashMap<>();

    MapControl(MapView mapView,Callback mapCallback) {
        this.aMap = mapView.getMap();
        this.context = mapView.getContext();
        this.mapCallback = mapCallback;
        this.db = new TrackDb(this.context);
        initMap();

        this.setDaemon(true);
        this.setName("地图控制器线程");
        this.start();
    }

    private void initMap() {
        UiSettings uiSettings = aMap.getUiSettings();
            uiSettings.setScaleControlsEnabled(true);
            uiSettings.setMyLocationButtonEnabled(true);//显示默认的定位按钮
            uiSettings.setLogoPosition(AMapOptions.LOGO_MARGIN_LEFT); //高德地图图标位置

        aMap.setMapType(AMap.MAP_TYPE_NORMAL);
        aMap.showBuildings(true);
        aMap.moveCamera(CameraUpdateFactory.zoomTo(18.0f));
        aMap.showIndoorMap(true); //显示室内地图
        aMap.setMyLocationStyle(new MyLocationStyle().myLocationType(MyLocationStyle.LOCATION_TYPE_MAP_ROTATE_NO_CENTER));
        aMap.setOnCameraChangeListener(new AMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {}

            @Override
            public void onCameraChangeFinish(CameraPosition cameraPosition) {
                aMap.moveCamera(CameraUpdateFactory.zoomTo(cameraPosition.zoom));
            }
        });
        aMap.setOnMyLocationChangeListener(this);
        aMap.setMyLocationEnabled(true);//可触发定位并显示当前位置
    }

    public void launch() {
        this.isLaunch = true;
        isFirst = true;
    }

    public void unLaunch() {
        this.isLaunch = false;
    }

    public void kill() {
        this.isStop = true;
    }

    @Override
    public void run() {
        while (!isStop){
            if (isLaunch){
                execute();
            }
            try {Thread.sleep(5000);} catch (InterruptedException ignored) {}
            if (isLaunch){
                clearLine();
            }
        }
    }

    private void clearLine() {
        Iterator<Polyline> iterator = lineMap.values().iterator();
        while (iterator.hasNext()){
            iterator.next().getPoints().clear();
        }

        iterator = lineMap2.values().iterator();
        while (iterator.hasNext()){
            iterator.next().getPoints().clear();
        }
    }

    private void execute() {
        //获取数据库存在的数据
        List<TrackDbBean> list = db.queryAll();

        mapCallback.clearLineInfo();

        for (TrackDbBean bean : list){

            //原始路径
            Polyline line = getLine(bean.getId());
            List<LatLng> path = getLinePath(bean.getTrack());
            showLine(line,path);
            //纠偏路径
            Polyline line2 = getLine2(bean.getId());
            List<LatLng> path2 = getLinePath2(bean.getCorrect());
            showLine(line2,path2);

            mapCallback.setLineInfo(bean.getId(),path.size(),path2.size());
        }

    }

    private Polyline getLine(int id) {
        Polyline line = lineMap.get(id);
        if (line == null) {
            line = createLine(8,Color.BLACK);
            lineMap.put(id,line);
        }
        return line;
    }

    private Polyline getLine2(int id) {
        Polyline line = lineMap2.get(id);
        if (line == null) {
            line = createLine(12,Color.RED);
            lineMap2.put(id,line);
        }
        return line;
    }

    private void showLine(Polyline line,List<LatLng> linePath) {
        line.setPoints(linePath);
    }

    private List<LatLng> getLinePath(String trackJson) {
        if (StrUtil.validate(trackJson)){
            List<TraceLocation> path = JsonUti.jsonToJavaBean(trackJson,new TypeToken<List<TraceLocation>>(){}.getType());
            return GdMapUtils.get().convertTracePointToLatLng(path);
        }
        return new ArrayList<>();
    }
    private List<LatLng> getLinePath2(String correctJson) {
        if (StrUtil.validate(correctJson)){
            return JsonUti.jsonToJavaBean(correctJson,new TypeToken<List<LatLng>>(){}.getType());
        }
        return new ArrayList<>();
    }

    private Polyline createLine(int size,int color) {
        return aMap.addPolyline(new PolylineOptions().width(size).color(color));
    }

    //地图对象的定位回调
    @Override
    public void onMyLocationChange(Location location) {
        toMapCenter(location);
    }

    private void toMapCenter(Location location) {
        //移动到地图中心
        if (isFirst){
            aMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(location.getLatitude(), location.getLongitude())));
            isFirst = false;
        }
    }




}
