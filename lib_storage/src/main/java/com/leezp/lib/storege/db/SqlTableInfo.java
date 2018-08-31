package com.leezp.lib.storege.db;

/**
 * Created by Leeping on 2018/6/28.
 * email: 793065165@qq.com
 */

public class SqlTableInfo {
    //版本名
    final static int dbVersion = 1;
    //数据库名
    final static String dbName = "map_string.db";
    //数据库表名
    final static String dbTableName = "map_string_table";

    final static String tableKey = "map_key";
    final static String tableValue = "map_value";

    final static String dbCreateSql = String.format("create table if not exists %s (%s text,%s text)", dbTableName, tableKey, tableValue);

    final static String[] dbColumns = new String[]{tableValue};

    final static String dbSelectionWhere = tableKey + "=?";

}
