package com.example.elab_yang.treadmill;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class getDBActivity extends AppCompatActivity {
    private final static String TAG = Timeline.class.getSimpleName();
    Context mContext;
    DB db;
    SQLiteDatabase sql;

    String data;
    String abc[] = {"", "", "", "", "", "", ""};
    List<CardItem2> lists;
    private MyRecyclerAdapter2 mAdapter;
    RecyclerView recycler_view;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_getdb);
        setRecyclerView();
        db = new DB(this);
        TextView textview = (TextView) findViewById(R.id.textview);
        Intent intent = getIntent();
        data = intent.getStringExtra("BLE");
//        textview.setText(data);
        // & = end bit로 구분
        int i = getCharNumber(data, '&');
//        Log.d(TAG, "몇개의 데이터가 있을까? " + i);
        String[] str = data.split("&");
//        Log.d(TAG, "str[0] = " + str[0]);
//        Log.d(TAG, "str[1] = " + str[1]);
        sql = db.getWritableDatabase();
        db.onUpgrade(sql, 1, 2);
        for (int y = 0; y < i; y++) {
            abc[0] = str[y].substring(12, 13) + "번 사용자의 운동값 ";
            abc[1] = str[y].substring(0, 4) + "년도 " + str[y].substring(4, 6) + "월 " + str[y].substring(6, 8) + "일 "
             + str[y].substring(8, 10) + "시 " + str[y].substring(10, 12) + "분 ";
            abc[2] = str[y].substring(13, 17) + "초동안 운동을 진행";
            abc[3] = str[y].substring(17, 18) + "m를 달렸구";
            abc[4] = str[y].substring(18, 20) + "m/s의 속도로 ";
            abc[5] =  "심박수는 " + str[y].substring(20, 23) + "입니다.";

            lists.add(new CardItem2(abc[0], abc[1], abc[2], abc[3], abc[4], abc[5]));
            mAdapter.notifyDataSetChanged();
            setDB(abc[0], abc[1], abc[2], abc[3], abc[4], abc[5]);
        }
    }

    public void setRecyclerView() {
        // 객체 생성
        recycler_view = (RecyclerView) findViewById(R.id.recycler_view);
        recycler_view.setHasFixedSize(false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        // 반대로 쌓기
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recycler_view.setLayoutManager(layoutManager);
        // 배열 null 예외처리
        try {
            lists = new ArrayList<>();
        } catch (Exception e) {
            e.printStackTrace();
        }
        mAdapter = new MyRecyclerAdapter2(lists);
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
