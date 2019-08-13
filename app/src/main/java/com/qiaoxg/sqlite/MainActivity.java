package com.qiaoxg.sqlite;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.qiaoxg.sqlite.bean.UserBean;
import com.qiaoxg.sqlite.db.MySQLiteManager;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView tipTv;
    private EditText inputEt;
    private RecyclerView userRv;
    private UserAdapter userAdapter;

    private List<UserBean> userBeanList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inputEt = findViewById(R.id.input_name_et);
        tipTv = findViewById(R.id.tip_tv);

        userRv = findViewById(R.id.user_rv);
        userAdapter = new UserAdapter(this);
        userRv.setAdapter(userAdapter);

        userRv.setLayoutManager(new LinearLayoutManager(this));

        initP();

        findViewById(R.id.search_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userAdapter.setUserBeanList(userBeanList);
                tipTv.setText("共有 " + userBeanList.size() + " 个记录");
            }
        });

        inputEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                tipTv.setText("正在查询...");
                String keyword = inputEt.getText().toString().trim();
                if (!TextUtils.isEmpty(keyword)) {
                    List<UserBean> list = MySQLiteManager.getInstance(MainActivity.this).selectMsg(keyword);
                    userAdapter.setUserBeanList(list);
                    tipTv.setText("共有 " + list.size() + " 个包含：'" + keyword + "' 的记录");
                } else {
                    userAdapter.setUserBeanList(userBeanList);
                    tipTv.setText("共有 " + userBeanList.size() + " 个记录");
                }
            }
        });
    }

    private void initP() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_CONTACTS}, 201);
        } else {
            initData();
        }
    }

    private void initData() {
        userBeanList = MySQLiteManager.getInstance(this).selectAllMsg();
        if (userBeanList.size() <= 0) {
            List<UserBean> list = readContacts();
            MySQLiteManager.getInstance(this).insertMsg(list);
            userBeanList = MySQLiteManager.getInstance(this).selectAllMsg();
        }

        userAdapter.setUserBeanList(userBeanList);
        tipTv.setText("共有 " + userBeanList.size() + " 个记录");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 201) {
            initData();
        } else {
            return;
        }
    }

    public List<UserBean> readContacts() {

        List<UserBean> list = new ArrayList<>();
        Cursor cursor = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);
        int contactIdIndex = 0;
        int nameIndex = 0;

        if (cursor.getCount() > 0) {
            contactIdIndex = cursor.getColumnIndex(ContactsContract.Contacts._ID);
            nameIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
        }
        while (cursor.moveToNext()) {
            String contactId = cursor.getString(contactIdIndex);
            String name = cursor.getString(nameIndex);
            /*
             * 查找该联系人的phone信息
             */
            Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + contactId,
                    null, null);
            int phoneIndex = 0;
            if (phones.getCount() > 0) {
                phoneIndex = phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            }
            String phoneNumber = "";
            while (phones.moveToNext()) {
                phoneNumber = phones.getString(phoneIndex);
            }

            UserBean bean = new UserBean();
            bean.setName(name);
            bean.setPhone(phoneNumber);

            list.add(bean);
        }

        return list;
    }
}
