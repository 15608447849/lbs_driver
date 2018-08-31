package com.leezp.lib.util;

import android.app.Activity;

import java.util.List;

import cn.qqtheme.framework.picker.DatePicker;
import cn.qqtheme.framework.picker.SinglePicker;


/**
 * Created by Leeping on 2018/8/18.
 * email: 793065165@qq.com
 */

public class PickerUtil {

    public interface DateCallback {
        void callback(String year, String month, String day);
    }

    public interface ItemCallback{
        void callback(int pos,String item);
    }
    /**
     *
     * @param activity
     * @param start  起点 年月日
     * @param end  终点 年月日
     * @param select 选择时间 年月日
     * @param callback
     */
    public static final void  alertDatePickerYMD(Activity activity, int[] start, int[] end, int[] select, final DateCallback callback){
        DatePicker picker = new DatePicker(activity);

        picker.setRangeStart(start[0],start[1],start[2]);

        picker.setRangeEnd(end[0],end[1],end[2]);

        picker.setSelectedItem(select[0],select[1],select[2]);

        picker.setCanceledOnTouchOutside(true);
//        picker.setDividerVisible(false);
        picker.setOffset(4);
        picker.setResetWhileWheel(false);
        picker.setOnDatePickListener(new DatePicker.OnYearMonthDayPickListener(){
            @Override
            public void onDatePicked(String year, String month, String day) {
                if (callback!=null) callback.callback(year,month,day);
            }
        });
        picker.show();
    }
    public static final void  alertDatePickerY(Activity activity, int startYear, int endYear, int selectYear, final DateCallback callback){
        DatePicker picker = new DatePicker(activity);

        picker.setRange(startYear,endYear);

        picker.setSelectedItem(selectYear,1,1);

        picker.setCanceledOnTouchOutside(true);
//        picker.setDividerVisible(false);
        picker.setOffset(4);
        picker.setResetWhileWheel(false);
        picker.setOnDatePickListener(new DatePicker.OnYearMonthDayPickListener(){
            @Override
            public void onDatePicked(String year, String month, String day) {
                if (callback!=null) callback.callback(year,month,day);
            }
        });
        picker.show();
    }

    public static final void alertSinglePicker(Activity activity,List<String> items,String select,final ItemCallback callback){
        SinglePicker<String> singlePicker = new SinglePicker<>(activity,items);
        singlePicker.setSelectedItem(select);
        singlePicker.setOnItemPickListener(new SinglePicker.OnItemPickListener<String>() {
            @Override
            public void onItemPicked(int index, String item) {
              if (callback!=null) callback.callback(index,item);
            }
        });
        singlePicker.show();
    }

}
