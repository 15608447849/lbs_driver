package tms.space.lbs_driver.tms_base.beans;

import tms.space.lbs_driver.tms_base.storage.JsonLocalSqlStorage;

/**
 * Created by lzp on 2018/3/13.
 * 用户信息
 *
 */

public class DriverUser extends JsonLocalSqlStorage {

    /**
     * 用户码
     */
    private int userCode;

    /**
     * 手机号码
     */
    private String phone;

    /**
     * 司机姓名
     */
    private String name;


    public int getUserCode() {
        return userCode;
    }

    public void setUserCode(int userCode) {
        this.userCode = userCode;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getK() {
        return "司机信息";
    }


}
