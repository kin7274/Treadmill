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
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import static com.example.elab_yang.treadmill.BluetoothLeService.ACTION_DATA_AVAILABLE;
import static com.example.elab_yang.treadmill.BluetoothLeService.ACTION_DATA_AVAILABLE_CHANGE;
import static com.example.elab_yang.treadmill.BluetoothLeService.EXTRA_DATA;
import static com.example.elab_yang.treadmill.DeviceControlActivity.EXTRAS_DEVICE_ADDRESS;
import static com.example.elab_yang.treadmill.IntentConst.REAL_TIME_INDOOR_BIKE_DEVICE;

// 실질적으로 블루투스값 리시브 액티비티입니다.
// 여기서 DB에도 저장합시다!
public class Timeline extends AppCompatActivity implements View.OnClickListener {
    private final static String TAG = Timeline.class.getSimpleName();
    Context mContext;
    DB my;
    SQLiteDatabase sql;
    Button donggihwa, insert_db, load_db, delete_db;
    String user_name2;
    TextView textview;
    List<CardItem> lists;
    private MyRecyclerAdapter mAdapter;
    RecyclerView recycler_view;
    String[] INSULIN1, INSULIN2;
    String insulin_data1, insulin_data2;
    String aaa, bbb;
    String eat_status = "";
    String deviceAddress = "";

    String aabbcc, message;
    BluetoothLeService mBluetoothLeService = new BluetoothLeService();
    // 브로드캐스트
    private final BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (BluetoothLeService.ACTION_DATA_AVAILABLE_CHANGE.equals(action)) {
                message = intent.getStringExtra(EXTRA_DATA);
                Log.d(TAG, "message = " + message);
//                textview.setText(message);
            }
//            aabbcc += message;
            aabbcc = aabbcc + "&&&" + message;
            textview.setText(aabbcc);
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
        textview = (TextView) findViewById(R.id.textview);
        set();

        deviceAddress = getIntent().getStringExtra(REAL_TIME_INDOOR_BIKE_DEVICE);
        if (deviceAddress != null) {
            Log.d(TAG, "onCreate: " + deviceAddress);
            // 이건 ok
        }
        IntentFilter intentfilter = new IntentFilter();
        intentfilter.addAction(ACTION_DATA_AVAILABLE);
        intentfilter.addAction(ACTION_DATA_AVAILABLE_CHANGE);
        registerReceiver(mMessageReceiver, intentfilter);
        Intent gattServiceIntent = new Intent(this, BluetoothLeService.class);
        bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);
    }

    public void set() {
        // DB관련
        my = new DB(this);
        // 버튼 객체
        donggihwa = (Button) findViewById(R.id.donggihwa);
        insert_db = (Button) findViewById(R.id.insert_db);
        load_db = (Button) findViewById(R.id.load_db);
        delete_db = (Button) findViewById(R.id.delete_db);
        donggihwa.setOnClickListener(this);
        insert_db.setOnClickListener(this);
        load_db.setOnClickListener(this);
        delete_db.setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        unbindService(mServiceConnection);
        mBluetoothLeService = null;
        unregisterReceiver(mMessageReceiver);
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.donggihwa:
                // sd카드 다 리드
                // "a" 값 전송
                mBluetoothLeService.writeCharacteristic("o");
                break;
            case R.id.insert_db:
                break;
            case R.id.load_db:
                break;
            case R.id.delete_db:
                break;
        }
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
