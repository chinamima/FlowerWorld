package com.flowerworld.app.ui.activity.friend;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import com.flowerworld.app.dao.bean.GlobalConstant;
import com.flowerworld.app.dao.bean.GlobalVariableBean;
import com.flowerworld.app.interf.IHttpProcess;

import java.util.Map;

public class SendManyMessagePageActivity extends BaseSendMessagePageActivity {

    public static final String INTENT_KEY_TITLE_GROUP_NAME = "groupName";

    public static final void goHere(Activity activity, String groupName, String friendIdJson) {
        Intent intent = new Intent(activity, SendManyMessagePageActivity.class);
        intent.putExtra(INTENT_KEY_TITLE_GROUP_NAME, groupName);
        intent.putExtra(INTENT_KEY_FRIEND_ID, friendIdJson);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getBanner().getTitleView().setText(getIntent().getStringExtra(INTENT_KEY_TITLE_GROUP_NAME));
    }

    @Override
    protected void sendMessageToFriend() {
        requestHttp(new IHttpProcess() {

            @Override
            public String processUrl(int sign) {
                return GlobalVariableBean.APIRoot + GlobalConstant.URL_FRIEND_SEND_MESSAGE_MANY;
            }

            @Override
            public boolean processResponseSucceed(String resultStr, int sign) throws Exception {
                finish();
                return false;
            }

            @Override
            public boolean processResponseFailed(String resultStr, int sign) throws Exception {
                return false;
            }

            @Override
            public void processParams(Map<String, Object> params, int sign) {
                params.put(GlobalConstant.sessionId, GlobalVariableBean.sessionId);
                params.put("memberId", GlobalVariableBean.userInfo.memberId);
                params.put("message", edtContent.getText().toString());
                params.put("title", edtTitle.getText().toString());
                params.put("users", getIntent().getStringExtra(INTENT_KEY_FRIEND_ID));
                params.put("image", getPhotoDataString());
            }
        });
    }

}
