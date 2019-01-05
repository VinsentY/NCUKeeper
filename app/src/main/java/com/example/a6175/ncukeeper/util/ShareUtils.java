package com.example.a6175.ncukeeper.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 项目名： SmartButler
 * 包名：   com.example.vinsent_y.smartbutler.util
 * 文件名： ShareUtils
 * 创建者： Vincent_Y
 * 创建时间： 2018/10/28 16:38
 * 描述：    TODO
 */
public class ShareUtils {

    public static final String NAME = "config";
    //键 值
    public static void putString(Context mContext, String key, String value) {
        SharedPreferences sp = mContext.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        sp.edit().putString(key,value).apply();
    }
    //键 默认值
    public static String getString(Context mContext, String key, String defValue) {
        SharedPreferences sp = mContext.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        return sp.getString(key, defValue);
    }

    public static void putInt(Context mContext, String key, int value) {
        SharedPreferences sp = mContext.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        sp.edit().putInt(key,value).apply();
    }

    public static int getInt(Context mContext, String key, int defValue) {
        SharedPreferences sp = mContext.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        return sp.getInt(key, defValue);
    }
    public static void putBoolean(Context mContext, String key, boolean value) {
        SharedPreferences sp = mContext.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        sp.edit().putBoolean(key,value).apply();
    }

    public static Boolean getBoolean(Context mContext, String key, boolean defValue) {
        SharedPreferences sp = mContext.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        return sp.getBoolean(key, defValue);
    }

    public static boolean delete(Context mContext, String key) {
        SharedPreferences sp = mContext.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        return sp.edit().remove(key).commit();
    }

    public static void deleteAll(Context mContext) {
        SharedPreferences sp = mContext.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        sp.edit().clear().apply();
    }

    //键 值
    public static void putLong(Context mContext, String key, long value) {
        SharedPreferences sp = mContext.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        sp.edit().putLong(key,value).apply();
    }

    //键 默认值
    public static long getLong(Context mContext, String key, long defValue) {
        SharedPreferences sp = mContext.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        return sp.getLong(key, defValue);
    }



}
