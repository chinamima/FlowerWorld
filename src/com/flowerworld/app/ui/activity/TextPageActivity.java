package com.flowerworld.app.ui.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import com.flowerworld.app.R;
import com.flowerworld.app.tool.util.UnitUtil;
import com.flowerworld.app.ui.base.BaseActivity;
import com.flowerworld.app.ui.widget.Banner;

public class TextPageActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.text_page);

        initView();
    }

    private void initView() {
        RelativeLayout bannerlayout = (RelativeLayout) findViewById(R.id.banner_layout);
        RelativeLayout.LayoutParams lp_banner = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT,
                RelativeLayout.LayoutParams.FILL_PARENT);
        bannerlayout.addView(initBanner(), lp_banner);

//		initBottomBtn();
//		initBottomBtnSelected();

        initText("");
    }

    private Banner initBanner() {
        Banner banner = new Banner(this);
        banner.setBackgroundResource(R.drawable.banner);
        banner.getTitleView().setText(R.string.signup_title);
        LayoutParams lp = (LayoutParams) banner.getTitleView().getLayoutParams();
        lp.width = UnitUtil.transformDipToPx(100);
        lp.height = UnitUtil.transformDipToPx(35);

        //left button
        Button left = new Button(this);
        left.setText(R.string.go_back);
        left.setTextSize(14);
        left.setBackgroundResource(R.drawable.button_corner_rectangle_selector);
        left.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
        banner.addLeftItem(left);

//		//right button
//		Button right = new Button(this);
//		right.setText(R.string.signup_declare);
//		right.setTextSize(UnitUtil.transformSpToPx(9));
//		right.setBackgroundResource(R.drawable.button_corner_rectangle_selector);
//		right.setOnClickListener(new OnClickListener()
//		{
//			
//			@Override
//			public void onClick(View v)
//			{
////				Intent intent = new Intent(SignUpMainPageActivity.this, SignUpMainPageActivity.class);
////				SignUpMainPageActivity.this.startActivity(intent);
//			}
//		});
//		banner.addRightItem(right);

        return banner;
    }

    private void initText(String str) {
        if (TextUtils.isEmpty(str)) {
            return;
        }

        TextView text = (TextView) findViewById(R.id.text_page_text);
        text.setText(str);
    }
}
