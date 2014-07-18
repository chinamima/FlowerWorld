package com.flowerworld.app.ui.activity.friend;

import com.flowerworld.app.dao.bean.GlobalConstant;
import com.flowerworld.app.dao.bean.GlobalVariableBean;
import com.flowerworld.app.interf.IHttpProcess;

import java.util.Map;

public class SendOneMessagePageActivity extends BaseSendMessagePageActivity {

    @Override
    protected void sendMessageToFriend() {
        requestHttp(new IHttpProcess() {

            @Override
            public String processUrl(int sign) {
                return GlobalVariableBean.APIRoot + GlobalConstant.URL_FRIEND_SEND_MESSAGE_ONE;
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
                params.put("touid", getIntent().getStringExtra(INTENT_KEY_FRIEND_ID));
                params.put("image", getPhotoDataString());
            }
        });
    }

}
