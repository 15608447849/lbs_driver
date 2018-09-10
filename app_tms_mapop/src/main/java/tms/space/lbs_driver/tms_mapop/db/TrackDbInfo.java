package tms.space.lbs_driver.tms_mapop.db;

/**
 * Created by Leeping on 2018/7/20.
 * email: 793065165@qq.com
 */

final class TrackDbInfo {
    static final int version = 2;
    static final String dbName = "tms_track.db";
    static final String tableName = "tms_track_table";
    static final String id = "id";
    static final String orderId = "orderId";
    static final String userId = "userId";
    static final String enterpriseId = "enterpriseId";
    static final String track = "track";
    static final String correct = "correct";
    static final String state = "state";
    static final String tCode = "tCode";
    static final String cCode = "cCode";
    static final String lCode = "lCode"; //传输次数

    static final String sql_deleteTable = "drop table "+tableName;

    static final String sql_createTable = "CREATE TABLE IF NOT EXISTS "+tableName+" (" +
            id + " INTEGER PRIMARY KEY AUTOINCREMENT,"+
            orderId+" BIGINT NOT NULL," +
            userId +" BIGINT NOT NULL," +
            enterpriseId+" INT NOT NULL," +
            track+" TEXT," +
            correct+" TEXT," +
            state+" TINYINT NOT NULL," +
            tCode+" TINYINT NOT NULL," +
            cCode+" TINYINT NOT NULL," +
            lCode+" TINYINT NOT NULL" +
            ");";

    //数据库升级版本2
    //static final String sql_updateTable_v2 = "ALTER TABLE "+tableName+" ADD COLUMN "+ lCode+"  TINYINT";

    static final String sql_inset = "INSERT INTO "+tableName+" VALUES (NULL,%d,'%d',%d,'','',0,0,0,0);";

    static final String sql_update_state = "UPDATE "+tableName+" SET "+state+" = %d WHERE "+orderId+" = %d;";

    static final String sql_update_track = "UPDATE "+tableName+" SET "+track+" = '%s', "+tCode+" = %d WHERE "+orderId+" = %d;";

    static final String sql_update_correct = "UPDATE "+tableName+" SET "+correct+" = '%s', "+cCode+" = %d WHERE "+id+" = %d;";

    static final String sql_update_transfer = "UPDATE "+tableName+" SET "+lCode+" = %d WHERE "+id+" = %d;";

    static final String sql_query_orderId_all = "SELECT * FROM "+ tableName+";";

    static final String sql_query_orderId_target = "SELECT * FROM "+ tableName+" WHERE "+orderId+" = ?";

    static final String sql_del = "DELETE FROM "+ tableName+" WHERE "+id+" = %d;";

}
