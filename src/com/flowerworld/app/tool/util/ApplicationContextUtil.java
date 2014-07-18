package com.flowerworld.app.tool.util;

import android.content.Context;

public class ApplicationContextUtil {

    private static Context mApplicationContextHandler = null;

    public static void setActivityHandler(Context context) {
        mApplicationContextHandler = context.getApplicationContext();
    }

    public static Context getApplicationContext() {
        return mApplicationContextHandler;
    }
}
