package com.flowerworld.app.tool.util;

import android.os.AsyncTask;
import android.os.Looper;

public class ThreadUtil {
    public static long mUIThreadId = -1;

    public static void init() {
        mUIThreadId = Looper.getMainLooper().getThread().getId();
    }


    /**
     * 用来启动新线程
     *
     * @param action         操作内容
     * @param forceNewThread 是否强制启动新线程
     * @return 0--未启动新线程，1--在UI线程中启动新线程，2--在子线程中启动新线程，-1--action为空；
     */
    public static int runOnNotMainThread(final boolean forceNewThread, final Runnable action) {
        if (null == action) {
            return -1;
        }

        if (mUIThreadId == Thread.currentThread().getId()) {
            new AsyncTask<Void, Void, Void>() {

                @Override
                protected Void doInBackground(Void... params) {
                    action.run();
                    return null;
                }
            }.execute();

            return 1;
        } else if (forceNewThread) {
            new Thread(action).start();
            return 2;
        } else {
            action.run();
            return 0;
        }
    }

//	public static void runOnUiThread(Runnable action)
//	{
//		UIBase.activity.runOnUiThread(action);
//	}
}
