package com.flowerworld.app.ui.activity.signup;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import com.flowerworld.app.R;
import com.flowerworld.app.ui.base.BaseActivity;

public class SignUpSelectTypePageActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up_select_type);

        initView();
    }

    private void initView() {
        initBanner(R.id.banner_layout, R.drawable.banner, R.string.signup_selecttype_title, 0, R.string.go_back,
                R.drawable.button_corner_rectangle_selector, R.string.signup_selecttype_banner_right,
                R.drawable.button_corner_rectangle_selector, new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                }, new OnClickListener() {

                    @Override
                    public void onClick(View v) {

                    }
                });

//		initBottomBtn();
        initButton();
//		initBottomBtnSelected();
    }

    private void initButton() {
        findViewById(R.id.sign_up_select_type_staff).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpSelectTypePageActivity.this, SignUpSearchEnterprisePageActivity.class);
                startActivity(intent);
            }
        });
        findViewById(R.id.sign_up_select_type_free).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpSelectTypePageActivity.this, SignUpFreePageActivity.class);
                startActivity(intent);
            }
        });
        findViewById(R.id.sign_up_select_type_interest).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpSelectTypePageActivity.this, SignUpInterestPageActivity.class);
                startActivity(intent);
            }
        });

    }

}
