package com.flowerworld.app.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import com.flowerworld.app.R;

/**
 * Created by guojj4 on 14/07/20.
 */
public class TabScrollBar extends RelativeLayout implements View.OnClickListener {

    private Button mBtnLeftArrow = null;
    private Button mBtnRightArrow = null;
    private LinearLayout mLinearTab = null;

    public interface ITabButtonClickListener {
        void onClick(int position, View view);
    }

    private ITabButtonClickListener mTabButtonListener = null;

    public TabScrollBar(Context context) {
        super(context);
        initialize();
    }

    public TabScrollBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    public TabScrollBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initialize();
    }

    private void initialize() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.widget_tab_scroll_bar, this, true);

        mBtnLeftArrow = (Button) findViewById(R.id.widget_tab_scroll_bar_button_arrow_left);
        mBtnRightArrow = (Button) findViewById(R.id.widget_tab_scroll_bar_button_arrow_right);
        mLinearTab = (LinearLayout) findViewById(R.id.widget_tab_scroll_bar_layout_tab);

        mBtnLeftArrow.setOnClickListener(this);
        mBtnRightArrow.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
        case R.id.widget_tab_scroll_bar_button_arrow_left:

            break;

        case R.id.widget_tab_scroll_bar_button_arrow_right:

            break;

        default:
            int position = (Integer) view.getTag();
            if (null != mTabButtonListener)
                mTabButtonListener.onClick(position, view);
            break;
        }
    }
}
