package com.leezp.lib.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.TextView;

import java.lang.reflect.Field;

/**
 * Created by Leeping on 2018/4/17.
 * email: 793065165@qq.com
 */

public class DialogUtil {
    public interface Action0 {
        void onAction0();
    }

    public static AlertDialog dialogSimple(Context context, String msg,String buttonText, final Action0 action0) {
        //弹出提示
        return dialogSimple(context,
                msg,
                buttonText,
                0,
                action0);
    }
    public static AlertDialog dialogSimple(Context context, String msg,String buttonText,int token, final Action0 action0) {
        //弹出提示
        return build(context,
                "提示",
                msg,
                R.drawable.ul_ic_info,
                null == buttonText ? "确定" : buttonText,
                null,
                null,
                token,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        if (which == DialogInterface.BUTTON_POSITIVE){
                            if (action0!=null) action0.onAction0();
                        }
                    }
                });
    }

    public static AlertDialog dialogSimple2(Context context, String msg, String sureText,final Action0 sure,String cancelText,final Action0 cancel) {
        //弹出提示
        return build(context,
                "提示",
                msg,
                R.drawable.ul_ic_info,
                sureText==null?"确定":sureText,
                cancelText==null?"取消":cancelText,
                null,
                0,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        if (which == DialogInterface.BUTTON_POSITIVE){
                            sure.onAction0();
                        }else if (which == DialogInterface.BUTTON_NEGATIVE){
                            cancel.onAction0();
                        }
                    }
                });
    }

    public static AlertDialog build(Context context,
                             String title,
                             String message,
                             int iconRid,
                             String positiveText,
                             String negativeText,
                             String neutralText,
                             int token,
                             DialogInterface.OnClickListener listener){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle(title) ;//设置标题
        builder.setMessage(message) ;//设置内容

        builder.setIcon(iconRid);//设置图标，
        if (positiveText!=null){
            builder.setPositiveButton(positiveText,listener);
        }
        if (negativeText!=null){
            builder.setNegativeButton(negativeText,listener);
        }
        if (neutralText!=null){
            builder.setNeutralButton(neutralText,listener);
        }
        builder.setCancelable(false);
        AlertDialog dialog = builder.create();
        if (token!=0){
            dialog.getWindow().setType(token);
        }
        dialog.show();
        return dialog;
    }

    public static void changeSystemDialogColor(AlertDialog dialog ,int titleColor,int contentColor,int positiveColor,int negativeColor,int neutralColor){
        if(dialog == null) return;
        if (titleColor!=0){
            TextView textView = getSystemDialogTextView(dialog ,"mTitleView");
            if (textView!=null) textView.setTextColor(titleColor);
        }
        if (contentColor!=0){
            TextView textView = getSystemDialogTextView(dialog ,"mMessageView");
            if (textView!=null) textView.setTextColor(titleColor);
        }
        if (positiveColor!=0){
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(positiveColor);
        }
        if (negativeColor!=0){
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(negativeColor);
        }
        if (neutralColor!=0){
            dialog.getButton(AlertDialog.BUTTON_NEUTRAL).setTextColor(neutralColor);
        }
    }

    private static TextView getSystemDialogTextView(AlertDialog dialog,String fieldName) {
        try {
            Field mAlert = AlertDialog.class.getDeclaredField("mAlert");
            mAlert.setAccessible(true);
            Object mAlertController = mAlert.get(dialog);
            Field mField = mAlertController.getClass().getDeclaredField(fieldName);
            mField.setAccessible(true);
            return (TextView) mField.get(mAlertController);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }
        return null;
    }



}
