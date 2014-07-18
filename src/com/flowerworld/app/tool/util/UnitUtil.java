package com.flowerworld.app.tool.util;


import android.util.TypedValue;

public class UnitUtil {

    /**
     * dip to px
     */
    public static int transformDipToPx(int value) {
        return (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, ApplicationContextUtil
                .getApplicationContext().getResources().getDisplayMetrics()) + 0.5f);
    }

    /**
     * sp to px
     */
    public static int transformSpToPx(int value) {
        return (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, value, ApplicationContextUtil
                .getApplicationContext().getResources().getDisplayMetrics()) + 0.5f);
    }
}
