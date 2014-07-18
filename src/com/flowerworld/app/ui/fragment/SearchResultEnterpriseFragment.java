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
import com.flowerworld.app.ui.adapter.EnterpriseAdapter;
import com.flowerworld.app.ui.base.BaseFragment;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.Map;

public class SearchResultEnterpriseFragment extends BaseFragment {
//	private static SearchResultEnterpriseFragment fragment = new SearchResultEnterpriseFragment();

    public static SearchResultEnterpriseFragment newInstance() {
//		return fragment;
        return new SearchResultEnterpriseFragment();
    }

    public static final String INTENT_KEY_SEARCH_WORD = "search_word";

    private PullToRefreshListView list = null;
    private EnterpriseAdapter adapter = null;
    private int pageCount = 1;

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.search_result_enterprise_list, container, false);
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
        requstData("1");
    }

    @Override
    protected void resumeData() {
        int count = pageCount;
        pageCount = 1;
        showData((JsonObject) getSavedData());
        pageCount = count;
    }

    private void initEvent() {
        list = (PullToRefreshListView) findViewById(R.id.search_result_enterprise_list_listview);
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
                return GlobalVariableBean.APIRoot + GlobalConstant.URL_SEARCH_RESULT_ENTERPRISE;
            }

            @Override
            public boolean processResponseSucceed(String resultStr, int sign) throws Exception {
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
            findViewById(R.id.search_result_enterprise_list_divider).setVisibility(View.VISIBLE);
            findViewById(R.id.search_result_enterprise_list_listview).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.search_result_text_fail).setVisibility(View.VISIBLE);
            findViewById(R.id.search_result_text_total).setVisibility(View.GONE);
            findViewById(R.id.search_result_enterprise_list_divider).setVisibility(View.GONE);
            findViewById(R.id.search_result_enterprise_list_listview).setVisibility(View.GONE);
        }
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
            adapter = new EnterpriseAdapter(getActivity());
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
    }
}
