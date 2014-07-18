package com.flowerworld.app.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import com.flowerworld.app.R;
import com.flowerworld.app.ui.activity.list.ProductTypeSingleSelectActivity;
import com.flowerworld.app.ui.base.BaseFragment;

public class SearchMainFragment extends BaseFragment {
    private static SearchMainFragment fragment = new SearchMainFragment();

    public static SearchMainFragment newInstance() {
        return fragment;
    }

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.search_main_page, container, false);
    }

    protected void initView() {
        initBanner(R.id.banner_layout, R.drawable.banner, R.string.search_main_title, 0, 0, 0, 0, 0, null, null);
    }

    @Override
    protected void initData() {
        initEvent();
    }

    @Override
    protected void resumeData() {
        int id = tabButtonCurrentClkId;
        tabButtonCurrentClkId = View.NO_ID;
        tabButtonLastClkId = View.NO_ID;
        findViewById(id).performClick();
    }

    private void initEvent() {
        initFirstItem();
        findViewById(R.id.search_main_button_select_product_type_search).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                ProductTypeSingleSelectActivity.goHere(getActivity());

            }
        });
        findViewById(R.id.search_main_button_select_product_type_enqueiry).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

            }
        });
        findViewById(R.id.search_main_text_my_enquiry).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

            }
        });
    }

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

            saveData(tabButtonCurrentClkId);
        }
    };

    private void initFirstItem() {
        findViewById(R.id.search_main_button_tab_product).setOnClickListener(listener);
        findViewById(R.id.search_main_button_tab_enterprise).setOnClickListener(listener);
        findViewById(R.id.search_main_button_tab_demand).setOnClickListener(listener);
        findViewById(R.id.search_main_button_tab_product).performClick();

        findViewById(R.id.search_main_button_search).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Fragment f = null;
                String keyword = ((EditText) findViewById(R.id.search_main_edit)).getText().toString();
                switch (tabButtonCurrentClkId) {
                case R.id.search_main_button_tab_product:
                    f = SearchResultProductFragment.newInstance();
                    break;

                case R.id.search_main_button_tab_enterprise:
                    f = SearchResultEnterpriseFragment.newInstance();
                    break;

                case R.id.search_main_button_tab_demand:
                    f = SearchResultSupplyDemandFragment.newInstance();
                    break;

                default:
                    break;
                }
                Bundle b = new Bundle();
                b.putString(SearchResultProductFragment.INTENT_KEY_SEARCH_WORD, keyword);
                f.setArguments(b);
                getFragmentTransaction().replace(FRAME_CONTENT_ID, f).addToBackStack(null).commit();
            }
        });
    }

}
