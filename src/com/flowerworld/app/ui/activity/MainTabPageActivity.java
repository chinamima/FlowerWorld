package com.flowerworld.app.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import com.flowerworld.app.R;
import com.flowerworld.app.tool.util.UnitUtil;
import com.flowerworld.app.ui.base.BaseFragmentActivity;
import com.flowerworld.app.ui.fragment.*;
import com.flowerworld.app.ui.widget.ImageTextButton;

public class MainTabPageActivity extends BaseFragmentActivity {
    private final static String TAG = MainTabPageActivity.class.getSimpleName();

    public final static int FRAME_CONTENT_ID = R.id.main_tab_frame_content;

    public int bottomBtnCurrentSelectedId = R.id.bottom_btn_browse;
    public int bottomBtnLastSelectedId = View.NO_ID;
    private Fragment lastFragment = null;
    public View.OnClickListener bottomBtnListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            View parent = (View) v.getParent();
            if (bottomBtnCurrentSelectedId == parent.getId()) {
                return;
            }
            bottomBtnLastSelectedId = bottomBtnCurrentSelectedId;
            bottomBtnCurrentSelectedId = parent.getId();

            v.setSelected(true);
            if (View.NO_ID != bottomBtnLastSelectedId) {
                ((ViewGroup) parent.getParent()).findViewById(bottomBtnLastSelectedId).setSelected(false);
            }

            Fragment f = null;
            switch (bottomBtnCurrentSelectedId) {
            case R.id.bottom_btn_browse:
                f = HomePageFragment.newInstance();
                break;

            case R.id.bottom_btn_search:
                f = SearchMainFragment.newInstance();
                break;

            case R.id.bottom_btn_message:
                f = TestFragment.newInstance("消息");
                break;

            case R.id.bottom_btn_friend:
                f = FriendListFragment.newInstance();
                break;

            case R.id.bottom_btn_more:
                f = MoreFragment.newInstance();
                break;

            default:
                break;
            }
//			mTransaction.disallowAddToBackStack();
//			getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
//			FragmentTransaction ft = getFragmentTransaction();
////			if (null != lastFragment)
////				ft.remove(lastFragment);
//			ft.replace(FRAME_CONTENT_ID, f).commit();
//			lastFragment = f;
//			getFragmentTransaction().replace(FRAME_CONTENT_ID, f).commit();
            changeFragment(f);
        }
    };

    private void changeFragment(Fragment f) {
        FragmentTransaction ft = getFragmentTransaction();
        if (!f.isAdded()) {
            ft.add(FRAME_CONTENT_ID, f);
        } else {
            ft.show(f);
        }
        if (null != lastFragment && !f.equals(lastFragment)) {
            ft.hide(lastFragment);
        }
        lastFragment = f;
        ft.commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main_tab_page);

        initView();

    }

    private void initView() {
        initBottomBtn();
        initBottomBtnSelected();

        lastFragment = HomePageFragment.newInstance();
//		getFragmentTransaction().replace(FRAME_CONTENT_ID, lastFragment).commit();
        changeFragment(lastFragment);
    }

    public void initBottomBtnSelected() {
        final ImageTextButton bottomBtn = (ImageTextButton) this.findViewById(bottomBtnCurrentSelectedId);
        bottomBtn.postDelayed(new Runnable() {

            @Override
            public void run() {
                bottomBtn.getButtonBg().performClick();
                bottomBtn.setSelected(true);
            }
        }, 100);
    }

    public void initBottomBtn() {
        //browse
        ImageTextButton browse = initOneBottomBtn(R.id.bottom_btn_browse, R.drawable.bottom_button_browse_selector,
                R.string.home_page_browse, R.color.bottom_button_color_selector, R.drawable.bottom_button_selector);
//		browse.setOnClickListener((OnClickListener) activity);

        //search
        ImageTextButton search = initOneBottomBtn(R.id.bottom_btn_search, R.drawable.bottom_button_search_selector,
                R.string.home_page_search, R.color.bottom_button_color_selector, R.drawable.bottom_button_selector);
//		search.setOnClickListener((OnClickListener) activity);

        //message
        ImageTextButton message = initOneBottomBtn(R.id.bottom_btn_message, R.drawable.bottom_button_message_selector,
                R.string.home_page_message, R.color.bottom_button_color_selector, R.drawable.bottom_button_selector);
//		message.setOnClickListener((OnClickListener) activity);

        //friend
        ImageTextButton friend = initOneBottomBtn(R.id.bottom_btn_friend, R.drawable.bottom_button_friend_selector,
                R.string.home_page_friend, R.color.bottom_button_color_selector, R.drawable.bottom_button_selector);
//		friend.setOnClickListener((OnClickListener) activity);

        //more
        ImageTextButton more = initOneBottomBtn(R.id.bottom_btn_more, R.drawable.bottom_button_more_selector,
                R.string.home_page_more, R.color.bottom_button_color_selector, R.drawable.bottom_button_selector);
//		more.setOnClickListener((OnClickListener) activity);

    }

    private ImageTextButton initOneBottomBtn(int bottomBtnId, int iconID, int wordId, int colorId, int bgId) {
        ImageTextButton btn = (ImageTextButton) this.findViewById(bottomBtnId);

        ImageView image = btn.getImageView();
        image.setImageResource(iconID);
        LayoutParams lp = (LayoutParams) image.getLayoutParams();
        lp.width = UnitUtil.transformDipToPx(25);
        lp.height = UnitUtil.transformDipToPx(25);

        TextView text = btn.getTextView();
        text.setText(wordId);
        text.setTextSize(13);
        text.setGravity(Gravity.CENTER_HORIZONTAL);
        text.setTextColor(this.getResources().getColorStateList(colorId));

        Button button = btn.getButtonBg();
        button.setBackgroundResource(bgId);
        button.setOnClickListener(bottomBtnListener);

        return btn;
    }

    public static final void goHere(Activity activity) {
        Intent intent = new Intent(activity, MainTabPageActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        activity.startActivity(intent);
    }
}
