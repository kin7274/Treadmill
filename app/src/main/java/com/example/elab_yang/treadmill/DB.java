package com.example.elab_yang.treadmill;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DB extends SQLiteOpenHelper {
    public DB(Context context) {
        super(context, "egometer", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 인덱스, 오늘날짜, 운동시간, 운동강도, 평균속도, 이동거리, 평균bpm, 소모 칼로리
        db.execSQL("create table tb_egometer(_id Integer primary key autoincrement, date text, time text, ei text, speed text, distance text, bpm text, kcal text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS tb_egometer");
        onCreate(db);
    }
}