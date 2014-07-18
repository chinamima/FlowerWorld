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
import com.flowerworld.app.tool.helper.GoEnterpriseTypeSelectPage;
import com.flowerworld.app.tool.helper.GoProvinceCitySelectPage;
import com.flowerworld.app.tool.util.LOG;
import com.flowerworld.app.tool.util.Md5Utils;
import com.flowerworld.app.tool.util.ToastUtil;
import com.flowerworld.app.ui.activity.list.EnterpriseTypeOneSelectPageActivity;
import com.flowerworld.app.ui.base.BaseActivity;
import com.flowerworld.app.ui.widget.SingleSelectButton;
import com.google.gson.Gson;

import java.util.List;
import java.util.Map;

public class SignUpNewEnterprisePageActivity extends BaseActivity {

    private NotEmptyReferenceValue mRefEnterpriseName = new NotEmptyReferenceValue();
    private NotEmptyReferenceValue mRefEnterpriseShort = new NotEmptyReferenceValue();
    private NotEmptyReferenceValue mRefRealName = new NotEmptyReferenceValue();
    private NotEmptyReferenceValue mRefDepartment = new NotEmptyReferenceValue();
    private NotEmptyReferenceValue mRefPhone1 = new NotEmptyReferenceValue();

    private SingleSelectButton mSelectGerder = null;

    public static final int REQUEST_CODE_LOCATION = 0X12340;
    public static final int REQUEST_CODE_TYPE_1 = 0X12341;
    public static final int REQUEST_CODE_TYPE_2 = 0X12342;
    public static final int REQUEST_CODE_TYPE_3 = 0X12343;

    //form value
    private FormEnterpriseStaffBean bean = new FormEnterpriseStaffBean();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up_new_enterprise);

        initView();
    }

    private void initView() {
        initBanner(R.id.banner_layout, R.drawable.banner, R.string.signup_newenterprise_title, 0, R.string.go_back,
                R.drawable.button_corner_rectangle_selector, R.string.signup_newenterprise_banner_right,
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
        findViewById(R.id.sign_up_new_enterprise_button_enterprise_location).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                GoProvinceCitySelectPage.startSingleSelect(SignUpNewEnterprisePageActivity.this, REQUEST_CODE_LOCATION);
            }
        });
        findViewById(R.id.sign_up_new_enterprise_button_enterprise_type_1).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                GoEnterpriseTypeSelectPage.goEnterpriseTypeSelectPage(SignUpNewEnterprisePageActivity.this, REQUEST_CODE_TYPE_1);
            }
        });
        findViewById(R.id.sign_up_new_enterprise_button_enterprise_type_2).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                GoEnterpriseTypeSelectPage.goEnterpriseTypeSelectPage(SignUpNewEnterprisePageActivity.this, REQUEST_CODE_TYPE_2);
            }
        });
        findViewById(R.id.sign_up_new_enterprise_button_enterprise_type_3).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                GoEnterpriseTypeSelectPage.goEnterpriseTypeSelectPage(SignUpNewEnterprisePageActivity.this, REQUEST_CODE_TYPE_3);
            }
        });
        findViewById(R.id.sign_up_new_enterprise_finish).setOnClickListener(new OnClickListener() {

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
                        Intent intent = new Intent(SignUpNewEnterprisePageActivity.this, SignUpEnterpriseFinishPageActivity.class);
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

        mSelectGerder = (SingleSelectButton) findViewById(R.id.sign_up_new_enterprise_button_personal_gender);
        mSelectGerder.setButtonDrawable(R.drawable.single_select_button_selector);
        mSelectGerder.addSelectButton("男", "1");
        mSelectGerder.addSelectButton("女", "2");

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (RESULT_OK == resultCode) {
            switch (requestCode) {
            case REQUEST_CODE_LOCATION:
                List<String> list = GoProvinceCitySelectPage
                        .onActivityResultSingle(data, R.id.sign_up_new_enterprise_button_enterprise_location, this);
//				ArrayList<String> list = data.getStringArrayListExtra(SingleSelectListPageActivity.KEY_RESULT_NAME_LIST);
//				String text = list.get(0) + " " + list.get(1);
//				((Button) findViewById(R.id.sign_up_new_enterprise_button_enterprise_location)).setText(text);
//				list = data.getStringArrayListExtra(SingleSelectListPageActivity.KEY_RESULT_ID_LIST);
                bean.resideprovince = list.get(0);
                bean.residecity = list.get(1);
                break;

            case REQUEST_CODE_TYPE_1:
//				bean.type1 = dealMultiSelect(data, R.id.sign_up_new_enterprise_button_enterprise_type_1);
                String[] result1 = dealSelectResult(data, R.id.sign_up_new_enterprise_button_enterprise_type_1);
                bean.field4 = result1[0];
                bean.type1 = result1[1];
                break;

            case REQUEST_CODE_TYPE_2:
//				bean.type2 = dealMultiSelect(data, R.id.sign_up_new_enterprise_button_enterprise_type_2);
                String[] result2 = dealSelectResult(data, R.id.sign_up_new_enterprise_button_enterprise_type_1);
                bean.field9 = result2[0];
                bean.type2 = result2[1];
                break;

            case REQUEST_CODE_TYPE_3:
//				bean.type3 = dealMultiSelect(data, R.id.sign_up_new_enterprise_button_enterprise_type_3);
                String[] result3 = dealSelectResult(data, R.id.sign_up_new_enterprise_button_enterprise_type_1);
                bean.field10 = result3[0];
                bean.type3 = result3[1];
                break;

            default:
                break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

//	private String dealMultiSelect(Intent data, int id)
//	{
//		//name
//		String text = data.getStringExtra(MultiSelectListPageActivity.KEY_RESULT_NAME_LIST);
//		JsonArray arr = GsonJsonUtil.parse(text).getAsJsonArray();
//		text = "";
//		for (int i = 0; i < arr.size(); i++)
//		{
//			text += "," + arr.get(i).getAsString();
//		}
//		text = text.substring(1);
//		if (7 < text.length())
//		{
//			text = text.substring(0, 6) + "...";
//		}
//		((TextView) findViewById(id)).setText(text);
//
//		//id
//		text = data.getStringExtra(MultiSelectListPageActivity.KEY_RESULT_ID_LIST);
//		arr = GsonJsonUtil.parse(text).getAsJsonArray();
//		text = "";
//		for (int i = 0; i < arr.size(); i++)
//		{
//			text += arr.get(i).getAsString() + ",";
//		}
//		return text.substring(1);
//	}

    private String[] dealSelectResult(Intent data, int id) {
        String[] result = new String[2];
        //name
        List<String> list = data.getStringArrayListExtra(EnterpriseTypeOneSelectPageActivity.KEY_RESULT_NAME_LIST);
        String text = "";
        for (int i = 0; i < list.size(); i++) {
            text += "," + list.get(i);
        }
        text = text.substring(1);
        result[0] = text;
        if (7 < text.length()) {
            text = text.substring(0, 6) + "...";
        }
        ((TextView) findViewById(id)).setText(text);

//		//id
//		list = data.getStringArrayListExtra(EnterpriseTypeOneSelectPageActivity.KEY_RESULT_ID_LIST);
//		text = "";
//		for (int i = 0; i < list.size(); i++)
//		{
//			text += "|" + list.get(i);
//		}
//		result[0] = text.substring(1);

        //children id
        text = data.getStringExtra(EnterpriseTypeOneSelectPageActivity.KEY_RESULT_CHILD_ID_LIST);
        result[1] = text;

        return result;
    }

    private void initVerify() {
        String error = "不能为空";
        //
        EditText enterpriseName = (EditText) findViewById(R.id.sign_up_new_enterprise_edit_enterprise_name);
//		TextWatcher enterpriseNameTw = new NotEmptyTextWatcher(enterpriseName, mRefEnterpriseName);
        enterpriseName.addTextChangedListener(new NotEmptyTextWatcher(enterpriseName, mRefEnterpriseName));
        enterpriseName.setError(error);
        //
        EditText enterpriseShort = (EditText) findViewById(R.id.sign_up_new_enterprise_edit_enterprise_short);
//		TextWatcher enterpriseShortTw = new NotEmptyTextWatcher(enterpriseShort, mRefEnterpriseShort);
        enterpriseShort.addTextChangedListener(new NotEmptyTextWatcher(enterpriseShort, mRefEnterpriseShort));
        enterpriseShort.setError(error);
        //
        EditText realName = (EditText) findViewById(R.id.sign_up_new_enterprise_edit_personal_real_name);
//		TextWatcher realNameTw = new NotEmptyTextWatcher(realName, mRefRealName);
        realName.addTextChangedListener(new NotEmptyTextWatcher(realName, mRefRealName));
        realName.setError(error);
        //
        EditText department = (EditText) findViewById(R.id.sign_up_new_enterprise_edit_personal_dept);
//		TextWatcher departmentTw = new NotEmptyTextWatcher(department, mRefDepartment);
        department.addTextChangedListener(new NotEmptyTextWatcher(department, mRefDepartment));
        department.setError(error);
        //
        EditText phone1 = (EditText) findViewById(R.id.sign_up_new_enterprise_edit_personal_phone_1);
//		TextWatcher phone1Tw = new NotEmptyTextWatcher(phone1, mRefPhone1);
        phone1.addTextChangedListener(new NotEmptyTextWatcher(phone1, mRefPhone1));
        phone1.setError(error);
    }

    private boolean verify() {
        bean.company = ((TextView) findViewById(R.id.sign_up_new_enterprise_edit_enterprise_name)).getText().toString();
        bean.field1 = ((TextView) findViewById(R.id.sign_up_new_enterprise_edit_enterprise_short)).getText().toString();
        bean.realname = ((TextView) findViewById(R.id.sign_up_new_enterprise_edit_personal_real_name)).getText().toString();
        bean.gender = ((SingleSelectButton) findViewById(R.id.sign_up_new_enterprise_button_personal_gender)).getSelectedValue()
                .toString();
        bean.position = ((TextView) findViewById(R.id.sign_up_new_enterprise_edit_personal_dept)).getText().toString();
        bean.mobile = ((TextView) findViewById(R.id.sign_up_new_enterprise_edit_personal_phone_1)).getText().toString();
        bean.field11 = ((TextView) findViewById(R.id.sign_up_new_enterprise_edit_personal_phone_2)).getText().toString();
        bean.telephone = ((TextView) findViewById(R.id.sign_up_new_enterprise_edit_personal_tel)).getText().toString();
        bean.field6 = ((TextView) findViewById(R.id.sign_up_new_enterprise_edit_personal_fax)).getText().toString();
        bean.qq = ((TextView) findViewById(R.id.sign_up_new_enterprise_edit_personal_qq)).getText().toString();

        if (TextUtils.isEmpty(bean.company) || TextUtils.isEmpty(bean.field1) || TextUtils.isEmpty(bean.realname)
                || TextUtils.isEmpty(bean.gender) || TextUtils.isEmpty(bean.position) || TextUtils.isEmpty(bean.mobile)
                || TextUtils.isEmpty(bean.resideprovince) || TextUtils.isEmpty(bean.residecity) || TextUtils.isEmpty(bean.field4)
                || TextUtils.isEmpty(bean.type1)) {
            ToastUtil.show(this, "请完成必填项");
            return false;
        }

        return true;
    }

}
