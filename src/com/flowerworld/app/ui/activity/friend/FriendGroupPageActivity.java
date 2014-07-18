package com.flowerworld.app.ui.activity.friend;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.flowerworld.app.R;
import com.flowerworld.app.dao.bean.Friend;
import com.flowerworld.app.dao.bean.FriendGroup;
import com.flowerworld.app.dao.bean.GlobalVariableBean;
import com.flowerworld.app.dao.beandao.FriendDao;
import com.flowerworld.app.tool.helper.FriendOperationHelper;
import com.flowerworld.app.ui.base.BaseActivity;
import com.flowerworld.app.ui.dialog.FriendOperationDialog;
import de.greenrobot.dao.query.QueryBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FriendGroupPageActivity extends BaseActivity implements OnClickListener {

    private static final int COLOR_HUI = Color.rgb(231, 231, 231);

    private String lastClickItemId = null;

    private LinearLayout layoutGroup = null;
    private EditText edtAddGroup = null;

    List<FriendGroup> groupList = null;
    Map<String, String> groupCountMap = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initialize();
    }

    private void initialize() {
        initData();
        initView();
        initEvent();
    }

    private void initView() {
        setContentView(R.layout.friend_group);

        initBanner(R.id.banner_layout, R.drawable.banner, R.string.friend_group_title, 0, R.string.go_back,
                R.drawable.button_corner_rectangle_selector, 0, 0, new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                }, null);

        edtAddGroup = (EditText) findViewById(R.id.friend_group_edit_add);

        layoutGroup = (LinearLayout) findViewById(R.id.friend_group_layout_group);
        addGroups();
//		((TextView) findViewById(R.id.friend_group_text_tip)).setText(Html.fromHtml(getString(R.string.friend_group_tip)));
        ((TextView) findViewById(R.id.friend_group_text_tip)).setText(Html.fromHtml("带<font color=\"red\">*</font>为默认分组，不可删除"));
    }

    private void initData() {
        groupList = GlobalVariableBean.getDaoSession(this, GlobalVariableBean.userInfo.memberId).getFriendGroupDao().loadAll();

        groupCountMap = new HashMap<String, String>();
        FriendDao fd = GlobalVariableBean.getDaoSession(this, GlobalVariableBean.userInfo.memberId).getFriendDao();

        FriendGroup fg = null;
        String id = null;
        String count = null;
        QueryBuilder<Friend> build = null;
        for (int i = 0; i < groupList.size(); i++) {
            build = fd.queryBuilder();
            fg = groupList.get(i);
            id = fg.getID();
            if ("-1".equals(id)) {
                count = build.count() + "";
            } else {
                count = build.where(FriendDao.Properties.GroupID.eq(fg.getID())).count() + "";
            }
            groupCountMap.put(fg.getID(), count);
        }
    }

    private void initEvent() {
        findViewById(R.id.friend_group_button_add).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.friend_group_button_add:
            FriendOperationHelper.addFriendGroup(this, edtAddGroup.getText().toString(), new Runnable() {

                @Override
                public void run() {
                    edtAddGroup.setText("");
                    initGroup();
                }
            });
            break;

        default:
            break;
        }
    }

    private void initGroup() {
        initData();
        layoutGroup.removeAllViews();
        addGroups();
    }

    private void addGroups() {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View line = null;
        RelativeLayout item = null;
        FriendGroup fg = null;
        for (int i = 0; i < groupList.size(); i++) {
            fg = groupList.get(i);
            item = newItem(inflater, fg.getName(), fg.getID(), groupCountMap.get(fg.getID()));

            StateListDrawable d = new StateListDrawable();
            d.addState(new int[] { android.R.attr.state_pressed }, new ColorDrawable(COLOR_HUI));
            d.addState(new int[] { }, new ColorDrawable(Color.TRANSPARENT));
            item.setBackgroundDrawable(d);

            layoutGroup.addView(item, LayoutParams.MATCH_PARENT,
                    (int) getResources().getDimension(R.dimen.friend_group_item_height));

            if ((i + 1) < groupList.size()) {
                line = new View(this);
                line.setBackgroundColor(Color.rgb(226, 226, 226));
                layoutGroup.addView(line, LayoutParams.MATCH_PARENT, 1);
            }
        }
    }

    private RelativeLayout newItem(LayoutInflater inflater, String name, String id, String count) {
        RelativeLayout item = (RelativeLayout) inflater.inflate(R.layout.friend_group_item, null);
        TextView textXing = (TextView) item.findViewById(R.id.friend_group_item_text_xing);
        TextView textName = (TextView) item.findViewById(R.id.friend_group_item_text_group_name);
        TextView textNum = (TextView) item.findViewById(R.id.friend_group_item_text_group_num);

        if ("-1".equals(id)) {
            textXing.setVisibility(View.VISIBLE);
        } else {
            textXing.setVisibility(View.INVISIBLE);
        }

        textName.setText(name);
        textNum.setText("[" + count + "]");

        item.setOnClickListener(itemLinstener);

        Map<String, String> m = new HashMap<String, String>();
        m.put("id", id);
        m.put("count", count);
        item.setTag(m);

        return item;
    }

    private OnClickListener itemLinstener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            Map<String, String> m = (Map<String, String>) v.getTag();
            FriendOperationDialog.goHere(FriendGroupPageActivity.this, m.get("id"), m.get("count"));

        }
    };

    protected void onActivityResult(int requestCode, int resultCode, android.content.Intent data) {
        if (RESULT_OK == resultCode) {
            if (FriendOperationDialog.REQUEST_CODE_THIS == requestCode) {
                initGroup();
            }
        }
    }

    ;
}
