package com.flowerworld.app.ui.widget;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.flowerworld.app.tool.util.GsonJsonUtil;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class ProductPpAttrLinear extends LinearLayout {
    private TextView mTextLeft = null;
    private TextView mTextRight = null;

    private static final String TEXT_COLOR = "#ff999999";
    private static final int TEXT_SIZE = 11;

    public ProductPpAttrLinear(Context context) {
        super(context);
        initView();
    }

    private void initView() {
        setOrientation(HORIZONTAL);

        mTextLeft = new TextView(getContext());
        mTextLeft.setTextColor(Color.parseColor(TEXT_COLOR));
        mTextLeft.setTextSize(TypedValue.COMPLEX_UNIT_SP, TEXT_SIZE);

        mTextRight = new TextView(getContext());
        mTextRight.setTextColor(Color.parseColor(TEXT_COLOR));
        mTextRight.setTextSize(TypedValue.COMPLEX_UNIT_SP, TEXT_SIZE);

        LayoutParams lp = new LayoutParams(0, LayoutParams.WRAP_CONTENT);
        lp.weight = 1.0f;

        addView(mTextLeft, lp);
        addView(mTextRight, lp);
    }

    public void setTextLeft(String text) {
        mTextLeft.setText(text);
    }

    public void setTextRight(String text) {
        mTextRight.setText(text);
    }

    public static void setPpAttr(Context context, LinearLayout parent, JsonArray arr, String KEY_PP_ATTR_NAME,
            String KEY_PP_ATTR_V1, String KEY_PP_ATTR_V2, String KEY_PP_ATTR_DW) {
        ProductPpAttrLinear pal = null;
        JsonObject obj = null;
        String text = null;
        String value1 = null;
        String value2 = null;
        int a = 0, p = 0;
        for (; a < arr.size(); a = a + 2, p++) {
            pal = (ProductPpAttrLinear) parent.getChildAt(p);
            if (null == pal) {
                pal = new ProductPpAttrLinear(context);
            }
            pal.setVisibility(View.VISIBLE);

            obj = arr.get(a).getAsJsonObject();
            text = GsonJsonUtil.optString(obj.get(KEY_PP_ATTR_NAME), "") + ":";
            value1 = GsonJsonUtil.optString(obj.get(KEY_PP_ATTR_V1), "").replace("&nbsp;", "");
            value2 = GsonJsonUtil.optString(obj.get(KEY_PP_ATTR_V2), "").replace("&nbsp;", "");
            if (TextUtils.isEmpty(value1) || TextUtils.isEmpty(value2)) {
                if (TextUtils.isEmpty(value1) && TextUtils.isEmpty(value2)) {
                    //do nothing
                } else if (!TextUtils.isEmpty(value1)) {
                    text += value1 + GsonJsonUtil.optString(obj.get(KEY_PP_ATTR_DW), "");
                } else if (!TextUtils.isEmpty(value2)) {
                    text += value2 + GsonJsonUtil.optString(obj.get(KEY_PP_ATTR_DW), "");
                }
            } else {
                text += value1 + "-" + value2 + GsonJsonUtil.optString(obj.get(KEY_PP_ATTR_DW), "");
            }
            pal.setTextLeft(text);

            if (a + 1 >= arr.size()) {
                break;
            }
            obj = arr.get(a + 1).getAsJsonObject();
            text = GsonJsonUtil.optString(obj.get(KEY_PP_ATTR_NAME), "") + ":";
            value1 = GsonJsonUtil.optString(obj.get(KEY_PP_ATTR_V1), "").replace("&nbsp;", "");
            value2 = GsonJsonUtil.optString(obj.get(KEY_PP_ATTR_V2), "").replace("&nbsp;", "");
            if (TextUtils.isEmpty(value1) || TextUtils.isEmpty(value2)) {
                if (TextUtils.isEmpty(value1) && TextUtils.isEmpty(value2)) {
                    //do nothing
                } else if (!TextUtils.isEmpty(value1)) {
                    text += value1 + GsonJsonUtil.optString(obj.get(KEY_PP_ATTR_DW), "");
                } else if (!TextUtils.isEmpty(value2)) {
                    text += value2 + GsonJsonUtil.optString(obj.get(KEY_PP_ATTR_DW), "");
                }
            } else {
                text += value1 + "-" + value2 + GsonJsonUtil.optString(obj.get(KEY_PP_ATTR_DW), "");
            }
            pal.setTextRight(text);

            if (null == pal.getParent()) {
                parent.addView(pal, LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            }
        }

        for (; p < parent.getChildCount(); p++) {
            pal = (ProductPpAttrLinear) parent.getChildAt(p);
            if (null != pal) {
                pal.setVisibility(View.GONE);
            }
        }
    }
}
