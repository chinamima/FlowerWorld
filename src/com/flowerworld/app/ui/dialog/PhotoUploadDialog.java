package com.flowerworld.app.ui.dialog;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import com.flowerworld.app.R;
import com.flowerworld.app.dao.bean.GlobalConstant;
import com.flowerworld.app.dao.bean.GlobalVariableBean;
import com.flowerworld.app.interf.IHttpProcess;
import com.flowerworld.app.tool.http.HttpRequestFacade;
import com.flowerworld.app.tool.util.GsonJsonUtil;
import com.flowerworld.app.tool.util.ProgressDialogUtil;
import com.flowerworld.app.ui.base.BaseActivity;
import com.flowerworld.app.ui.widget.PhotoSelect;
import com.flowerworld.app.ui.widget.PhotoSelect.ISelectionChanged;
import com.google.gson.JsonObject;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class PhotoUploadDialog extends BaseActivity implements View.OnClickListener {

    public static final String INTENT_KEY_UPLOAD_SUCCEED_DATA = "INTENT_KEY_UPLOAD_SUCCEED_DATA";

    private PhotoSelect select_1 = null;
    private PhotoSelect select_2 = null;
    private PhotoSelect select_3 = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initialize();
    }

    private void initialize() {
        initView();
        initEvent();
    }

    private void initView() {
        setContentView(R.layout.dialog_photo_upload);

        select_1 = (PhotoSelect) findViewById(R.id.dialog_photo_upload_widget_photo_select_1);
        select_2 = (PhotoSelect) findViewById(R.id.dialog_photo_upload_widget_photo_select_2);
        select_3 = (PhotoSelect) findViewById(R.id.dialog_photo_upload_widget_photo_select_3);
    }

    private void initEvent() {
        findViewById(R.id.dialog_photo_upload_button_goback).setOnClickListener(this);
        findViewById(R.id.dialog_photo_upload_button_upload).setOnClickListener(this);

        select_1.setSelectionChangedListener(selectionChangedListener);
        select_2.setSelectionChangedListener(selectionChangedListener);
        select_3.setSelectionChangedListener(selectionChangedListener);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.dialog_photo_upload_button_goback:
            finish();
            break;

        case R.id.dialog_photo_upload_button_upload:
            uploadPhoto();
            break;

        default:
            break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (RESULT_OK == resultCode) {
            if (requestCode == PhotoSelect.REQUEST_CODE_PHOTO_PICKER) {
                PhotoSelect.getCurrentSelectInstance().onActivityResult(requestCode, resultCode, data);

            }
        }
    }

    private Map<String, String> mapBitmapPath = new HashMap<String, String>();
    private Map<String, String> mapBitmapTitle = new HashMap<String, String>();

    private ISelectionChanged selectionChangedListener = new ISelectionChanged() {

        @Override
        public void onTextChanged(PhotoSelect self, String text) {
            if (null == text) {
                text = "";
            }

            if (mapBitmapPath.containsKey(self.toString())) {
                mapBitmapTitle.put(self.toString(), text);
            } else if (mapBitmapTitle.containsKey(self.toString())) {
                mapBitmapTitle.remove(self.toString());
            }
        }

        @Override
        public void onPhotoSelected(PhotoSelect self, String path) {
            mapBitmapPath.put(self.toString(), path);
        }

        @Override
        public void onPhotoDeleted(PhotoSelect self) {
            if (mapBitmapPath.containsKey(self.toString())) {
                mapBitmapPath.remove(self.toString());
            }

            if (mapBitmapTitle.containsKey(self.toString())) {
                mapBitmapTitle.remove(self.toString());
            }
        }
    };

    private void uploadPhoto() {
        ProgressDialogUtil.show(this, null);

        Map<String, Object> params = new HashMap<String, Object>();
        params.put(GlobalConstant.sessionId, GlobalVariableBean.sessionId);
        params.put("memberId", GlobalVariableBean.userInfo.memberId);
        params.put("model", "BbsCommonMember");
        params.put("tag", TAG);

        if (mapBitmapPath.size() > 0) {
            int count = 1;
            String path = null;
            Entry<String, String> en = null;
            Iterator<Entry<String, String>> it = mapBitmapPath.entrySet().iterator();
            while (it.hasNext()) {
                en = it.next();
                path = en.getValue();

                File f = new File(path);
                if (f.exists()) {
                    params.put("images" + count, f);
                    count++;
                }
            }
        }

        doRequestHttpRequestFacade(params);
    }

    private void doRequestHttpRequestFacade(final Map<String, Object> map) {
        HttpRequestFacade.requestHttp(this, new IHttpProcess() {

            @Override
            public String processUrl(int sign) {
                return GlobalVariableBean.APIRoot + GlobalConstant.URL_UPLOAD_PHOTO;
            }

            @Override
            public boolean processResponseSucceed(String resultStr, int sign) throws Exception {
                Log.d(TAG, "===============processResponseSucceed=resultStr: " + resultStr);
                JsonObject resultObj = GsonJsonUtil.parse(resultStr).getAsJsonObject();
                String error = GsonJsonUtil.optString(resultObj.get("error"), "-1");

                if ("1".equals(error)) {
                    uploadSucceed(GsonJsonUtil.optJsonObject(resultObj.get("result")).toString());
                }

                return true;
            }

            @Override
            public boolean processResponseFailed(String resultStr, int sign) throws Exception {
                Log.d(TAG, "===============processResponseFailed=resultStr: " + resultStr);
                return false;
            }

            @Override
            public void processParams(Map<String, Object> params, int sign) {
                params.putAll(map);
            }
        }, HttpRequestFacade.DEFAULT_SIGN, HttpRequestFacade.REQUEST_POST_FILE, true);
    }

    private void uploadSucceed(String content) {
        Intent data = new Intent();
        data.putExtra(INTENT_KEY_UPLOAD_SUCCEED_DATA, content);
        setResult(RESULT_OK, data);
        finish();
    }
}
