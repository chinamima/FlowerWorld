package com.flowerworld.app.ui.base;

import android.os.AsyncTask;

import java.util.LinkedList;
import java.util.List;

@SuppressWarnings("rawtypes")
public class ActivityRequestThreadManager {
    private List<AsyncTask> mRequestThreadList = new LinkedList<AsyncTask>();

    /**
     * 记录http请求线程
     *
     * @param at http请求线程
     */
    public void registerRequestThread(AsyncTask at) {
        mRequestThreadList.add(at);
    }

    /**
     * 去除已完成的http请求线程
     *
     * @param at http请求线程
     */
    public void removeRequestThread(AsyncTask at) {
        mRequestThreadList.remove(at);
    }

    public void stopAllCurrentActivityRequestThreads() {
        for (AsyncTask at : mRequestThreadList) {
            at.cancel(true);
        }
        mRequestThreadList.clear();
    }
}
