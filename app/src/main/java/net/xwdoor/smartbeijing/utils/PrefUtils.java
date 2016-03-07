package net.xwdoor.smartbeijing.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by XiaoWei on 2016/1/18 018.
 */
public class PrefUtils {

    public static boolean getBoolean(Context context, String key, boolean defValue){
        SharedPreferences sp = context.getSharedPreferences("config", context.MODE_PRIVATE);
        return sp.getBoolean(key,defValue);
    }

    public static void setBoolean(Context context,String key,boolean value){
        SharedPreferences sp = context.getSharedPreferences("config", context.MODE_PRIVATE);
        sp.edit().putBoolean(key,value).apply();
    }

    public static int getInt(Context context, String key, int defValue){
        SharedPreferences sp = context.getSharedPreferences("config", context.MODE_PRIVATE);
        return sp.getInt(key, defValue);
    }

    public static void setInt(Context context,String key,int value){
        SharedPreferences sp = context.getSharedPreferences("config", context.MODE_PRIVATE);
        sp.edit().putInt(key, value).apply();
    }

    public static String getString(Context context, String key, String defValue){
        SharedPreferences sp = context.getSharedPreferences("config", context.MODE_PRIVATE);
        return sp.getString(key, defValue);
    }

    public static void setString(Context context,String key,String value){
        SharedPreferences sp = context.getSharedPreferences("config", context.MODE_PRIVATE);
        sp.edit().putString(key, value).apply();
    }
}
