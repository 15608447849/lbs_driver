package tms.space.lbs_driver.tms_base.storage;

import android.content.Context;

import com.leezp.lib.storege.db.LocalSql;

import java.io.Closeable;
import java.io.IOException;

/**
 * Created by Leeping on 2018/6/28.
 * email: 793065165@qq.com
 * string-string (k-v) 数据存储
 */

public class DbStore implements Closeable {
    private DbStore() {
    }

    private static final class Holder{
        private final static DbStore DB_STORE = new DbStore();
    }

    public static DbStore get(){
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
