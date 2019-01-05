package com.example.a6175.ncukeeper;

import android.app.ProgressDialog;
import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.a6175.ncukeeper.ui.SuccessActivity;
import com.example.a6175.ncukeeper.util.L;
import com.example.a6175.ncukeeper.util.ShareUtils;
import com.example.a6175.ncukeeper.util.TextUtil;
import com.example.a6175.ncukeeper.view.CustomDialog;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText ed_username;
    private EditText ed_password;
    private Button login;
    private NcuWlan ncuWlan = new NcuWlan();
    private CustomDialog dialog;

    private static final String TAG = "MainActivity";

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
        login = findViewById(R.id.btn_login);

        login.setOnClickListener(this);

        dialog = new CustomDialog(this, 100, 100, R.layout.dialog_loading, R.style.Theme_Dialog, Gravity.CENTER, R.style.pop_anim_style);
        //设置触摸无法取消
        dialog.setCancelable(false);

    }

    private void initData() {
        ncuWlan = new NcuWlan();
        ed_password.setText(ShareUtils.getString(this, "password", ""));
        ed_username.setText(ShareUtils.getString(this, "username", ""));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                dialog.show();
                String password = ed_password.getText().toString().trim();
                String username = ed_username.getText().toString().trim();

                if (TextUtil.isEmpty(password) || TextUtil.isEmpty(username)) {
                    Toast.makeText(this, "用户名或密码不能为空", Toast.LENGTH_SHORT).show();
                    break;
                }

                ShareUtils.putString(this, "password", password);
                ShareUtils.putString(this, "username", username);

                ncuWlan.setPassword(password);
                ncuWlan.setUsername(username);

                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        if (ncuWlan.connectWLAN(MainActivity.this)) {
                            ncuWlan.login();
                            while (!ncuWlan.callFinish) {
                                if (ncuWlan.callFinish)
                                L.e("Not Finish!");
                            };

                            L.e("Run to Here");

                            if (ncuWlan.connectInternet) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(MainActivity.this, "连接成功", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                ncuWlan.status();
                                Intent intent = new Intent(MainActivity.this, SuccessActivity.class);
                                startActivity(intent);
                                finish();
                            } else if (ncuWlan.connectTooFast) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(MainActivity.this, "两次认证的间隔太短,请十秒后再尝试", Toast.LENGTH_SHORT).show();

                                    }
                                });

                            } else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(MainActivity.this, "连接失败,请检测密码是否有误", Toast.LENGTH_SHORT).show();

                                    }
                                });
//                                ncuWlan.status();
                            }

                        } else {
                            //跳转至设置?
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(MainActivity.this, "检测到未连接NCUWLAN?", Toast.LENGTH_LONG).show();
                                }
                            });
                            startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                        }
                        dialog.dismiss();
                        L.e("Dismiss OutSide");
                    }
                }).start();

                break;
        }
    }
}
