package com.flowerworld.app.ui.widget;

import android.app.Activity;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.flowerworld.app.R;
import com.flowerworld.app.tool.util.BitmapUtil;
import com.flowerworld.app.tool.util.BitmapUtil.ScalingLogic;

public class PhotoSelect extends LinearLayout implements View.OnClickListener {

    private static final String TAG = PhotoSelect.class.getSimpleName();

    private static PhotoSelect currentSelectInstance = null;

    private ImageButton imgPhoto = null;
    private EditText edtTitle = null;
    private TextView txtDelete = null;

    public static final int REQUEST_CODE_PHOTO_PICKER = 0X12341;

    private ISelectionChanged selectionChangedListener = null;

    public interface ISelectionChanged {
        void onPhotoSelected(PhotoSelect self, String path);

        void onPhotoDeleted(PhotoSelect self);

        void onTextChanged(PhotoSelect self, String text);
    }

    public PhotoSelect(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initialize();
    }

    public PhotoSelect(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    public PhotoSelect(Context context) {
        super(context);
        initialize();
    }

    private void initialize() {
        initView();
        initEvent();
    }

    private void initView() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.widget_photo_select, this, true);

        imgPhoto = (ImageButton) v.findViewById(R.id.widget_photo_select_image);
        edtTitle = (EditText) v.findViewById(R.id.widget_photo_select_edit_title);
        txtDelete = (TextView) v.findViewById(R.id.widget_photo_select_text_delete);
    }

    private void initEvent() {
        imgPhoto.setOnClickListener(this);
        txtDelete.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.widget_photo_select_image:
            currentSelectInstance = this;
            openPhotoPicker();
            break;

        case R.id.widget_photo_select_text_delete:
            imgPhoto.setImageDrawable(null);

            if (null == selectionChangedListener) {
                return;
            }
            selectionChangedListener.onPhotoDeleted(this);
            break;

        default:
            break;
        }
    }

    private void openPhotoPicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//		intent.setType("image/*");
//		intent.putExtra("crop", true);
//		intent.putExtra("return-data", true);
        ((Activity) getContext()).startActivityForResult(intent, REQUEST_CODE_PHOTO_PICKER);
    }

    public static PhotoSelect getCurrentSelectInstance() {
        return currentSelectInstance;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (Activity.RESULT_OK == resultCode) {
            if (REQUEST_CODE_PHOTO_PICKER == requestCode) {
                currentSelectInstance = null;
                Log.d(TAG, "===============REQUEST_CODE_PHOTO_PICKER=data: " + data);
//				Bitmap bm = getBitmap(data);
                Uri uri = getBitmapUri(data);

                if (null == uri) {
                    return;
                }
                imgPhoto.setImageURI(uri);

                if (null == selectionChangedListener) {
                    return;
                }
                selectionChangedListener.onPhotoSelected(this, getRealPathFromURI(uri));
            }
        }
    }

    private Bitmap getBitmap(Intent data) {
        boolean isNull = false;
        try {
            Bundle extras = data.getExtras();
            Uri uri = null;
            Bitmap photoCaptured = null;

            Log.d(TAG, "==========开始保存照片,开始获取照片 ");
            if (extras != null) {
                Log.d(TAG, "==========extras: " + extras);
                /* 生成照片对象 */
                photoCaptured = (Bitmap) extras.get("data");
                if (photoCaptured == null) {
                    isNull = true;
                } else {
                    photoCaptured = BitmapUtil.operateBitmap(photoCaptured, 2000, 2000, ScalingLogic.FIT);
                }
            } else {
                isNull = true;
            }

            if (isNull) {
                uri = data.getData();
                Log.d(TAG, "==========uri: " + uri);
                /* 生成照片对象 */
                photoCaptured = BitmapUtil.operateBitmap(getContext(), uri, 2000, 2000, ScalingLogic.FIT);
            }

            return photoCaptured;

//			ByteArrayOutputStream baos = new ByteArrayOutputStream();
//			photoCaptured.compress(Bitmap.CompressFormat.JPEG, 70, baos); // 压缩文件
//
//			return baos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private Uri getBitmapUri(Intent data) {
        try {
            Uri uri = data.getData();
            Log.d(TAG, "==========uri: " + uri);

            return uri;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getRealPathFromURI(Uri uri) {
        String[] proj = { MediaStore.Images.Media.DATA };
        CursorLoader loader = new CursorLoader(getContext(), uri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    public ISelectionChanged getSelectionChangedListener() {
        return selectionChangedListener;
    }

    public void setSelectionChangedListener(ISelectionChanged l) {
        this.selectionChangedListener = l;
    }

}
