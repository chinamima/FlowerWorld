package com.flowerworld.app.dao.bean;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.text.TextUtils;
import com.flowerworld.app.R;
import com.flowerworld.app.dao.base.DaoMaster;
import com.flowerworld.app.dao.base.DaoMaster.DevOpenHelper;
import com.flowerworld.app.dao.base.DaoSession;
import com.flowerworld.app.tool.util.GsonJsonUtil;
import com.google.gson.JsonObject;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

public class GlobalVariableBean {
    private static final String TAG = GlobalVariableBean.class.getSimpleName();

    //{"error":1,"result":{"code":"chinamima111","sessionId":"pjns501iiem2o6v3u2q7p9vae1"}}
    public static String username = "chinamima111";
    public static String sessionId = "pjns501iiem2o6v3u2q7p9vae1";
    public static String verifyCode = "test";
    //	public static UserInfoBean userInfo = null;
    public static UserInfoBean userInfo = null;

    public static final void getDefaultUser(Context context) {
        String userInfoStr = context.getSharedPreferences("default_user", Context.MODE_PRIVATE).getString("default_user", "");
        if (TextUtils.isEmpty(userInfoStr)) {
            return;
        }
        userInfo = GsonJsonUtil.mGson.fromJson(userInfoStr, UserInfoBean.class);
        sessionId = userInfo.sessionId;
    }

    public static String APIRoot = null;
    public static JsonObject indexNewsCategory = null;
    public static JsonObject indexBBSCategory = null;

    public static DisplayImageOptions options = new DisplayImageOptions.Builder().showImageForEmptyUri(R.drawable.image_empty)
            .showImageForEmptyUri(R.drawable.image_empty).showImageOnFail(R.drawable.image_loadfailed)
            .showStubImage(R.drawable.image_empty).resetViewBeforeLoading().cacheInMemory().cacheOnDisc()
            .imageScaleType(ImageScaleType.EXACTLY).bitmapConfig(Bitmap.Config.ARGB_4444)
            .displayer(new FadeInBitmapDisplayer(300)).build();

    private static DevOpenHelper daoHelper = null;
    private static DaoSession daoSession = null;

    public static DaoSession getDaoSession(Context context, String memberId) {
        if (null == daoSession) {
            daoHelper = new DaoMaster.DevOpenHelper(context, memberId + ".db", null);
            SQLiteDatabase db = daoHelper.getWritableDatabase();
            daoSession = new DaoMaster(db).newSession();
        }
        return daoSession;
    }

}
