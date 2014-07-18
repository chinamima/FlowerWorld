package com.flowerworld.app.tool.util;

import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import java.lang.ref.SoftReference;
import java.util.HashMap;

public class AsyncImageLoader {
    public interface ImageCallback {
        public void imageLoaded(Drawable imageDrawable, String imageUrl);
    }

    private static final String Tag = "AsyncImageLoader";

    private HashMap<String, SoftReference<Drawable>> imageCache;

    private static AsyncImageLoader instance = new AsyncImageLoader();

    public static AsyncImageLoader getInstance() {
        return instance;
    }

    private AsyncImageLoader() {
        imageCache = new HashMap<String, SoftReference<Drawable>>();
    }

    /**
     * 加载网络图片
     *
     * @param imageUrl
     * @param isNew
     * @param callback
     * @return
     */
    private Drawable loadDrawable(final String imageUrl, boolean isNew, final ImageCallback callback) {
        Log.i(Tag, imageUrl);
        if (!isNew && imageCache.containsKey(imageUrl)) {
            SoftReference<Drawable> softReference = imageCache.get(imageUrl);
            Drawable drawable = softReference.get();
            if (drawable != null) {
                return drawable;
            }
        }
        final Handler handler = new Handler() {
            public void handleMessage(Message message) {
                if (callback != null) {
                    callback.imageLoaded((Drawable) message.obj, imageUrl);
                }
            }
        };

        if (TextUtils.isEmpty(imageUrl)) {
            return null;
        }

        if (imageUrl.startsWith("http:")) {
            LOG.i(Tag, "imageCache not contain down from http" + imageUrl);
            //建立新一个新的线程下载图片
            new Thread() {
                @Override
                public void run() {
                    Drawable drawable = null;
                    try {
                        drawable = ImageUtil.geRoundDrawableFromUrl(imageUrl, 1);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    imageCache.put(imageUrl, new SoftReference<Drawable>(drawable));

                    Message message = handler.obtainMessage(0, drawable);
                    handler.sendMessage(message);
                }
            }.start();
        } else {
            LOG.e(Tag, "The image is not from url!");
        }
        return null;
    }

    public static Drawable setImageAsync(final View view, final String imgPath, boolean isNew, final String defaultImg) {
        Drawable img = AsyncImageLoader.getInstance().loadDrawable(imgPath, isNew, new ImageCallback() {
            @Override
            public void imageLoaded(Drawable imageDrawable, String imageUrl) {
                if (imgPath != null) {
                    view.setBackgroundDrawable(imageDrawable);
                } else if (null != defaultImg) {
                    int id = ApplicationContextUtil
                            .getApplicationContext()
                            .getResources()
                            .getIdentifier(defaultImg, "drawable",
                                    ApplicationContextUtil.getApplicationContext().getPackageName());
                    if (id == 0) {
                        return;
                    }
                    view.setBackgroundResource(id);
                }
            }
        });

        if (img != null && view != null) {
            view.setBackgroundDrawable(img);
        }
        return img;
    }
}
