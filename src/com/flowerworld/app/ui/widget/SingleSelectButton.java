package com.flowerworld.app.ui.widget;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import com.flowerworld.app.tool.util.UnitUtil;

public class SingleSelectButton extends RelativeLayout implements OnClickListener {
    private int mButtonDrawableResId = NO_ID;
    private int mButtonId = 0x1234560;
    private int mLastButtonId = 0x1234560;
    private int mLastClickId = NO_ID;

    public SingleSelectButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView();
    }

    public SingleSelectButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public SingleSelectButton(Context context) {
        super(context);
        initView();
    }

    private void initView() {

    }

    public void setButtonDrawable(int id) {
        mButtonDrawableResId = id;
    }

    public void addSelectButton(String text, String value) {
        Button btn = new Button(getContext());
        btn.setTag(value);
        btn.setBackgroundResource(mButtonDrawableResId);
        btn.setText(text);
        btn.setTextColor(Color.BLACK);
        btn.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        btn.setId(mButtonId);
        btn.setOnClickListener(this);

        LayoutParams lp = new LayoutParams(UnitUtil.transformDipToPx(60), UnitUtil.transformDipToPx(40));
        lp.setMargins(0, 0, UnitUtil.transformDipToPx(10), 0);
        lp.addRule(CENTER_VERTICAL);
        if (mButtonId != mLastButtonId) {
            lp.addRule(RIGHT_OF, mLastButtonId);
        }
        addView(btn, lp);

        mLastButtonId = mButtonId++;
    }

    @Override
    public void onClick(View v) {
        Log.d(VIEW_LOG_TAG, "=====id: " + v.getId());
        if (mLastClickId == v.getId()) {
            return;
        }

        v.setSelected(true);

        if (NO_ID != mLastClickId) {
            findViewById(mLastClickId).setSelected(false);
        }

        mLastClickId = v.getId();
    }

    public Object getSelectedValue() {
        if (NO_ID == mLastClickId) {
            return "";
        }
        return findViewById(mLastClickId).getTag();
    }

}
