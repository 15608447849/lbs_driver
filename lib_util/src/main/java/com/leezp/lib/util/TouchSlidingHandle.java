package com.leezp.lib.util;

import android.view.MotionEvent;

/**
 * Created by Leeping on 2018/8/23.
 * email: 793065165@qq.com
 */

public class TouchSlidingHandle {

    public interface Callback {
        boolean  leftSlide();
        boolean  rightSlide();
        boolean  topSlide();
        boolean  bottomSlide();
    }


    //手指按下的点为(downX, downY)
    private float downX = 0;
    private float downY = 0;

    //手指离开屏幕的点为(upX, upY)
    private float upX = 0;
    private float upY = 0;

    private long downTime = 0L;

    //滑动距离判断标准
    private final int sliding = 210;
    private final int sTime = 110;


    private void reset(){
        downX = 0;
        downY = 0;
        upX = 0;
        upY = 0;
        downTime = 0;
    }

    public boolean onTouchEvent(MotionEvent event,TouchSlidingHandle.Callback callback) {

        if (callback==null) return false;
        int action = event.getAction();
        if(action == MotionEvent.ACTION_DOWN) {
            reset();
            //当手指按下的时候
            downX = event.getX();
            downY = event.getY();
            downTime = System.currentTimeMillis();
//            Log.i("onTouchEvent","按下:" +downX+" "+downY);
        }
        if(event.getAction() == MotionEvent.ACTION_UP) {
            //当手指离开的时候
            upX = event.getX();
            upY = event.getY();

            long cTime = System.currentTimeMillis() - downTime;

//            Log.i("onTouchEvent","抬起:" +upX+" "+upY+" 时间间隔:"+ cTime );
            if ( cTime > sTime ) return false;

//            Log.i("onTouchEvent","X移动:" + Math.abs(upX-downX) + " Y移动"+Math.abs(upY-downY) +" , 标准:"+ this.sliding);


            if(downY - upY > sliding) {
                //向上滑
//                Log.i("onTouchEvent","上滑");
                return callback.topSlide();
            } else if(upY - downY > sliding) {
                //向下滑
//                Log.i("onTouchEvent","下滑");
                return callback.bottomSlide();
            } else if(downX - upX > sliding) {
                //向左滑
//                Log.i("onTouchEvent","左滑");
                return callback.leftSlide();
            } else if(upX - downX > sliding) {
                //向右滑
//                Log.i("onTouchEvent","右滑");
                return callback.rightSlide();
            }
        }
        return false;
    }



}
