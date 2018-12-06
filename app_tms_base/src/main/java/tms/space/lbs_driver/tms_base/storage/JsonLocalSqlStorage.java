package tms.space.lbs_driver.tms_base.storage;

import com.leezp.lib.storege.db.SQLiteStore;
import com.leezp.lib.storege.inf.ICacheMap;
import com.leezp.lib.storege.obs.IDataObjectAbs;
import com.leezp.lib.util.JsonUtil;

/**
 * Created by Leeping on 2018/6/28.
 * email: 793065165@qq.com
 */

public abstract class JsonLocalSqlStorage<T> extends IDataObjectAbs{

    @Override
    protected ICacheMap<String, String> getStorage() {
        return SQLiteStore.get().getSql();
    }

    @Override
    protected String convert(Object object) {
        return JsonUtil.javaBeanToJson(object);
    }

    @Override
    protected Object reverse(String data) {
        return JsonUtil.jsonToJavaBean(data,this.getClass());
    }


}
