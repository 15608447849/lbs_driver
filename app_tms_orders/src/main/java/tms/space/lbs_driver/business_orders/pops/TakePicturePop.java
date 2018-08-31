package tms.space.lbs_driver.business_orders.pops;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import java.io.Closeable;
import java.io.IOException;

import tms.space.lbs_driver.business_orders.R;

/**
 * Created by Leeping on 2018/7/28.
 * email: 793065165@qq.com
 */

public final class TakePicturePop implements Closeable{

    public interface Callback{
        void onTakePicture();
        void onFromAlbum();
    }

    private Callback callback;
    private PopupWindow window;
    private View rootView;

    public TakePicturePop(View rootView,Callback callback) {
        this.rootView = rootView;
        this.callback = callback;
        create();
    }
    @Override
    public void close() throws IOException {
        this.window.dismiss();
        this.window = null;
        this.rootView = null;
        this.callback = null;
    }

    //创建
    private void create() {
        LayoutInflater inflater = (LayoutInflater)rootView.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.pop_take_picture, null);

         view.findViewById(R.id.pop_take_picture_btn_take).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
              window.dismiss();
              callback.onTakePicture();
            }
        });

        view.findViewById(R.id.pop_take_picture_btn_album).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
                callback.onFromAlbum();
            }
        });

        //第一个参数是用于PopupWindow中的View，第二个参数是PopupWindow的宽度，
        // 第三个参数是PopupWindow的高度，第四个参数指定PopupWindow能否获得焦点
        // 设置popWindow弹出窗体可点击，这句话必须添加，并且是true
        window = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT,true);

        // 实例化一个ColorDrawable颜色为半透明
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // 设置PopupWindow是否能响应外部点击事件
        window.setOutsideTouchable(true);
        // 设置PopupWindow是否能响应点击事件
        window.setTouchable(true);
        //设置popWindow的显示和消失动画
        window.setAnimationStyle(R.style.mypopwindow_anim_style);
    }

    /**
        第一个参数是PopupWindow的锚点，第二和第三个参数分别是PopupWindow相对锚点的x、y偏移
        window.showAsDropDown(anchor, xoff, yoff);
        或者也可以调用此方法显示PopupWindow，其中：
        第一个参数是PopupWindow的父View，第二个参数是PopupWindow相对父View的位置，
        第三和第四个参数分别是PopupWindow相对父View的x、y偏移在底部显示
     * */
    public void show(){
        window.showAtLocation(rootView, Gravity.BOTTOM, 0, 0);
    }

    public void hide(){
// 在底部显示
        window.dismiss();
    }


}
