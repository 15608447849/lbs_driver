package tms.space.lbs_driver.business_orders.converts;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.View;

import com.hsf.framework.api.driver.OrderComplex;
import com.hsf.framework.api.driver.OrderInfo;
import com.hsf.framework.api.driver.sCLAIM;
import com.hsf.framework.api.driver.sGRAB;
import com.leezp.lib.util.AppUtil;
import com.leezp.lib.util.StrUtil;
import com.leezp.lib_gdmap.GdMapUtils;

import tms.space.lbs_driver.tms_base.business.contracts.OrderDetailContract;

/**
 * Created by Leeping on 2018/8/12.
 * email: 793065165@qq.com
 */

public class OrderDetailConvert {


    StringBuffer sBuff = new StringBuffer();
    int start;
    OrderDetailConvert append(Object str) {
        sBuff.append(str);
        if (start == 0) start = sBuff.length();
        return this;
    }

    String colon(String str){
        int size = 4 - str.length();
        return StrUtil.colon(str,2,size+1);
    }

    private void setSpannableStringStyle(SpannableString span, int s, int e, Object style){
        if (span!=null && style!=null){
            span.setSpan(style,s,e, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        }
    }

    SpannableString _createSpannable(int s,int e,Object... styleList) {
        if (s == -1) s = start;
        if (e == -1) e = sBuff.length();
        SpannableString span = new SpannableString(sBuff.toString());
        if (styleList!=null){
            for (Object style : styleList) setSpannableStringStyle(span,s,e,style);
        }
        reset();
        return span;
    }
    SpannableString createSpannable(Object... styleList) {return _createSpannable(-1,-1,styleList);}



    void reset(){
        sBuff.setLength(0);
        start=0;
    }

    String backColor = "#FFFFFFFF";
    ForegroundColorSpan fontStyle =  new ForegroundColorSpan(0xff000000);
    RelativeSizeSpan contentSize = new RelativeSizeSpan(1.2f);
    RelativeSizeSpan sizeStyle = new RelativeSizeSpan(0.8f);


    //添加地址 - 取货
    void claimAddress(OrderDetailContract.OrderDetailView view, OrderInfo info,final OrderComplex complex) {
        append(colon("取货地址")).append(complex.sAdd);
        ClickableSpan clickableSpan = null;
        if (info.state == sGRAB.value) {
            clickableSpan = new ClickableSpan() {
                @Override
                public void onClick(View widget) {
                    GdMapUtils.get().openNaviToDesAddress(complex.sAdd);
                }
            };
        }
        view.setListItemData(createSpannable(fontStyle,contentSize,clickableSpan),backColor);
    }
    //添加地址 - 送货
    void deliverAddress(OrderDetailContract.OrderDetailView view, OrderInfo info,final OrderComplex complex) {
        append(colon("送达地址")).append(complex.dAdd);
        ClickableSpan clickableSpan = null;
        if (info.state == sCLAIM.value) {
            clickableSpan = new ClickableSpan() {
                @Override
                public void onClick(View widget) {
                    GdMapUtils.get().openNaviToDesAddress(complex.dAdd);
                }
            };
        }
        view.setListItemData(createSpannable(fontStyle,contentSize,clickableSpan),backColor);
    }

    //取货联系电话
    void contactInformation(final Context context,final OrderDetailContract.OrderDetailView view, OrderInfo info, final OrderComplex complex) {
        final String[] phoneArr = complex.contactInfo.split(",");
        ClickableSpan clickableSpan = null;
        String phone = "无";
        if (phoneArr.length>0){
            phone = complex.contactInfo;
            if (info.state == sGRAB.value){
                clickableSpan =  new ClickableSpan(){
                    @Override
                    public void onClick(View widget) {
                        if (phoneArr.length==1){
                            AppUtil.callPhoneNo(context,phoneArr[0]);
                        }else{
                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setTitle("请选择");
                            builder.setItems(phoneArr, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    AppUtil.callPhoneNo(context,phoneArr[which]);
                                }
                            });
                            builder.create().show();
                        }
                    }
                };
            }

        }
        append(colon("联系方式")).append(phone);
        view.setListItemData(createSpannable(fontStyle,contentSize,clickableSpan),backColor);
    }

    //收货人
    void takeInfo(final Context context,final OrderDetailContract.OrderDetailView view, OrderInfo info, final OrderComplex complex) {
        append(colon("收货人")).append(complex.takeInfo);
        ClickableSpan clickableSpan = null;
        if (info.state ==  sCLAIM.value){
            String[] arr = complex.takeInfo.split(",");
            if (arr.length>=2 && arr[1].length()==11) {
                final String phone = arr[1];
                clickableSpan = new ClickableSpan() {
                    @Override
                    public void onClick(View widget) {
                        AppUtil.callPhoneNo(context,phone);
                    }
                };
            }
        }

        view.setListItemData(createSpannable(fontStyle,contentSize,clickableSpan),backColor);
    }

    //支付费用
    void feeInfo(final OrderDetailContract.OrderDetailView view, OrderInfo info, final OrderComplex complex) {


        append(colon("订单费用")).append(complex.payType);
        SpannableString s1 = createSpannable(fontStyle,contentSize);

        append(complex.payFee+StrUtil.placeholder+StrUtil.placeholder+StrUtil.placeholder+StrUtil.placeholder+StrUtil.placeholder);
        SpannableString s2 = _createSpannable(0,complex.payFee.length(),new ForegroundColorSpan(0xffcc0033),contentSize);

        String bg = backColor;
        if (info.state == sGRAB.value) bg = null;
        view.setListItemData(s1,s2,bg);
    }
    //操作时间
    void time(OrderDetailContract.OrderDetailView view, OrderComplex complex) {

        append(colon("发单时间")).append(complex.sendTime);
        view.setListItemData(_createSpannable(2,-1,sizeStyle),null);

        append(colon("抢单时间")).append(complex.robTime);
        view.setListItemData(_createSpannable(2,-1,sizeStyle),null);

        if (StrUtil.validate(complex.claimTime)){
            append(colon("提货时间")).append(complex.claimTime);
            view.setListItemData(_createSpannable(2,-1,sizeStyle),null);
        }
        if (StrUtil.validate(complex.signTime)){
            append(colon("签收时间")).append(complex.signTime);
            view.setListItemData(_createSpannable(2,-1,sizeStyle),null);
        }

        //间隔线
        view.setListItemData(2);
        //间隔线
        view.setListItemData(2);
    }

    void image(String tag,OrderDetailContract.OrderDetailView  view,String urlDir,int state) {
        append(colon(tag));
        view.setListItemData(createSpannable(2,tag.length()+2,fontStyle),backColor);
        String[] urls = new String[3];
        for (int i = 0 ;i < urls.length ; i++){
            urls[i] =urlDir+state+"_"+i;
        }
        view.setListItemData(urls);
    }


    public void convert(Context context,OrderDetailContract.OrderDetailView view, OrderInfo info, OrderComplex complex,String imagePath){

        //订单号
        append(colon("订单号")).append(info.orderNo);
        view.setListItemData(createSpannable(fontStyle,contentSize),backColor);
        //运单号
        append(colon("运单号")).append(complex.freightNo);
        view.setListItemData(createSpannable(fontStyle,contentSize),backColor);

    }
}
