package com.example.a6175.ncukeeper.ui;

import android.content.Intent;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.a6175.ncukeeper.R;
import com.example.a6175.ncukeeper.util.HttpUtil;
import com.example.a6175.ncukeeper.util.L;
import com.example.a6175.ncukeeper.util.ShareUtils;
import com.example.a6175.ncukeeper.util.StaticClass;
import com.example.a6175.ncukeeper.util.UtilTools;
import com.example.a6175.ncukeeper.view.CustomDialog;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.example.a6175.ncukeeper.util.UtilTools.runOnUiThreadToToast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText ed_username;
    private EditText ed_password;
    private Button bt_login;
    private CustomDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();
        initData();

    }

    private void initView() {
        ed_username = findViewById(R.id.user_name);
        ed_password = findViewById(R.id.password);
        bt_login = findViewById(R.id.btn_login);

        bt_login.setOnClickListener(this);

        dialog = new CustomDialog(this, 100, 100, R.layout.dialog_loading, R.style.Theme_Dialog, Gravity.CENTER, R.style.pop_anim_style);
        //设置触摸无法取消
        dialog.setCancelable(false);

    }

    private void initData() {
        ed_password.setText(ShareUtils.getString(this, "password", ""));
        ed_username.setText(ShareUtils.getString(this, "username", ""));
    }

    public void login(final String username, final String password) {


        HttpUtil.get(StaticClass.URL_DETECT, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                L.e("未连接WLAN");
                runOnUiThreadToToast(MainActivity.this, getString(R.string.tip_of_connect_wlan_fail));
                startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                L.e("成功连接WLAN");
                final RequestBody requestBody = new FormBody.Builder()
                        .add("action", "login")
                        .add("ac_id", "1")
                        .add("user_ip", "")
                        .add("nas_ip", "")
                        .add("user_mac", "")
                        .add("save_me", "1")
                        .add("ajax", "1")
                        .add("username", username)
                        .add("password", password)
                        .build();
                HttpUtil.post(StaticClass.URL_AUTH, requestBody, new Callback() {
                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {
                        L.e("连接失败");
                        runOnUiThreadToToast(MainActivity.this, getString(R.string.tip_of_login_fail));
                    }

                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                        String res = response.body().string();
                        L.e("连接成功, 返回结果: " + res);
                        if (res.contains(getString(R.string.flag_login_succ))) {
                            //启动SuccessActivity活动
                            SuccessActivity.startActivity(MainActivity.this);
                        } else if (res.contains("E2532")) { //两次认证时间太短
                            runOnUiThreadToToast(MainActivity.this, getString(R.string.login_too_fast));
                        } else if (res.contains("E2531") || res.contains("E2553")) { //用户名或密码错误
                            runOnUiThreadToToast(MainActivity.this, getString(R.string.login_user_or_password_error));
                        } else {
                            runOnUiThreadToToast(MainActivity.this, getString(R.string.login_error));
                        }
                    }
                });
            }
        });

    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                dialog.show();
                String password = ed_password.getText().toString().trim();
                String username = ed_username.getText().toString().trim();

                if (TextUtils.isEmpty(password) || TextUtils.isEmpty(username)) {
                    Toast.makeText(this, getString(R.string.tip_of_input_valid), Toast.LENGTH_SHORT).show();
                } else {
                    // 保存账户密码
                    ShareUtils.putString(this, "password", password);
                    ShareUtils.putString(this, "username", username);

                    //登陆
                    login(username,password);
                }


                dialog.dismiss();
                break;
        }
    }
}
