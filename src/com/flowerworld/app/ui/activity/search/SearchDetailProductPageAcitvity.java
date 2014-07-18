package com.flowerworld.app.ui.activity.search;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.flowerworld.app.R;
import com.flowerworld.app.dao.bean.GlobalConstant;
import com.flowerworld.app.dao.bean.GlobalVariableBean;
import com.flowerworld.app.interf.IHttpProcess;
import com.flowerworld.app.tool.http.HttpRequestFacade;
import com.flowerworld.app.tool.util.ApplicationContextUtil;
import com.flowerworld.app.tool.util.GsonJsonUtil;
import com.flowerworld.app.tool.util.MemberLevelUtility;
import com.flowerworld.app.tool.util.UnitUtil;
import com.flowerworld.app.ui.base.BaseActivity;
import com.flowerworld.app.ui.dialog.EnquiryDialog;
import com.flowerworld.app.ui.widget.MultiImageGestureShow;
import com.flowerworld.app.ui.widget.ProductPpAttrLinear;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Map;

public class SearchDetailProductPageAcitvity extends BaseActivity {
    public static final String INTENT_KEY_PRODUCT_ID = "id";

    private static final String KEY_PP_ATTR_NAME = "name";
    private static final String KEY_PP_ATTR_V1 = "v1";
    private static final String KEY_PP_ATTR_V2 = "v2";
    private static final String KEY_PP_ATTR_PP = "pp";
    private static final String KEY_PP_ATTR_DW = "dw";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_detail_product);

        initView();
    }

    private void initView() {
        initBanner(R.id.banner_layout, R.drawable.banner, R.string.search_detail_product_title, 0, R.string.go_back,
                R.drawable.button_corner_rectangle_selector, 0, 0, new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                }, null);
//		initBottomBtn();
//		initBottomBtnSelected();
        initEvent();

        requestHttp(new IHttpProcess() {

            @Override
            public String processUrl(int sign) {
                return GlobalVariableBean.APIRoot + GlobalConstant.URL_SEARCH_DETAIL_PRODUCT;
            }

            @Override
            public boolean processResponseSucceed(String resultStr, int sign) throws Exception {
                JsonObject obj = GsonJsonUtil.parse(resultStr).getAsJsonObject();
                JsonObject resultObj = obj.get(HttpRequestFacade.RESULT_PARAMS_RESULT).getAsJsonObject();

                //
                ((TextView) findViewById(R.id.search_detail_product_text_name)).setText(GsonJsonUtil.optString(
                        resultObj.get("productName"), ""));
                //
                String price = GsonJsonUtil.optString(resultObj.get("price"), "0");
                if ("0".equals(price)) {
                    price = ApplicationContextUtil.getApplicationContext().getString(R.string.search_detail_product_price_talk);
                } else {
                    price = "￥" + price;
                }
                ((TextView) findViewById(R.id.search_detail_product_text_price)).setText(price);
                //
                ((TextView) findViewById(R.id.search_detail_product_text_company_name)).setText(GsonJsonUtil.optString(
                        resultObj.get("simpleName"), ""));
                //
                ((TextView) findViewById(R.id.search_detail_product_text_description)).setText(GsonJsonUtil.optString(
                        resultObj.get("Descript"), ""));
                //
                ((ImageView) findViewById(R.id.search_detail_product_image_member_type)).setImageResource(MemberLevelUtility
                        .switchMemberName(GsonJsonUtil.optString(resultObj.get("memberLevelName"), "")));
                //
                ProductPpAttrLinear.setPpAttr(SearchDetailProductPageAcitvity.this,
                        (LinearLayout) findViewById(R.id.search_detail_product_layout_property), resultObj.get("ppAttr")
                                .getAsJsonArray(), KEY_PP_ATTR_NAME, KEY_PP_ATTR_V1, KEY_PP_ATTR_V2, KEY_PP_ATTR_DW);
                //
                JsonArray smallImages = resultObj.get("smallimg").getAsJsonArray();
                ArrayList<String> small = new ArrayList<String>();
                for (int i = 0; i < smallImages.size(); i++) {
                    obj = smallImages.get(i).getAsJsonObject();
                    small.add(GsonJsonUtil.optString(obj.get("images"), ""));
                }
                ((MultiImageGestureShow) findViewById(R.id.search_detail_product_image_product_pic)).setImageShowPaths(small);
                JsonArray bigImages = resultObj.get("bigimg").getAsJsonArray();
                ArrayList<String> big = new ArrayList<String>();
                for (int i = 0; i < bigImages.size(); i++) {
                    obj = bigImages.get(i).getAsJsonObject();
                    big.add(GsonJsonUtil.optString(obj.get("images"), ""));
                }
                ((MultiImageGestureShow) findViewById(R.id.search_detail_product_image_product_pic)).setImageBigPaths(big);

                return true;
            }

            @Override
            public boolean processResponseFailed(String resultStr, int sign) throws Exception {
                return false;
            }

            @Override
            public void processParams(Map<String, Object> params, int sign) {
                params.put("productid", getIntent().getLongExtra(INTENT_KEY_PRODUCT_ID, -1));
            }
        });
    }

    private void initEvent() {
        findViewById(R.id.search_detail_product_button_privous).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

            }
        });
        findViewById(R.id.search_detail_product_button_next).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

            }
        });
        findViewById(R.id.search_detail_product_button_enquiry).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                EnquiryDialog dialog = new EnquiryDialog(SearchDetailProductPageAcitvity.this,
                        R.style.Theme_Dialog_NoTitle_NoActionBar);
                WindowManager.LayoutParams lp = dialog.getWindow().getAttributes(); // 获取对话框当前的参数值
                lp.width = UnitUtil.transformDipToPx(300);
                dialog.getWindow().setAttributes(lp);
                dialog.setProductId(getIntent().getLongExtra(INTENT_KEY_PRODUCT_ID, -1) + "");
                dialog.show();
            }
        });
    }

}
