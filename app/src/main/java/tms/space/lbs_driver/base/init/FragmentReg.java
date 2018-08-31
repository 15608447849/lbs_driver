package tms.space.lbs_driver.base.init;


import com.leezp.lib.singlepageapplication.use.SpaRegisterCentre;

import tms.space.lbs_driver.business_orders.fragments.OrderClaimFragment;
import tms.space.lbs_driver.business_orders.fragments.OrderFeeFragment;
import tms.space.lbs_driver.business_orders.fragments.OrderDetailFragment;
import tms.space.lbs_driver.business_orders.fragments.TakePictureFragment;
import tms.space.lbs_driver.business_orders.fragments.OrderListFragment;
import tms.space.lbs_driver.tms_required.business_login.LoginFragment;
import tms.space.lbs_driver.tms_required.business_person.ChangePasswordFragment;
import tms.space.lbs_driver.tms_required.business_person.PersonalFragment;
import tms.space.lbs_driver.tms_required.head_bottom.BottomMenuFragment;
import tms.space.lbs_driver.tms_required.head_bottom.HeadFragment;
import tms.space.lbs_driver.tms_scanqr.ScanQrFragment;

/**
 * Created by Leeping on 2018/6/28.
 * email: 793065165@qq.com
 * fragment注册中心
 */

public class FragmentReg {
    public static void init(){
        //底部tag
        SpaRegisterCentre.register("bottom",BottomMenuFragment.class,"bottom");
        //头部元素
        SpaRegisterCentre.register("head", HeadFragment.class,"head");
        //登陆
        SpaRegisterCentre.register("content",LoginFragment.class,"login");
        //扫码
        SpaRegisterCentre.register("content",ScanQrFragment.class,"scan");
        //个人中心
        SpaRegisterCentre.register("content",PersonalFragment.class,"personal");
        //修改密码
        SpaRegisterCentre.register("content",ChangePasswordFragment.class,"change_password");
        //订单
        SpaRegisterCentre.register("content",OrderListFragment.class,"order");
        //订单详情
        SpaRegisterCentre.register("content",OrderDetailFragment.class,"order_detail");
        //取货
        SpaRegisterCentre.register("content",OrderClaimFragment.class,"order_claim");
        //拍照
        SpaRegisterCentre.register("content",TakePictureFragment.class,"take_picture");
        //确认收款
        SpaRegisterCentre.register("content",OrderFeeFragment.class,"order_fee");

    }

}
