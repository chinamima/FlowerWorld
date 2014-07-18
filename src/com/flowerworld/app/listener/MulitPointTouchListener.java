package com.flowerworld.app.listener;

import android.app.Activity;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.DisplayMetrics;
import android.util.FloatMath;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;

/**
 * ��㴥��,���Ʋ���ͼƬ,������
 *
 * @author guo.jinjun
 */
public class MulitPointTouchListener implements OnTouchListener {

    private static final String TAG = MulitPointTouchListener.class.getSimpleName();
    // These matrices will be used to move and zoom image 
    Matrix matrix = new Matrix();
    Matrix savedMatrix = new Matrix();

    // We can be in one of these 3 states 
    static final int NONE = 0;
    static final int DRAG = 1;
    static final int ZOOM = 2;
    int mode = NONE;

    // Remember some things for zooming 
    PointF start = new PointF();
    PointF mid = new PointF();
    float oldDist = 1f;

    //center
    DisplayMetrics dm = null;
    ImageView imgView = null;
//    Bitmap bitmap = null;

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        ImageView view = (ImageView) v;

        // Handle touch events here...
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
        // ���㰴��
        case MotionEvent.ACTION_DOWN:
            matrix.set(view.getImageMatrix());
            savedMatrix.set(matrix);
            start.set(event.getX(), event.getY());
            mode = DRAG;
            break;

        // ���㰴��
        case MotionEvent.ACTION_POINTER_DOWN:
            oldDist = spacing(event);
            // �����������������10�����ж�Ϊ���ģʽ
            if (oldDist > 10f) {
                savedMatrix.set(matrix);
                midPoint(mid, event);
                mode = ZOOM;
            }
            break;

        case MotionEvent.ACTION_UP:
        case MotionEvent.ACTION_POINTER_UP:
            mode = NONE;
            break;

        case MotionEvent.ACTION_MOVE:
            if (mode == DRAG) {
                matrix.set(savedMatrix);
                matrix.postTranslate(event.getX() - start.x, event.getY() - start.y);
            } else if (mode == ZOOM) {
                float newDist = spacing(event);
                if (newDist > 10f) {
                    matrix.set(savedMatrix);
                    float scale = newDist / oldDist;
                    matrix.postScale(scale, scale, mid.x, mid.y);
                }
            }
            break;

        }

        view.setImageMatrix(matrix);
//		center();
        return true; // indicate event was handled
    }


    /**
     * ����ľ���
     */
    private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return FloatMath.sqrt(x * x + y * y);
    }

    /**
     * ������е�
     */
    private void midPoint(PointF point, MotionEvent event) {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        point.set(x / 2, y / 2);
    }

    public void setCenterParameters(Activity activity, ImageView imgView) {
        this.dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(this.dm);// ��ȡ�ֱ���

        this.imgView = imgView;

//    	BitmapDrawable bd = (BitmapDrawable) imgView.getDrawable();
//    	this.bitmap = bd.getBitmap();
    }

    public void center() {
        center(true, true);
    }

    /**
     * �����������
     */
    protected void center(boolean horizontal, boolean vertical) {

        Matrix m = new Matrix();
        m.set(matrix);
//        RectF rect = new RectF(0, 0, bitmap.getWidth(), bitmap.getHeight());
//        m.mapRect(rect);
        Rect rect = imgView.getDrawable().getBounds();

        float height = rect.height();    //��Ƭ�߶�
        float width = rect.width();        //��Ƭ����

        float deltaX = 0, deltaY = 0;

        if (vertical) {
            // ͼƬС����Ļ��С���������ʾ��������Ļ���Ϸ������������ƣ��·�������������
            int screenHeight = dm.heightPixels;
            if (height < screenHeight) {
                deltaY = (screenHeight - height) / 2;
            } else if (rect.top > 0) {
                deltaY = -rect.top;
            } else if (rect.bottom < screenHeight) {
                deltaY = imgView.getHeight() - rect.bottom;
            }
        }

        if (horizontal) {
            int screenWidth = dm.widthPixels;
            if (width < screenWidth) {
                deltaX = (screenWidth - width) / 2;
            } else if (rect.left > 0) {
                deltaX = -rect.left;
            } else if (rect.right < screenWidth) {
                deltaX = screenWidth - rect.right;
            } else if (rect.right > screenWidth) {

            }
        }
        matrix.postTranslate(deltaX, deltaY);
        RectF rectF = new RectF(0, 0, 480, 480);
        matrix.mapRect(rectF);
        imgView.setImageMatrix(matrix);
    }

} 

