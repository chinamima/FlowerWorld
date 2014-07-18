package com.flowerworld.app.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import com.flowerworld.app.R;
import com.flowerworld.app.dao.bean.GlobalConstant;
import com.flowerworld.app.dao.bean.GlobalVariableBean;
import com.flowerworld.app.dao.bean.UserInfoBean;
import com.flowerworld.app.interf.IHttpProcess;
import com.flowerworld.app.tool.helper.FriendOperationHelper;
import com.flowerworld.app.tool.helper.InputMethodHelper;
import com.flowerworld.app.tool.http.HttpRequestFacade;
import com.flowerworld.app.tool.util.GsonJsonUtil;
import com.flowerworld.app.tool.util.Md5Utils;
import com.flowerworld.app.ui.activity.signup.SignUpMainPageActivity;
import com.flowerworld.app.ui.base.BaseActivity;
import com.google.gson.JsonObject;

import java.util.Map;

public class SignInPageActivity extends BaseActivity {
    private EditText username = null;
    private EditText password = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in_page);

        initView();
        initEvent();
    }

    private void initView() {
        initBanner(R.id.banner_layout, R.drawable.banner, R.string.signin_title, 0, R.string.go_back,
                R.drawable.button_corner_rectangle_selector, 0, 0, new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                }, null);

        username = (EditText) findViewById(R.id.sign_in_username);
        password = (EditText) findViewById(R.id.sign_in_password);
    }

    private void initEvent() {
        findViewById(R.id.sign_in_button_signin).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (verify()) {
                    requestData();
                }
            }
        });
        findViewById(R.id.sign_in_button_signup).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish(false);
                Intent intent = new Intent(SignInPageActivity.this, SignUpMainPageActivity.class);
                startActivity(intent);
            }
        });
    }

    private boolean verify() {
        final String usernameStr = username.getText().toString();
        final String passwordStr = password.getText().toString();

        if (TextUtils.isEmpty(usernameStr)) {
            username.setError("不能为空");
            return false;
        }
        if (TextUtils.isEmpty(passwordStr)) {
            password.setError("不能为空");
            return false;
        }

        return true;
    }

    private void requestData() {
        InputMethodHelper.hideForce(this.getWindow().getDecorView());
        final String usernameStr = username.getText().toString();
        final String passwordStr = password.getText().toString();

        requestHttp(new IHttpProcess() {

            @Override
            public String processUrl(int sign) {
                return GlobalVariableBean.APIRoot + GlobalConstant.URL_SIGN_IN;
            }

            @Override
            public boolean processResponseSucceed(String resultStr, int sign) throws Exception {
                JsonObject obj = GsonJsonUtil.parse(resultStr).getAsJsonObject();
                JsonObject resultObj = obj.get(HttpRequestFacade.RESULT_PARAMS_RESULT).getAsJsonObject();

                GlobalVariableBean.userInfo = GsonJsonUtil.mGson.fromJson(resultObj, UserInfoBean.class);
                GlobalVariableBean.username = usernameStr;
                GlobalVariableBean.sessionId = GlobalVariableBean.userInfo.sessionId;

                Editor editor = getSharedPreferences("default_user", Context.MODE_PRIVATE).edit();
                editor.putString("default_user", resultObj.toString());
                editor.commit();

                FriendOperationHelper.requestFriendData(SignInPageActivity.this, new Runnable() {

                    @Override
                    public void run() {
                        SignInPageActivity.this.finish();
                    }
                });

                return true;
            }

            @Override
            public boolean processResponseFailed(String resultStr, int sign) throws Exception {
                return false;
            }

            @Override
            public void processParams(Map<String, Object> params, int sign) {
                String md5Pwd = Md5Utils.md5(passwordStr);
                params.put("username", usernameStr);
                params.put("pwd", md5Pwd);
                params.put("code", Md5Utils.md5(usernameStr + md5Pwd + GlobalVariableBean.verifyCode));
            }
        });
    }

}
