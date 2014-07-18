package com.flowerworld.app.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import com.flowerworld.app.R;
import com.flowerworld.app.tool.util.UnitUtil;
import com.flowerworld.app.ui.activity.SignInPageActivity;
import com.flowerworld.app.ui.activity.signup.SignUpMainPageActivity;
import com.flowerworld.app.ui.base.BaseFragment;

public class HomePageFragment extends BaseFragment {
    private static HomePageFragment fragment = new HomePageFragment();

    public static HomePageFragment newInstance() {
        return fragment;
//		return new HomePageFragment();
    }

    private static final String[] CONTENT = new String[] { "首页", "资讯", "产品", "企业", "供求", "论坛" };

    public static int buttonCurrentSelectedId = R.id.home_page_button_home;
    public int buttonLastSelectedId = View.NO_ID;
    private static Fragment lastFragment = null;
    private static int currnetIndex = 0;
    //	private int lastIndex = 0;
    public View.OnClickListener bottomBtnListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            if (buttonCurrentSelectedId == v.getId()) {
                return;
            }
            buttonLastSelectedId = buttonCurrentSelectedId;
            buttonCurrentSelectedId = v.getId();

            v.setSelected(true);
            if (View.NO_ID != buttonLastSelectedId) {
                ((ViewGroup) v.getParent()).findViewById(buttonLastSelectedId).setSelected(false);
            }

//			if (currnetIndex == lastIndex)
//				return;

            switch (buttonCurrentSelectedId) {
            case R.id.home_page_button_home:
                currnetIndex = 0;
                break;

            case R.id.home_page_button_information:
                currnetIndex = 1;
                break;

            case R.id.home_page_button_product:
                currnetIndex = 2;
                break;

            case R.id.home_page_button_enterprise:
                currnetIndex = 3;
                break;

            case R.id.home_page_button_supply_demand:
                currnetIndex = 4;
                break;

            case R.id.home_page_button_bbs:
                currnetIndex = 5;
                break;

            default:
                break;
            }

//			Fragment f = getFragment(CONTENT[currnetIndex]);
////			((BaseFragment) f).setSavedState();
//			getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
//			FragmentTransaction ft = getFragmentTransaction();
////			if (null != lastFragment)
////				ft.remove(lastFragment);
//			ft.replace(R.id.home_page_view_pager, f).commit();
//			initTitle(CONTENT[currnetIndex]);
//			lastFragment = f;

            Fragment f = getFragment(CONTENT[currnetIndex]);
            changeFragment(f);

//			lastIndex = currnetIndex;
        }
    };

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.home_page_new, container, false);
    }

    @Override
    protected void initView() {
        Fragment f = getFragment(CONTENT[currnetIndex]);
        changeFragment(f);

        ((Button) findViewById(buttonCurrentSelectedId)).setSelected(true);
        initTitle(CONTENT[currnetIndex]);
        initEvent();
    }

    @Override
    protected void initData() {
//		getFragmentTransaction().replace(R.id.home_page_view_pager, getFragment(CONTENT[currnetIndex])).commit();		
    }

    @Override
    protected void resumeData() {

    }

    private void initEvent() {
        findViewById(R.id.home_page_button_home).setOnClickListener(bottomBtnListener);
        findViewById(R.id.home_page_button_information).setOnClickListener(bottomBtnListener);
        findViewById(R.id.home_page_button_product).setOnClickListener(bottomBtnListener);
        findViewById(R.id.home_page_button_enterprise).setOnClickListener(bottomBtnListener);
        findViewById(R.id.home_page_button_supply_demand).setOnClickListener(bottomBtnListener);
        findViewById(R.id.home_page_button_bbs).setOnClickListener(bottomBtnListener);
    }

    private void changeFragment(Fragment f) {
        FragmentTransaction ft = getFragmentTransaction();
        if (!f.isAdded()) {
            ft.add(R.id.home_page_view_pager, f);
        } else {
            ft.show(f);
        }
        if (null != lastFragment && !f.equals(lastFragment)) {
            ft.hide(lastFragment);
        }
        lastFragment = f;
        ft.commit();
    }

    private Fragment getFragment(String key) {
        Fragment f = null;
        if ("首页".equals(key)) {
            f = TestFragment.newInstance(key);
        } else if ("咨询".equals(key)) {
            f = TestFragment.newInstance(key);
        } else if ("产品".equals(key)) {
            f = ProductListFragment.newInstance();
        } else if ("企业".equals(key)) {
            f = EnterpriseListFragment.newInstance();
        } else if ("供求".equals(key)) {
            f = SupplyDemandListFragment.newInstance();
        } else if ("论坛".equals(key)) {
            f = TestFragment.newInstance(key);
        } else {
            f = TestFragment.newInstance(key);
        }

        return f;
    }

    private void initTitle(String title) {
        title = CONTENT[0];
        if (CONTENT[0].equals(title)) {
            initBanner(R.id.banner_layout, R.drawable.banner, 0, R.drawable.flower_logo, R.string.home_page_sign_up,
                    R.drawable.button_corner_rectangle_selector, R.string.home_page_sign_in,
                    R.drawable.button_corner_rectangle_selector, new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getActivity(), SignUpMainPageActivity.class);
                            getActivity().startActivity(intent);
                        }
                    }, new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getActivity(), SignInPageActivity.class);
                            getActivity().startActivity(intent);
                        }
                    });

            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) getBanner().getTitleView().getLayoutParams();
            lp.width = UnitUtil.transformDipToPx(100);
            lp.height = UnitUtil.transformDipToPx(35);

        } else if (CONTENT[1].equals(title)) {
            initBanner(R.id.banner_layout, R.drawable.banner, R.string.home_page_information, 0, 0, 0, R.string.common_word,
                    R.drawable.button_corner_rectangle_selector, null, new OnClickListener() {

                        @Override
                        public void onClick(View v) {
//							Intent intent = new Intent(HomePageActivity.this, SignInPageActivity.class);
//							HomePageActivity.this.startActivity(intent);
                        }
                    });

        } else if (CONTENT[2].equals(title)) {
            initBanner(R.id.banner_layout, R.drawable.banner, R.string.home_page_product, 0, 0, 0, R.string.product_list_select,
                    R.drawable.button_corner_rectangle_selector, null, new OnClickListener() {

                        @Override
                        public void onClick(View v) {
//							Intent intent = new Intent(HomePageActivity.this, SignInPageActivity.class);
//							HomePageActivity.this.startActivity(intent);
                        }
                    });

        } else if (CONTENT[3].equals(title)) {
            initBanner(R.id.banner_layout, R.drawable.banner, R.string.home_page_enterprise, 0, 0, 0, R.string.common_word,
                    R.drawable.button_corner_rectangle_selector, null, new OnClickListener() {

                        @Override
                        public void onClick(View v) {
//							Intent intent = new Intent(HomePageActivity.this, SignInPageActivity.class);
//							HomePageActivity.this.startActivity(intent);
                        }
                    });

        } else if (CONTENT[4].equals(title)) {
            initBanner(R.id.banner_layout, R.drawable.banner, R.string.home_page_supply_demand, 0, 0, 0, R.string.common_word,
                    R.drawable.button_corner_rectangle_selector, null, new OnClickListener() {

                        @Override
                        public void onClick(View v) {
//							Intent intent = new Intent(HomePageActivity.this, SignInPageActivity.class);
//							HomePageActivity.this.startActivity(intent);
                        }
                    });

        } else if (CONTENT[5].equals(title)) {
            initBanner(R.id.banner_layout, R.drawable.banner, R.string.home_page_bbs, 0, 0, 0, R.string.common_word,
                    R.drawable.button_corner_rectangle_selector, null, new OnClickListener() {

                        @Override
                        public void onClick(View v) {
//							Intent intent = new Intent(HomePageActivity.this, SignInPageActivity.class);
//							HomePageActivity.this.startActivity(intent);
                        }
                    });
        } else {
            initBanner(R.id.banner_layout, R.drawable.banner, 0, R.drawable.flower_logo, 0, 0, 0, 0, null, null);
        }
    }

}
