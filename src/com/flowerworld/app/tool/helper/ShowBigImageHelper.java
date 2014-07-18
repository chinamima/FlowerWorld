package com.flowerworld.app.tool.helper;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.flowerworld.app.R;
import com.flowerworld.app.tool.util.UnitUtil;
import com.flowerworld.app.ui.widget.MultiImageGestureShow;
import com.flowerworld.app.ui.widget.MultiImageGestureShow.DisplayType;

import java.util.ArrayList;

public class ShowBigImageHelper {
    private static ShowBigImageHelper self = new ShowBigImageHelper();

    public static final ShowBigImageHelper getInstance() {
        return self;
    }

    public void showBigImage(Activity activity, ArrayList<String> paths) {
        BigImageLayout big = new BigImageLayout(activity);
        big.mImage.setImageShowPaths(paths);

//		//对图片进行手势操作
//		image.setScaleType(ScaleType.MATRIX);
//		MulitPointTouchListener touchListener = new MulitPointTouchListener();
//		touchListener.setCenterParameters(activity, image);
//		image.setOnTouchListener(touchListener);
//		touchListener.center();

        //初始化一个Dialog,把ImageView展示出来
        Dialog dialog = new Dialog(activity, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        dialog.setCancelable(true);
//		dialog.setTitle("查看照片");
        dialog.setContentView(big);

        dialog.show();

//		//居中显示
//		touchListener.center();
    }

    private class BigImageLayout extends RelativeLayout {
        public MultiImageGestureShow mImage = null;
        private LinearLayout mAlert = null;

        public BigImageLayout(Context context) {
            super(context);
            initView();
        }

        private void initView() {
            mImage = new MultiImageGestureShow(getContext());
            mImage.setDisplayType(DisplayType.SAVE);
            this.addView(mImage, LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);

            mAlert = new LinearLayout(getContext());
            mAlert.setOrientation(LinearLayout.HORIZONTAL);
            ImageView im = new ImageView(getContext());
            im.setImageResource(R.drawable.ring_right);
            im.setScaleType(ScaleType.FIT_CENTER);
            TextView text = new TextView(getContext());
            text.setText("保存到手机成功！");
            text.setTextColor(Color.WHITE);
            text.setTextSize(14);

            LinearLayout.LayoutParams lp_im = new LinearLayout.LayoutParams(UnitUtil.transformDipToPx(10),
                    UnitUtil.transformDipToPx(10));
            mAlert.addView(im, lp_im);
            mAlert.addView(text);
            LayoutParams lp = new LayoutParams(LayoutParams.FILL_PARENT, UnitUtil.transformDipToPx(70));
            lp.addRule(CENTER_IN_PARENT);
            this.addView(mAlert, lp);

            mAlert.setVisibility(View.GONE);
            this.setBackgroundColor(Color.parseColor("#7d0f0f0f"));
        }
    }
}
