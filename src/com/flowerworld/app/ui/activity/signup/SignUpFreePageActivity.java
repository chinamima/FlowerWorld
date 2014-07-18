package com.flowerworld.app.ui.activity.signup;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import com.flowerworld.app.R;
import com.flowerworld.app.dao.bean.FormFreePeopleBean;
import com.flowerworld.app.dao.bean.GlobalConstant;
import com.flowerworld.app.dao.bean.GlobalVariableBean;
import com.flowerworld.app.dao.bean.NotEmptyReferenceValue;
import com.flowerworld.app.interf.IHttpProcess;
import com.flowerworld.app.listener.NotEmptyTextWatcher;
import com.flowerworld.app.tool.helper.GoProvinceCitySelectPage;
import com.flowerworld.app.tool.util.LOG;
import com.flowerworld.app.tool.util.Md5Utils;
import com.flowerworld.app.tool.util.ToastUtil;
import com.flowerworld.app.ui.base.BaseActivity;
import com.flowerworld.app.ui.widget.SingleSelectButton;
import com.google.gson.Gson;

import java.util.List;
import java.util.Map;

public class SignUpFreePageActivity extends BaseActivity {
    private FormFreePeopleBean bean = new FormFreePeopleBean();

    private NotEmptyReferenceValue mRefRealName = new NotEmptyReferenceValue();
    private NotEmptyReferenceValue mRefPhone = new NotEmptyReferenceValue();
    private NotEmptyReferenceValue mRefBio = new NotEmptyReferenceValue();

    private static final int REQUEST_CODE_LOCATION = 0X98437;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up_free);

        initView();
    }

    private void initView() {
        initBanner(R.id.banner_layout, R.drawable.banner, R.string.signup_free_title, 0, R.string.go_back,
                R.drawable.button_corner_rectangle_selector, R.string.signup_free_banner_right,
                R.drawable.button_corner_rectangle_selector, new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                }, new OnClickListener() {

                    @Override
                    public void onClick(View v) {

                    }
                });

//		initBottomBtn();
        initButton();
//		initBottomBtnSelected();

        ((TextView) findViewById(R.id.sign_up_free_edit_username)).setText(GlobalVariableBean.username);
        initVerify();
    }

    private void initButton() {
        findViewById(R.id.sign_up_free_finish).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!verify()) {
                    return;
                }

                requestHttp(new IHttpProcess() {

                    @Override
                    public String processUrl(int sign) {
                        return GlobalVariableBean.APIRoot + GlobalConstant.URL_SIGN_UP_FREE;
                    }

                    @Override
                    public boolean processResponseSucceed(String resultStr, int sign) throws Exception {
                        Intent intent = new Intent(SignUpFreePageActivity.this, SignUpStaffFinishPageActivity.class);
                        startActivity(intent);
                        return true;
                    }

                    @Override
                    public boolean processResponseFailed(String resultStr, int sign) throws Exception {
                        // TODO Auto-generated method stub
                        return false;
                    }

                    @Override
                    public void processParams(Map<String, Object> params, int sign) {
                        Gson gson = new Gson();
                        String jsonStr = gson.toJson(bean);
                        params.putAll(gson.fromJson(jsonStr, params.getClass()));
                        params.put(GlobalConstant.sessionId, GlobalVariableBean.sessionId);
                        params.put(GlobalConstant.vcode,
                                Md5Utils.md5(GlobalVariableBean.sessionId + GlobalVariableBean.verifyCode));
                        params.put("st", "3");
                        LOG.d(TAG, "======processParams=params: " + params);
                    }
                });
            }
        });
        findViewById(R.id.sign_up_free_button_verification).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
//				Intent intent = new Intent(SignUpFreePageActivity.this, MultiSelectListPageActivity.class);
//				startActivity(intent);
            }
        });
        findViewById(R.id.sign_up_free_button_location).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                GoProvinceCitySelectPage.startSingleSelect(SignUpFreePageActivity.this, REQUEST_CODE_LOCATION);
            }
        });

        SingleSelectButton sBtn = (SingleSelectButton) findViewById(R.id.sign_up_free_button_gender);
        sBtn.setButtonDrawable(R.drawable.single_select_button_selector);
        sBtn.addSelectButton("男", "1");
        sBtn.addSelectButton("女", "2");

    }

    private void initVerify() {
        String error = "不能为空";
        //
        EditText realName = (EditText) findViewById(R.id.sign_up_free_edit_real_name);
        realName.addTextChangedListener(new NotEmptyTextWatcher(realName, mRefRealName));
        realName.setError(error);
        //
        EditText department = (EditText) findViewById(R.id.sign_up_free_edit_phone_1);
        department.addTextChangedListener(new NotEmptyTextWatcher(department, mRefPhone));
        department.setError(error);
        //
        EditText phone1 = (EditText) findViewById(R.id.sign_up_free_edit_introduction);
        phone1.addTextChangedListener(new NotEmptyTextWatcher(phone1, mRefBio));
        phone1.setError(error);
    }

    private boolean verify() {
        bean.realname = ((TextView) findViewById(R.id.sign_up_free_edit_real_name)).getText().toString();
        bean.gender = ((SingleSelectButton) findViewById(R.id.sign_up_free_button_gender)).getSelectedValue().toString();
        bean.mobile = ((TextView) findViewById(R.id.sign_up_free_edit_phone_1)).getText().toString();
        bean.smscode = ((TextView) findViewById(R.id.sign_up_free_edit_verification)).getText().toString();
        bean.field11 = ((TextView) findViewById(R.id.sign_up_free_edit_phone_2)).getText().toString();
        bean.telephone = ((TextView) findViewById(R.id.sign_up_free_edit_tel)).getText().toString();
        bean.field6 = ((TextView) findViewById(R.id.sign_up_free_edit_fax)).getText().toString();
        bean.qq = ((TextView) findViewById(R.id.sign_up_free_edit_qq)).getText().toString();
        bean.address = ((TextView) findViewById(R.id.sign_up_free_edit_pecific_address)).getText().toString();
        bean.bio = ((TextView) findViewById(R.id.sign_up_free_edit_introduction)).getText().toString();

        if (TextUtils.isEmpty(bean.realname) || TextUtils.isEmpty(bean.gender) || TextUtils.isEmpty(bean.mobile)
                || TextUtils.isEmpty(bean.bio) || TextUtils.isEmpty(bean.resideprovince) || TextUtils.isEmpty(bean.residecity)) {
            ToastUtil.show(this, "请完成必填项");
            return false;
        }

        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (RESULT_OK == resultCode) {
            switch (requestCode) {
            case REQUEST_CODE_LOCATION:
                List<String> list = GoProvinceCitySelectPage.onActivityResultSingle(data, R.id.sign_up_free_button_location, this);
                bean.resideprovince = list.get(0);
                bean.residecity = list.get(1);
                break;

            default:
                break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
