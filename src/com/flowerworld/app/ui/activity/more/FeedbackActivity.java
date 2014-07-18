package com.flowerworld.app.ui.activity.more;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import com.flowerworld.app.R;
import com.flowerworld.app.interf.IHttpProcess;
import com.flowerworld.app.tool.util.ToastUtil;
import com.flowerworld.app.ui.base.BaseActivity;

import java.util.Map;

/**
 * Created by guojj4 on 14/07/13.
 */
public class FeedbackActivity extends BaseActivity implements View.OnClickListener {

    private EditText mEditMessage = null;
    private EditText mEditContact = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feedback_page);
        initialize();
    }

    private void initialize() {
        initBanner(R.id.banner_layout, R.drawable.banner, R.string.feedback_title, 0, R.string.go_back,
                R.drawable.button_corner_rectangle_selector, 0, 0, new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                }, null);

        mEditMessage = (EditText) findViewById(R.id.feedback_edit_message);
        mEditContact = (EditText) findViewById(R.id.feedback_edit_contact);
        findViewById(R.id.feedback_button_send).setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        if (isValid()) {
            sendFeedbackMessage();
        }
    }

    private boolean isValid() {
        if (TextUtils.isEmpty(mEditMessage.getText().toString())) {
            return false;
        }
        if (TextUtils.isEmpty(mEditContact.getText().toString())) {
            return false;
        }
        return true;
    }

    private void sendFeedbackMessage() {
        ToastUtil.show(this, "发送成功");
        finish();
    }
}
