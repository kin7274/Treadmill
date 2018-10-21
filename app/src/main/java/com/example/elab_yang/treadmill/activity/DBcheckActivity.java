package com.example.elab_yang.treadmill.activity;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.example.elab_yang.treadmill.model.CardItem2;
import com.example.elab_yang.treadmill.model.DB;
import com.example.elab_yang.treadmill.adapter.MyRecyclerAdapter;
import com.example.elab_yang.treadmill.R;

import java.util.ArrayList;
import java.util.List;

public class DBcheckActivity extends AppCompatActivity {
    private final static String TAG = Timeline.class.getSimpleName();
    Context mContext;
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
        setContentView(R.layout.activity_dbcheck);
        setRecyclerView();
        db = new DB(this);
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
        cursor = sql.rawQuery("select*from tb_treadmill", null);
        while (cursor.moveToNext()) {
            data += cursor.getString(0) + ","
                    + cursor.getString(1) + ","
                    + cursor.getString(2) + ","
                    + cursor.getString(3) + ","
                    + cursor.getString(4) + ","
                    + cursor.getString(5) + ","
                    + cursor.getString(6) + "\n";
            lists.add(new CardItem2(cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6)));
        }
        mAdapter.notifyDataSetChanged();
        cursor.close();
        sql.close();
        Toast.makeText(getApplicationContext(), "조회하였습니다.", Toast.LENGTH_SHORT).show();
    }
}
