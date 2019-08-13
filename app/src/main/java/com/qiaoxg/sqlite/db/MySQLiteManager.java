package com.qiaoxg.sqlite.db;

import android.content.Context;
import android.util.Log;

import com.qiaoxg.sqlite.bean.UserBean;
import com.tencent.wcdb.Cursor;
import com.tencent.wcdb.database.SQLiteCipherSpec;
import com.tencent.wcdb.database.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class MySQLiteManager {
    private static final String TAG = "MySQLiteManager";

    private static MySQLiteManager mInstance;

    private MySQLiteHelper mySQLiteHelper;

    private MySQLiteManager(Context context) {
        SQLiteCipherSpec spec = new SQLiteCipherSpec().setPageSize(1024).setKDFIteration(4000);
        mySQLiteHelper = new MySQLiteHelper(context);
    }

    public static MySQLiteManager getInstance(Context context) {
        if (mInstance == null) {
            synchronized (MySQLiteManager.class) {
                if (mInstance == null) {
                    mInstance = new MySQLiteManager(context);
                }
            }
        }
        return mInstance;
    }

    public void insertMsg(List<UserBean> list) {
        SQLiteDatabase db = mySQLiteHelper.getWritableDatabase();

        for (int i = 0; i < list.size(); i++) {
            UserBean bean = list.get(i);
            String preName = "==";
            if (i > 1) {
                preName = list.get(i - 1).getName();
            }
            String sql = "INSERT INTO table_msg(title, body) VALUES('" + bean.getName() + "', '" + preName + bean.getPhone() + "');";
            db.execSQL(sql);
            Log.e(TAG, "insertMsg: =======");
        }
    }

    public List<UserBean> selectAllMsg() {

        List<UserBean> list = new ArrayList<>();
        SQLiteDatabase db = mySQLiteHelper.getReadableDatabase();

        String sql = "SELECT * FROM table_msg";
        Cursor cursor = db.rawQuery(sql, null);

        Log.e(TAG, "selectMsg MATCH  count: =======" + cursor.getCount());

        while (cursor.moveToNext()) {
            String name = cursor.getString(0);
            String phone = cursor.getString(1);

            UserBean bean = new UserBean();
            bean.setPhone(phone);
            bean.setName(name);
            list.add(bean);
        }
        cursor.close();
        return list;
    }


    public List<UserBean> selectMsg(String keyword) {

        List<UserBean> list = new ArrayList<>();
        SQLiteDatabase db = mySQLiteHelper.getReadableDatabase();

        String sql = "SELECT * FROM table_msg WHERE table_msg MATCH '*" + keyword + "*';";
        Cursor cursor = db.rawQuery(sql, null);

        Log.e(TAG, "selectMsg MATCH  count: =======" + cursor.getCount());

        while (cursor.moveToNext()) {
            String name = cursor.getString(0);
            String phone = cursor.getString(1);

            UserBean bean = new UserBean();
            bean.setPhone(phone);
            bean.setName(name);
            list.add(bean);
        }

        cursor.close();
        return list;
    }
}
