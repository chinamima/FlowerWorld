package com.flowerworld.app.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
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

public final class EnterpriseListFragment extends BaseFragment {
    private static EnterpriseListFragment fragment = new EnterpriseListFragment();

    public static EnterpriseListFragment newInstance() {
        return fragment;
//		return new EnterpriseListFragment();
    }

    private PullToRefreshListView list = null;
    private EnterpriseAdapter adapter = null;
    private int pageCount = 1;

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.enterprise_list_page, container, false);
    }

    @Override
    protected void initView() {

        list = (PullToRefreshListView) findViewById(R.id.enterprise_list_listview);
        list.setOnRefreshListener(new OnRefreshListener<ListView>() {

            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                requstData(pageCount + "");
            }
        });
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

    private void requstData(final String p) {
        requestHttp(new IHttpProcess() {

            @Override
            public String processUrl(int sign) {
                return GlobalVariableBean.APIRoot + GlobalConstant.URL_HOMEPAGE_ENTERPRISE;
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
                return false;
            }

            @Override
            public void processParams(Map<String, Object> params, int sign) {
                params.put("p", p);
            }
        });
    }

    private void showData(JsonObject result) {
        saveData(result);

        JsonArray data = result.get("data").getAsJsonArray();

        TextView total = (TextView) findViewById(R.id.enterprise_list_total);

        int amount = result.get("count").getAsInt();
        total.setText("共找到" + amount + "个条记录。");
//		EnterpriseAdapter adapter = new EnterpriseAdapter(getActivity());
//		adapter.setData(data);
//		list.setAdapter(adapter);
        if (null == adapter) {
            adapter = new EnterpriseAdapter(getActivity());
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
