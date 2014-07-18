package com.flowerworld.app;

import android.app.Application;
import android.content.Context;
import com.flowerworld.app.dao.bean.GlobalVariableBean;
import com.flowerworld.app.tool.util.ApplicationContextUtil;
import com.flowerworld.app.tool.util.ThreadUtil;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

public class FlowerApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        ApplicationContextUtil.setActivityHandler(this);
        ThreadUtil.init();
        initImageLoader(getApplicationContext());
        initUser();
    }

    public static void initImageLoader(Context context) {
        // This configuration tuning is custom. You can tune every option, you may tune some of them,
        // or you can create default configuration by
        //  ImageLoaderConfiguration.createDefault(this);
        // method.
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context).threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory().discCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO).enableLogging() // Not necessary in common
                .build();
        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config);
    }

    private void initUser() {
        GlobalVariableBean.getDefaultUser(this);
    }
}
