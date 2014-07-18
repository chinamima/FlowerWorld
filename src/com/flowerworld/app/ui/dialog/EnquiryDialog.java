package com.flowerworld.app.ui.dialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;
import com.flowerworld.app.R;
import com.flowerworld.app.dao.bean.GlobalConstant;
import com.flowerworld.app.dao.bean.GlobalVariableBean;
import com.flowerworld.app.interf.IHttpProcess;
import com.flowerworld.app.tool.util.ToastUtil;
import com.flowerworld.app.ui.base.BaseDialog;

import java.util.Map;

public class EnquiryDialog extends BaseDialog implements View.OnClickListener {
    protected static final String PARAMS_PRODUCT_ID = "productpost";

    private String productId = null;

    public EnquiryDialog(Context context, int theme) {
        super(context, theme);
        initView();
    }

//	public EnquiryDialog(Context context, boolean cancelable, OnCancelListener cancelListener)
//	{
//		super(context, cancelable, cancelListener);
//		initView();
//	}

    public EnquiryDialog(Context context) {
        super(context);
        initView();
    }

    private void initView() {
        LayoutInflater inflate = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflate.inflate(R.layout.dialog_enquiry_detail, null);
        setContentView(v, new LayoutParams(LayoutParams.FILL_PARENT/* UnitUtil.transformDipToPx(200) */,
                LayoutParams.WRAP_CONTENT));

        initEvent();
    }

    private void initEvent() {
        findViewById(R.id.dialog_enquiry_detail_button_goback).setOnClickListener(this);
        findViewById(R.id.dialog_enquiry_detail_button_send).setOnClickListener(this);
        findViewById(R.id.dialog_enquiry_detail_button_cancle).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.dialog_enquiry_detail_button_goback:
            this.dismiss();
            break;

        case R.id.dialog_enquiry_detail_button_send:
            requestData();
            break;

        case R.id.dialog_enquiry_detail_button_cancle:
            this.dismiss();
            break;

        default:
            break;
        }

    }

    public void setProductId(String id) {
        this.productId = id;
    }

    private void requestData() {
        requestHttp(new IHttpProcess() {

            @Override
            public String processUrl(int sign) {
                return GlobalVariableBean.APIRoot + GlobalConstant.URL_PRODUCT_ENQUIRY;
            }

            @Override
            public boolean processResponseSucceed(String resultStr, int sign) throws Exception {
                EnquiryDialog.this.dismiss();
                ToastUtil.show(getContext(), "成功");
                return true;
            }

            @Override
            public boolean processResponseFailed(String resultStr, int sign) throws Exception {
                return false;
            }

            @Override
            public void processParams(Map<String, Object> params, int sign) {
                params.put(PARAMS_PRODUCT_ID, productId);
                params.put("sessionId", GlobalVariableBean.sessionId);
                params.put("content", ((TextView) findViewById(R.id.dialog_enquiry_detail_edit_enquiry)).getText().toString());
            }
        });
    }
}
