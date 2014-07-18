package com.flowerworld.app.ui.widget;

import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.flowerworld.app.tool.util.UnitUtil;

public class Banner extends RelativeLayout {
    protected static final String TAG = Banner.class.getSimpleName();

    protected TextView mTitleTextView = null;
    protected LinearLayout mLeftLayout = null;
    protected LinearLayout mRightLayout = null;

    public Banner(Context context) {
        super(context);
        initializeView();
    }

    protected void initializeView() {
//		LayoutInflater.from(getContext());

        //background
//		this.setBackgroundColor(Color.YELLOW);

        //title
        mTitleTextView = new TextView(getContext());
        mTitleTextView.setText("");
        mTitleTextView.setTextSize(22);
        mTitleTextView.setTextColor(Color.WHITE);
        mTitleTextView.setGravity(Gravity.CENTER);
        mTitleTextView.setSingleLine();

        LayoutParams lp_title = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        lp_title.addRule(RelativeLayout.CENTER_IN_PARENT);
//		lp_title.setMargins(0, UnitUtil.transformDipToPx(5), 0, UnitUtil.transformDipToPx(5));
        this.addView(mTitleTextView, lp_title);

        //left
        mLeftLayout = new LinearLayout(getContext());
        mLeftLayout.setOrientation(LinearLayout.HORIZONTAL);

        LayoutParams lp_left = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        lp_left.addRule(RelativeLayout.CENTER_VERTICAL);
        lp_left.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        this.addView(mLeftLayout, lp_left);

        //right
        mRightLayout = new LinearLayout(getContext());
        mRightLayout.setOrientation(LinearLayout.HORIZONTAL);

        LayoutParams lp_right = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        lp_right.addRule(RelativeLayout.CENTER_VERTICAL);
        lp_right.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        this.addView(mRightLayout, lp_right);

        //test
//		this.setBackgroundColor(Color.GREEN);
    }

    public TextView getTitleView() {
        return mTitleTextView;
    }

    public void addLeftItem(int leftText, int leftBg, OnClickListener leftClk) {
        if (leftText > 0 || leftBg > 0 || null != leftClk) {
            Button left = new Button(getContext());
            if (leftText > 0) {
                left.setText(leftText);
            }
            left.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            if (leftBg > 0) {
                left.setBackgroundResource(leftBg);
            }
            if (null != leftClk) {
                left.setOnClickListener(leftClk);
            }
            addLeftItem(left);
        }
    }

    public void addLeftItem(View item, LinearLayout.LayoutParams params) {
        mLeftLayout.addView(item, params);
    }

    public void addLeftItem(View item) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(UnitUtil.transformDipToPx(50),
                UnitUtil.transformDipToPx(30));
        params.setMargins(UnitUtil.transformDipToPx(10), 0, 0, 0);
        addLeftItem(item, params);
    }

    public void addRightItem(int rightText, int rightBg, OnClickListener rightClk) {
        if (rightText > 0 || rightBg > 0 || null != rightClk) {
            Button right = new Button(getContext());
            if (rightText > 0) {
                right.setText(rightText);
            }
            right.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            if (rightBg > 0) {
                right.setBackgroundResource(rightBg);
            }
            if (null != rightClk) {
                right.setOnClickListener(rightClk);
            }
            addRightItem(right);
        }
    }

    public void addRightItem(View item, LinearLayout.LayoutParams params) {
        mRightLayout.addView(item, 0, params);
    }

    public void addRightItem(View item) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(UnitUtil.transformDipToPx(50),
                UnitUtil.transformDipToPx(30));
        params.setMargins(0, 0, UnitUtil.transformDipToPx(10), 0);
        addRightItem(item, params);
    }
}
