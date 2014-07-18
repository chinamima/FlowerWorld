package com.flowerworld.app.ui.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint.Style;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.widget.TextView;

import java.lang.reflect.Field;

public class StrokeText extends TextView {
    private boolean m_bDrawSideLine = true; // 默认不采用描边
    private TextPaint m_TextPaint = null;
    private int mStrokeColor = Color.WHITE;

    public StrokeText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public StrokeText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public StrokeText(Context context) {
        super(context);
    }

    private void initPaint() {
        m_TextPaint = this.getPaint();
    }

    public void setTextStrokeColor(int color) {
        this.mStrokeColor = color;
    }

    /*
     * (non-Javadoc)
     *
     * @see android.widget.TextView#onDraw(android.graphics.Canvas)
     */
    @Override
    protected void onDraw(Canvas canvas) {
        if (m_bDrawSideLine) {
            initPaint();
            // 描外层
            //super.setTextColor(Color.BLUE); // 不能直接这么设，如此会导致递归
            setTextColorUseReflection(mStrokeColor);
            m_TextPaint.setStrokeWidth(5); // 描边宽度
            m_TextPaint.setStyle(Style.FILL_AND_STROKE); //描边种类
            m_TextPaint.setFakeBoldText(true); // 外层text采用粗体
//			m_TextPaint.setShadowLayer(1, 0, 0, 0); //字体的阴影效果，可以忽略
            super.onDraw(canvas);

            // 描内层，恢复原先的画笔
//			canvas.save();
//			canvas.translate(0, -1);
            //super.setTextColor(Color.BLUE); // 不能直接这么设，如此会导致递归 ;
            setTextColorUseReflection(getTextColors().getColorForState(getDrawableState(), Color.WHITE));
            m_TextPaint.setStrokeWidth(0);
            m_TextPaint.setStyle(Style.FILL_AND_STROKE);
            m_TextPaint.setFakeBoldText(false);
//			m_TextPaint.setShadowLayer(0, 0, 0, 0);
        }
        super.onDraw(canvas);
//		canvas.restore();
    }

    private void setTextColorUseReflection(int color) {
        Field textColorField;
        try {
            textColorField = TextView.class.getDeclaredField("mCurTextColor");
            textColorField.setAccessible(true);
            textColorField.set(this, color);
            textColorField.setAccessible(false);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        m_TextPaint.setColor(color);
    }

}
