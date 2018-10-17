package com.example.elab_yang.treadmill;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

// 스캔 액티비티
public class DeviceScanActivity extends AppCompatActivity {
    //
    private static final String TAG = "DeviceScanActivity";
    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1000;
    private static final int REQUEST_ENABLE_BT = 1;
    private static final long SCAN_PERIOD = 10000;
    //
    BluetoothManager bluetoothManager;
    BluetoothAdapter bluetoothAdapter;
    BluetoothLeScanner bluetoothLeScanner;
    //
    Button button, button1;
    RecyclerView recyclerView;
    //
    Handler handler;
    //
    DeviceScanAdapter adapter;
    ArrayList<BluetoothDevice> bleDeviceList;
    //
    boolean mScanning;
    //
    SharedPreferences preferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_scan);
        //
        preferences = getSharedPreferences("ActivityPREF", Context.MODE_PRIVATE);
        //
        bleDeviceList = new ArrayList<>();
        handler = new Handler();

        // 다음에 할게요
        button = (Button) findViewById(R.id.button);
        button.setOnClickListener((View v) -> {
//            SharedPreferences.Editor editor = preferences.edit();
//            editor.putBoolean("activity_executed", true);
//            editor.apply();
//            startActivity(new Intent(DeviceScanActivity.this, MainActivity.class));
//            finish();
        });

        // 스캔버튼
        button1 = (Button) findViewById(R.id.button1);
        button1.setOnClickListener(v -> {
            if (!mScanning) {
                button1.setText("STOP");
                bleDeviceList.clear();
                adapter.notifyDataSetChanged();
                scanLeDevice(true);

            } else {
                button1.setText("SCAN");
                scanLeDevice(false);
            }
        });
        checkScanPermission();
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        checkBleSupport();
        getBluetoothAdapter();
        checkBluetoothSupport();
    }

    private void checkBleSupport() {
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, "x", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void getBluetoothAdapter() {
        bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        bluetoothAdapter = bluetoothManager.getAdapter();
        bluetoothLeScanner = bluetoothAdapter.getBluetoothLeScanner();
    }

    private void checkBluetoothSupport() {
        if (bluetoothAdapter == null) {
            Toast.makeText(this, "x", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void checkScanPermission() {
        if (this.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("허용1");
            builder.setMessage("허용2");
            builder.setPositiveButton(android.R.string.ok, null);
            builder.setOnDismissListener(dialog -> requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_COARSE_LOCATION));
            builder.show();
        }
    }

    private void scanLeDevice(final boolean enable) {
        if (!bluetoothAdapter.isEnabled()) { // TODO: 2018-10-06 블루투스 오류 처리 하기
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        } else {
            if (enable) {
                handler.postDelayed(() -> {
                    mScanning = false;
                    bluetoothLeScanner.stopScan(leScanCallback);
                    button.setText("SCAN");
                }, SCAN_PERIOD);
                mScanning = true;
                startNEWBTLEDiscovery();
            } else {
                mScanning = false;
                bluetoothLeScanner.stopScan(leScanCallback);
            }
        }
    }

    private ScanCallback leScanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            BluetoothDevice device = result.getDevice();
            // TODO: 2018-07-21 장비가 중복되어 리스트에 추가되는 현상을 막아줍니다. - 박제창
            if (bleDeviceList.size() < 1) {
                bleDeviceList.add(device);
                adapter.notifyDataSetChanged();
            } else {
                boolean flag = true;
                for (int i = 0; i < bleDeviceList.size(); i++) {
                    if (device.getAddress().equals(bleDeviceList.get(i).getAddress())) {
                        flag = false;
                    }
                }
                if (flag) {
                    bleDeviceList.add(device);
                    adapter.notifyDataSetChanged();
                }
            }
        }

        @Override
        public void onBatchScanResults(List<ScanResult> results) {
            super.onBatchScanResults(results);
        }

        @Override
        public void onScanFailed(int errorCode) {
            super.onScanFailed(errorCode);
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case PERMISSION_REQUEST_COARSE_LOCATION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // TODO: 2018-07-21 스캔 동작하기 -- 박제창 (Dreamwalker)
                    Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
                } else {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Functionality limited");
                    builder.setMessage("Since location access has not been granted, " +
                            "this app will not be able to discover BLE Device when in the background.");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.setOnDismissListener(dialog -> finish());
                    builder.show();
                }
                return;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_ENABLE_BT && resultCode == Activity.RESULT_CANCELED) {
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        } else {
            adapter = new DeviceScanAdapter(bleDeviceList, this);
            recyclerView.setAdapter(adapter);
            scanLeDevice(true);
            button1.setText("STOP");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        scanLeDevice(false);
        bleDeviceList.clear();
    }

    private void stopBLEDiscovery() {
        if (adapter != null)
            bluetoothLeScanner.stopScan(leScanCallback);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void startNEWBTLEDiscovery() {
        if (bluetoothLeScanner == null) {
            getBluetoothAdapter();
        } else {
            bluetoothLeScanner.startScan(getScanFilters(), getScanSettings(), leScanCallback);
        }
    }

    private List<ScanFilter> getScanFilters() {
        List<ScanFilter> allFilters = new ArrayList<>();
        ScanFilter scanFilter0 = new ScanFilter.Builder().setDeviceName("KNU EG0").build();
        allFilters.add(scanFilter0);
        return allFilters;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private ScanSettings getScanSettings() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return new ScanSettings.Builder()
                    .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
                    .setMatchMode(ScanSettings.MATCH_MODE_STICKY)
                    .build();
        } else {
            return new ScanSettings.Builder()
                    .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
                    .build();
        }
    }
}
