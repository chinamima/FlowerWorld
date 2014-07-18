package com.flowerworld.app.ui.activity.friend;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;
import com.flowerworld.app.R;
import com.flowerworld.app.dao.bean.GlobalConstant;
import com.flowerworld.app.dao.bean.GlobalVariableBean;
import com.flowerworld.app.interf.IHttpProcess;
import com.flowerworld.app.tool.helper.FriendOperationHelper;
import com.flowerworld.app.tool.http.HttpRequestFacade;
import com.flowerworld.app.tool.util.GsonJsonUtil;
import com.flowerworld.app.tool.util.MemberLevelUtility;
import com.flowerworld.app.tool.util.ToastUtil;
import com.flowerworld.app.ui.base.BaseActivity;
import com.flowerworld.app.ui.dialog.FriendAddDialog;
import com.google.gson.JsonObject;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;
import java.util.Map;

public class AddFriendDetailPageActivity extends BaseActivity implements OnClickListener {
    public static final String INTENT_KEY_FRIEND_ID = "id";
    public static final String INTENT_KEY_MEMBER_LEVEL = "memberLevel";
    public static final String INTENT_KEY_COMPANY = "company";
    public static final String INTENT_KEY_USERNAME = "username";
    public static final String INTENT_KEY_REALNAME = "realname";
    public static final String INTENT_KEY_HEADER = "header";

    public static final void goHere(Activity activity, String fid, String memberLevel, String company, String userName,
            String realName, String header) {
        Intent intent = new Intent(activity, AddFriendDetailPageActivity.class);
        intent.putExtra(INTENT_KEY_FRIEND_ID, fid);
        intent.putExtra(INTENT_KEY_MEMBER_LEVEL, memberLevel);
        intent.putExtra(INTENT_KEY_COMPANY, company);
        intent.putExtra(INTENT_KEY_USERNAME, userName);
        intent.putExtra(INTENT_KEY_HEADER, realName);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friend_add_detail);
        initialize();
    }

    private void initialize() {
        initView();
        initEvent();
        requestData();
    }

    private void initView() {
        initBanner(R.id.banner_layout, R.drawable.banner, R.string.add_friend_title, 0, R.string.go_back,
                R.drawable.button_corner_rectangle_selector, 0, 0, new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                }, null);
    }

    private void initEvent() {
        findViewById(R.id.friend_add_detail_button_add_relation).setOnClickListener(this);
        findViewById(R.id.friend_add_detail_button_collect_enterprise).setOnClickListener(this);
        findViewById(R.id.friend_add_detail_button_send_message).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.friend_add_detail_button_add_relation:
            showAddFriendMessageDialog();
            break;

        case R.id.friend_add_detail_button_send_message:
            goSendMessagePage();
            break;

        case R.id.friend_add_detail_button_collect_enterprise:
            showFriendGroupSelection();
            break;

        default:
            break;
        }

    }

    private void goSendMessagePage() {
        Intent i = new Intent(this, SendOneMessagePageActivity.class);
        i.putExtra(SendOneMessagePageActivity.INTENT_KEY_FRIEND_ID, getIntent().getStringExtra(INTENT_KEY_FRIEND_ID));
        startActivity(i);
    }

    private void requestData() {
        requestHttp(new IHttpProcess() {

            @Override
            public String processUrl(int sign) {
                return GlobalVariableBean.APIRoot + GlobalConstant.URL_FRIEND_DETAIL;
            }

            @Override
            public boolean processResponseSucceed(String resultStr, int sign) throws Exception {
                JsonObject resultObj = HttpRequestFacade.paserResultObj(resultStr);
                dealData(resultObj);

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
                params.put(INTENT_KEY_FRIEND_ID, getIntent().getStringExtra(INTENT_KEY_FRIEND_ID));
            }
        });
    }

    private void dealData(JsonObject resultObj) {
        ((TextView) findViewById(R.id.friend_add_detail_text_name)).setText(getIntent().getStringExtra(INTENT_KEY_REALNAME));
        ((TextView) findViewById(R.id.friend_add_detail_text_job)).setText(GsonJsonUtil.optString(resultObj.get("position"), ""));
        ((TextView) findViewById(R.id.friend_add_detail_text_location)).setText(GsonJsonUtil.optString(
                resultObj.get("resideprovince"), "")
                + " " + GsonJsonUtil.optString(resultObj.get("residecity"), ""));
        ((TextView) findViewById(R.id.friend_add_detail_text_company)).setText(getIntent().getStringExtra(INTENT_KEY_COMPANY));
        ((TextView) findViewById(R.id.friend_add_detail_text_mobile)).setText("手机："
                + GsonJsonUtil.optString(resultObj.get("mobile"), ""));
        ((TextView) findViewById(R.id.friend_add_detail_text_telephone)).setText("固话："
                + GsonJsonUtil.optString(resultObj.get("telephone"), ""));
        ((TextView) findViewById(R.id.friend_add_detail_text_fax)).setText("传真："
                + GsonJsonUtil.optString(resultObj.get("eFax"), ""));

        ((ImageView) findViewById(R.id.friend_add_detail_image_member_type)).setImageResource(MemberLevelUtility
                .switchMemberNum(getIntent().getStringExtra(INTENT_KEY_MEMBER_LEVEL)));
        ImageLoader.getInstance().displayImage(getIntent().getStringExtra(INTENT_KEY_HEADER),
                (ImageView) findViewById(R.id.friend_add_detail_image_header), GlobalVariableBean.options);
    }

    private void showAddFriendMessageDialog() {
        FriendAddDialog dialog = new FriendAddDialog(this, R.style.Theme_Dialog_NoTitle_NoActionBar);
        dialog.setFuid(getIntent().getStringExtra(INTENT_KEY_FRIEND_ID));
        dialog.show();
    }

    private void showFriendGroupSelection() {
        ListView selectList = new ListView(this);
        selectList.setBackgroundResource(R.drawable.dialog_corner_rectangle);
        final List<Map<String, String>> listData = FriendOperationHelper.getFriendGroupWithoutSrcGid(null);
        selectList.setAdapter(new SimpleAdapter(this, listData, R.layout.dialog_item_transfer_friend_group,
                new String[] { "name" }, new int[] { R.id.dialog_item_transfer_friend_group_text }));

        final Dialog dialog = new Dialog(this, R.style.Theme_Dialog_NoTitle_NoActionBar);
        selectList.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dialog.dismiss();
                Map<String, String> m = listData.get(position);
                final String targetGid = m.get("id");

                requestHttp(new IHttpProcess() {

                    @Override
                    public String processUrl(int sign) {
                        return GlobalVariableBean.APIRoot + GlobalConstant.URL_ADD_FAVOR;
                    }

                    @Override
                    public boolean processResponseSucceed(String resultStr, int sign) throws Exception {
                        JsonObject resultObj = HttpRequestFacade.paserResultObj(resultStr);
                        ToastUtil.show(AddFriendDetailPageActivity.this, "收藏成功");
                        return true;
                    }

                    @Override
                    public boolean processResponseFailed(String resultStr, int sign) throws Exception {
                        // TODO Auto-generated method stub
                        return false;
                    }

                    @Override
                    public void processParams(Map<String, Object> params, int sign) {
                        params.put(GlobalConstant.sessionId, GlobalVariableBean.sessionId);
                        params.put("memberId", GlobalVariableBean.userInfo.memberId);
                        params.put("favuid", getIntent().getStringExtra(INTENT_KEY_FRIEND_ID));
                        params.put("gid", targetGid);
                    }
                });
            }
        });

        dialog.setContentView(selectList);
        dialog.show();

    }
}
