package com.flowerworld.app.listener;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;
import com.flowerworld.app.dao.bean.NotEmptyReferenceValue;

public class NotEmptyTextWatcher implements TextWatcher {
    private EditText mEdit = null;
    private NotEmptyReferenceValue mRef = null;

    public NotEmptyTextWatcher(EditText edit) {
        this.mEdit = edit;
        this.mRef = new NotEmptyReferenceValue();
    }

    public NotEmptyTextWatcher(EditText edit, NotEmptyReferenceValue ref) {
        this.mEdit = edit;
        this.mRef = ref;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
//		Log.w("", "======mEdit: "+mEdit);
//		Log.i("", "======s: "+s);
        mRef.text = s.toString();
        if (TextUtils.isEmpty(s.toString())) {
            this.mEdit.setError("不能为空");
        } else {
            mRef.verify = true;
            return;
        }
        mRef.verify = false;
    }


}
