// ITrackServivrInterfaces.aidl
package tms.space.lbs_driver.tms_mapop;

// Declare any non-default types here with import statements

interface ITrackService {

    //添加一条轨迹记录数据
     int addTrack(int userid,long orderId,int enterpriseId);

    //修改一条轨迹记录的状态, 0保持记录 1停止记录
    int updateState(long orderId,int state);

    //获取一条轨迹的当前原始轨迹json
     String getTrack(long orderId);

    //获取一条轨迹的纠偏轨迹json
    String getCorrect(long  orderId);

}
