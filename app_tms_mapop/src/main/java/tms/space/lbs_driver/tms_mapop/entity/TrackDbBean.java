package tms.space.lbs_driver.tms_mapop.entity;

/**
 * Created by Leeping on 2018/7/21.
 * email: 793065165@qq.com
 */

public class TrackDbBean {

    private int id;
    private long orderId;
    private int userId;
    private int enterpriseId;
    private String track;
    private String correct;
    private int state;
    private int tCode;
    private int cCode;
    private int lCode;


    //取出数据库全部记录
    public TrackDbBean(int id, long orderId, int userId, int enterpriseId, String track, String correct, int state, int tCode, int cCode,int lCode) {
        this.id = id;
        this.orderId = orderId;
        this.userId = userId;
        this.enterpriseId = enterpriseId;
        this.track = track;
        this.correct = correct;
        this.state = state;
        this.tCode = tCode;
        this.cCode = cCode;
        this.lCode = lCode;
    }

    //新增一条记录
    public TrackDbBean(long orderId, int userId, int enterpriseId) {
        this(0,orderId,userId,enterpriseId,null,null,0,0,0,0);
    }

    //根据订单号改变状态
    public TrackDbBean(long orderId, int state) {
        this(0,orderId,0,0,null,null,state,0,0,0);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getEnterpriseId() {
        return enterpriseId;
    }

    public void setEnterpriseId(int enterpriseId) {
        this.enterpriseId = enterpriseId;
    }

    public String getTrack() {
        return track;
    }

    public void setTrack(String track) {
        this.track = track;
    }

    public String getCorrect() {
        return correct;
    }

    public void setCorrect(String correct) {
        this.correct = correct;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int gettCode() {
        return tCode;
    }

    public void settCode(int tCode) {
        this.tCode = tCode;
    }

    public int getcCode() {
        return cCode;
    }

    public void setcCode(int cCode) {
        this.cCode = cCode;
    }

    public int getlCode() {
        return lCode;
    }

    public void setlCode(int lCode) {
        this.lCode = lCode;
    }

    @Override
    public String toString() {
        return "TrackDbBean{" +
                "id=" + id +
                ", orderId=" + orderId +
                ", userId=" + userId +
                ", enterpriseId=" + enterpriseId +
                ", track='" + track + '\'' +
                ", correct='" + correct + '\'' +
                ", state=" + state +
                ", tCode=" + tCode +
                ", cCode=" + cCode +
                '}';
    }
}
