package com.example.a6175.ncukeeper.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a6175.ncukeeper.ui.MainActivity;

/**
 * 项目名： SmartButler
 * 包名：   com.example.vinsent_y.smartbutler.util
 * 文件名： UtilTools
 * 创建者： Vincent_Y
 * 创建时间： 2018/10/27 20:44
 * 描述：    TODO
 */
public class UtilTools {

    //设置字体
    public static void setFont(Context mContext, TextView textView) {
        textView.setTypeface(Typeface.createFromAsset(mContext.getAssets(), "fonts/FONT.TTF"));
    }

    public static void runOnUiThreadToToast(final Activity activity, final String content) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(activity, content, Toast.LENGTH_SHORT).show();
            }
        });
    }

}
