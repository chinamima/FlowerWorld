package com.flowerworld.app.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.flowerworld.app.R;
import com.flowerworld.app.dao.bean.GlobalVariableBean;
import com.flowerworld.app.ui.activity.SignInPageActivity;
import com.flowerworld.app.ui.activity.more.BackgroundMainActivity;
import com.flowerworld.app.ui.activity.more.FeedbackActivity;
import com.flowerworld.app.ui.activity.signup.SignUpMainPageActivity;
import com.flowerworld.app.ui.base.BaseFragment;

public final class MoreFragment extends BaseFragment implements View.OnClickListener {
    private static MoreFragment fragment = new MoreFragment();

    public static MoreFragment newInstance() {
        return fragment;
    }

    private Button mBtnLeft = null;
    private Button mBtnRight = null;

    private CheckBox mCheckNotification = null;
    private TextView mTextAccountStatus = null;
    private TextView mTextCache = null;

    private RelativeLayout mRelativeShare = null;
    private RelativeLayout mRelativeFeedback = null;
    private RelativeLayout mRelativeUpdateVersion = null;
    private RelativeLayout mRelativeAboutUs = null;



    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.more_main, container, false);
    }

    @Override
    protected void initView() {
        initBanner(R.id.banner_layout, R.drawable.banner, R.string.home_page_more, 0, 0, 0, 0, 0, null, null);

        //view
        mBtnLeft = (Button) findViewById(R.id.more_main_button_left);
        mBtnRight = (Button) findViewById(R.id.more_main_button_right);

        mCheckNotification = (CheckBox) findViewById(R.id.more_main_check_notification);
        mTextAccountStatus = (TextView) findViewById(R.id.more_main_layout_text_account_status);
        mTextCache = (TextView) findViewById(R.id.more_main_layout_text_cache);

        mRelativeShare = (RelativeLayout) findViewById(R.id.more_main_layout_item_share);
        mRelativeFeedback = (RelativeLayout) findViewById(R.id.more_main_layout_item_feedback);
        mRelativeUpdateVersion = (RelativeLayout) findViewById(R.id.more_main_layout_update_version);
        mRelativeAboutUs = (RelativeLayout) findViewById(R.id.more_main_layout_item_about_us);

        //event
        mCheckNotification.setOnClickListener(this);
        mTextAccountStatus.setOnClickListener(this);
        mTextCache.setOnClickListener(this);

        mRelativeShare.setOnClickListener(this);
        mRelativeFeedback.setOnClickListener(this);
        mRelativeUpdateVersion.setOnClickListener(this);
        mRelativeAboutUs.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        if (GlobalVariableBean.userInfo == null) {
            mBtnLeft.setText("登陆");
            mBtnRight.setText("注册");
            mBtnLeft.setOnClickListener(mListenerLogin);
            mBtnRight.setOnClickListener(mListenerLogin);
        } else {
            mBtnLeft.setText("企业后台");
            mBtnRight.setText("个人后台");
            mBtnLeft.setOnClickListener(mListenerBackground);
            mBtnRight.setOnClickListener(mListenerBackground);
        }
    }

    @Override
    protected void resumeData() {

    }

    private View.OnClickListener mListenerLogin = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = null;
            switch (view.getId()) {
            case R.id.more_main_button_left:
                intent = new Intent(getActivity(), SignInPageActivity.class);
                getActivity().startActivity(intent);
                break;

            case R.id.more_main_button_right:
                intent = new Intent(getActivity(), SignUpMainPageActivity.class);
                getActivity().startActivity(intent);
                break;
            }
        }
    };

    private View.OnClickListener mListenerBackground = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = null;
            switch (view.getId()) {
            case R.id.more_main_button_left:
                intent = new Intent(getActivity(), BackgroundMainActivity.class);
                getActivity().startActivity(intent);
                break;

            case R.id.more_main_button_right:
//                intent = new Intent(getActivity(), SignUpMainPageActivity.class);
//                getActivity().startActivity(intent);
                break;
            }
        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
        case R.id.more_main_check_notification:

            break;

        case R.id.more_main_layout_text_account_status:

            break;

        case R.id.more_main_layout_text_cache:

            break;

        case R.id.more_main_layout_item_share:

            break;

        case R.id.more_main_layout_item_feedback:
            Intent feedbackIntent = new Intent(getActivity(), FeedbackActivity.class);
            getActivity().startActivity(feedbackIntent);
            break;

        case R.id.more_main_layout_update_version:

            break;

        case R.id.more_main_layout_item_about_us:

            break;
        }
    }
}
