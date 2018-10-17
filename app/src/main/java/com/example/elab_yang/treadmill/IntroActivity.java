package com.example.elab_yang.treadmill;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

// 인트로
public class IntroActivity extends AppCompatActivity {
    SharedPreferences pref;
    @Override
    protected void onStart() {
        super.onStart();
        pref = getSharedPreferences("ActivityPREF", Context.MODE_PRIVATE);
        // 처음이 아니군요? 그냥 가세욥
        if(pref.getBoolean("activity_executed", false)){
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        Button next_btn = (Button) findViewById(R.id.next_btn);
        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 시작하기
                Intent intent = new Intent(IntroActivity.this, DeviceScanActivity.class);
                IntroActivity.this.startActivity(intent);
            }
        });
    }
}
