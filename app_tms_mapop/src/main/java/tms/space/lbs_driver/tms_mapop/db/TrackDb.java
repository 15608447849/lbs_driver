package tms.space.lbs_driver.tms_mapop.db;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.leezp.lib_log.LLog;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import tms.space.lbs_driver.tms_mapop.entity.TrackDbBean;


/**
 * Created by Leeping on 2018/7/20.
 * email: 793065165@qq.com
 */

public class TrackDb extends SQLiteOpenHelper {

    public TrackDb(Context context) {
        super(context, TrackDbInfo.dbName, null, TrackDbInfo.version);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TrackDbInfo.sql_createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        if (oldVersion<TrackDbInfo.version){
            db.execSQL(TrackDbInfo.sql_deleteTable);
            db.execSQL(TrackDbInfo.sql_createTable);
        }
    }

    private int executeSqlWrite(String sql){
        int result = 1;
        try {
            getWritableDatabase().execSQL(sql);
            result =  0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
//        LLog.print("SQL写入: "+ sql+"\n result = "+ result);
        return result;
    }
    private Cursor executeSqlRead(String sql){
        return executeSqlRead(sql,null);
    }
    private Cursor executeSqlRead(String sql,String[] params){
        try {
            if (params==null) params = new String[]{};
            Cursor cursor =  getReadableDatabase().rawQuery(sql,params);
            if (cursor.getCount()>0) return cursor;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public int inset(TrackDbBean bean){
        //查询是否存在相同orderId
        TrackDbBean temp = queryByOrder(bean.getOrderId());
        if (temp!=null) return -1;
        String sql = String.format(Locale.getDefault(),
                TrackDbInfo.sql_inset,
                bean.getOrderId(),
                bean.getUserId(),
                bean.getEnterpriseId());
        return executeSqlWrite(sql);
    }

    public int updateState(long orderId,int state){
        TrackDbBean temp = queryByOrder(orderId);
        if (temp==null) return -1;
        String sql = String.format(Locale.getDefault(),
                TrackDbInfo.sql_update_state,
                state,
                orderId
                );
        return executeSqlWrite(sql);
    };

    public int updateTrack(TrackDbBean bean){
        String sql = String.format(Locale.getDefault(),
                TrackDbInfo.sql_update_track,
                bean.getTrack(),
                bean.gettCode(),
                bean.getOrderId());
        return executeSqlWrite(sql);
    };

    public void updateCorrect(TrackDbBean bean) {
        String sql = String.format(Locale.getDefault(),
                TrackDbInfo.sql_update_correct,
                bean.getCorrect(),
                bean.getcCode(),
                bean.getId());
        executeSqlWrite(sql);;
    }

    public void updateTransfer(TrackDbBean bean) {
        String sql = String.format(Locale.getDefault(),
                TrackDbInfo.sql_update_transfer,
                bean.getlCode(),
                bean.getId());
        executeSqlWrite(sql);;
    }

    public synchronized List<TrackDbBean> queryAll(){

        List<TrackDbBean> list = new ArrayList<>();
        Cursor cursor = executeSqlRead(TrackDbInfo.sql_query_orderId_all);
        if (cursor!=null){
            while (cursor.moveToNext()) {
                list.add(bind(cursor));
            }
        }
        return list;
    }

    public TrackDbBean queryByOrder(long orderId){
        Cursor cursor = executeSqlRead(TrackDbInfo.sql_query_orderId_target,new String[]{String.valueOf(orderId)});
        if (cursor!=null){
            if (cursor.moveToFirst()) {
                return bind(cursor);
            }
        }
        return null;
    }

    private TrackDbBean bind(Cursor cursor) {
        return new TrackDbBean(
                cursor.getInt(cursor.getColumnIndex(TrackDbInfo.id)),
                cursor.getLong(cursor.getColumnIndex(TrackDbInfo.orderId)),
                cursor.getInt(cursor.getColumnIndex(TrackDbInfo.userId)),
                cursor.getInt(cursor.getColumnIndex(TrackDbInfo.enterpriseId)),
                cursor.getString(cursor.getColumnIndex(TrackDbInfo.track)),
                cursor.getString(cursor.getColumnIndex(TrackDbInfo.correct)),
                cursor.getInt(cursor.getColumnIndex(TrackDbInfo.state)),
                cursor.getInt(cursor.getColumnIndex(TrackDbInfo.tCode)),
                cursor.getInt(cursor.getColumnIndex(TrackDbInfo.cCode)),
                cursor.getInt(cursor.getColumnIndex(TrackDbInfo.lCode))
        );
    }


    public int deleteTrack(int id) {
        String sql = String.format(Locale.getDefault(),TrackDbInfo.sql_del,id);
        return executeSqlWrite(sql);
    }
}
