package tms.space.lbs_driver.tms_base.business.ice;

import com.google.gson.reflect.TypeToken;
import com.hsf.framework.api.driver.DriverCompInfo;
import com.hsf.framework.api.driver.DriverServicePrx;
import com.hsf.framework.api.driver.OrderComplex;
import com.hsf.framework.api.driver.OrderInfo;
import com.hsf.framework.api.driver.sCLAIM;
import com.hsf.framework.api.driver.sSIGN;
import com.hsf.framework.api.driver.sUnload;
import com.hsf.framework.api.driver.scanTag;
import com.leezp.lib.util.HttpUtil;
import com.leezp.lib.util.JsonUti;
import com.leezp.lib.util.StrUtil;
import com.leezp.lib.util.TimeUtil;
import com.leezp.lib.zerocice.IceIo;
import com.leezp.lib.zerocice.IceServerAbs;
import com.leezp.lib_gdmap.GdMapUtils;
import com.leezp.lib_log.LLog;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import tms.space.lbs_driver.tms_base.beans.DriverOrderInfo;
import tms.space.lbs_driver.tms_base.business.contracts.OrderDetailContract;
import tms.space.lbs_driver.tms_base.business.contracts.OrderListContract;
import tms.space.lbs_driver.tms_base.business.contracts.ScanQrContract;

/**
 * Created by Leeping on 2018/7/26.
 * email: 793065165@qq.com
 *
 */
public class OrderIceModel extends IceServerAbs<DriverServicePrx> implements OrderListContract.Model, OrderDetailContract.Model,ScanQrContract.Model {


    public OrderIceModel(){
        initFileServer();
    }

    //获取文件服务器地址
    private void initFileServer(){
        try {
           String fileServerFlag = getParam("fileServerFlag");
           if (fileServerFlag==null){
               String json = getProxy().getFileServer();
               LLog.print("文件服务信息: "+json);
               Map<String,String> map = JsonUti.jsonToJavaBean(json,new TypeToken<Map<String,String>>(){}.getType());
               assert map!=null;
               IceIo.get().addParams("fileServerUpload", map.get("fileUpload"));
               IceIo.get().addParams("fileServerDownload", map.get("fileDownload"));
           }
        }catch (Exception e){
            //e.printStackTrace();
        }
    }

    //查询订单详情
    @Override
    public OrderComplex queryOrderInfo(int userid,int enterpriseid,long orderid) {
        try {
            printParam("查询订单详情",userid,enterpriseid,orderid);
            OrderComplex complex = getProxy().driverQueryOrderInfo(userid,enterpriseid,orderid);
            if (complex.isValid){
                //printParam(ReflectionTool.reflectionObjectFields(complex));
                return complex;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //查询订单状态
    @Override
    public int queryOrderState(int enterpriseid,long orderid) {
        try {
            printParam("查询订单状态",enterpriseid,orderid);
            return getProxy().driverQueryOrderState(enterpriseid,orderid);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    //改变订单状态
    @Override
    public boolean changeOrderState(int userid, int enterpriseid,long orderid, int state) {
        try {
            printParam("改变订单状态",userid,enterpriseid,orderid,state);
            return getProxy().driverOrderStateOp(userid,enterpriseid,orderid,state);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    //添加订单走货文本
    @Override
    public void addStackText(String name,String phone,int userid, int enterpriseid, long orderid, int state) {
        try {
            String address = GdMapUtils.get().getCurrentLocationDesc();
            String stateStr = "无法记录的状态操作(code:"+state+")";
            if (state == sCLAIM.value) {
                address = StrUtil.format("司机(%s-%s)在%s成功取货", name,phone,address);
                stateStr = "取货完成";
            } else if (state == sSIGN.value) {
                address = StrUtil.format("司机(%s-%s)在%s成功签收",name,phone , address);
                stateStr = "签收完成";
            } else if (state == sUnload.value) {
                address = StrUtil.format("司机(%s-%s)在%s将货放入仓库,卸载完成",name,phone, address);
                stateStr = "中转卸货完成";
            }

            Map<String, String> map = new HashMap<>();
            String date = TimeUtil.formatUTCByCurrent("yyyy-MM-dd");
            String time = TimeUtil.formatUTCByCurrent(" HH:mm:ss");
            map.put("date", date);
            map.put("time", time);
            map.put("des", address);
            map.put("status", stateStr);
            String json = JsonUti.javaBeanToJson(map);
            printParam("添加走货信息: "+ json);
            getProxy().driverUploadNode(userid,enterpriseid,orderid,json);
        } catch (Exception e) {
             e.printStackTrace();
        }
    }

    //修改订单价格
    @Override
    public boolean updateOrderFee( int enterpriseid, long orderid, double fee) {
        try {
            printParam("修改订单价格", enterpriseid,orderid,fee);
            return getProxy().driverSureOrderFee(enterpriseid,orderid,fee);
        } catch (Exception e){
             e.printStackTrace();
        }
        return false;
    }

    // 取货/签收 图片上传
    @Override
    public void uploadImage(int userid, int enterpriseid, long orderid,int state, List<File> files, HttpUtil.Callback callback) {
        String url = getParam("fileServerUpload");
        printParam("上传文件",files.size(),url);
        List<HttpUtil.FormItem> formItems = new ArrayList<>();
        for (int i = 0; i < files.size() ;i++){
            formItems.add(genImageUploadForm(userid,enterpriseid,orderid,state,files,i));
        }
        HttpUtil.Request request = new HttpUtil.Request(url, HttpUtil.Request.POST, callback);
        request.setForm().addFormItemList(formItems).upload().execute();
    }

    private HttpUtil.FormItem genImageUploadForm(int userid, int enterpriseid, long orderid, int state, List<File> files, int i) {
        HashMap<String,String> map = new HashMap<>();
            map.put("orderid",orderid+"");
            map.put("compid",enterpriseid+"");
            map.put("picno", state+"_"+i);
        return new HttpUtil.FormItem("file",
                        JsonUti.javaBeanToJson(map),
                        files.get(i));
    }

    //取货/签收 文件图片地址
    @Override
    public String imagePath(int enterpriseid, long orderid) {
        String filePath = "";
        try {
            filePath = getProxy().getUploadPath(enterpriseid+"",orderid+"");

        } catch (Exception e) {
             e.printStackTrace();
        }
        filePath = getParam("fileServerDownload")+"/"+filePath+"/";
        printParam("获取图片后台存放的路径",enterpriseid,orderid,filePath);
        return filePath;
    }

    //请求用户所属企业
    @Override
    public DriverCompInfo[] queryComp(int userid) {
        try {
            printParam("请求企业",userid);
            DriverCompInfo[] array = getProxy().driverQueryCompList(userid);
            if (array.length>0){
                return array;
            }
        } catch (Exception e) {
             e.printStackTrace();
        }
        return null;
    }

    //获取后台订单数据列表
    @Override
    public OrderInfo[] queryOrderList(int userid, int enterpriseid, int state,int year,int index,int range) {
        try {
            printParam("请求订单列表",userid,enterpriseid,state,year,index,range);
            OrderInfo[] arr = getProxy().driverQueryOrderList(userid,enterpriseid,state,year,index,range);
            if (arr.length>0){
               return arr;
            }
        } catch (Exception e) {
             e.printStackTrace();
        }
        return null;
    }

    /**
     * 处理二维码
     */
    @Override
    public boolean identificationCode(DriverOrderInfo info , String code) {

        try {
            printParam("处理二维码",code);
            //fspace#企业号#订单号#状态码
            String[] result = code.split("#");
            if (!result[0].equals(scanTag.value)) return false;

                int enterpriseid = Integer.parseInt(result[1]);
                long orderid = Long.parseLong(result[2]);
                int state = Integer.parseInt(result[3]);
                int userid = info.getUser().getUserCode();

                //获取企业列表
                DriverCompInfo[] comps = queryComp(userid);
                if (comps.length == 0) return false;
                OrderInfo order;
                //判断企业列表
                for (DriverCompInfo comp : comps){
                    if (comp.compid == enterpriseid){

                        //查询一个订单详情
                        order = new OrderInfo();
                        order.orderNo = orderid;

                        //查询当前订单状态
                        order.state = queryOrderState(enterpriseid,order.orderNo);
                        if (order.state != state){
                            return false;
                        }
                        //查询详情
                        OrderComplex complex = queryOrderInfo(userid,enterpriseid,order.orderNo);
                        if (complex == null){
                            return false;
                        }
                        order.complex = complex;
                        info.setComp(comp); //设置公司
                        info.setInfo(order);//设置订单
                        return true;
                    }
                }

        } catch (Exception e) {
             e.printStackTrace();
        }
        return false;
    }
}

