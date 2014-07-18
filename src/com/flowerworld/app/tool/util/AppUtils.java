package com.flowerworld.app.tool.util;

import android.content.Context;
import android.net.ConnectivityManager;

public class AppUtils {

    /**
     * 检测网络是否可用
     *
     * @param context
     * @return
     */
    public static boolean netWorkIsEnable(Context context) {
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connManager.getActiveNetworkInfo() != null) {
            return connManager.getActiveNetworkInfo().isAvailable();
        }
        return false;
    }

}
