package tms.space.lbs_driver.business_orders.interfaces;

import android.view.View;
import android.widget.Button;

import tms.space.lbs_driver.tms_base.beans.IViewOnClickAction;

/**
 * Created by Leeping on 2018/8/1.
 * email: 793065165@qq.com
 创建按钮和状态绑定
 状态与状态逻辑关联
 设置状态管理器保存所有状态对象
 设置状态管理器当前状态,通知所有状态对象(观察者模式) 比如, 当前为取货状态1 , 则 '取货状态对象' 收到1时,显示其对应的按钮,当被按下时,会被触发 按下的动作,操作成功, 由它通知状态管理器下一个状态码,管理器将重复以上动作

 */

public abstract class OrderDetailButtonStateAbs extends IViewOnClickAction {
    public Button button;
    public final int state;

    public OrderDetailButtonStateAbs(Button button, int state) {
        super(button);
        this.button = button;
        this.state = state;
    }

    public void onState(int state){
        if (this.state == state) button.setVisibility(View.VISIBLE);
        else button.setVisibility(View.GONE);
    }
}
