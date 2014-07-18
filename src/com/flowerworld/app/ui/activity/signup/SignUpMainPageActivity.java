package com.flowerworld.app.ui.activity.signup;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import com.flowerworld.app.R;
import com.flowerworld.app.dao.bean.GlobalConstant;
import com.flowerworld.app.dao.bean.GlobalVariableBean;
import com.flowerworld.app.interf.IHttpProcess;
import com.flowerworld.app.tool.http.HttpRequestFacade;
import com.flowerworld.app.tool.util.GsonJsonUtil;
import com.flowerworld.app.tool.util.Md5Utils;
import com.flowerworld.app.tool.util.ToastUtil;
import com.flowerworld.app.tool.util.UnitUtil;
import com.flowerworld.app.ui.base.BaseActivity;
import com.flowerworld.app.ui.widget.Banner;
import com.google.gson.JsonObject;

import java.util.Map;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpMainPageActivity extends BaseActivity {
    private boolean verifyUsername = false;
    private boolean verifyPassword = false;
    private boolean verifyPasswordAgain = false;
    private boolean verifyEmail = false;

    private String mTextUsername = null;
    private String mTextPassword = null;
    private String mTextEmail = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up_main);

        initView();
    }

    private void initView() {
        RelativeLayout bannerlayout = (RelativeLayout) findViewById(R.id.banner_layout);
        RelativeLayout.LayoutParams lp_banner = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT,
                RelativeLayout.LayoutParams.FILL_PARENT);
        bannerlayout.addView(initBanner(), lp_banner);

//		initBottomBtn();
        initNextButton();
//		initBottomBtnSelected();
//		initText(R.id.sign_up_text_username);
//		initText(R.id.sign_up_text_password);
//		initText(R.id.sign_up_text_password_again);
//		initText(R.id.sign_up_text_email);

        initVerify();
    }

    private Banner initBanner() {
        Banner banner = new Banner(this);
        banner.setBackgroundResource(R.drawable.banner);
        banner.getTitleView().setText(R.string.signup_title);
        LayoutParams lp = (LayoutParams) banner.getTitleView().getLayoutParams();
        lp.width = UnitUtil.transformDipToPx(100);
        lp.height = UnitUtil.transformDipToPx(35);

        //left button
        Button left = new Button(this);
        left.setText(R.string.go_back);
        left.setTextSize(14);
        left.setBackgroundResource(R.drawable.button_corner_rectangle_selector);
        left.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
        banner.addLeftItem(left);

        //right button
        Button right = new Button(this);
        right.setText(R.string.signup_declare);
        right.setTextSize(14);
        right.setBackgroundResource(R.drawable.button_corner_rectangle_selector);
        right.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
//				Intent intent = new Intent(SignUpMainPageActivity.this, SignUpSelectTypePageActivity.class);
//				SignUpMainPageActivity.this.startActivity(intent);
            }
        });
        banner.addRightItem(right);

        return banner;
    }

    private void goNextPage() {
        Intent intent = new Intent(SignUpMainPageActivity.this, SignUpSelectTypePageActivity.class);
        SignUpMainPageActivity.this.startActivity(intent);
    }

    private void initNextButton() {
        findViewById(R.id.sign_up_next).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
//				goNextPage();

                if (!verify()) {
                    return;
                }

                requestHttp(new IHttpProcess() {

                    @Override
                    public String processUrl(int sign) {
                        return GlobalVariableBean.APIRoot + GlobalConstant.URL_SIGN_UP_MAIN;
                    }

                    @Override
                    public boolean processResponseSucceed(String resultStr, int sign) throws Exception {
                        JsonObject resultObj = GsonJsonUtil.parse(resultStr).getAsJsonObject();
                        JsonObject data = resultObj.get(HttpRequestFacade.RESULT_PARAMS_RESULT).getAsJsonObject();
                        GlobalVariableBean.username = GsonJsonUtil.optString(data.get("code"), "");
                        GlobalVariableBean.sessionId = GsonJsonUtil.optString(data.get("sessionId"), "");

                        goNextPage();
                        return true;
                    }

                    @Override
                    public boolean processResponseFailed(String resultStr, int sign) throws Exception {
                        return false;
                    }

                    @Override
                    public void processParams(Map<String, Object> params, int sign) {
                        params.put("code", mTextUsername);
                        params.put("vcode", Md5Utils.md5(mTextUsername + GlobalVariableBean.verifyCode));
                        params.put("password", mTextPassword);
                        params.put("email", mTextEmail);
                    }
                });
            }
        });
    }

    private boolean verify() {
        boolean verifyCheck = ((CheckBox) findViewById(R.id.signup_consent_agreement_text)).isChecked();
        if (true == verifyCheck && true == verifyUsername && true == verifyPassword && true == verifyPasswordAgain
                && true == verifyEmail) {
            return true;
        }

        ToastUtil.show(this, "请完成必填项");
        return false;
    }

    private void initVerify() {
        final EditText username = (EditText) findViewById(R.id.sign_up_edit_username);
        username.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                mTextUsername = s.toString();

                if (6 > s.length()) {
                    username.setError("需大于6个字符");
                } else if (!match(s.toString(), "[0-9a-zA-z]+")) {
                    username.setError("需由英文和数字组成");
                } else {
                    verifyUsername = true;
                    return;
                }
                verifyUsername = false;
            }
        });

        final EditText password = (EditText) findViewById(R.id.sign_up_edit_password);
        password.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                mTextPassword = s.toString();

                if (6 > s.length()) {
                    password.setError("需大于6个字符");
                } else if (!match(s.toString(), "[0-9a-zA-z]+")) {
                    password.setError("需由英文和数字组成");
                } else {
                    verifyPassword = true;
                    return;
                }
                verifyPassword = false;
            }
        });

        final EditText passwordAgain = (EditText) findViewById(R.id.sign_up_edit_password_again);
        passwordAgain.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().equals(password.getText().toString())) {
                    passwordAgain.setError("两次输入的密码不匹配");
                } else {
                    verifyPasswordAgain = true;
                    return;
                }
                verifyPasswordAgain = false;
            }
        });

        final EditText email = (EditText) findViewById(R.id.sign_up_edit_email);
        email.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                mTextEmail = s.toString();

                if (1 > s.length()) {
                    email.setError("请输入邮箱");
                } else if (!match(s.toString(),
                        "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$")) {
                    email.setError("您输入的邮箱格式不正确");
                } else {
                    verifyEmail = true;
                    return;
                }
                verifyEmail = false;
            }
        });
    }

    private boolean match(String s, String patternStr) {
        Pattern p = Pattern.compile(patternStr);

        Matcher m = p.matcher(s);
        while (m.find()) {
            MatchResult mr = m.toMatchResult();
            String dstR = mr.group(0);

            if (s.equals(dstR)) {
                return true;
            }
        }

        return false;
    }

//	private void initText(int id)
//	{
//		TextView text = (TextView) findViewById(id);
//		TextJustification.justify(text, text.getWidth());
//	}

}
