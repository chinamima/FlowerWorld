package com.flowerworld.app.ui.fragment.background;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.flowerworld.app.R;
import com.flowerworld.app.dao.bean.GlobalConstant;
import com.flowerworld.app.dao.bean.GlobalVariableBean;
import com.flowerworld.app.interf.IHttpProcess;
import com.flowerworld.app.tool.http.HttpRequestFacade;
import com.flowerworld.app.tool.util.GsonJsonUtil;
import com.flowerworld.app.ui.base.BaseFragment;
import com.google.gson.JsonObject;

import java.util.Map;

/**
 * Created by guojj4 on 14/08/10.
 */
public class EnterpriseBackgroundFragment extends BaseFragment implements View.OnClickListener {

    private TextView mTextPreview = null;
    private TextView mTextCompany = null;
    private Button mBtnSortManage = null;
    private TextView mTextMemberLevel = null;
    private TextView mTextSignUpTime = null;
    private TextView mTextValidTime = null;
    private TextView mTextMemberType = null;
    private TextView mTextHomepageVisitCount = null;
    private TextView mTextProductVisitCount = null;
    private Button mBtnPublishProduct = null;
    private TextView mTextHelper = null;

    private EnterpriseBackgroundFragment() {
    }
    private static EnterpriseBackgroundFragment mSingle = new EnterpriseBackgroundFragment();

    public static EnterpriseBackgroundFragment newInstance() {
        return  mSingle;
    }

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.background_welcome_page, container, false);
    }

    @Override
    protected void initView() {
        mTextPreview = (TextView) findViewById(R.id.welcome_text_preview);
        mTextCompany = (TextView) findViewById(R.id.welcome_text_company);
        mBtnSortManage = (Button) findViewById(R.id.welcome_button_sort_manage);
        mTextMemberLevel = (TextView) findViewById(R.id.welcome_text_member_level);
        mTextSignUpTime = (TextView) findViewById(R.id.welcome_text_sign_up_time);
        mTextValidTime = (TextView) findViewById(R.id.welcome_text_valid_time);
        mTextMemberType = (TextView) findViewById(R.id.welcome_text_member_type);
        mTextHomepageVisitCount = (TextView) findViewById(R.id.welcome_text_homepage_visit_count);
        mTextProductVisitCount = (TextView) findViewById(R.id.welcome_text_product_visit_count);
        mBtnPublishProduct = (Button) findViewById(R.id.welcome_button_publish_product);
        mTextHelper = (TextView) findViewById(R.id.welcome_text_helper);

        mTextPreview.setOnClickListener(this);
        mBtnSortManage.setOnClickListener(this);
        mBtnPublishProduct.setOnClickListener(this);
        mTextHelper.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        requestHttpData();
    }

    @Override
    protected void resumeData() {
        dealDate((JsonObject) getSavedData());
    }

    private void requestHttpData() {
        requestHttp(new IHttpProcess() {
            @Override
            public String processUrl(int sign) {
                return GlobalVariableBean.APIRoot + GlobalConstant.URL_ENTERPRISE_WELCOME;
            }

            @Override
            public void processParams(Map<String, Object> params, int sign) {
                params.put(GlobalConstant.sessionId, GlobalVariableBean.sessionId);
                params.put("memberId", GlobalVariableBean.userInfo.memberId);
            }

            @Override
            public boolean processResponseSucceed(String resultStr, int sign) throws Exception {
                JsonObject resultObj = HttpRequestFacade.paserResultObj(resultStr);
                dealDate(resultObj);

                return true;
            }

            @Override
            public boolean processResponseFailed(String resultStr, int sign) throws Exception {
                return false;
            }
        });
    }

    private void dealDate(JsonObject resultObj) {
        saveData(resultObj);
        mTextCompany.setText(GsonJsonUtil.optString(resultObj.get("enterpriseName")));
        mTextMemberLevel.setText(GsonJsonUtil.optString(resultObj.get("memberLevelName")));
        mTextSignUpTime.setText(GsonJsonUtil.optString(resultObj.get("createDate")));
        mTextValidTime.setText(GsonJsonUtil.optString(resultObj.get("endDate")));
        mTextMemberType.setText(GsonJsonUtil.optString(resultObj.get("type2Name")));
        mTextHomepageVisitCount.setText(GsonJsonUtil.optString(resultObj.get("clickCount")));
        mTextProductVisitCount.setText(GsonJsonUtil.optString(resultObj.get("productCicking")));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
        case R.id.welcome_text_preview:
            break;
        case R.id.welcome_button_sort_manage:
            break;
        case R.id.welcome_button_publish_product:
            break;
        case R.id.welcome_text_helper:
            break;
        }
    }
}
