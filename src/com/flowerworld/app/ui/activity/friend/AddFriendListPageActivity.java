package com.flowerworld.app.ui.activity.friend;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import com.flowerworld.app.R;
import com.flowerworld.app.dao.bean.Friend;
import com.flowerworld.app.dao.bean.GlobalConstant;
import com.flowerworld.app.dao.bean.GlobalVariableBean;
import com.flowerworld.app.interf.IHttpProcess;
import com.flowerworld.app.tool.http.HttpRequestFacade;
import com.flowerworld.app.tool.util.GsonJsonUtil;
import com.flowerworld.app.tool.util.UnitUtil;
import com.flowerworld.app.ui.adapter.AddFriendAdapter;
import com.flowerworld.app.ui.base.BaseActivity;
import com.flowerworld.app.ui.dialog.FriendAddDialog;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AddFriendListPageActivity extends BaseActivity implements OnClickListener, OnItemClickListener {
    public static final int REQUEST_CODE_HERE = 0X8765;

    public static final void goHere(Activity activity) {
        Intent intent = new Intent(activity, AddFriendListPageActivity.class);
        activity.startActivityForResult(intent, REQUEST_CODE_HERE);
    }

    private EditText mEditSearch = null;
    private PullToRefreshListView mListFriend = null;
    private AddFriendAdapter mAdapter = null;

    private int mPageCount = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friend_add_list);
        initialize();
    }

    private void initialize() {
        initView();
        initEvent();
        requestData("", "", "", "", mPageCount + "");
    }

    private void initView() {
        initBanner(R.id.banner_layout, R.drawable.banner, R.string.add_friend_title, 0, R.string.go_back,
                R.drawable.button_corner_rectangle_selector, R.string.select, R.drawable.button_corner_rectangle_selector,
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                }, new OnClickListener() {

                    @Override
                    public void onClick(View v) {

                    }
                });

        mEditSearch = (EditText) findViewById(R.id.friend_add_list_edit_search_word);
        mListFriend = (PullToRefreshListView) findViewById(R.id.friend_add_list_listview);
    }

    private void initEvent() {
        findViewById(R.id.friend_add_list_button_search).setOnClickListener(this);
        mListFriend.setOnRefreshListener(new OnRefreshListener<ListView>() {

            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                String keys = mEditSearch.getText().toString();
                requestData(keys, "", "", "", mPageCount + "");
            }
        });
        mListFriend.setOnItemClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.friend_add_list_button_search:
            String keys = mEditSearch.getText().toString();
            mPageCount = 1;
            requestData(keys, "", "", "", mPageCount + "");
            break;

        default:
            break;
        }

    }

    private void requestData(final String keys, final String provinceName, final String cityName, final String entTypeName,
            final String p) {
        requestHttp(new IHttpProcess() {

            @Override
            public String processUrl(int sign) {
                return GlobalVariableBean.APIRoot + GlobalConstant.URL_FRIEND_SEARCH;
            }

            @Override
            public boolean processResponseSucceed(String resultStr, int sign) throws Exception {
                JsonObject resultObj = HttpRequestFacade.paserResultObj(resultStr);
                dealData(resultObj);

                mPageCount++;
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
                params.put("keys", keys);
                params.put("provinceName", provinceName);
                params.put("cityName", cityName);
                params.put("entTypeName", entTypeName);
                params.put("p", p);
            }
        });
    }

    private void dealData(JsonObject resultObj) {
        if (null == resultObj) {
            return;
        }
        if (!resultObj.has("data")) {
            return;
        }

        JsonArray data = GsonJsonUtil.optJsonArray(resultObj.get("data"));
//		List<Friend> friends = GsonJsonUtil.mGson.fromJson(data, new TypeToken<List<Friend>>()
//		{}.getType());
        List<Friend> friends = new ArrayList<Friend>(data.size());
        JsonObject obj = null;
        for (int i = 0; i < data.size(); i++) {
            obj = data.get(i).getAsJsonObject();
            friends.add(new Friend(GsonJsonUtil.optString(obj.get("uid"), "-1"), i, GsonJsonUtil.optString(obj.get("username"),
                    ""), GsonJsonUtil.optString(obj.get("realname"), ""), GsonJsonUtil.optString(obj.get("groupID"), "-1"),
                    GsonJsonUtil.optString(obj.get("position"), ""), GsonJsonUtil.optString(obj.get("company"), ""), GsonJsonUtil
                    .optString(obj.get("memberLevel"), ""), GsonJsonUtil.optString(obj.get("logo"), "")));
        }
        if (null == mAdapter) {
            mAdapter = new AddFriendAdapter(this);
            mAdapter.setButtonListener(mAddFriendListener);
            mListFriend.setAdapter(mAdapter);
        }
        if (1 == mPageCount) {
            mAdapter.setData(friends);
        } else {
            mAdapter.appendData(friends);
        }
        mAdapter.notifyDataSetChanged();
    }

    private OnClickListener mAddFriendListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            final Friend f = (Friend) v.getTag();
            FriendAddDialog dialog = new FriendAddDialog(AddFriendListPageActivity.this, R.style.Theme_Dialog_NoTitle_NoActionBar);
            WindowManager.LayoutParams lp = dialog.getWindow().getAttributes(); // 获取对话框当前的参数值
            lp.width = UnitUtil.transformDipToPx(300);
            dialog.getWindow().setAttributes(lp);
            dialog.setFuid(f.getID());
            dialog.show();
        }
    };

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Friend f = (Friend) mAdapter.getItem(position);
        AddFriendDetailPageActivity.goHere(this, f.getID(), f.getCompanyLevel(), f.getCompany(), f.getUserName(),
                f.getRealName(), f.getHeader());
    }

}
