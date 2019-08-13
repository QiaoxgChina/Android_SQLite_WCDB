package com.qiaoxg.sqlite.db;

import android.content.Context;

import com.tencent.wcdb.database.SQLiteDatabase;
import com.tencent.wcdb.database.SQLiteOpenHelper;

public class MySQLiteHelper extends SQLiteOpenHelper {

    private static final String TAG = "MySQLiteHelper";

    private static int VERSION = 1;
    private static String DB_NAME = "sqlite_icu.db";


    private String TABLE_MSG = "table_msg";

    public MySQLiteHelper(Context context) {
        this(context, DB_NAME, null, VERSION);
    }

    public MySQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE VIRTUAL TABLE table_msg USING fts3(title, body, tokenize=mmicu)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
