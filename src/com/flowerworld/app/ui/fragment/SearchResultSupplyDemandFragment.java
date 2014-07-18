package com.flowerworld.app.ui.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import com.flowerworld.app.R;
import com.flowerworld.app.dao.bean.GlobalConstant;
import com.flowerworld.app.dao.bean.GlobalVariableBean;
import com.flowerworld.app.interf.IHttpProcess;
import com.flowerworld.app.tool.http.HttpRequestFacade;
import com.flowerworld.app.tool.util.GsonJsonUtil;
import com.flowerworld.app.ui.adapter.SupplyDemandAdapter;
import com.flowerworld.app.ui.base.BaseFragment;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.Map;

public class SearchResultSupplyDemandFragment extends BaseFragment {
//	private static SearchResultSupplyDemandFragment fragment = new SearchResultSupplyDemandFragment();

    public static SearchResultSupplyDemandFragment newInstance() {
//		return fragment;
        return new SearchResultSupplyDemandFragment();
    }

    public static final String INTENT_KEY_SEARCH_WORD = "search_word";

    private PullToRefreshListView list = null;
    private SupplyDemandAdapter adapter = null;
    private int pageCount = 1;

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.search_result_supply_demand_list, container, false);
    }

    protected void initView() {
        initBanner(R.id.banner_layout, R.drawable.banner, R.string.search_result_title, 0, R.string.go_back,
                R.drawable.button_corner_rectangle_selector, 0, 0, new OnClickListener() {

                    @Override
                    public void onClick(View v) {
//						finish();
                        getFragmentManager().popBackStack();
                    }
                }, null);
        initEvent();
    }

    @Override
    protected void initData() {
        requstData(pageCount + "");
    }

    @Override
    protected void resumeData() {
        int count = pageCount;
        pageCount = 1;
        showData((JsonObject) getSavedData());
        pageCount = count;
    }

    private void initEvent() {
        list = (PullToRefreshListView) findViewById(R.id.search_result_supply_demand_list_listview);
        list.setOnRefreshListener(new OnRefreshListener<ListView>() {

            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                requstData(pageCount + "");
            }
        });
    }

    private void requstData(final String p) {
        requestHttp(new IHttpProcess() {

            @Override
            public String processUrl(int sign) {
                return GlobalVariableBean.APIRoot + GlobalConstant.URL_SEARCH_RESULT_SUPPLY_DEMAND;
            }

            @Override
            public boolean processResponseSucceed(String resultStr, int sign) throws Exception {
                PullToRefreshListView list = (PullToRefreshListView) findViewById(R.id.search_result_supply_demand_list_listview);
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
            }
        });
    }

    private void hasCount(boolean has) {
        if (has) {
            findViewById(R.id.search_result_text_fail).setVisibility(View.GONE);
            findViewById(R.id.search_result_text_total).setVisibility(View.VISIBLE);
            findViewById(R.id.search_result_supply_demand_list_divider).setVisibility(View.VISIBLE);
            findViewById(R.id.search_result_supply_demand_list_listview).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.search_result_text_fail).setVisibility(View.VISIBLE);
            findViewById(R.id.search_result_text_total).setVisibility(View.GONE);
            findViewById(R.id.search_result_supply_demand_list_divider).setVisibility(View.GONE);
            findViewById(R.id.search_result_supply_demand_list_listview).setVisibility(View.GONE);
        }
    }

    private boolean showData(JsonObject resultObj) {
        saveData(resultObj);

        PullToRefreshListView list = (PullToRefreshListView) findViewById(R.id.search_result_supply_demand_list_listview);
        String countStr = GsonJsonUtil.optString(resultObj.get("count"), "0");
        if (TextUtils.isEmpty(countStr)) {
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

        //data
        JsonArray dataArr = resultObj.get("data").getAsJsonArray();
        if (null == adapter) {
            adapter = new SupplyDemandAdapter(getActivity());
            adapter.setData(dataArr);
            list.setAdapter(adapter);
        } else {
            if (1 < pageCount) {
                adapter.appendData(dataArr);
            } else {
                adapter.setData(dataArr);
            }
            adapter.notifyDataSetChanged();
        }
        return false;
    }
}
