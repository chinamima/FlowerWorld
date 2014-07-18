package com.flowerworld.app.ui.activity.search;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.flowerworld.app.R;
import com.flowerworld.app.dao.bean.GlobalConstant;
import com.flowerworld.app.dao.bean.GlobalVariableBean;
import com.flowerworld.app.interf.IHttpProcess;
import com.flowerworld.app.tool.http.HttpRequestFacade;
import com.flowerworld.app.tool.util.GsonJsonUtil;
import com.flowerworld.app.tool.util.LOG;
import com.flowerworld.app.ui.adapter.ProductAdapter;
import com.flowerworld.app.ui.base.BaseActivity;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.Map;

public class SearchAdvancedResultProductActivity extends BaseActivity {

    public static final String INTENT_KEY_TYPE = "INTENT_KEY_TYPE";
    public static final String INTENT_KEY_PRODUCTNAME = "INTENT_KEY_PRODUCTNAME";
    public static final String INTENT_KEY_PARENTCLASS = "INTENT_KEY_PARENTCLASS";
    public static final String INTENT_KEY_PROVINCE = "INTENT_KEY_PROVINCE";
    public static final String INTENT_KEY_CITY = "INTENT_KEY_CITY";
    public static final String INTENT_KEY_GROUP = "INTENT_KEY_GROUP";
    public static final String INTENT_KEY_PPATTR = "INTENT_KEY_PPATTR";

    private PullToRefreshListView list = null;
    private ProductAdapter adapter = null;
    private int pageCount = 1;

    private TextView mTextType = null;
    private TextView mTextTotal = null;
    private ImageView mImageDivider = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_advanced_product_list_page);
        initView();
    }

    protected void initView() {
        initBanner(R.id.banner_layout, R.drawable.banner, R.string.search_advanced_result_title, 0, R.string.go_back,
                R.drawable.button_corner_rectangle_selector, R.string.search_advanced_result_banner_right,
                R.drawable.button_corner_rectangle_selector, new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                }, new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });

        mTextType = (TextView) findViewById(R.id.product_list_advanced_type);
        mTextTotal = (TextView) findViewById(R.id.product_list_total);
        mImageDivider = (ImageView) findViewById(R.id.product_list_divider);
        list = (PullToRefreshListView) findViewById(R.id.product_list_listview);
        list.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LOG.d(TAG, "===========onItemClick=position: " + position);
                Intent intent = new Intent(SearchAdvancedResultProductActivity.this, SearchDetailProductPageAcitvity.class);
                intent.putExtra(SearchDetailProductPageAcitvity.INTENT_KEY_PRODUCT_ID, id/* adapter.getItemId(position) */);
                SearchAdvancedResultProductActivity.this.startActivity(intent);
            }
        });
        list.setOnRefreshListener(new OnRefreshListener<ListView>() {

            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                requstData(pageCount + "");
            }
        });

        requstData(pageCount + "");
    }

    private void requstData(final String p) {
        requestHttp(new IHttpProcess() {

            @Override
            public String processUrl(int sign) {
                return GlobalVariableBean.APIRoot + GlobalConstant.URL_SEARCH_PRODUCT_ADVANCED;
            }

            @Override
            public boolean processResponseSucceed(String resultStr, int sign) {
                list.onRefreshComplete();

                JsonObject root = GsonJsonUtil.optJsonObject(GsonJsonUtil.parse(resultStr));
                JsonObject result = root.get(HttpRequestFacade.RESULT_PARAMS_RESULT).getAsJsonObject();

                showData(result);

                pageCount++;
                return true;
            }

            @Override
            public boolean processResponseFailed(String resultStr, int sign) {
                mTextType.setVisibility(View.VISIBLE);
                mTextType.setText("抱歉，暂无搜索结果！");
                mTextTotal.setVisibility(View.GONE);
                mImageDivider.setVisibility(View.GONE);
                list.setVisibility(View.GONE);

                return false;
            }

            @Override
            public void processParams(Map<String, Object> params, int sign) {
                params.put("productName", getIntent().getStringExtra(INTENT_KEY_PRODUCTNAME));
                params.put("parentClass", getIntent().getStringExtra(INTENT_KEY_PARENTCLASS));
                params.put("Province", getIntent().getStringExtra(INTENT_KEY_PROVINCE));
                params.put("City", getIntent().getStringExtra(INTENT_KEY_CITY));
                params.put("group", getIntent().getStringExtra(INTENT_KEY_GROUP));
                params.put("ppAttr", getIntent().getStringExtra(INTENT_KEY_PPATTR));
                params.put("p", p);
                LOG.d(TAG, "======processParams=params: " + params);
            }
        });
    }

    private void showData(JsonObject result) {
        String type = getIntent().getStringExtra(INTENT_KEY_TYPE);
        mTextType.setText(type);
        mTextType.setVisibility(View.VISIBLE);

        JsonArray data = result.get("data").getAsJsonArray();
        int amount = result.get("count").getAsInt();
        mTextTotal.setText("共" + amount + "条" + getIntent().getStringExtra(INTENT_KEY_PRODUCTNAME) + "记录。");

        if (null == adapter) {
            adapter = new ProductAdapter(this);
            adapter.setData(data);
            list.setAdapter(adapter);
        } else {
            if (1 < pageCount) {
                adapter.appendData(data);
            } else {
                adapter.setData(data);
            }
            adapter.notifyDataSetChanged();
        }
    }
}
