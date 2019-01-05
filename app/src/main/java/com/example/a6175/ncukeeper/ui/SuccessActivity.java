package com.example.a6175.ncukeeper.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.a6175.ncukeeper.MainActivity;
import com.example.a6175.ncukeeper.NcuWlan;
import com.example.a6175.ncukeeper.R;
import com.example.a6175.ncukeeper.util.L;
import com.example.a6175.ncukeeper.util.ShareUtils;
//import com.example.a6175.ncukeeper.service.DetectService;

import java.util.Date;

public class SuccessActivity extends AppCompatActivity {

    private long startTime;
    private TextView text_time;
    private Button btn_loginOut;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success);

        initView();
        initData();

        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            setTime(startTime);
                        }
                    });
                }
            }
        }).start();

        btn_loginOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NcuWlan.loginOut();
                ShareUtils.delete(SuccessActivity.this,"startTime");
                ShareUtils.putBoolean(SuccessActivity.this,"connect",false);
                startActivity(new Intent(SuccessActivity.this,MainActivity.class));
                finish();
            }
        });

    }

    private void setTime(long startTime) {
        String temp = new Date(System.currentTimeMillis() - startTime).toString();
        final String time = temp.substring(11, temp.length() - 15);
        text_time.setText(time);
    }

    private void initData() {
        ShareUtils.putBoolean(this,"connect",true);
        if ((startTime = ShareUtils.getLong(this,"startTime",0)) == 0) {
            startTime = System.currentTimeMillis() + 8 * 60 * 60 * 1000;
        }
        setTime(startTime);
        ShareUtils.putLong(this,"startTime", startTime);
    }

    private void initView() {
        text_time = findViewById(R.id.time);
        btn_loginOut = findViewById(R.id.loginOut);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
