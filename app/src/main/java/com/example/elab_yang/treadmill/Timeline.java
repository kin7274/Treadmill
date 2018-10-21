package com.example.elab_yang.treadmill;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static com.example.elab_yang.treadmill.BluetoothLeService.ACTION_DATA_AVAILABLE;
import static com.example.elab_yang.treadmill.BluetoothLeService.ACTION_DATA_AVAILABLE_CHANGE;
import static com.example.elab_yang.treadmill.BluetoothLeService.EXTRA_DATA;
import static com.example.elab_yang.treadmill.IntentConst.REAL_TIME_INDOOR_BIKE_DEVICE;

// 데이터받고 db에 추가, 조회 전부다
public class Timeline extends AppCompatActivity implements View.OnClickListener {
    private final static String TAG = Timeline.class.getSimpleName();
    Context mContext;
    // BLE값 읽어오기, 저장
    Button receiveBLEandSetDB;
    // DB 보기
    Button viewDB;

    String deviceAddress = "";
    String message, abc = "";

    //
    BluetoothLeService mBluetoothLeService = new BluetoothLeService();
    private final BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (BluetoothLeService.ACTION_DATA_AVAILABLE_CHANGE.equals(action)) {
                message = (intent.getStringExtra(EXTRA_DATA)).substring(0, 19);
                Log.d(TAG, "message = " + message);
                abc += message;
            }
            // 블루투스값 쭉 모음 = abc
            Log.d(TAG, "abc = " + abc);
        }
    };

    private final ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            mBluetoothLeService = ((BluetoothLeService.LocalBinder) service).getService();
            if (!mBluetoothLeService.initialize()) {
                Log.e(TAG, "Unable to initialize Bluetooth");
                finish();
            }
            mBluetoothLeService.connect(deviceAddress);
            Log.d(TAG, "서비스가 연결되었습니다!");
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBluetoothLeService = null;
        }
    };

    // 메인
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        mContext = this;
        set();
        //
        deviceAddress = getIntent().getStringExtra(REAL_TIME_INDOOR_BIKE_DEVICE);
        if (deviceAddress != null) {
            Log.d(TAG, "onCreate: " + deviceAddress);
        }
        IntentFilter intentfilter = new IntentFilter();
        intentfilter.addAction(ACTION_DATA_AVAILABLE);
        intentfilter.addAction(ACTION_DATA_AVAILABLE_CHANGE);
        registerReceiver(mMessageReceiver, intentfilter);
        Intent gattServiceIntent = new Intent(this, BluetoothLeService.class);
        bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);
    }

    public void set() {
        receiveBLEandSetDB = (Button) findViewById(R.id.receiveBLEandSetDB);
        receiveBLEandSetDB.setOnClickListener(this);
        viewDB = (Button) findViewById(R.id.viewDB);
        viewDB.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // 읽고 저장
            case R.id.receiveBLEandSetDB:
                mBluetoothLeService.writeCharacteristic("o");
                break;
                // 인텐트로 abc값을 보내
            case R.id.viewDB:
                Intent intent = new Intent(Timeline.this, getDBActivity.class);
                intent.putExtra("BLE", abc);
                startActivity(intent);
                finish();
                break;
        }
    }

//    public void set_setDB() {
//        int cnt = lists.size();
//        int i = getCharNumber(abc, '&');
//
//        Toast.makeText(getApplicationContext(), "cnt = " + cnt, Toast.LENGTH_SHORT).show();
//        sql = db.getWritableDatabase();
//        db.onUpgrade(sql, 1, 2);
//
//        for (int i = 0; i < cnt; i++) {
//            Log.d(TAG, i + " = " + lists.get(i).getDate());
//            Log.d(TAG, i + " = " + lists.get(i).getTime());
//            Log.d(TAG, i + " = " + lists.get(i).getEi());
//            Log.d(TAG, i + " = " + lists.get(i).getSpeed());
//            Log.d(TAG, i + " = " + lists.get(i).getDistance());
//            Log.d(TAG, i + " = " + lists.get(i).getBpm());
//            Log.d(TAG, i + " = " + lists.get(i).getKcal());
//            setDB(lists.get(i).getDate(), lists.get(i).getTime(), lists.get(i).getEi(), lists.get(i).getSpeed(), lists.get(i).getDistance(), lists.get(i).getBpm(), lists.get(i).getKcal());
//        }
//    }

    // DB에 저장하는 메서드
//    public void setDB(String date, String time, String ei, String speed, String distance, String bpm, String kcal) {
//        sql = db.getWritableDatabase();
//
//        sql.execSQL(String.format("INSERT INTO tb_treadmill VALUES(null, '%s','%s초','%s','%s','%s','%s','%s')", date, time, ei, speed, distance, bpm, kcal));
//        sql.close();
//    }

    @Override
    protected void onDestroy() {
        unbindService(mServiceConnection);
        mBluetoothLeService = null;
        unregisterReceiver(mMessageReceiver);
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
