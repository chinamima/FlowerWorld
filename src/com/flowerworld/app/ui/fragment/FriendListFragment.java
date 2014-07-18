package com.flowerworld.app.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout.LayoutParams;
import com.flowerworld.app.R;
import com.flowerworld.app.dao.base.DaoSession;
import com.flowerworld.app.dao.bean.Friend;
import com.flowerworld.app.dao.bean.FriendGroup;
import com.flowerworld.app.dao.bean.GlobalVariableBean;
import com.flowerworld.app.dao.beandao.FriendDao;
import com.flowerworld.app.tool.util.LOG;
import com.flowerworld.app.tool.util.ToastUtil;
import com.flowerworld.app.tool.util.UnitUtil;
import com.flowerworld.app.ui.activity.friend.*;
import com.flowerworld.app.ui.adapter.FriendAdapter;
import com.flowerworld.app.ui.base.BaseFragment;
import com.google.gson.JsonArray;
import com.google.gson.JsonPrimitive;
import de.greenrobot.dao.query.QueryBuilder;

import java.util.List;

public class FriendListFragment extends BaseFragment {
    private static FriendListFragment fragment = new FriendListFragment();

    public static FriendListFragment newInstance() {
        return fragment;
//		return new FriendListFragment();
    }

    public static final String INTENT_KEY_SEARCH_WORD = "search_word";

    private ListView list = null;
    private FriendAdapter adapter = null;

    private EditText edtSearchWord = null;

    private static int BUTTON_ID = 0x1247433;

    private int tabButtonCurrentClkId = View.NO_ID;
    private int tabButtonLastClkId = View.NO_ID;

    private OnClickListener groupListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            if (tabButtonCurrentClkId == v.getId()) {
                return;
            }
            tabButtonLastClkId = tabButtonCurrentClkId;
            tabButtonCurrentClkId = v.getId();

            v.setSelected(true);
            if (View.NO_ID != tabButtonLastClkId) {
                findViewById(tabButtonLastClkId).setSelected(false);
            }

//			pageCount = 1;
//			m_pc2 = v.getTag().toString();
//			requstData(pageCount + "", m_pc2);
            groupChangedListener.onClick(v);
        }
    };

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.friend_list, container, false);
    }

    protected void initView() {
        initBanner(R.id.banner_layout, R.drawable.banner, R.string.friend_list_title, 0, R.string.friend_list_banner_left,
                R.drawable.button_corner_rectangle_selector, R.string.friend_list_banner_right,
                R.drawable.button_corner_rectangle_selector, new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        FavoriteListPageActivity.goHere(getActivity());
                    }
                }, new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), FriendGroupPageActivity.class);
                        getActivity().startActivity(intent);
                    }
                });

        //init view
        edtSearchWord = (EditText) findViewById(R.id.friend_list_edit_search_word);
        list = (ListView) findViewById(R.id.friend_list_listview);

        initEvent();
    }

    private void initEvent() {
        list.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LOG.d(TAG, "===========onItemClick=position: " + position);
                Friend f = (Friend) adapter.getItem(position);
                Intent intent = new Intent(getActivity(), FriendDetailPageActivity.class);
                intent.putExtra(FriendDetailPageActivity.INTENT_KEY_USERNAME, f.getUserName());
                intent.putExtra(FriendDetailPageActivity.INTENT_KEY_REALRNAME, f.getRealName());
                intent.putExtra(FriendDetailPageActivity.INTENT_KEY_HEADER, f.getHeader());
                intent.putExtra(FriendDetailPageActivity.INTENT_KEY_FRIEND_ID, f.getID());
                intent.putExtra(FriendDetailPageActivity.INTENT_KEY_MEMBER_LEVEL, f.getCompanyLevel());
                intent.putExtra(FriendDetailPageActivity.INTENT_KEY_COMPANY, f.getCompany());
                intent.putExtra(FriendDetailPageActivity.INTENT_KEY_GROUP_ID, f.getGroupID());
                getActivity().startActivity(intent);
            }
        });
        findViewById(R.id.friend_list_button_add).setOnClickListener(addFriendListener);
        findViewById(R.id.friend_list_button_send_msg_all).setOnClickListener(sendManyMessageListener);
        findViewById(R.id.friend_list_button_search).setOnClickListener(searchFriendListener);
    }

    @Override
    protected void initData() {
        getDataFromSql();
    }

    @Override
    protected void resumeData() {
//		getDataFromSql();
        String word = (String) getSavedData();
        showFriendListByCondition(word);
    }

    private void getDataFromSql() {
        if (null == GlobalVariableBean.userInfo) {
            ToastUtil.show(getActivity(), "请先登录");
            return;
        }
        DaoSession session = GlobalVariableBean.getDaoSession(getActivity(), GlobalVariableBean.userInfo.memberId);
        List<Friend> friends = session.getFriendDao().loadAll();
        List<FriendGroup> group = session.getFriendGroupDao().loadAll();
        showData(friends, group);
    }

    private void hasCount(int count) {
        if (count > 0) {
            findViewById(R.id.friend_list_text_fail).setVisibility(View.GONE);
            findViewById(R.id.friend_list_text_empty).setVisibility(View.GONE);
            findViewById(R.id.friend_list_listview).setVisibility(View.VISIBLE);
        } else if (count == 0) {
            findViewById(R.id.friend_list_text_fail).setVisibility(View.GONE);
            findViewById(R.id.friend_list_text_empty).setVisibility(View.VISIBLE);
            findViewById(R.id.friend_list_listview).setVisibility(View.GONE);
        } else {
            findViewById(R.id.friend_list_text_fail).setVisibility(View.VISIBLE);
            findViewById(R.id.friend_list_text_empty).setVisibility(View.GONE);
            findViewById(R.id.friend_list_listview).setVisibility(View.GONE);
        }
    }

    private void addButton(String select, List<FriendGroup> data) {
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, UnitUtil.transformDipToPx(30));
        lp.setMargins(UnitUtil.transformDipToPx(10), 0, UnitUtil.transformDipToPx(10), 0);
        lp.gravity = Gravity.CENTER_VERTICAL;
        LinearLayout parent = (LinearLayout) findViewById(R.id.friend_list_layout_button_type);
        parent.removeAllViews();

        FriendGroup groupObj = null;
        Button btn = null;
        String id = null;
        for (int i = 0; i < data.size(); i++) {
            groupObj = data.get(i);
            btn = newButton();
            btn.setText(groupObj.getName());
            id = groupObj.getID();
            btn.setTag(id);
            parent.addView(btn, lp);

            if (select.equals(id)) {
                btn.setSelected(true);
                tabButtonCurrentClkId = btn.getId();
            }
        }
    }

    private Button newButton() {
        Button btn = new Button(getActivity());
        btn.setId(BUTTON_ID++);
        btn.setTextSize(14);
        btn.setTextColor(this.getResources().getColorStateList(R.color.search_result_type_button_selector));
        btn.setBackgroundResource(R.drawable.search_result_type_button_selector);
        btn.setOnClickListener(groupListener);
        btn.setGravity(Gravity.CENTER);
        btn.setPadding(UnitUtil.transformDipToPx(2), 0, UnitUtil.transformDipToPx(2), 0);

        return btn;
    }

    private void showData(List<Friend> friends, List<FriendGroup> group) {
        showFriendList(friends);
        addButton("-1", group);

        hasCount(friends.size());
    }

    private void showFriendList(List<Friend> friends) {
        adapter = new FriendAdapter(getActivity());
        adapter.setData(friends);
        list.setAdapter(adapter);
    }

    private OnClickListener groupChangedListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            String groupId = (String) v.getTag();
            QueryBuilder<Friend> b = GlobalVariableBean.getDaoSession(getActivity(), GlobalVariableBean.userInfo.memberId)
                    .getFriendDao().queryBuilder();
            if (!"-1".equals(groupId)) {
                b.where(FriendDao.Properties.GroupID.eq(groupId));
            }
            adapter.setData(b.list());
            adapter.notifyDataSetChanged();
        }
    };

    private OnClickListener searchFriendListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            String word = edtSearchWord.getText().toString();
            saveData(word);

            showFriendListByCondition(word);
        }
    };

    private OnClickListener addFriendListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            AddFriendListPageActivity.goHere(getActivity());
        }
    };

    private OnClickListener sendManyMessageListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            List<Friend> friends = adapter.getData();
            JsonArray arr = new JsonArray();
            for (Friend f : friends) {
                arr.add(new JsonPrimitive(f.getUserName()));
            }

            SendManyMessagePageActivity.goHere(getActivity(), "群发", arr.toString());
        }
    };

    private void showFriendListByCondition(String word) {
        DaoSession session = GlobalVariableBean.getDaoSession(getActivity(), GlobalVariableBean.userInfo.memberId);
        List<Friend> friends = null;
        if (TextUtils.isEmpty(word)) {
            friends = session.getFriendDao().loadAll();
        } else {
            word = "%" + word + "%";
            friends = session.getFriendDao().queryBuilder()
                    .whereOr(FriendDao.Properties.RealName.like(word), FriendDao.Properties.UserName.like(word)).list();
        }
        adapter.setData(friends);
        adapter.notifyDataSetChanged();
    }

}
