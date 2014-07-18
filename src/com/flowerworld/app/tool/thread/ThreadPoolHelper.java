package com.flowerworld.app.tool.thread;

import android.content.Context;
import com.flowerworld.app.R;
import com.flowerworld.app.tool.util.AppUtils;
import com.flowerworld.app.tool.util.ToastUtil;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class ThreadPoolHelper {

    private ThreadPoolHelper() {

    }

    private static int CORE_POOL_SIZE = 5;

    private static int MAX_POOL_SIZE = 100;

    private static int KEEP_ALIVE_TIME = 10000;

    private static BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<Runnable>(10);

    private static ThreadFactory threadFactory = new ThreadFactory() {
        private final AtomicInteger integer = new AtomicInteger();

        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, "myThreadPool thread:" + integer.getAndIncrement());
        }
    };

    private static ThreadPoolExecutor threadPool;

    static {
        threadPool = new ThreadPoolExecutor(CORE_POOL_SIZE, MAX_POOL_SIZE, KEEP_ALIVE_TIME, TimeUnit.SECONDS, workQueue,
                threadFactory);
    }

    public static void execute(Runnable runnable, Context context) {
        if (!AppUtils.netWorkIsEnable(context)) {
            ToastUtil.show(context, context.getResources().getString(R.string.network_unenable));
            return;
        } else {
            threadPool.execute(runnable);
        }
    }
}
