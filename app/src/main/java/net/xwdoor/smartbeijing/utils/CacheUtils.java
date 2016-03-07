package net.xwdoor.smartbeijing.utils;

import android.content.Context;

/**
 * Created by XiaoWei on 2016/1/25 025.
 */
public class CacheUtils {

    public static void setCache(Context ctx, String url, String json){
        PrefUtils.setString(ctx,url,json);
    }

    public static String getCache(Context ctx, String url){
        return PrefUtils.getString(ctx,url,null);
    }
}
