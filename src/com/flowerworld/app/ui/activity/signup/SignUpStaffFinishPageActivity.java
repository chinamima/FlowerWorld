package com.flowerworld.app.ui.activity.signup;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import com.flowerworld.app.R;
import com.flowerworld.app.ui.activity.MainTabPageActivity;
import com.flowerworld.app.ui.base.BaseActivity;

public class SignUpStaffFinishPageActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up_staff_finish);

        initView();
    }

    private void initView() {
        initBanner(R.id.banner_layout, R.drawable.banner, R.string.signup_stafffinish_title, 0, 0, 0, 0, 0, null, null);

//		initBottomBtn();
        initButton();
//		initBottomBtnSelected();
    }

    private void initButton() {
        findViewById(R.id.sign_up_staff_finish_button).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                MainTabPageActivity.goHere(SignUpStaffFinishPageActivity.this);
            }
        });

    }

}
