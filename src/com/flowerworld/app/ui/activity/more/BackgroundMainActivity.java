package com.flowerworld.app.ui.activity.more;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import com.flowerworld.app.R;
import com.flowerworld.app.ui.base.BaseActivity;

/**
 * Created by guojj4 on 2014/7/16.
 */
public class BackgroundMainActivity extends BaseActivity implements View.OnClickListener {

    private Button mBtnExit = null;
    private Button mBtnMemberCenter = null;
    private Button mBtnTemplate = null;
    private Button mBtnProduct = null;
    private Button mBtnWechat = null;

    private ViewPager mViewPager = null;

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
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.background_main_button_exit:

            break;

        case R.id.background_main_button_member_center:

            break;

        case R.id.background_main_button_template:

            break;

        case R.id.background_main_button_product:

            break;

        case R.id.background_main_button_wechat:

            break;
        }
    }
}
