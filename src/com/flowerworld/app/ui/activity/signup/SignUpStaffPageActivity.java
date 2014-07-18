package com.flowerworld.app.ui.activity.signup;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import com.flowerworld.app.R;
import com.flowerworld.app.dao.bean.FormEnterpriseStaffBean;
import com.flowerworld.app.dao.bean.GlobalConstant;
import com.flowerworld.app.dao.bean.GlobalVariableBean;
import com.flowerworld.app.dao.bean.NotEmptyReferenceValue;
import com.flowerworld.app.interf.IHttpProcess;
import com.flowerworld.app.listener.NotEmptyTextWatcher;
import com.flowerworld.app.tool.util.LOG;
import com.flowerworld.app.tool.util.Md5Utils;
import com.flowerworld.app.tool.util.ToastUtil;
import com.flowerworld.app.ui.base.BaseActivity;
import com.flowerworld.app.ui.widget.SingleSelectButton;
import com.google.gson.Gson;

import java.util.Map;

public class SignUpStaffPageActivity extends BaseActivity {
    private NotEmptyReferenceValue mRefRealName = new NotEmptyReferenceValue();
    private NotEmptyReferenceValue mRefDepartment = new NotEmptyReferenceValue();
    private NotEmptyReferenceValue mRefPhone1 = new NotEmptyReferenceValue();

    private SingleSelectButton mSelectGerder = null;

    public static final int REQUEST_CODE_LOCATION = 0X12340;
    public static final int REQUEST_CODE_TYPE_1 = 0X12341;
    public static final int REQUEST_CODE_TYPE_2 = 0X12342;
    public static final int REQUEST_CODE_TYPE_3 = 0X12343;

    //
    public static final String INTENT_KEY_COMPANY_FULL_NAME = "company";
    public static final String INTENT_KEY_COMPANY_SHORT_NAME = "field1";

    //form value
    private FormEnterpriseStaffBean bean = new FormEnterpriseStaffBean();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up_staff);

        initView();
    }

    private void initView() {
        initBanner(R.id.banner_layout, R.drawable.banner, R.string.signup_staff_title, 0, R.string.go_back,
                R.drawable.button_corner_rectangle_selector, R.string.signup_staff_banner_right,
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
        initVerify();
    }

    private void initButton() {
//		findViewById(R.id.sign_up_staff_button_enterprise_location).setOnClickListener(new OnClickListener()
//		{
//
//			@Override
//			public void onClick(View v)
//			{
//				GoProvinceCitySelectPage.startProvinceCitySelect(SignUpStaffPageActivity.this, REQUEST_CODE_LOCATION);
//			}
//		});
        findViewById(R.id.sign_up_staff_finish).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!verify()) {
                    return;
                }

                requestHttp(new IHttpProcess() {

                    @Override
                    public String processUrl(int sign) {
                        return GlobalVariableBean.APIRoot + GlobalConstant.URL_SIGN_UP_ENTERPRISE_STAFF;
                    }

                    @Override
                    public boolean processResponseSucceed(String resultStr, int sign) throws Exception {
                        Intent intent = new Intent(SignUpStaffPageActivity.this, SignUpEnterpriseFinishPageActivity.class);
                        startActivity(intent);
                        return true;
                    }

                    @Override
                    public boolean processResponseFailed(String resultStr, int sign) throws Exception {
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
                        params.put("st", "2");
                        LOG.d(TAG, "======processParams=params: " + params);
                    }
                });
            }
        });

        mSelectGerder = (SingleSelectButton) findViewById(R.id.sign_up_staff_button_personal_gender);
        mSelectGerder.setButtonDrawable(R.drawable.single_select_button_selector);
        mSelectGerder.addSelectButton("男", "1");
        mSelectGerder.addSelectButton("女", "2");

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (RESULT_OK == resultCode) {
            switch (requestCode) {
//			case REQUEST_CODE_LOCATION:
//				ArrayList<String> list = data.getStringArrayListExtra(SingleSelectListPageActivity.KEY_RESULT_NAME_LIST);
//				String text = list.get(0) + " " + list.get(1);
//				((Button) findViewById(R.id.sign_up_staff_button_enterprise_location)).setText(text);
//				list = data.getStringArrayListExtra(SingleSelectListPageActivity.KEY_RESULT_ID_LIST);
//				bean.resideprovince = list.get(0);
//				bean.residecity = list.get(1);
//				break;

            default:
                break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void initVerify() {
        String error = "不能为空";
        //
        EditText realName = (EditText) findViewById(R.id.sign_up_staff_edit_personal_real_name);
//		TextWatcher realNameTw = new NotEmptyTextWatcher(realName, mRefRealName);
        realName.addTextChangedListener(new NotEmptyTextWatcher(realName, mRefRealName));
        realName.setError(error);
        //
        EditText department = (EditText) findViewById(R.id.sign_up_staff_edit_personal_dept);
//		TextWatcher departmentTw = new NotEmptyTextWatcher(department, mRefDepartment);
        department.addTextChangedListener(new NotEmptyTextWatcher(department, mRefDepartment));
        department.setError(error);
        //
        EditText phone1 = (EditText) findViewById(R.id.sign_up_staff_edit_personal_phone_1);
//		TextWatcher phone1Tw = new NotEmptyTextWatcher(phone1, mRefPhone1);
        phone1.addTextChangedListener(new NotEmptyTextWatcher(phone1, mRefPhone1));
        phone1.setError(error);
    }

    private boolean verify() {
        bean.company = getIntent().getStringExtra(INTENT_KEY_COMPANY_FULL_NAME);
        bean.field1 = getIntent().getStringExtra(INTENT_KEY_COMPANY_SHORT_NAME);
        bean.realname = ((TextView) findViewById(R.id.sign_up_staff_edit_personal_real_name)).getText().toString();
        bean.gender = ((SingleSelectButton) findViewById(R.id.sign_up_staff_button_personal_gender)).getSelectedValue()
                .toString();
        bean.position = ((TextView) findViewById(R.id.sign_up_staff_edit_personal_dept)).getText().toString();
        bean.mobile = ((TextView) findViewById(R.id.sign_up_staff_edit_personal_phone_1)).getText().toString();
        bean.field11 = ((TextView) findViewById(R.id.sign_up_staff_edit_personal_phone_2)).getText().toString();
        bean.telephone = ((TextView) findViewById(R.id.sign_up_staff_edit_personal_tel)).getText().toString();
        bean.field6 = ((TextView) findViewById(R.id.sign_up_staff_edit_personal_fax)).getText().toString();
        bean.qq = ((TextView) findViewById(R.id.sign_up_staff_edit_personal_qq)).getText().toString();

        if (TextUtils.isEmpty(bean.company) || TextUtils.isEmpty(bean.field1) || TextUtils.isEmpty(bean.realname)
                || TextUtils.isEmpty(bean.gender) || TextUtils.isEmpty(bean.position) || TextUtils.isEmpty(bean.mobile)) {
            ToastUtil.show(this, "请完成必填项");
            return false;
        }

        return true;
    }

}
