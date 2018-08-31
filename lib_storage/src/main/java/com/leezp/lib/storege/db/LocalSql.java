package com.leezp.lib.storege.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.Closeable;
import java.io.IOException;

import com.leezp.lib.storege.inf.ICacheMap;

/**
 * Created by Leeping on 2018/6/28.
 * email: 793065165@qq.com
 */

public class LocalSql implements ICacheMap<String,String>,Closeable {

    private SQLiteOpenHelper sqLiteOpenHelper = null;

    @Override
    public void init(Context context) {
        if (sqLiteOpenHelper==null){

            createSql(context);
        }
    }
    /**
     * 创建数据库
     * @param context
     */
    private void createSql(Context context){
        sqLiteOpenHelper = new SQLiteOpenHelper(context, com.leezp.lib.storege.db.SqlTableInfo.dbName,null, com.leezp.lib.storege.db.SqlTableInfo.dbVersion) {
            @Override
            public void onCreate(SQLiteDatabase db) {

                db.execSQL(com.leezp.lib.storege.db.SqlTableInfo.dbCreateSql);
            }

            @Override
            public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
                //node
            }
        };
    }

 /* query(String table,String[] columns,String selection,String[]  selectionArgs,String groupBy,String having,String orderBy,String limit);
    参数table:表名称
    参数columns:列名称数组
    参数selection:条件字句，相当于where的约束条件
    参数selectionArgs:条件字句，参数数组 为where中的占位符提供具体的值
    参数groupBy:分组列
    参数having:分组条件
    参数orderBy:排序列
    参数limit:分页查询限制
    select k,v form tablename where k="?"
            */
    @Override
    public String getValue(String k) {
        if (sqLiteOpenHelper==null) return null;
        final SQLiteDatabase sqLiteDatabase = sqLiteOpenHelper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query(
                com.leezp.lib.storege.db.SqlTableInfo.dbTableName,
                com.leezp.lib.storege.db.SqlTableInfo.dbColumns,
                com.leezp.lib.storege.db.SqlTableInfo.dbSelectionWhere,
                new String[]{k},
                null,
                null,
                null,
                null);
        if (cursor.getCount()>0){
            if (cursor.moveToFirst()){
                return cursor.getString(0);
            }
        }
        return null;
    }

    @Override
    public void putValue(String k, String v) {
        if (sqLiteOpenHelper==null) return;
        final String value  = getValue(k);
        StringBuffer  sql = new StringBuffer ();
        if (value==null){
            sql.append(
                   String.format("insert into %s(%s,%s) values('%s','%s')",
                           com.leezp.lib.storege.db.SqlTableInfo.dbTableName, com.leezp.lib.storege.db.SqlTableInfo.tableKey, com.leezp.lib.storege.db.SqlTableInfo.tableValue,k,v
                    )
            );
        }else{
            sql.append(
                    String.format("update %s set %s = '%s' where %s = '%s'",
                            com.leezp.lib.storege.db.SqlTableInfo.dbTableName, com.leezp.lib.storege.db.SqlTableInfo.tableValue,v, com.leezp.lib.storege.db.SqlTableInfo.tableKey,k
                    ));
        }
        sqLiteOpenHelper.getWritableDatabase().execSQL(sql.toString());
    }

    @Override
    public void removeKey(String k) {
        if (sqLiteOpenHelper==null) return;
        sqLiteOpenHelper.getWritableDatabase().delete(com.leezp.lib.storege.db.SqlTableInfo.dbTableName, com.leezp.lib.storege.db.SqlTableInfo.dbSelectionWhere,new String[]{k});
    }

    @Override
    public void close() throws IOException {
        if (sqLiteOpenHelper!=null) sqLiteOpenHelper.close();
        sqLiteOpenHelper = null;
    }
}
