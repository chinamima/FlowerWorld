package com.flowerworld.app.ui.activity.signup;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import com.flowerworld.app.R;
import com.flowerworld.app.ui.activity.MainTabPageActivity;
import com.flowerworld.app.ui.activity.list.base.MultiSelectListPageActivity;
import com.flowerworld.app.ui.activity.list.base.SingleSelectListPageActivity;
import com.flowerworld.app.ui.base.BaseActivity;

public class SignUpEnterpriseFinishPageActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up_enterprise_finish);

        initView();
    }

    private void initView() {
        initBanner(R.id.banner_layout, R.drawable.banner, R.string.signup_enterprisefinish_title, 0, 0, 0, 0, 0, null, null);

//		initBottomBtn();
//		initBottomBtnSelected();
        initButton();
    }

    private void initButton() {
        findViewById(R.id.sign_up_enterprise_finish_button_member).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpEnterpriseFinishPageActivity.this, SingleSelectListPageActivity.class);
                startActivity(intent);
            }
        });
        findViewById(R.id.sign_up_enterprise_finish_button_information).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpEnterpriseFinishPageActivity.this, MultiSelectListPageActivity.class);
                startActivity(intent);
            }
        });
        findViewById(R.id.sign_up_enterprise_finish_button_browser).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                MainTabPageActivity.goHere(SignUpEnterpriseFinishPageActivity.this);
            }
        });

    }

}
