package com.flowerworld.app.ui.base;

import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import com.flowerworld.app.ui.widget.Banner;

public class BaseTools {

    private View view = null;

    public BaseTools(View v) {
        this.view = v;
    }

//	public void initBottomBtnSelected()
//	{
//		final ImageTextButton bottomBtn = (ImageTextButton) view.findViewById(GlobalVariableBean.bottomBtnCurrentSelectedId);
//		bottomBtn.postDelayed(new Runnable()
//		{
//
//			@Override
//			public void run()
//			{
//				bottomBtn.getButtonBg().performClick();
//				bottomBtn.setSelected(true);
//			}
//		}, 100);
//	}
//
//	public void initBottomBtn()
//	{
//		//browse
//		ImageTextButton browse = initOneBottomBtn(R.id.bottom_btn_browse, R.drawable.bottom_button_browse_selector,
//				R.string.home_page_browse, R.color.bottom_button_color_selector, R.drawable.bottom_button_selector);
////		browse.setOnClickListener((OnClickListener) activity);
//
//		//search
//		ImageTextButton search = initOneBottomBtn(R.id.bottom_btn_search, R.drawable.bottom_button_search_selector,
//				R.string.home_page_search, R.color.bottom_button_color_selector, R.drawable.bottom_button_selector);
////		search.setOnClickListener((OnClickListener) activity);
//
//		//message
//		ImageTextButton message = initOneBottomBtn(R.id.bottom_btn_message, R.drawable.bottom_button_message_selector,
//				R.string.home_page_message, R.color.bottom_button_color_selector, R.drawable.bottom_button_selector);
////		message.setOnClickListener((OnClickListener) activity);
//
//		//friend
//		ImageTextButton friend = initOneBottomBtn(R.id.bottom_btn_friend, R.drawable.bottom_button_friend_selector,
//				R.string.home_page_friend, R.color.bottom_button_color_selector, R.drawable.bottom_button_selector);
////		friend.setOnClickListener((OnClickListener) activity);
//
//		//more
//		ImageTextButton more = initOneBottomBtn(R.id.bottom_btn_more, R.drawable.bottom_button_more_selector,
//				R.string.home_page_more, R.color.bottom_button_color_selector, R.drawable.bottom_button_selector);
////		more.setOnClickListener((OnClickListener) activity);
//
//	}
//
//	private ImageTextButton initOneBottomBtn(int bottomBtnId, int iconID, int wordId, int colorId, int bgId)
//	{
//		ImageTextButton btn = (ImageTextButton) view.findViewById(bottomBtnId);
//
//		ImageView image = btn.getImageView();
//		image.setImageResource(iconID);
//		LayoutParams lp = (LayoutParams) image.getLayoutParams();
//		lp.width = UnitUtil.transformDipToPx(25);
//		lp.height = UnitUtil.transformDipToPx(25);
//
//		TextView text = btn.getTextView();
//		text.setText(wordId);
//		text.setTextSize(13);
//		text.setGravity(Gravity.CENTER_HORIZONTAL);
//		text.setTextColor(view.getResources().getColorStateList(colorId));
//
//		Button button = btn.getButtonBg();
//		button.setBackgroundResource(bgId);
//		button.setOnClickListener(GlobalVariableBean.bottomBtnListener);
//
//		return btn;
//	}

    public Banner initBanner(int parent, int bg, int title, int titleBg, int leftText, int leftBg, int rightText, int rightBg,
            OnClickListener leftClk, OnClickListener rightClk) {
        Banner banner = new Banner(view.getContext());
        if (bg > 0) {
            banner.setBackgroundResource(bg);
        }
        if (title > 0) {
            banner.getTitleView().setText(title);
        }
        if (titleBg > 0) {
            banner.getTitleView().setBackgroundResource(titleBg);
        }
//		LayoutParams lp = (LayoutParams) banner.getTitleView().getLayoutParams();
//		lp.width = UnitUtil.transformDipToPx(100);
//		lp.height = UnitUtil.transformDipToPx(35);

        //left button
        if (leftText > 0 || leftBg > 0 || null != leftClk) {
            Button left = new Button(view.getContext());
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
            banner.addLeftItem(left);
        }

        //right button
        if (rightText > 0 || rightBg > 0 || null != rightClk) {
            Button right = new Button(view.getContext());
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
            banner.addRightItem(right);
        }

        RelativeLayout bannerlayout = (RelativeLayout) view.findViewById(parent);
        RelativeLayout.LayoutParams lp_banner = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT,
                RelativeLayout.LayoutParams.FILL_PARENT);
        bannerlayout.addView(banner, lp_banner);

        return banner;
    }
}
