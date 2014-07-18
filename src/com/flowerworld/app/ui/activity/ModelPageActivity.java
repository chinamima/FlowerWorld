package com.flowerworld.app.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import com.flowerworld.app.R;
import com.flowerworld.app.ui.base.BaseActivity;

public class ModelPageActivity extends BaseActivity implements OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.module_page);
        initView();
    }

    protected void initView() {
        findViewById(R.id.module_page_button).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.module_page_button:
            MainTabPageActivity.goHere(this);
            finish(false);
            break;
        }
    }
}
