package com.flowerworld.app.ui.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.flowerworld.app.R;

/**
 * 图文按钮
 *
 * @author Guo.Jinjun
 */
public class ImageTextButton extends RelativeLayout {
    protected static final String TAG = ImageTextButton.class.getSimpleName();

    protected ImageView mImage = null;
    protected TextView mText = null;
    protected Button mButtonBg = null;

    private ColorStateList mTextColor = null;

    public ImageTextButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView();
    }

    public ImageTextButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public ImageTextButton(Context context) {
        super(context);
        initView();
    }

    protected void initView() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        RelativeLayout widget = (RelativeLayout) inflater.inflate(R.layout.widget_image_text_button, this);
        mImage = (ImageView) widget.findViewById(R.id.widget_image_text_pic);
        mText = (TextView) widget.findViewById(R.id.widget_image_text_text);
        mButtonBg = new Button(getContext()) {
            @Override
            protected void drawableStateChanged() {
                super.drawableStateChanged();
                changeDrawableState(mButtonBg.getDrawableState());
            }
        };
        this.addView(mButtonBg, 0, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
    }

    protected void changeDrawableState(final int[] drawableState) {
//		logaa(drawableState);
//		mImage.getDrawable().setState(drawableState);
//		mImage.getDrawable().setState(filterState(drawableState));
//		logbb(mImage.getDrawable().getState());

        this.postDelayed(new Runnable() {

            @Override
            public void run() {
                mImage.getDrawable().setState(drawableState);
//				mImage.getDrawable().setState(filterState(drawableState));

                if (null == mTextColor) {
                    mTextColor = mText.getTextColors();
                }

                int color = mTextColor.getColorForState(drawableState, Color.GRAY);
                mText.setTextColor(color);
            }
        }, 100);
    }

//	private int[] filterState(int[] drawableState)
//	{
//		List<Integer> list = new LinkedList<Integer>();
//		for (int i = 0; i < drawableState.length; i++)
//		{
//			int state = drawableState[i];
//			switch (state)
//			{
//			case android.R.attr.state_enabled:
////				list.add(state);
//				break;
//			case android.R.attr.state_pressed:
//				list.add(state);
//				break;
//			case android.R.attr.state_selected:
//				list.add(state);
//				break;
//			}
//		}
//
//		int[] newState = new int[list.size()];
//		for (int j=0; j<list.size(); j++)
//		{
//			newState[j] = list.get(j);
//		}
//		logaa(newState);
//		return newState;
//	}
//
//	private void logaa(int[] drawableState)
//	{
//		StringBuffer sb = new StringBuffer();
//		for (int i = 0; i < drawableState.length; i++)
//		{
//			sb.append(drawableState[i] + ",");
//		}
//		LOG.d(TAG, "=====" + mText.getText() + "=drawableState: " + sb.toString());
//	}

//	private void logbb(int[] drawableState)
//	{
//		StringBuffer sb = new StringBuffer();
//		for (int i = 0; i < drawableState.length; i++)
//		{
//			sb.append(drawableState[i] + ",");
//		}
//		LOG.d(TAG, "=====" + mText.getText() + "=mImage=drawableState: " + sb.toString());
//	}

    public ImageView getImageView() {
        return mImage;
    }

    public TextView getTextView() {
        return mText;
    }

    public Button getButtonBg() {
        return mButtonBg;
    }

    @Override
    public void setSelected(boolean selected) {
        if (null == mButtonBg) {
            return;
        }
        mButtonBg.setSelected(selected);
    }

}
