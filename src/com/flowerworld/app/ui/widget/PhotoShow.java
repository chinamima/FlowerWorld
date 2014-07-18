package com.flowerworld.app.ui.widget;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.flowerworld.app.R;
import com.flowerworld.app.dao.bean.GlobalVariableBean;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;
import java.io.FileInputStream;

/**
 * 图片展示控件，支持删除和描述
 *
 * @author Guo.Jinjun
 */
public class PhotoShow extends LinearLayout implements View.OnClickListener {

    private static final String TAG = PhotoShow.class.getSimpleName();

    private ImageView imgPhoto = null;
    private TextView txtTitle = null;
    private TextView txtDelete = null;

    private String photoSrc = null;

    public static void addPhotoShow(Context context, String title, String url, IPhotoShowDelete listener, LinearLayout parent) {
        PhotoShow ps = new PhotoShow(context);
        ps.setPhotoUrl(url);
        ps.setPhotoTitle(title);
        ps.setPhotoShowDeleteLinstener(listener);

        LayoutParams lp = null;
        if (LinearLayout.HORIZONTAL == parent.getOrientation()) {
            lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        } else {
            lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        }
        parent.addView(ps, lp);
    }

    public interface IPhotoShowDelete {
        void deleteSelf(PhotoShow self);
    }

    private IPhotoShowDelete photoShowDeleteLinstener = null;
    private IPhotoShowDelete photoShowDeleteLinstenerDefault = new IPhotoShowDelete() {

        @Override
        public void deleteSelf(PhotoShow self) {
        }
    };

    public PhotoShow(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initialize();
    }

    public PhotoShow(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    public PhotoShow(Context context) {
        super(context);
        initialize();
    }

    private void initialize() {
        initView();
        initEvent();
    }

    private void initView() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.widget_photo_show, this, true);

        imgPhoto = (ImageView) v.findViewById(R.id.widget_photo_show_image);
        txtTitle = (TextView) v.findViewById(R.id.widget_photo_show_text_title);
        txtDelete = (TextView) v.findViewById(R.id.widget_photo_show_text_delete);
    }

    private void initEvent() {
        txtDelete.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.widget_photo_show_text_delete:
            getPhotoShowDeleteLinstener().deleteSelf(this);
            break;

        default:
            break;
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////
    ////以下是api方法////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////

    public IPhotoShowDelete getPhotoShowDeleteLinstener() {
        if (null == photoShowDeleteLinstener) {
            return photoShowDeleteLinstenerDefault;
        }
        return photoShowDeleteLinstener;
    }

    public void setPhotoShowDeleteLinstener(IPhotoShowDelete photoShowDeleteLinstener) {
        this.photoShowDeleteLinstener = photoShowDeleteLinstener;
    }

    public void setPhotoUrl(String url) {
        if (TextUtils.isEmpty(url)) {
            return;
        }

        ImageLoader.getInstance().displayImage(url, imgPhoto, GlobalVariableBean.options);
        photoSrc = url;
    }

    public void setPhotoPath(String path) {
        if (TextUtils.isEmpty(path)) {
            return;
        }

        try {
            File f = new File(path);
            FileInputStream fis = new FileInputStream(f);
            BitmapDrawable bd = new BitmapDrawable(getContext().getResources(), fis);
            imgPhoto.setImageDrawable(bd);

            photoSrc = path;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setPhotoTitle(String title) {
        if (TextUtils.isEmpty(title)) {
            txtTitle.setText("");
            return;
        }

        txtTitle.setText(title);
    }

    public String getPhotoTitle() {
        return txtTitle.getText().toString();
    }

    public String getPhotoSrc() {
        return photoSrc;
    }
}
