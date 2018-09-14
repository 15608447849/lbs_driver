package tms.space.lbs_driver.tms_base.storage;

import com.leezp.lib.storege.db.SQLiteStore;
import com.leezp.lib.storege.inf.ICacheMap;
import com.leezp.lib.storege.obs.IDataObjectAbs;
import com.leezp.lib.util.JsonUti;

/**
 * Created by Leeping on 2018/6/28.
 * email: 793065165@qq.com
 */

public abstract class JsonLocalSqlStorage<T> extends IDataObjectAbs<T>{

    @Override
    protected ICacheMap<String, String> getStorage() {
        return SQLiteStore.get().getSql();
    }

    @Override
    protected String convert(Object object) {
        return JsonUti.javaBeanToJson(object);
    }

    @Override
    protected Object reverse(String data) {
        return JsonUti.jsonToJavaBean(data,this.getClass());
    }


}
