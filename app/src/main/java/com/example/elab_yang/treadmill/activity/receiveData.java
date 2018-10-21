package com.example.elab_yang.treadmill.activity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.example.elab_yang.treadmill.R;
import com.example.elab_yang.treadmill.adapter.MyRecyclerAdapter;
import com.example.elab_yang.treadmill.model.CardItem2;
import com.example.elab_yang.treadmill.model.DB;

import java.util.ArrayList;
import java.util.List;

public class receiveData extends AppCompatActivity {
    DB db;
    SQLiteDatabase sql;

    String data;
    String abc[] = {"", "", "", "", "", "", ""};
    List<CardItem2> lists;
    private MyRecyclerAdapter mAdapter;
    RecyclerView recycler_view;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_getdb);
        setRecyclerView();
        db = new DB(this);
        Intent intent = getIntent();
        data = intent.getStringExtra("BLE");
        // & = end bit로 구분
        int i = getCharNumber(data, '&');
//        Log.d(TAG, "몇개의 데이터가 있을까? " + i);
        String[] str = data.split("&");
//        Log.d(TAG, "str[0] = " + str[0]);
//        Log.d(TAG, "str[1] = " + str[1]);
        sql = db.getWritableDatabase();
        db.onUpgrade(sql, 1, 2);
        for (int y = 0; y < i; y++) {
            abc[0] = "사용자" + str[y].substring(12, 13);
            abc[1] = str[y].substring(2, 4) + "년 " + str[y].substring(4, 6) + "월 " + str[y].substring(6, 8) + "일 "
             + str[y].substring(8, 10) + "시 " + str[y].substring(10, 12) + "분 ";
            abc[2] = str[y].substring(13, 15).replace("0", "") + str[y].substring(15, 17) + "초";
            abc[3] = str[y].substring(17, 18) + "km";
            abc[4] = str[y].substring(18, 20) + "m/s";
            abc[5] = str[y].substring(20, 23);
            //
            lists.add(new CardItem2(abc[0], abc[1], abc[2], abc[3], abc[4], abc[5]));
            mAdapter.notifyDataSetChanged();
            setDB(abc[0], abc[1], abc[2], abc[3], abc[4], abc[5]);
        }
    }

    public void setRecyclerView() {
        recycler_view = (RecyclerView) findViewById(R.id.recycler_view);
        recycler_view.setHasFixedSize(false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recycler_view.setLayoutManager(layoutManager);
        try {
            lists = new ArrayList<>();
        } catch (Exception e) {
            e.printStackTrace();
        }
        mAdapter = new MyRecyclerAdapter(lists);
        recycler_view.setAdapter(mAdapter);
    }

    public void setDB(String user_code, String date, String time, String distance, String speed, String bpm) {
        sql = db.getWritableDatabase();
        sql.execSQL(String.format("INSERT INTO tb_treadmill VALUES(null, '%s','%s','%s','%s','%s','%s')", user_code, date, time, distance, speed, bpm));
        Toast.makeText(getApplicationContext(), "저장햇구요", Toast.LENGTH_SHORT).show();
        sql.close();
    }

    // 특정문자 반복 갯수 확인
    int getCharNumber(String str, char c) {
        int cnt = 0;
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == c)
                cnt++;
        }
        return cnt;
    }
}
