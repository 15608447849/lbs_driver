package tms.space.lbs_driver.tms_mapop.display;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import tms.space.lbs_driver.tms_base.activitys.BaseActivityAbs;
import tms.space.lbs_driver.tms_mapop.R;


/**
 * Created by Leeping on 2018/8/17.
 * email: 793065165@qq.com
 */

public class MapActivity extends BaseActivityAbs implements MapControl.Callback {

    private com.amap.api.maps.MapView gdMapView;
    private TextView textView;
    private MapControl mapControl;
//    private MapLbs lbs;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_map);
        gdMapView = $(R.id.act_map_gdmapview);
        textView = $(R.id.act_map_tv);
        gdMapView.onCreate(savedInstanceState);
        mapControl = new MapControl(gdMapView,this);
//        lbs = new MapLbs(this,gdMapView.getMap());
    }

    @Override
    protected void onResume() {
        super.onResume();
        gdMapView.onResume();
        mapControl.launch();
//        lbs.launch();
    }
    @Override
    public void onLowMemory() {
        gdMapView.onLowMemory();
        super.onLowMemory();
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        gdMapView.onSaveInstanceState(outState);
    }
    @Override
    protected void onPause() {
        super.onPause();
        mapControl.unLaunch();
    }

    @Override
    protected void onDestroy() {
        mapControl.kill();
//        lbs.unLaunch();
        gdMapView.onDestroy();
        super.onDestroy();

    }

    @Override
    public void clearLineInfo() {
        $ui(new Runnable() {
            @Override
            public void run() {
                textView.setText("");
            }
        });
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void setLineInfo(final int id, final int pointSize,final int correctSize) {
        $ui(new Runnable() {
            @Override
            public void run() {
                String text = textView.getText().toString();
                textView.setText(text + "\n"+ "ID="+id+" GPS坐标点数量:"+pointSize+" 已纠正坐标数量:"+correctSize);
            }
        });

    }
}
