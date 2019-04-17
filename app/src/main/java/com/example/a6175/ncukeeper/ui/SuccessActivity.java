package com.example.a6175.ncukeeper.ui;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a6175.ncukeeper.R;
import com.example.a6175.ncukeeper.util.HttpUtil;
import com.example.a6175.ncukeeper.util.ShareUtils;
//import com.example.a6175.ncukeeper.service.DetectService;

import java.io.IOException;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.example.a6175.ncukeeper.util.StaticClass.URL_AUTH;
import static com.example.a6175.ncukeeper.util.UtilTools.runOnUiThreadToToast;

public class SuccessActivity extends AppCompatActivity {

    private long startTime;
    private TextView text_time;
    private Button btn_loginOut;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, SuccessActivity.class);
        context.startActivity(intent);
    }

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
                loginOut();
            }
        });

    }

    private void loginOut() {
        RequestBody requestBody= new FormBody.Builder()
                .add("action","logout")
                .add("username","")
                .add("password","")
                .add("ajax","1")
                .build();
        HttpUtil.post(URL_AUTH, requestBody, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThreadToToast(SuccessActivity.this, getString(R.string.loginout_fail));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                runOnUiThreadToToast(SuccessActivity.this, getString(R.string.loginout_succ));
            }
        });
        ShareUtils.delete(SuccessActivity.this,"startTime");
        ShareUtils.putBoolean(SuccessActivity.this,"connect",false);
        startActivity(new Intent(SuccessActivity.this,MainActivity.class));
        finish();
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

}
