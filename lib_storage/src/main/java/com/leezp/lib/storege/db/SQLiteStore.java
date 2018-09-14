package com.leezp.lib.storege.db;

import android.content.Context;

import java.io.Closeable;
import java.io.IOException;

/**
 * Created by Leeping on 2018/6/28.
 * email: 793065165@qq.com
 * string-string (k-v) 数据存储
 */

public class SQLiteStore implements Closeable {
    private SQLiteStore() {
    }

    private static final class Holder{
        private final static SQLiteStore DB_STORE = new SQLiteStore();
    }

    public static SQLiteStore get(){
        return Holder.DB_STORE;
    }

    private boolean isInit;

    private void checkInit(){
        if (!isInit) throw new IllegalStateException("未初始化数据库对象");
    }

    private LocalSql sql;

    public void init(Context context){
        if (isInit) return;
        sql = new LocalSql();
        sql.init(context);
        isInit = true;
    }


    public LocalSql getSql() {
        checkInit();
        return sql;
    }

    @Override
    public void close() throws IOException {
        checkInit();
        sql.close();
    }

}
