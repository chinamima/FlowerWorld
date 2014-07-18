package com.flowerworld.app.ui.dialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import com.flowerworld.app.R;
import com.flowerworld.app.dao.bean.GlobalConstant;
import com.flowerworld.app.dao.bean.GlobalVariableBean;
import com.flowerworld.app.interf.IHttpProcess;
import com.flowerworld.app.tool.util.ToastUtil;
import com.flowerworld.app.ui.base.BaseDialog;

import java.util.Map;

public class FriendAddDialog extends BaseDialog implements View.OnClickListener {

    private String fuid = null;

    public FriendAddDialog(Context context, int theme) {
        super(context, theme);
        initView();
    }

    public FriendAddDialog(Context context) {
        super(context);
        initView();
    }

    private void initView() {
        LayoutInflater inflate = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflate.inflate(R.layout.dialog_friend_add, null);
        setContentView(v, new LayoutParams(LayoutParams.FILL_PARENT/* UnitUtil.transformDipToPx(200) */,
                LayoutParams.WRAP_CONTENT));

        initEvent();
    }

    private void initEvent() {
        findViewById(R.id.dialog_friend_add_button_goback).setOnClickListener(this);
        findViewById(R.id.dialog_friend_add_button_send).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.dialog_friend_add_button_goback:
            this.dismiss();
            break;

        case R.id.dialog_friend_add_button_send:
            sendAddFriendMessage();
            break;

        default:
            break;
        }

    }

    private void sendAddFriendMessage() {
        requestHttp(new IHttpProcess() {

            @Override
            public String processUrl(int sign) {
                return GlobalVariableBean.APIRoot + GlobalConstant.URL_FRIEND_ADD;
            }

            @Override
            public boolean processResponseSucceed(String resultStr, int sign) throws Exception {
                FriendAddDialog.this.dismiss();
                ToastUtil.show(getContext(), "添加成功");
                return true;
            }

            @Override
            public boolean processResponseFailed(String resultStr, int sign) throws Exception {
                return false;
            }

            @Override
            public void processParams(Map<String, Object> params, int sign) {
                params.put(GlobalConstant.sessionId, GlobalVariableBean.sessionId);
                params.put("memberId", GlobalVariableBean.userInfo.memberId);
                params.put("note", ((EditText) findViewById(R.id.dialog_friend_add_edit_message)).getText().toString());
                params.put("fuid", fuid);
                params.put("gid", "0");
            }
        });
    }

    public void setFuid(String fuid) {
        this.fuid = fuid;
    }
}
