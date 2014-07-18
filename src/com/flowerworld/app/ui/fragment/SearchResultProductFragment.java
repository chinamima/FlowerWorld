package com.flowerworld.app.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout.LayoutParams;
import com.flowerworld.app.R;
import com.flowerworld.app.dao.bean.GlobalConstant;
import com.flowerworld.app.dao.bean.GlobalVariableBean;
import com.flowerworld.app.interf.IHttpProcess;
import com.flowerworld.app.tool.http.HttpRequestFacade;
import com.flowerworld.app.tool.util.GsonJsonUtil;
import com.flowerworld.app.tool.util.LOG;
import com.flowerworld.app.tool.util.UnitUtil;
import com.flowerworld.app.ui.activity.search.SearchDetailProductPageAcitvity;
import com.flowerworld.app.ui.adapter.ProductAdapter;
import com.flowerworld.app.ui.base.BaseFragment;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.Map;

public class SearchResultProductFragment extends BaseFragment {
//	private static SearchResultProductFragment fragment = new SearchResultProductFragment();

    public static SearchResultProductFragment newInstance() {
//		return fragment;
        return new SearchResultProductFragment();
    }

    public static final String INTENT_KEY_SEARCH_WORD = "search_word";

    private PullToRefreshListView list = null;
    private ProductAdapter adapter = null;
    private int pageCount = 1;
    private String m_pc2 = "";

    private static int BUTTON_ID = 0x1247433;

    private int tabButtonCurrentClkId = View.NO_ID;
    private int tabButtonLastClkId = View.NO_ID;

    private OnClickListener listener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            if (tabButtonCurrentClkId == v.getId()) {
                return;
            }
            tabButtonLastClkId = tabButtonCurrentClkId;
            tabButtonCurrentClkId = v.getId();

            v.setSelected(true);
            if (View.NO_ID != tabButtonLastClkId) {
                findViewById(tabButtonLastClkId).setSelected(false);
            }

            pageCount = 1;
            m_pc2 = v.getTag().toString();
            requstData(pageCount + "", m_pc2);
        }
    };

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.search_result_product_list, container, false);
    }

    protected void initView() {
        initBanner(R.id.banner_layout, R.drawable.banner, R.string.search_result_title, 0, R.string.go_back,
                R.drawable.button_corner_rectangle_selector, R.string.search_result_select,
                R.drawable.button_corner_rectangle_selector, new OnClickListener() {

                    @Override
                    public void onClick(View v) {
//						finish();
                        getFragmentManager().popBackStack();
                    }
                }, new OnClickListener() {

                    @Override
                    public void onClick(View v) {

                    }
                });
        initEvent();
    }

    private void initEvent() {
        list = (PullToRefreshListView) findViewById(R.id.search_result_product_list_listview);
        list.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LOG.d(TAG, "===========onItemClick=position: " + position);
                Intent intent = new Intent(getActivity(), SearchDetailProductPageAcitvity.class);
                intent.putExtra(SearchDetailProductPageAcitvity.INTENT_KEY_PRODUCT_ID, id/* adapter.getItemId(position) */);
                getActivity().startActivity(intent);
            }
        });
        list.setOnRefreshListener(new OnRefreshListener<ListView>() {

            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                requstData(pageCount + "", m_pc2);
            }
        });
    }

    @Override
    protected void initData() {
        requstData(pageCount + "", m_pc2);
    }

    @Override
    protected void resumeData() {
        int count = pageCount;
        pageCount = 1;
        showData((JsonObject) getSavedData());
        pageCount = count;
    }

    private void requstData(final String p, final String pc2) {
        requestHttp(new IHttpProcess() {

            @Override
            public String processUrl(int sign) {
                return GlobalVariableBean.APIRoot + GlobalConstant.URL_SEARCH_RESULT_PRODUCT;
            }

            @Override
            public boolean processResponseSucceed(String resultStr, int sign) throws Exception {
                PullToRefreshListView list = (PullToRefreshListView) findViewById(R.id.search_result_product_list_listview);
                list.onRefreshComplete();

                JsonObject obj = GsonJsonUtil.parse(resultStr).getAsJsonObject();
                JsonObject resultObj = obj.get(HttpRequestFacade.RESULT_PARAMS_RESULT).getAsJsonObject();

                showData(resultObj);

                pageCount++;
                return true;
            }

            @Override
            public boolean processResponseFailed(String resultStr, int sign) throws Exception {
                JsonObject obj = GsonJsonUtil.parse(resultStr).getAsJsonObject();
                JsonObject resultObj = obj.get(HttpRequestFacade.RESULT_PARAMS_RESULT).getAsJsonObject();

                String countStr = GsonJsonUtil.optString(resultObj.get("count"), "0");
                if (TextUtils.isEmpty(resultStr)) {
                    hasCount(false);
                    return true;
                }
                int count = Integer.parseInt(countStr);
                if (1 > count) {
                    hasCount(false);
                    return true;
                }

                hasCount(true);
                TextView total = (TextView) findViewById(R.id.search_result_text_total);
                total.setText("共" + count + "条“" + getArguments().getString(INTENT_KEY_SEARCH_WORD) + "”记录。");
                return false;
            }

            @Override
            public void processParams(Map<String, Object> params, int sign) {
                params.put("keys", getArguments().getString(INTENT_KEY_SEARCH_WORD));
                params.put("p", p);
                if (!TextUtils.isEmpty(pc2)) {
                    params.put("pc2", pc2);
                }
            }
        });
    }

    private void hasCount(boolean has) {
        if (has) {
            findViewById(R.id.search_result_text_fail).setVisibility(View.GONE);
            findViewById(R.id.search_result_layout_type).setVisibility(View.VISIBLE);
            findViewById(R.id.search_result_text_total).setVisibility(View.VISIBLE);
            findViewById(R.id.search_result_product_list_divider).setVisibility(View.VISIBLE);
            findViewById(R.id.search_result_product_list_listview).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.search_result_text_fail).setVisibility(View.VISIBLE);
            findViewById(R.id.search_result_layout_type).setVisibility(View.GONE);
            findViewById(R.id.search_result_text_total).setVisibility(View.GONE);
            findViewById(R.id.search_result_product_list_divider).setVisibility(View.GONE);
            findViewById(R.id.search_result_product_list_listview).setVisibility(View.GONE);
        }
    }

    private void addButton(String pc2, JsonArray pc2Arr) {
        m_pc2 = pc2;
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, UnitUtil.transformDipToPx(30));
        lp.setMargins(UnitUtil.transformDipToPx(10), 0, UnitUtil.transformDipToPx(10), 0);
        LinearLayout parent = (LinearLayout) findViewById(R.id.search_result_layout_button_type);
        parent.removeAllViews();

        JsonObject obj = null;
        Button btn = null;
        String id = null;
        for (int i = 0; i < pc2Arr.size(); i++) {
            obj = GsonJsonUtil.optJsonObject(pc2Arr.get(i));
            btn = newButton();
            btn.setText(obj.get("name").getAsString());
            id = obj.get("id").getAsString();
            btn.setTag(id);
            parent.addView(btn, lp);

            if (pc2.equals(id)) {
                btn.setSelected(true);
                tabButtonCurrentClkId = btn.getId();
            }
        }
    }

    private Button newButton() {
        Button btn = new Button(getActivity());
        btn.setId(BUTTON_ID++);
        btn.setTextSize(14);
        btn.setTextColor(this.getResources().getColorStateList(R.color.search_result_type_button_selector));
        btn.setBackgroundResource(R.drawable.search_result_type_button_selector);
        btn.setOnClickListener(listener);
        btn.setGravity(Gravity.CENTER);
        btn.setPadding(UnitUtil.transformDipToPx(2), 0, UnitUtil.transformDipToPx(2), 0);

        return btn;
    }

    private void showData(JsonObject resultObj) {
        saveData(resultObj);

        String countStr = GsonJsonUtil.optString(resultObj.get("count"), "0");
        if (TextUtils.isEmpty(countStr)) {
            hasCount(false);
            return;
        }
        int count = Integer.parseInt(countStr);
        if (1 > count) {
            hasCount(false);
            return;
        }

        hasCount(true);
        TextView total = (TextView) findViewById(R.id.search_result_text_total);
        total.setText("共" + count + "条“" + getArguments().getString(INTENT_KEY_SEARCH_WORD) + "”记录。");

        //data
        JsonArray dataArr = resultObj.get("data").getAsJsonArray();
        if (null == adapter) {
            adapter = new ProductAdapter(getActivity());
            adapter.setData(dataArr);
            PullToRefreshListView list = (PullToRefreshListView) findViewById(R.id.search_result_product_list_listview);
            list.setAdapter(adapter);
            addButton(resultObj.get("pc2").getAsString(), resultObj.get("pc2Arr").getAsJsonArray());
        } else {
            if (1 < pageCount) {
                adapter.appendData(dataArr);
            } else {
                adapter.setData(dataArr);
            }
            adapter.notifyDataSetChanged();
        }
    }

}
