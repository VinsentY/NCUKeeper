package com.example.a6175.ncukeeper.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.a6175.ncukeeper.R;
//import com.example.a6175.ncukeeper.service.DetectService;
import com.example.a6175.ncukeeper.util.ShareUtils;
import com.example.a6175.ncukeeper.util.StaticClass;

public class SplashActivity extends AppCompatActivity {

    private int dealayTime = 1000;


    private TextView textView;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case StaticClass.HANDLER_SPLASH:
                    if (ShareUtils.getBoolean(SplashActivity.this,"connect",false)) {
                        startActivity(new Intent(SplashActivity.this, SuccessActivity.class));
                    } else {
                        startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    }
                    finish();
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initView();
    }



    private void initView() {
        //延时
        handler.sendEmptyMessageDelayed(StaticClass.HANDLER_SPLASH, dealayTime);
        textView = findViewById(R.id.tv_splash);
    }

}
