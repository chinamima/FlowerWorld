package com.flowerworld.app.ui.activity.more;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import com.flowerworld.app.R;
import com.flowerworld.app.ui.base.BaseFragmentActivity;
import com.flowerworld.app.ui.fragment.TestFragment;
import com.flowerworld.app.ui.fragment.background.MemberCenterFragment;

/**
 * Created by guojj4 on 2014/7/16.
 */
public class BackgroundMainActivity extends BaseFragmentActivity implements View.OnClickListener {

    //widget
    private Button mBtnExit = null;
    private Button mBtnMemberCenter = null;
    private Button mBtnTemplate = null;
    private Button mBtnProduct = null;
    private Button mBtnWechat = null;

    private ViewPager mViewPager = null;
    private FrameLayout mFrameSwitcher = null;

    //selection
    protected int mSelectionButtonId = View.NO_ID;

    //data
    private Fragment[] mFragments = new Fragment[4];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.background_main_page);
        initialize();
    }

    private void initialize() {
        mBtnExit = (Button) findViewById(R.id.background_main_button_exit);
        mBtnMemberCenter = (Button) findViewById(R.id.background_main_button_member_center);
        mBtnTemplate = (Button) findViewById(R.id.background_main_button_template);
        mBtnProduct = (Button) findViewById(R.id.background_main_button_product);
        mBtnWechat = (Button) findViewById(R.id.background_main_button_wechat);
        mViewPager = (ViewPager) findViewById(R.id.background_main_viewpager);

        mBtnExit.setOnClickListener(this);
        mBtnMemberCenter.setOnClickListener(this);
        mBtnTemplate.setOnClickListener(this);
        mBtnProduct.setOnClickListener(this);
        mBtnWechat.setOnClickListener(this);

        mSelectionButtonId = R.id.background_main_button_member_center;
        mBtnMemberCenter.setSelected(true);

        initFragments();
//        mViewPager.setAdapter(new CustomPageAdapter(getSupportFragmentManager()));
        switchFragment(0);
    }

    private void initFragments() {
        mFragments[0] = MemberCenterFragment.newInstance();
        mFragments[1] = TestFragment.newInstance("模板管理");
        mFragments[2] = TestFragment.newInstance("产品");
        mFragments[3] = TestFragment.newInstance("微信平台");
    }

    @Override
    public void onClick(View v) {
        int btnId = v.getId();
        if (btnId == mSelectionButtonId) {
            return;
        } else {
            findViewById(mSelectionButtonId).setSelected(false);
            findViewById(btnId).setSelected(true);
            mSelectionButtonId = btnId;
        }

        int index = 0;
        switch (btnId) {
        case R.id.background_main_button_exit:
            finish();
            break;

        case R.id.background_main_button_member_center:
            index = 0;
            break;

        case R.id.background_main_button_template:
            index = 1;
            break;

        case R.id.background_main_button_product:
            index = 2;
            break;

        case R.id.background_main_button_wechat:
            index = 3;
            break;
        }
        switchFragment(index);
    }

    private void switchFragment(int index) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.background_main_frame, mFragments[index]);
        transaction.disallowAddToBackStack();
        transaction.commit();
    }

    private class CustomPageAdapter extends FragmentPagerAdapter {

        public CustomPageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return mFragments.length;
        }

        @Override
        public Fragment getItem(int i) {
            return mFragments[i];
        }


    }
}
