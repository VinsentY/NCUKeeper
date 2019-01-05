//package com.example.a6175.ncukeeper.service;
//
//import android.app.AlarmManager;
//import android.app.IntentService;
//import android.app.PendingIntent;
//import android.content.Intent;
//import android.os.Binder;
//import android.os.IBinder;
//import android.os.Looper;
//import android.os.SystemClock;
//import android.support.annotation.Nullable;
//import android.util.Log;
//import android.widget.Toast;
//
//import com.example.a6175.ncukeeper.NcuWlan;
//
//public class DetectService extends IntentService {
//
//    private static final String TAG = "DetectService";
//
//    private NcuWlan ncuWlan;
//    private int interval = 10;
//
//    private DetectBinder mBinder = new DetectBinder();
//
//    public DetectService() {
//        super("DetectService");
//    }
//
//    @Override
//    public int onStartCommand(final Intent intent, int flags, int startId) {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                if (ncuWlan != null) {
//                    if (ncuWlan.connectWLAN) {
//                        interval = 30;
//                    } else {
//                        interval = 3;
//                    }
//                    ncuWlan.detectWLAN();
//                    mBinder.toast();
//                } else {
//                    //等待
//                }
//            }
//        }).start();
//
//        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
//        int SECOND = 1000;
//        long triggerTime = SystemClock.elapsedRealtime() + interval * SECOND;
//        Intent i = new Intent(this, DetectService.class);
//        PendingIntent pi = PendingIntent.getService(this, 0, i, 0);
//        manager.cancel(pi);
//        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerTime, pi);
//        return super.onStartCommand(intent, flags, startId);
//
//    }
//
//    public class DetectBinder extends Binder {
//
//        public void toast() {
//            Looper.prepare();
//            if (ncuWlan.connectWLAN) {
//                Log.d(TAG, "testNCU: 是ncuwlan");
//                Toast.makeText(DetectService.this,"是ncuwlan",Toast.LENGTH_SHORT).show();
//            } else {
//                Log.d(TAG, "testNCU: 不是ncuwlan");
//                Toast.makeText(DetectService.this, "不是ncuwlan", Toast.LENGTH_SHORT).show();
//            }
//            Looper.loop();
//        }
//
//        public void startDetect(NcuWlan wlan) {
//            ncuWlan = wlan;
//        }
//    }
//
//    @Override
//    public IBinder onBind(Intent intent) {
//        return mBinder;
//    }
//
//    @Override
//    protected void onHandleIntent(@Nullable Intent intent) {
//        Log.d(TAG, "onHandleIntent: ");
//    }
//}
