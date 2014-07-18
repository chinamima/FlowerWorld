package com.flowerworld.app.ui.activity.friend;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import com.flowerworld.app.R;
import com.flowerworld.app.tool.util.GsonJsonUtil;
import com.flowerworld.app.ui.base.BaseActivity;
import com.flowerworld.app.ui.dialog.PhotoUploadDialog;
import com.flowerworld.app.ui.widget.PhotoShow;
import com.flowerworld.app.ui.widget.PhotoShow.IPhotoShowDelete;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public abstract class BaseSendMessagePageActivity extends BaseActivity implements OnClickListener {

    protected Button btnUploadPhoto = null;
    protected Button btnSendMessage = null;
    protected EditText edtTitle = null;
    protected EditText edtContent = null;
    protected LinearLayout layoutPhotoShow = null;

    protected static final int REQUEST_CODE_PHOTO_UPLOAD_DIALOG = 0X12342;

    public static final String INTENT_KEY_FRIEND_ID = "friend_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.send_message);
        initialize();
    }

    private void initialize() {
        initView();
        initEvent();
    }

    private void initView() {
        initBanner(R.id.banner_layout, R.drawable.banner, R.string.send_message_title, 0, R.string.go_back,
                R.drawable.button_corner_rectangle_selector, 0, 0, new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                }, null);

        edtTitle = (EditText) findViewById(R.id.send_message_text_title);
        edtContent = (EditText) findViewById(R.id.send_message_text_content);
        layoutPhotoShow = (LinearLayout) findViewById(R.id.send_message_layout_photo_show);
        btnUploadPhoto = (Button) findViewById(R.id.send_message_button_upload_photo);
        btnSendMessage = (Button) findViewById(R.id.send_message_button_send_msg);
    }

    private void initEvent() {
        btnUploadPhoto.setOnClickListener(this);
        btnSendMessage.setOnClickListener(this);
    }

    private IPhotoShowDelete photoShowDeleteLinstener = new IPhotoShowDelete() {

        @Override
        public void deleteSelf(PhotoShow self) {
            layoutPhotoShow.removeView(self);
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.send_message_button_upload_photo:
            showPhotoUploadDialog();
            break;

        case R.id.send_message_button_send_msg:
            sendMessageToFriend();
            break;

        default:
            break;
        }
    }

    private void showPhotoUploadDialog() {
//		PhotoUploadDialog dialog = new PhotoUploadDialog(this, R.style.Theme_Dialog_NoTitle_NoActionBar);
//		dialog.show();

        Intent intent = new Intent(this, PhotoUploadDialog.class);
        startActivityForResult(intent, REQUEST_CODE_PHOTO_UPLOAD_DIALOG);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (RESULT_OK == resultCode) {
            if (requestCode == REQUEST_CODE_PHOTO_UPLOAD_DIALOG) {
                uploadSucceed(data);
            }
        }
    }

    private void uploadSucceed(Intent data) {
        try {
            String result = data.getStringExtra(PhotoUploadDialog.INTENT_KEY_UPLOAD_SUCCEED_DATA);
            JsonObject obj = GsonJsonUtil.parse(result).getAsJsonObject();
            JsonArray dataArr = GsonJsonUtil.optJsonArray(obj.get("data"));

            String url = null;
            for (int i = 0; i < dataArr.size(); i++) {
                obj = GsonJsonUtil.optJsonObject(dataArr.get(i)).getAsJsonObject();
                url = GsonJsonUtil.optString(obj.get("url"), "");
                PhotoShow.addPhotoShow(this, "", url, photoShowDeleteLinstener, layoutPhotoShow);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected abstract void sendMessageToFriend();

    protected String getPhotoDataString() {
        JsonArray arr = new JsonArray();
        JsonObject obj = null;
        View v = null;
        String title = null;
        String image = null;
        for (int i = 0; i < layoutPhotoShow.getChildCount(); i++) {
            v = layoutPhotoShow.getChildAt(i);
            if (v instanceof PhotoShow) {
                title = ((PhotoShow) v).getPhotoTitle();
                image = ((PhotoShow) v).getPhotoSrc();

                obj = new JsonObject();
                obj.addProperty("image", image);
                obj.addProperty("title", title);
                arr.add(obj);
            }
        }

        return arr.toString();
    }
}
