package com.flowerworld.app.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.RelativeLayout;
import com.flowerworld.app.dao.bean.GlobalConstant;
import com.flowerworld.app.dao.bean.GlobalVariableBean;
import com.flowerworld.app.interf.IHttpProcess;
import com.flowerworld.app.tool.util.FileUtility;
import com.flowerworld.app.tool.util.GsonJsonUtil;
import com.flowerworld.app.tool.util.InputStreamUtility;
import com.flowerworld.app.tool.util.LOG;
import com.flowerworld.app.ui.base.BaseActivity;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.charset.Charset;
import java.util.Map;

public class StartPageActivity extends BaseActivity {

    private int gowhere = -1;
    private ReferenceBoolean mFinishRegNote = new ReferenceBoolean();
    private ReferenceBoolean mFinishRegPotocol = new ReferenceBoolean();
    private ReferenceBoolean mFinishProvince = new ReferenceBoolean();
    private ReferenceBoolean mFinishCity = new ReferenceBoolean();
    private ReferenceBoolean mFinishCompanyType = new ReferenceBoolean();
    private ReferenceBoolean mFinishProductType = new ReferenceBoolean();

    private class ReferenceBoolean {
        boolean finish = false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        RelativeLayout r = new RelativeLayout(this);
        r.setBackgroundColor(Color.RED);
        setContentView(r);

        initView();
    }

    protected void initView() {
//        GlobalVariableBean.getDefaultUser(this);

        requestHttp(new IHttpProcess() {

            @Override
            public String processUrl(int sign) {
                return "http://mysql.flowerworld.cn/app/index/index/mb/andirod/";
            }

            @Override
            public boolean processResponseSucceed(String resultStr, int sign) {
                LOG.d(TAG, "===processResponseSucceed=resultStr: " + resultStr);
                doCheck(resultStr);
                return true;
            }

            @Override
            public boolean processResponseFailed(String resultStr, int sign) {
                LOG.d(TAG, "===processResponseFailed=resultStr: " + resultStr);
                doCheck(resultStr);
                goNextPageWait.execute();
                return false;
            }

            @Override
            public void processParams(Map<String, Object> params, int sign) {

            }
        });
    }

    private void doCheck(String resultStr) {
        JsonObject remoteVersionJson = GsonJsonUtil.parse(resultStr).getAsJsonObject();
        JsonObject localVersionJson = readVersion();

        //common
//		saveFile(GlobalConstant.VERSION_FILE_NAME, resultStr);
        GlobalVariableBean.APIRoot = remoteVersionJson.get(GlobalConstant.APIRoot).getAsString();
        GlobalVariableBean.indexBBSCategory = remoteVersionJson.get(GlobalConstant.indexBBSCategory).getAsJsonObject();
        GlobalVariableBean.indexNewsCategory = remoteVersionJson.get(GlobalConstant.indexNewsCategory).getAsJsonObject();

        if (null == localVersionJson) {
            gowhere = 0;
            boolean isContinue = checkAppVersion(remoteVersionJson, null);

//			if (!isContinue)
//				return;

            JsonObject versionsRemote = GsonJsonUtil.optJsonObject(remoteVersionJson.get(GlobalConstant.versions));
            checkVersionsChild(GsonJsonUtil.optJsonObject(versionsRemote.get(GlobalConstant.regNote)), null,
                    GlobalConstant.regNote + ".txt", mFinishRegNote);
            checkVersionsChild(GsonJsonUtil.optJsonObject(versionsRemote.get(GlobalConstant.regPotocol)), null,
                    GlobalConstant.regPotocol + ".txt", mFinishRegPotocol);
            checkVersionsChild(GsonJsonUtil.optJsonObject(versionsRemote.get(GlobalConstant.province)), null,
                    GlobalConstant.province + ".txt", mFinishProvince);
            checkVersionsChild(GsonJsonUtil.optJsonObject(versionsRemote.get(GlobalConstant.city)), null, GlobalConstant.city
                    + ".txt", mFinishCity);
            checkVersionsChild(GsonJsonUtil.optJsonObject(versionsRemote.get(GlobalConstant.companyType)), null,
                    GlobalConstant.companyType + ".txt", mFinishCompanyType);
            checkVersionsChild(GsonJsonUtil.optJsonObject(versionsRemote.get(GlobalConstant.productType)), null,
                    GlobalConstant.productType + ".txt", mFinishProductType);
        } else {
            gowhere = 1;
            boolean isContinue = checkAppVersion(remoteVersionJson,
                    GsonJsonUtil.optJsonObject(localVersionJson.get(GlobalConstant.appVersion)));

//			if (!isContinue)
//				return;

            JsonObject versionsRemote = GsonJsonUtil.optJsonObject(remoteVersionJson.get(GlobalConstant.versions),
                    new JsonObject());
            JsonObject versionsLocal = GsonJsonUtil
                    .optJsonObject(localVersionJson.get(GlobalConstant.versions), new JsonObject());
            checkVersionsChild(GsonJsonUtil.optJsonObject(versionsRemote.get(GlobalConstant.regNote)),
                    GsonJsonUtil.optJsonObject(versionsLocal.get(GlobalConstant.regNote)), GlobalConstant.regNote + ".txt",
                    mFinishRegNote);
            checkVersionsChild(GsonJsonUtil.optJsonObject(versionsRemote.get(GlobalConstant.regPotocol)),
                    GsonJsonUtil.optJsonObject(versionsLocal.get(GlobalConstant.regPotocol)), GlobalConstant.regPotocol + ".txt",
                    mFinishRegPotocol);
            checkVersionsChild(GsonJsonUtil.optJsonObject(versionsRemote.get(GlobalConstant.province)),
                    GsonJsonUtil.optJsonObject(versionsLocal.get(GlobalConstant.province)), GlobalConstant.province + ".txt",
                    mFinishProvince);
            checkVersionsChild(GsonJsonUtil.optJsonObject(versionsRemote.get(GlobalConstant.city)),
                    GsonJsonUtil.optJsonObject(versionsLocal.get(GlobalConstant.city)), GlobalConstant.city + ".txt", mFinishCity);
            checkVersionsChild(GsonJsonUtil.optJsonObject(versionsRemote.get(GlobalConstant.companyType)),
                    GsonJsonUtil.optJsonObject(versionsLocal.get(GlobalConstant.companyType)), GlobalConstant.companyType
                            + ".txt", mFinishCompanyType);
            checkVersionsChild(GsonJsonUtil.optJsonObject(versionsRemote.get(GlobalConstant.productType)),
                    GsonJsonUtil.optJsonObject(versionsLocal.get(GlobalConstant.productType)), GlobalConstant.productType
                            + ".txt", mFinishProductType);
        }
    }

    private JsonObject readVersion() {
        JsonObject json = null;
        try {
            FileInputStream is = this.openFileInput(GlobalConstant.VERSION_FILE_NAME);
            String str = InputStreamUtility.toString(is);
            json = new JsonParser().parse(str).getAsJsonObject();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return json;
    }

    private void saveFile(String name, String value) {
        try {
            FileOutputStream os = this.openFileOutput(name, MODE_PRIVATE);
            ByteArrayInputStream is = new ByteArrayInputStream(value.getBytes(Charset.forName("utf-8")));
            byte[] cache = new byte[10 * 1024];
            for (int len = 0; (len = is.read(cache)) != -1; ) {
                os.write(cache, 0, len);
            }
            os.close();
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String readFile(String name) {
        return FileUtility.readFileInData(this, name);
    }

    /**
     * 比较版本号
     *
     * @param remoteVersion 服务端版本
     * @param localVersion  本地版本
     * @return true---版本一致；false---版本不一致
     */
    private boolean compareVersion(String remoteVersion, String localVersion) {
        if (remoteVersion.compareTo(localVersion) > 0) {
            return false;
        }

        return true;
    }

    /**
     * 比较应用版本
     *
     * @param remote
     * @param local
     * @return 是否继续执行
     */
    private boolean checkAppVersion(JsonObject remote, JsonObject local) {
        if (null == remote) {
            return false;
        }

        saveFile(GlobalConstant.VERSION_FILE_NAME, remote.toString());
        remote = GsonJsonUtil.optJsonObject(remote.get(GlobalConstant.appVersion));
        if (null == remote) {
            return false;
        }

        if (null != local && compareVersion(remote.get("version").getAsString(), local.get("version").getAsString())) {
            // to-do

            return false;
        }
        return true;
    }

    private void checkVersionsChild(JsonObject remote, JsonObject local, final String fileName, final ReferenceBoolean finish) {
        if (null == remote) {
            return;
        }

        if (null == local || null == readFile(fileName)
                || !compareVersion(remote.get("version").getAsString(), local.get("version").getAsString())) {
            LOG.e(TAG, "======fileName: " + fileName);
            finish.finish = false;
            final String url = remote.get("url").getAsString();
            requestHttp(new IHttpProcess() {

                @Override
                public String processUrl(int sign) {
                    return url;
                }

                @Override
                public boolean processResponseSucceed(String resultStr, int sign) {
                    LOG.w(TAG, "======fileName: " + fileName);
                    saveFile(fileName, resultStr);
                    finish.finish = true;
                    return true;
                }

                @Override
                public boolean processResponseFailed(String resultStr, int sign) {
                    LOG.w(TAG, "======fileName: " + fileName);
                    saveFile(fileName, resultStr);
                    finish.finish = true;
                    return true;
                }

                @Override
                public void processParams(Map<String, Object> params, int sign) {
                }
            });
        } else {
            finish.finish = true;
        }
    }

    private void goHomePage() {
//		Intent intent = new Intent(this, HomePageActivity.class);
        Intent intent = new Intent(this, MainTabPageActivity.class);
        startActivity(intent);
    }

    private void goModulePage() {
        Intent intent = new Intent(this, ModelPageActivity.class);
        startActivity(intent);
    }

    private AsyncTask<Void, Void, Void> goNextPageWait = new AsyncTask<Void, Void, Void>() {

        @Override
        protected Void doInBackground(Void... params) {
            while (false == mFinishRegNote.finish || false == mFinishRegPotocol.finish || false == mFinishProvince.finish
                    || false == mFinishCity.finish || false == mFinishCompanyType.finish || false == mFinishProductType.finish) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        protected void onPostExecute(Void result) {
            switch (gowhere) {
            case 0:
                goModulePage();
                break;
            case 1:
                goHomePage();
                break;
            default:
                break;
            }
        }

        ;

    };

}
