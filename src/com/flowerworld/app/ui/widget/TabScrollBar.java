package com.flowerworld.app.ui.widget;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import com.flowerworld.app.R;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by guojj4 on 14/07/20.
 */
public class TabScrollBar extends RelativeLayout implements View.OnClickListener, View.OnTouchListener {

    private Button mBtnLeftArrow = null;
    private Button mBtnRightArrow = null;
    private LinearLayout mLinearTab = null;
    private HorizontalScrollView mScroll = null;

    //data
    private int mPosition = 0;
    private List<Object> mDataList = new LinkedList<Object>();
    private static int mIdCount = 0x948573;

    //
    private View mLastSelectedButton = null;

    //
    private static final int STEP = 5;

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (MotionEvent.ACTION_MOVE != event.getAction()) {
            return true;
        }

        switch (v.getId()) {
        case R.id.widget_tab_scroll_bar_button_arrow_left:
            mScroll.scrollBy(-STEP, 0);
//            mLinearTab.scrollBy(-STEP, 0);
            break;

        case R.id.widget_tab_scroll_bar_button_arrow_right:
            mScroll.scrollBy(STEP, 0);
//            mLinearTab.scrollBy(STEP, 0);
            break;
        }
        return true;
    }


    public interface ITabButtonClickListener {
        void onClick(View view, int position, Object data);
    }

    public void setTabButtonListener(ITabButtonClickListener listener) {
        this.mTabButtonListener = listener;
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
        mScroll = (HorizontalScrollView) findViewById(R.id.widget_tab_scroll_bar_scroll);

//        mBtnLeftArrow.setOnClickListener(this);
//        mBtnRightArrow.setOnClickListener(this);
        mBtnLeftArrow.setOnTouchListener(this);
        mBtnRightArrow.setOnTouchListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
//        case R.id.widget_tab_scroll_bar_button_arrow_left:
//            mScroll.scrollBy(-STEP, 0);
////            mLinearTab.scrollBy(-STEP, 0);
//            break;
//
//        case R.id.widget_tab_scroll_bar_button_arrow_right:
//            mScroll.scrollBy(STEP, 0);
////            mLinearTab.scrollBy(STEP, 0);
//            break;

        default:
            if (null == mLastSelectedButton || view.hashCode() == mLastSelectedButton.hashCode()) {
                break;
            }
            mLastSelectedButton.setSelected(false);
            view.setSelected(true);
            int position = (Integer) view.getTag();
            if (null != mTabButtonListener) {
                mTabButtonListener.onClick(view, position, mDataList.get(position));
            }
            mLastSelectedButton = view;
            break;
        }
    }

    public Button addButton(String name, Object data) {
        Button button = new Button(getContext());
        button.setId(mIdCount++);
        button.setBackgroundResource(R.drawable.background_tab_selector);
        button.setText(name);
        button.setTextColor(Color.rgb(0, 138, 181));
        button.setTextSize(16);
        button.setOnClickListener(this);
        mDataList.add(data);

        if (0 == mPosition) {
            button.setSelected(true);
            mLastSelectedButton = button;
        }
        button.setTag(mPosition++);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.FILL_PARENT);
        mLinearTab.addView(button, lp);

        return button;
    }

}
