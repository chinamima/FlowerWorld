package com.flowerworld.app.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import com.flowerworld.app.R;
import com.flowerworld.app.ui.base.BaseFragment;

public final class SupplyDemandListFragment extends BaseFragment {
    private static SupplyDemandListFragment fragment = new SupplyDemandListFragment();

    public static SupplyDemandListFragment newInstance() {
        return fragment;
//		return new SupplyDemandListFragment();
    }

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.supply_demand_list_page, container, false);
    }

    @Override
    protected void initView() {

        final TextView total = (TextView) findViewById(R.id.supply_demand_list_total);
        final ListView list = (ListView) findViewById(R.id.supply_demand_list_listview);

//		requestHttp(new IHttpProcess()
//		{
//
//			@Override
//			public String processUrl(int sign)
//			{
//				return GlobalVariableBean.APIRoot + GlobalConstant.URL_HOMEPAGE_ENTERPRISE;
//			}
//
//			@Override
//			public boolean processResponseSucceed(String resultStr, int sign)
//			{
//				JsonObject root = GsonJsonUtil.optJsonObject(GsonJsonUtil.parse(resultStr));
//				JsonObject result = root.get(HttpRequestFacade.RESULT_PARAMS_RESULT).getAsJsonObject();
//				JsonArray data = result.get("data").getAsJsonArray();
//
//				int amount = result.get("count").getAsInt();
//				total.setText("共" + amount + "个条求购信息。");
//				EnterpriseAdapter adapter = new EnterpriseAdapter(getActivity());
//				adapter.setData(data);
//				list.setAdapter(adapter);
//
//				return true;
//			}
//
//			@Override
//			public boolean processResponseFailed(String resultStr, int sign)
//			{
//				return false;
//			}
//
//			@Override
//			public void processParams(Map<String, Object> params, int sign)
//			{
//				params.put("p", 1);
//			}
//		});
    }

    @Override
    protected void initData() {
        // TODO Auto-generated method stub

    }

    @Override
    protected void resumeData() {
        // TODO Auto-generated method stub

    }

}
