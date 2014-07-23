package com.flowerworld.app.ui.fragment.background;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.flowerworld.app.R;
import com.flowerworld.app.ui.base.BaseFragment;
import com.flowerworld.app.ui.fragment.TestFragment;
import com.flowerworld.app.ui.widget.TabScrollBar;

/**
 * Created by guojj4 on 14/07/19.
 */
public class MemberCenterFragment extends BaseFragment implements View.OnClickListener, TabScrollBar.ITabButtonClickListener {

    private MemberCenterFragment() {
    }

    private static MemberCenterFragment mSingleInstance = new MemberCenterFragment();
    public static MemberCenterFragment newInstance() {
        return mSingleInstance;
    }

    private Fragment[] mFragments = new Fragment[2];
    private TabScrollBar mScroll = null;

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.member_center_page, container, false);
    }

    @Override
    protected void initView() {
        mFragments[0] = TestFragment.newInstance("欢迎页面");
        mFragments[1] = TestFragment.newInstance("个人资料");

//        findViewById(R.id.member_center_button_welcome).setOnClickListener(this);
//        findViewById(R.id.member_center_button_information).setOnClickListener(this);
        mScroll = (TabScrollBar) findViewById(R.id.member_center_scroll);
        mScroll.setTabButtonListener(this);
    }

    @Override
    protected void initData() {
        mScroll.addButton("欢迎页面", null);
        mScroll.addButton("个人资料", null);
        mScroll.addButton("个人资料", null);
        mScroll.addButton("个人资料", null);
        mScroll.addButton("个人资料", null);
        mScroll.addButton("个人资料", null);
        mScroll.addButton("个人资料", null);
        mScroll.addButton("个人资料", null);
        mScroll.addButton("个人资料", null);
        mScroll.addButton("个人资料", null);
        mScroll.addButton("个人资料", null);
        mScroll.addButton("个人资料", null);
        saveData(true);
    }

    @Override
    protected void resumeData() {

    }

    private void switchFragment(int index) {
        FragmentTransaction transaction = getFragmentTransaction();
        transaction.replace(R.id.member_center_frame, mFragments[index]);
        transaction.commit();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

//        int index = 0;
//        switch (id) {
//        case R.id.member_center_button_welcome:
//            index = 0;
//            break;
//
//        case R.id.member_center_button_information:
//            index = 1;
//            break;
//        }
//
//        switchFragment(index);
    }

    @Override
    public void onClick(View view, int position, Object data) {
        switchFragment(position);
    }
}
