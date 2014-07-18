package com.flowerworld.app.tool.util;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.*;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory.Options;
import android.net.Uri;
import android.util.Log;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class BitmapUtil {

    private static String TAG = BitmapUtil.class.getSimpleName();

    /**
     * 把图片源改变成规定大小<br/>
     * 改变大小后的图片宽度和高度都将小于maxWidth和maxHeight
     *
     * @param bitmap    要改变大小的图片源
     * @param maxWidth  改变大小的最大宽度值
     * @param maxHeight 改变大小的最大高度值
     * @return
     */
    public static Bitmap getBitmapBySize(Bitmap bitmap, int maxWidth, int maxHeight) {
        int[] size = getMaxSize(bitmap, maxWidth, maxHeight);
        int width = size[0];
        int height = size[1];

        //等比例缩放
        Bitmap retBitmap = Bitmap.createScaledBitmap(bitmap, width, height, true);
        return retBitmap;
    }

    /**
     * 把图片源改变成规定大小<br/>
     * 改变大小后的图片宽度和高度都将小于maxWidth和maxHeight
     */
    public static Bitmap operateBitmap(Bitmap unscaledBitmap, int maxWidth, int maxHeight, ScalingLogic scalingLogic) {
//		int[] size = getMaxSize(unscaledBitmap, maxWidth, maxHeight);
//		int dstWidth = size[0];
//		int dstHeight = size[1];

        int dstWidth = maxWidth;
        int dstHeight = maxHeight;

        Bitmap scaledBitmap = createScaledBitmap(unscaledBitmap, dstWidth, dstHeight, scalingLogic);
        return scaledBitmap;
    }

    /**
     * 把图片源改变成规定大小<br/>
     * 改变大小后的图片宽度和高度都将小于maxWidth和maxHeight
     */
    public static Bitmap operateBitmap(Context context, Uri uri, int maxWidth, int maxHeight, ScalingLogic scalingLogic) {
//		int[] size = getMaxSize(unscaledBitmap, maxWidth, maxHeight);
        int dstWidth = maxWidth;//size[0];
        int dstHeight = maxHeight;//size[1];

        Bitmap unscaledBitmap = decodeStream(context, uri, dstWidth, dstHeight, scalingLogic);
        Bitmap scaledBitmap = createScaledBitmap(unscaledBitmap, dstWidth, dstHeight, scalingLogic);
        return scaledBitmap;
    }

    public static enum ScalingLogic {
        CROP, FIT
    }

    protected static int[] getMaxSize(Bitmap bitmap, int maxWidth, int maxHeight) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        //如果图片源本身已经符合条件,则返回图片源
        if (width < maxWidth && height < maxHeight) {
            return new int[] { width, height };
        }

        //获取图片源应缩小的倍数(beishu),因为width大于maxWidth,所以beiWidth大于1
        double beiWidth = (double) width / (double) maxWidth;
        double beiHeight = (double) height / (double) maxHeight;
        double beishu = 0;

        if (beiWidth > beiHeight) {
            beishu = beiWidth;
        } else {
            beishu = beiHeight;
        }

        //计算得到缩小后的宽高
        width /= beishu;
        height /= beishu;
        Log.d(TAG, "======width: " + width + " ==height: " + height);

        return new int[] { width, height };
    }

    public static Bitmap createScaledBitmap(Bitmap unscaledBitmap, int dstWidth, int dstHeight, ScalingLogic scalingLogic) {
        Rect srcRect = calculateSrcRect(unscaledBitmap.getWidth(), unscaledBitmap.getHeight(), dstWidth, dstHeight, scalingLogic);
//		Log.d(TAG, "==========srcRect: "+srcRect);
        Rect dstRect = calculateDstRect(unscaledBitmap.getWidth(), unscaledBitmap.getHeight(), dstWidth, dstHeight, scalingLogic);
//		Log.d(TAG, "==========dstRect: "+dstRect);
        Bitmap scaledBitmap = Bitmap.createBitmap(dstRect.width(), dstRect.height(), Config.ARGB_4444);
        Canvas canvas = new Canvas(scaledBitmap);
        canvas.drawBitmap(unscaledBitmap, srcRect, dstRect, new Paint(Paint.FILTER_BITMAP_FLAG));
//		Log.d(TAG, "==========scaledBitmap: "+scaledBitmap);
        return scaledBitmap;
    }

    public static Rect calculateSrcRect(int srcWidth, int srcHeight, int dstWidth, int dstHeight, ScalingLogic scalingLogic) {
        if (scalingLogic == ScalingLogic.CROP) {
            final float srcAspect = (float) srcWidth / (float) srcHeight;
            final float dstAspect = (float) dstWidth / (float) dstHeight;
            if (srcAspect > dstAspect) {
                final int srcRectWidth = (int) (srcHeight * dstAspect);
                final int srcRectLeft = (srcWidth - srcRectWidth) / 2;
                return new Rect(srcRectLeft, 0, srcRectLeft + srcRectWidth, srcHeight);
            } else {
                final int srcRectHeight = (int) (srcWidth / dstAspect);
                final int scrRectTop = (int) (srcHeight - srcRectHeight) / 2;
                return new Rect(0, scrRectTop, srcWidth, scrRectTop + srcRectHeight);
            }
        } else {
            return new Rect(0, 0, srcWidth, srcHeight);
        }
    }

    public static Rect calculateDstRect(int srcWidth, int srcHeight, int dstWidth, int dstHeight, ScalingLogic scalingLogic) {
        if (scalingLogic == ScalingLogic.FIT) {
            final float srcAspect = (float) srcWidth / (float) srcHeight;
            final float dstAspect = (float) dstWidth / (float) dstHeight;
            if (srcAspect > dstAspect) {
                return new Rect(0, 0, dstWidth, (int) (dstWidth / srcAspect));
            } else {
                return new Rect(0, 0, (int) (dstHeight * srcAspect), dstHeight);
            }
        } else {
            return new Rect(0, 0, dstWidth, dstHeight);
        }
    }

    public static Bitmap decodeStream(Context context, Uri uri, int dstWidth, int dstHeight, ScalingLogic scalingLogic) {
        InputStream is;
        Bitmap unscaledBitmap = null;
        ContentResolver cr = context.getContentResolver();
        try {
            //第一次是为得到原始图片的宽度、高度，进行比例缩
            is = cr.openInputStream(uri);
            Options options = new Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(is, null, options);

            //第二次是根据第一次得到的缩放比，生成缩略图
            is = cr.openInputStream(uri);
            options.inJustDecodeBounds = false;
            options.inPurgeable = true;//设置图片可以被回收
            options.inTempStorage = new byte[3 * 1024 * 1024];//临时存储
            options.inSampleSize = calculateSampleSize(options.outWidth, options.outHeight, dstWidth, dstHeight, scalingLogic);
            unscaledBitmap = BitmapFactory.decodeStream(is, null, options);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "==========unscaledBitmap: " + unscaledBitmap);
        return unscaledBitmap;
    }

    public static int calculateSampleSize(int srcWidth, int srcHeight, int dstWidth, int dstHeight, ScalingLogic scalingLogic) {
        if (scalingLogic == ScalingLogic.FIT) {
            final float srcAspect = (float) srcWidth / (float) srcHeight;
            final float dstAspect = (float) dstWidth / (float) dstHeight;
            if (srcAspect > dstAspect) {
                return srcWidth / dstWidth;
            } else {
                return srcHeight / dstHeight;
            }
        } else {
            final float srcAspect = (float) srcWidth / (float) srcHeight;
            final float dstAspect = (float) dstWidth / (float) dstHeight;
            if (srcAspect > dstAspect) {
                return srcHeight / dstHeight;
            } else {
                return srcWidth / dstWidth;
            }
        }
    }



    /////////////////////////////////////////////////////////////////////////////////////////
    //下面是抽样缩放,上面是FIT,CROP缩放
    /////////////////////////////////////////////////////////////////////////////////////////

    /**
     * 根据maxWidth,maxHeight进行抽样缩放
     */
    public static Bitmap getSampleBitmap(Context context, Uri uri, int maxWidth, int maxHeight) {
        ContentResolver cr = context.getContentResolver();
        InputStream is;
        Bitmap sampleBitmap = null;
        try {
            //第一次是为得到原始图片的宽度、高度，进行比例缩
            is = cr.openInputStream(uri);
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(is, null, o);

            //第二次是根据第一次得到的缩放比，生成缩略图
            is = cr.openInputStream(uri);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = computeSampleSize(o, -1, maxWidth * maxHeight);
            options.inJustDecodeBounds = false;
            options.inPurgeable = true;//设置图片可以被回收
            options.inTempStorage = new byte[4 * 1024 * 1024];//临时存储
            sampleBitmap = BitmapFactory.decodeStream(is, null, options);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return sampleBitmap;
    }

    public static int computeSampleSize(BitmapFactory.Options options,
            int minSideLength, int maxNumOfPixels) {
        int initialSize = computeInitialSampleSize(options, minSideLength, maxNumOfPixels);
        int roundedSize;
        if (initialSize <= 8) {
            roundedSize = 1;
            while (roundedSize < initialSize) {
                roundedSize <<= 1;
            }
        } else {
            roundedSize = (initialSize + 7) / 8 * 8;
        }
        return roundedSize;
    }

    private static int computeInitialSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
        double w = options.outWidth;
        double h = options.outHeight;
        int lowerBound = (maxNumOfPixels == -1) ? 1 :
                (int) Math.ceil(Math.sqrt(w * h / maxNumOfPixels));
        int upperBound = (minSideLength == -1) ? 128 :
                (int) Math.min(Math.floor(w / minSideLength),
                        Math.floor(h / minSideLength));
        if (upperBound < lowerBound) {
            return lowerBound;
        }
        if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
            return 1;
        } else if (minSideLength == -1) {
            return lowerBound;
        } else {
            return upperBound;
        }
    }



}
