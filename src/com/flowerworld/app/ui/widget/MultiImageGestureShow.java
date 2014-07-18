package com.flowerworld.app.ui.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;
import com.flowerworld.app.R;
import com.flowerworld.app.listener.MulitPointTouchListener;
import com.flowerworld.app.tool.helper.ShowBigImageHelper;
import com.flowerworld.app.tool.util.LOG;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import java.util.ArrayList;

public class MultiImageGestureShow extends RelativeLayout {
    public enum DisplayType {
        SAVE(0), PREVIEW(1);

        private int value;

        private DisplayType(int v) {
            this.value = v;
        }

    }

    private DisplayType mDisplayType = DisplayType.PREVIEW;

    //	protected int mCurrentPicIndex = 0;
    protected ArrayList<String> mSmallPicPaths = new ArrayList<String>();
    protected ArrayList<String> mBigPicPaths = new ArrayList<String>();

    protected ViewPager mViewPager = null;
    protected Button mButtonImageLeftBottom = null;
    protected StrokeText mTextAmount = null;

    protected ImagePagerAdapter mAdapter = null;
    protected ImageLoader imageLoader = ImageLoader.getInstance();
    protected DisplayImageOptions options = null;

    public MultiImageGestureShow(Context context) {
        super(context);
        initView();
    }

    public MultiImageGestureShow(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public MultiImageGestureShow(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView();
    }

    protected void initView() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        RelativeLayout widget = (RelativeLayout) inflater.inflate(R.layout.widget_multi_image_gesture_show, this);

        options = new DisplayImageOptions.Builder().showImageForEmptyUri(R.drawable.image_empty)
                .showImageOnFail(R.drawable.image_loadfailed).resetViewBeforeLoading().cacheOnDisc()
                .imageScaleType(ImageScaleType.EXACTLY).bitmapConfig(Bitmap.Config.ARGB_4444)
                .displayer(new FadeInBitmapDisplayer(300)).build();

        mViewPager = (ViewPager) widget.findViewById(R.id.view_pager);
        mButtonImageLeftBottom = (Button) widget.findViewById(R.id.widget_m_i_g_s_image);
        mTextAmount = (StrokeText) widget.findViewById(R.id.widget_m_i_g_s_amount);

        mAdapter = new ImagePagerAdapter();
        mViewPager.setAdapter(mAdapter);
        mViewPager.setOnPageChangeListener(new ImagePageChangedListener());
        mViewPager.setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);
        mButtonImageLeftBottom.setBackgroundResource(R.drawable.show_big_image);
        mButtonImageLeftBottom.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (DisplayType.PREVIEW == mDisplayType) {
                    ShowBigImageHelper.getInstance().showBigImage((Activity) getContext(),
                            null == mBigPicPaths ? mSmallPicPaths : mBigPicPaths);
                } else if (DisplayType.SAVE == mDisplayType) {

                }
            }
        });
        mTextAmount.setTextStrokeColor(Color.BLACK);
        mTextAmount.setText(mViewPager.getCurrentItem() + "/" + mSmallPicPaths.size());
    }

    public void setImageShowPaths(ArrayList<String> paths) {
        this.mSmallPicPaths = paths;
        mAdapter.notifyDataSetChanged();
        setAmount();
    }

    public void setImageBigPaths(ArrayList<String> paths) {
        this.mBigPicPaths = paths;
    }

    private void setAmount() {
        int current = mViewPager.getCurrentItem() + 1;
        if (current > mSmallPicPaths.size()) {
            current = mSmallPicPaths.size();
        }
        mTextAmount.setText(current + "/" + mSmallPicPaths.size());
    }

    private class ImagePagerAdapter extends PagerAdapter {

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager) container).removeView((View) object);
        }

        @Override
        public void finishUpdate(View container) {
        }

        @Override
        public int getCount() {
            return mSmallPicPaths.size();
        }

        @Override
        public Object instantiateItem(ViewGroup view, int position) {
            ImageView image = new ImageView(getContext());
//			image.setAdjustViewBounds(true);
//			image.setScaleType(ScaleType.CENTER_CROP);
            image.setScaleType(ScaleType.FIT_CENTER);
            image.setLayoutParams(new ViewPager.LayoutParams());

            if (mDisplayType == DisplayType.SAVE) {
                //对图片进行手势操作
                image.setScaleType(ScaleType.MATRIX);
                final MulitPointTouchListener touchListener = new MulitPointTouchListener();
                touchListener.setCenterParameters((Activity) getContext(), image);
                image.setOnTouchListener(touchListener);
//				touchListener.center();
//				image.postDelayed(new Runnable()
//				{
//
//					@Override
//					public void run()
//					{
//						LOG.w("", "======touchListener.center");
//						touchListener.center();
//					}
//				}, 400);

                imageLoader.displayImage(mSmallPicPaths.get(position), image, options, new ImageLoadingListener() {

                    @Override
                    public void onLoadingStarted(String imageUri, View view) {
                    }

                    @Override
                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                    }

                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        LOG.w("", "======touchListener.center");
                        touchListener.center();
                    }

                    @Override
                    public void onLoadingCancelled(String imageUri, View view) {
                    }
                });
            } else {
                imageLoader.displayImage(mSmallPicPaths.get(position), image, options);
            }

            ((ViewPager) view).addView(image, 0);

            return image;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view.equals(object);
        }

        @Override
        public void restoreState(Parcelable state, ClassLoader loader) {
        }

        @Override
        public Parcelable saveState() {
            return null;
        }

        @Override
        public void startUpdate(View container) {
        }
    }

    private class ImagePageChangedListener implements OnPageChangeListener {

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageSelected(int position) {
            setAmount();
        }

    }

    public void setDisplayType(DisplayType dt) {
        this.mDisplayType = dt;

        if (DisplayType.PREVIEW == mDisplayType) {
            mButtonImageLeftBottom.setBackgroundResource(R.drawable.show_big_image);

        } else if (DisplayType.SAVE == mDisplayType) {
            mButtonImageLeftBottom.setBackgroundResource(R.drawable.save_file);
            options = new DisplayImageOptions.Builder().showImageForEmptyUri(R.drawable.image_empty)
                    .showImageOnFail(R.drawable.image_loadfailed).resetViewBeforeLoading().cacheOnDisc()
                    .imageScaleType(ImageScaleType.EXACTLY).bitmapConfig(Bitmap.Config.ARGB_8888)
                    .displayer(new FadeInBitmapDisplayer(300)).build();
        }

    }

}
