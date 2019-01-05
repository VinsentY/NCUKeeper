package com.example.a6175.ncukeeper;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import com.example.a6175.ncukeeper.entity.Data;
import com.example.a6175.ncukeeper.util.L;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class NcuWlan {

    private static final String TAG = "NcuWlan";

    final static String URL_DETECT = "http://aaa.ncu.edu.cn:803/srun_portal_phone.php?";
//    final String URL_DETECT = "http://www.baidu.com";

    final static String URL_AUTH = "http://aaa.ncu.edu.cn:803/include/auth_action.php";
    final static String URL_STATUS = "http://aaa.ncu.edu.cn:803/srun_portal_pc_succeed.php";
    private String username;
    private String password;
    private static OkHttpClient okHttpClient;

    static {
        okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .build();
    }

    public static boolean connectInternet = false;
    public static boolean callFinish = false;
    public static boolean connectTooFast = false;

    public NcuWlan() {
        this("", "");
    }

    public NcuWlan(String username, String password) {
        this.username = username;//用户名
        this.password = password;//密码
        //设置超时时间

    }//初始化对象

    public void login() {

        Data data = new Data();
        data.setUsername(username);
        data.setPassword(password);
//        Gson gson = new Gson();
//        String json = gson.toJson(data);
        //获得数据并将对象装换成json格式
//        RequestBody body = new FormBody.Builder()
//                .add("password", password)
//                .add("username",username)
//                .build();

//        RequestBody requestBody = FormBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        //创建一个请求对象

        RequestBody requestBody = new FormBody.Builder()
                .add("action", "login")
                .add("ac_id", "1")
                .add("user_ip", "")
                .add("nas_ip", "")
                .add("user_mac", "")
                .add("save_me", "1")
                .add("ajax", "1")
                .add("username", data.getUsername())
                .add("password", data.getPassword())
                .build();

        final Request request = new Request.Builder()
                .url(URL_AUTH)
                .post(requestBody)
                .build();

//        //创建一个请求对象
//        final Request request=new Request.Builder()
//                .url("http://aaa.ncu.edu.cn:802/srun_portal_pc.php?")
//                .post(requestBody)
//                .build();

        callFinish = false;
        Call call = okHttpClient.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                connectInternet = false;
                callFinish = true;
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //连接不成功
//                Log.d(TAG, "onResponse: " + response.body().string());
//               Log.d(TAG, "onResponse: " + response.body());
//               Log.d(TAG, "onResponse: " + response);
//               if (response.body().string().contains("login_ok")) {
//                   Log.d(TAG, "onResponse: SUCCESS!!!");
//               }
                String str = response.body().string();
                Log.d(TAG, "onResponse: " + str);
                if (str.contains("login_ok")) {
                    Log.d(TAG, "onResponse: We Did It!");
                    connectInternet = true;
                } else if (str.contains("两次认证的间隔太短")) {
                    connectTooFast = true;
                }
                callFinish = true;

            /*
                这里要写登陆成功后怎么办（获得URL_STATUS的信息）
            */
            }
        });

    }//登陆NCUWlan

    public void status() {

        final Request request = new Request.Builder()
                .url(URL_STATUS)
                .get()
                .build();
        callFinish = false;
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callFinish = true;
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d(TAG, "onResponseStatus: " + response.body().string());
                callFinish = true;
            }
        });

    }//登录成功后，获得各个信息

//    public void detectWLAN() {
//        Request request = new Request.Builder()
//                .url(URL_DETECT)
//                .get()
//                .build();
//        callFinish = false;
//        Call call = okHttpClient.newCall(request);
//
//        call.enqueue(new Callback() {
//
//
//            @Override
//            public void onFailure(Call call, IOException e) {
//                connectWLAN = false;
//                callFinish = true;
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                if (response.code() == 200) {
//                    connectWLAN = true;
//                    callFinish = true;
//                }
//            }
//        });
//    }
    //检测是否为ncuwlan

    public boolean connectWLAN(Context context) {
        WifiManager wifiMgr = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        int wifiState = wifiMgr.getWifiState();
        WifiInfo info = wifiMgr.getConnectionInfo();
        String wifiId = info != null ? info.getSSID() : null;
        return wifiId.equals("\"NCUWLAN\"");
    }

    public static void loginOut(){
        RequestBody requestBody= new FormBody.Builder()
                .add("action","logout")
                .add("username","")
                .add("password","")
                .add("ajax","1")
                .build();

        final Request request=new Request.Builder()
                .url(URL_AUTH)
                .post(requestBody)
                .build();

        Call call =okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //失败不知道写啥
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                connectInternet = false;
                //登出成功
            }
        });
//        for (int i = 0; i < 10; i++) {
//            try {
//                Thread.currentThread().sleep(100);
//                System.out.println("我是主线程,线程Id为:" + i);
//                if (isLogin)
//                {
//                    i=9;
//                    break;
//                }
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }



    }//登出

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


}
