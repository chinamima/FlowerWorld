package com.flowerworld.app.ui.activity.search;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.flowerworld.app.R;
import com.flowerworld.app.dao.bean.*;
import com.flowerworld.app.interf.IHttpProcess;
import com.flowerworld.app.listener.NotEmptyTextWatcher;
import com.flowerworld.app.tool.helper.CreateFromPageHelper;
import com.flowerworld.app.tool.http.HttpRequestFacade;
import com.flowerworld.app.tool.util.GsonJsonUtil;
import com.flowerworld.app.tool.util.ToastUtil;
import com.flowerworld.app.ui.activity.list.MultiSelectLocationPageActivity;
import com.flowerworld.app.ui.activity.list.ProductTypeSingleSelectActivity;
import com.flowerworld.app.ui.base.BaseActivity;
import com.flowerworld.app.ui.widget.SingleSelectButton;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Map;

public class SearchAdvancedMainPageActivity extends BaseActivity {

    private NotEmptyReferenceValue mRefEnterpriseName = new NotEmptyReferenceValue();
    private NotEmptyReferenceValue mRefEnterpriseShort = new NotEmptyReferenceValue();
    private NotEmptyReferenceValue mRefRealName = new NotEmptyReferenceValue();
    private NotEmptyReferenceValue mRefDepartment = new NotEmptyReferenceValue();
    private NotEmptyReferenceValue mRefPhone1 = new NotEmptyReferenceValue();

    private SingleSelectButton mSelectCombine = null;

    private String mProvinceIdStr = null;
    private String mCityIdStr = null;

    public static final int REQUEST_CODE_LOCATION = 0X12340;
    public static final int REQUEST_CODE_TYPE_1 = 0X12341;
    public static final int REQUEST_CODE_TYPE_2 = 0X12342;
    public static final int REQUEST_CODE_TYPE_3 = 0X12343;

    //form value
    private FormEnterpriseStaffBean bean = new FormEnterpriseStaffBean();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_advanced_main);

        initView();
    }

    private void initView() {
        initBanner(R.id.banner_layout, R.drawable.banner, R.string.search_advanced_main_title, 0, R.string.go_back,
                R.drawable.button_corner_rectangle_selector, 0, 0, new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                }, null);

        initButton();
        initVerify();
    }

    private void initButton() {
        ArrayList<String> nameList = getIntent().getStringArrayListExtra(ProductTypeSingleSelectActivity.KEY_RESULT_NAME_LIST);
        ((TextView) findViewById(R.id.search_advanced_main_button_type)).setText(nameList.get(0) + "->" + nameList.get(1));

        findViewById(R.id.search_advanced_main_button_location).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                MultiSelectLocationPageActivity.goHere(SearchAdvancedMainPageActivity.this, REQUEST_CODE_LOCATION);
            }
        });
        findViewById(R.id.search_advanced_main_button_search).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!verify()) {
                    return;
                }

                Intent intent = new Intent(SearchAdvancedMainPageActivity.this, SearchAdvancedResultProductActivity.class);
                intent.putExtra(SearchAdvancedResultProductActivity.INTENT_KEY_PRODUCTNAME,
                        ((TextView) findViewById(R.id.search_advanced_main_edit_product_name)).getText().toString());
                ArrayList<String> idList = getIntent()
                        .getStringArrayListExtra(ProductTypeSingleSelectActivity.KEY_RESULT_ID_LIST);
                intent.putExtra(SearchAdvancedResultProductActivity.INTENT_KEY_PARENTCLASS, idList.get(1));
                intent.putExtra(SearchAdvancedResultProductActivity.INTENT_KEY_PROVINCE, mProvinceIdStr);
                intent.putExtra(SearchAdvancedResultProductActivity.INTENT_KEY_CITY, mCityIdStr);
                intent.putExtra(SearchAdvancedResultProductActivity.INTENT_KEY_GROUP, mSelectCombine.getSelectedValue()
                        .toString());

                JsonArray ppAttr = new JsonArray();
                JsonObject obj = null;
                TextView txt = null;
                for (int i = 0; i < formItems.size(); i++) {
                    txt = (TextView) formItems.get(i);
                    obj = new JsonObject();
                    obj.addProperty("pp", ((CreateFormBean) txt.getTag()).pp);
                    if (txt instanceof Button) {
                        obj.addProperty("v", ((CreateFormBean) txt.getTag()).v);
                    } else {
                        obj.addProperty("v", txt.getText().toString());
                    }
                    ppAttr.add(obj);
                }
                intent.putExtra(SearchAdvancedResultProductActivity.INTENT_KEY_PPATTR, ppAttr.toString());

                ArrayList<String> nameList = getIntent().getStringArrayListExtra(
                        ProductTypeSingleSelectActivity.KEY_RESULT_NAME_LIST);
                String type = nameList.get(0) + "->" + nameList.get(1);
                intent.putExtra(SearchAdvancedResultProductActivity.INTENT_KEY_TYPE, type);

                SearchAdvancedMainPageActivity.this.startActivity(intent);
            }
        });

        mSelectCombine = (SingleSelectButton) findViewById(R.id.search_advanced_main_button_combine);
        mSelectCombine.setButtonDrawable(R.drawable.single_select_button_selector);
        mSelectCombine.addSelectButton("是", "1");
        mSelectCombine.addSelectButton("否", "0");

        requestHttp(new IHttpProcess() {

            @Override
            public String processUrl(int sign) {
                return GlobalVariableBean.APIRoot + GlobalConstant.URL_PRODUCT_ATTR;
            }

            @Override
            public boolean processResponseSucceed(String resultStr, int sign) throws Exception {
                JsonObject obj = GsonJsonUtil.parse(resultStr).getAsJsonObject();
                JsonArray result = obj.get(HttpRequestFacade.RESULT_PARAMS_RESULT).getAsJsonArray();

                createFormItem(result);

                return true;
            }

            @Override
            public boolean processResponseFailed(String resultStr, int sign) throws Exception {
                return false;
            }

            @Override
            public void processParams(Map<String, Object> params, int sign) {
                ArrayList<String> idList = getIntent()
                        .getStringArrayListExtra(ProductTypeSingleSelectActivity.KEY_RESULT_ID_LIST);
                params.put("parentClass", idList.get(1));
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (RESULT_OK == resultCode) {
            switch (requestCode) {
            case REQUEST_CODE_LOCATION:
                JsonArray provinceNameArr = GsonJsonUtil.parse(
                        data.getStringExtra(MultiSelectLocationPageActivity.RESULT_INTENT_PROVINCE_NAME_ARRAY)).getAsJsonArray();
                JsonArray provinceIdArr = GsonJsonUtil.parse(
                        data.getStringExtra(MultiSelectLocationPageActivity.RESULT_INTENT_PROVINCE_ID_ARRAY)).getAsJsonArray();
                mProvinceIdStr = "";
                for (int i = 0; i < provinceIdArr.size(); i++) {
                    mProvinceIdStr += "|" + provinceIdArr.get(i).getAsString();
                }
                if (!TextUtils.isEmpty(mProvinceIdStr)) {
                    mProvinceIdStr = mProvinceIdStr.substring(1);
                }

                JsonArray cityNameArr = null;
                JsonArray cityIdArr = null;

                Button btn = (Button) findViewById(R.id.search_advanced_main_button_location);
                String cityNameStr = data.getStringExtra(MultiSelectLocationPageActivity.RESULT_INTENT_CITY_NAME_ARRAY);
                String cityIdStr = data.getStringExtra(MultiSelectLocationPageActivity.RESULT_INTENT_CITY_ID_ARRAY);
                mCityIdStr = "";
                if (TextUtils.isEmpty(cityIdStr)) {
                    btn.setText(provinceNameArr.size() + "个省");

                } else {
                    cityNameArr = GsonJsonUtil.parse(cityNameStr).getAsJsonArray();
                    cityIdArr = GsonJsonUtil.parse(cityIdStr).getAsJsonArray();
                    btn.setText(provinceNameArr.get(0).getAsString() + " " + cityNameArr.get(0).getAsString());

                    for (int i = 0; i < cityIdArr.size(); i++) {
                        mCityIdStr += "|" + cityIdArr.get(i).getAsString();
                    }
                    if (!TextUtils.isEmpty(mCityIdStr)) {
                        mCityIdStr = mCityIdStr.substring(1);
                    }
                }

                break;

            default:
                break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void initVerify() {
        String error = "不能为空";
        //
        EditText enterpriseName = (EditText) findViewById(R.id.search_advanced_main_edit_product_name);
        enterpriseName.addTextChangedListener(new NotEmptyTextWatcher(enterpriseName));
        enterpriseName.setError(error);
    }

    private boolean verify() {
        String productName = ((TextView) findViewById(R.id.search_advanced_main_edit_product_name)).getText().toString();
        String combine = (String) mSelectCombine.getSelectedValue();

        if (TextUtils.isEmpty(productName) || TextUtils.isEmpty(mProvinceIdStr) || TextUtils.isEmpty(combine)) {
            ToastUtil.show(this, "请完成必填项");
            return false;
        }

        return true;
    }

    private ArrayList<View> formItems = new ArrayList<View>();

    private void createFormItem(JsonArray result) {
        String type = null;
        String name = null;
        String pp = null;
        JsonObject obj = null;
        LinearLayout linear = (LinearLayout) findViewById(R.id.search_advanced_main_layout_field);
        for (int i = 0; i < result.size(); i++) {
            obj = result.get(i).getAsJsonObject();
            type = obj.get("type").getAsString();
            name = obj.get("name").getAsString();
            pp = obj.get("pp").getAsString();
            if ("2".equals(type)) {
                EditText edit = CreateFromPageHelper.addEdit(this, linear, name, pp, false, false, null);
                edit.setInputType(InputType.TYPE_CLASS_NUMBER);
                edit.setImeOptions(EditorInfo.IME_ACTION_NEXT);
                formItems.add(edit);

            } else if ("3".equals(type)) {
                String ctrl = obj.get("ctrl").getAsString();
                if (ctrl.startsWith(",")) {
                    ctrl = ctrl.substring(1);
                }
                Button btn = CreateFromPageHelper.addSelect(this, linear, name, pp, false, false, ctrl.split(","), null);
                formItems.add(btn);

            } else {
                EditText edit = CreateFromPageHelper.addEdit(this, linear, name, pp, false, false, null);
                edit.setImeOptions(EditorInfo.IME_ACTION_NEXT);
                formItems.add(edit);
            }
        }
    }

}
