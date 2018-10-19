package com.example.elab_yang.treadmill;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class getDBActivity extends AppCompatActivity {
    private static final String TAG = "getDBActivity";

    Context mContext;
    DB db;
    SQLiteDatabase sql;
    String data;

    List<CardItem> lists;
    private MyRecyclerAdapter mAdapter;
    RecyclerView recycler_view;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_getdb);
        //
        mContext = this;


        setRecyclerView();
        db = new DB(this);
        //
        getDB();
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
        mAdapter = new MyRecyclerAdapter(lists);
        recycler_view.setAdapter(mAdapter);
    }

    public void getDB() {
        sql = db.getReadableDatabase();
        // 화면 clear
        data = "";
        Cursor cursor;
        cursor = sql.rawQuery("select*from tb_egometer", null);
        while (cursor.moveToNext()) {
            data += cursor.getString(0) + ","
                    + cursor.getString(1) + ","
                    + cursor.getString(2) + ","
                    + cursor.getString(3) + ","
                    + cursor.getString(4) + ","
                    + cursor.getString(5) + ","
                    + cursor.getString(6) + ","
                    + cursor.getString(7) + "\n";
            lists.add(new CardItem(cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7)));
        }
        mAdapter.notifyDataSetChanged();
        cursor.close();
        sql.close();
        Toast.makeText(getApplicationContext(), "조회하였습니다.", Toast.LENGTH_SHORT).show();
    }

    public void set_setDB(){
        int cnt = lists.size();
        Toast.makeText(getApplicationContext(), "cnt = " + cnt, Toast.LENGTH_SHORT).show();
        sql = db.getWritableDatabase();
        db.onUpgrade(sql, 1, 2);

        for (int i = 0; i < cnt; i++) {
            Log.d(TAG, i + " = " + lists.get(i).getDate());
            Log.d(TAG, i + " = " + lists.get(i).getTime());
            Log.d(TAG, i + " = " + lists.get(i).getEi());
            Log.d(TAG, i + " = " + lists.get(i).getSpeed());
            Log.d(TAG, i + " = " + lists.get(i).getDistance());
            Log.d(TAG, i + " = " + lists.get(i).getBpm());
            Log.d(TAG, i + " = " + lists.get(i).getKcal());
            setDB(lists.get(i).getDate(), lists.get(i).getTime(), lists.get(i).getEi(), lists.get(i).getSpeed(), lists.get(i).getDistance(), lists.get(i).getBpm(), lists.get(i).getKcal());
        }
    }

    // DB에 저장하는 메서드
    public void setDB(String date, String time, String ei, String speed, String distance, String bpm, String kcal) {
        sql = db.getWritableDatabase();

        sql.execSQL(String.format("INSERT INTO tb_egometer VALUES(null, '%s','%s초','%s','%s','%s','%s','%s')", date, time, ei, speed, distance, bpm, kcal));
        sql.close();
    }
//
    @Override
    public void onBackPressed() {
        set_setDB();
        finish();
//
//        super.onBackPressed();
    }
}
