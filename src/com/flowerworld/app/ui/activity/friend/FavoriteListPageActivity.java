package com.flowerworld.app.ui.activity.friend;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;
import com.flowerworld.app.R;
import com.flowerworld.app.dao.base.DaoSession;
import com.flowerworld.app.dao.bean.Favorites;
import com.flowerworld.app.dao.bean.Friend;
import com.flowerworld.app.dao.bean.FriendGroup;
import com.flowerworld.app.dao.bean.GlobalVariableBean;
import com.flowerworld.app.dao.beandao.FavoritesDao;
import com.flowerworld.app.dao.beandao.FriendDao;
import com.flowerworld.app.tool.util.MemberLevelUtility;
import com.flowerworld.app.tool.util.UnitUtil;
import com.flowerworld.app.ui.base.BaseActivity;
import de.greenrobot.dao.query.QueryBuilder;
import de.greenrobot.dao.query.WhereCondition;

import java.util.LinkedList;
import java.util.List;

public class FavoriteListPageActivity extends BaseActivity implements OnClickListener, OnItemClickListener {
    public static final int REQUEST_CODE_HERE = 0X8762;
    private static int mButtonId = 0x398572;
    private DaoSession mDaoSession;

    public static final void goHere(Activity activity) {
        Intent intent = new Intent(activity, FavoriteListPageActivity.class);
        activity.startActivityForResult(intent, REQUEST_CODE_HERE);
    }

    private EditText mEditSearch = null;
    private ListView mListFavorites = null;
    private CustomListAdapter mAdapter = null;
    private TextView mTextEmptySearch = null;

    private int tabButtonCurrentClkId = View.NO_ID;
    private int tabButtonLastClkId = View.NO_ID;
    private String mGroupId = "-1";

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

            mGroupId = (String) v.getTag();
            String keys = mEditSearch.getText().toString();
            searchFavoriteData(keys, mGroupId);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.enterprise_collect_list);
        initialize();
    }

    private void initialize() {
        initView();
        initEvent();
    }

    private void initView() {
        initBanner(R.id.banner_layout, R.drawable.banner, R.string.favorite_title, 0, R.string.go_back,
                R.drawable.button_corner_rectangle_selector, 0, 0,
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                }, null);

        mEditSearch = (EditText) findViewById(R.id.friend_list_edit_search_word);
        mTextEmptySearch = (TextView) findViewById(R.id.friend_list_text_fail);
        mListFavorites = (ListView) findViewById(R.id.friend_list_listview);
        mListFavorites.setVisibility(View.VISIBLE);

        mDaoSession = GlobalVariableBean.getDaoSession(this, GlobalVariableBean.userInfo.memberId);
        addButton(mGroupId, mDaoSession.getFriendGroupDao().loadAll());
        String keys = mEditSearch.getText().toString();
        searchFavoriteData(keys, mGroupId);
    }

    private void initEvent() {
        findViewById(R.id.friend_list_button_search).setOnClickListener(this);
        mListFavorites.setOnItemClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.friend_list_button_search:
            String keys = mEditSearch.getText().toString();
            searchFavoriteData(keys, mGroupId);
            break;

        default:
            break;
        }

    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Friend f = (Friend) mAdapter.getItem(position);
        AddFriendDetailPageActivity.goHere(this, f.getID(), f.getCompanyLevel(), f.getCompany(), f.getUserName(),
                f.getRealName(), f.getHeader());
    }

    private void searchFavoriteData(String key, String groupId) {
        FavoritesDao dao = mDaoSession.getFavoritesDao();
        QueryBuilder<Favorites> builder = dao.queryBuilder();
        List<WhereCondition> whereConditionList = new LinkedList<WhereCondition>();
        if (!TextUtils.isEmpty(key)) {
            whereConditionList.add(FavoritesDao.Properties.Company.eq("%" + key + "%"));
        }
        if (!"-1".equals(groupId)) {
            whereConditionList.add(FavoritesDao.Properties.GroupID.eq(groupId));
        }
        if (2 == whereConditionList.size()) {
            builder.where(whereConditionList.get(0), whereConditionList.get(1));
        } else if (1 == whereConditionList.size()) {
            builder.where(whereConditionList.get(0));
        }
        List<Favorites> favoritesList = builder.build().list();

        if (null == mAdapter) {
            mAdapter = new CustomListAdapter();
            mListFavorites.setAdapter(mAdapter);
        }
        mAdapter.setData(favoritesList);
        mAdapter.notifyDataSetChanged();

        if (0 == favoritesList.size()) {
            mTextEmptySearch.setVisibility(View.VISIBLE);
        } else {
            mTextEmptySearch.setVisibility(View.GONE);
        }
    }

    private void addButton(String select, List<FriendGroup> data) {
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, UnitUtil.transformDipToPx(30));
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
        Button btn = new Button(this);
        btn.setId(mButtonId++);
        btn.setTextSize(14);
        btn.setTextColor(this.getResources().getColorStateList(R.color.search_result_type_button_selector));
        btn.setBackgroundResource(R.drawable.search_result_type_button_selector);
        btn.setOnClickListener(groupListener);
        btn.setGravity(Gravity.CENTER);
        btn.setPadding(UnitUtil.transformDipToPx(2), 0, UnitUtil.transformDipToPx(2), 0);

        return btn;
    }

    private class CustomListAdapter extends BaseAdapter {

        private List<Favorites> favoritesList = new LinkedList<Favorites>();

        public void setData(List<Favorites> list) {
            if (null == list) {
                favoritesList = new LinkedList<Favorites>();
            } else {
                favoritesList.clear();
                favoritesList.addAll(list);
            }
        }

        @Override
        public int getCount() {
            return favoritesList.size();
        }

        @Override
        public Object getItem(int position) {
            return favoritesList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            CustomHolder holder = null;
            if (null == convertView) {
                holder = new CustomHolder();
                convertView = getLayoutInflater().inflate(R.layout.enterprise_collect_list_item, null);
                holder.companyName = (TextView) convertView.findViewById(R.id.enterprise_list_item_company);
                holder.level = (ImageView) convertView.findViewById(R.id.enterprise_list_item_member);
                holder.major = (TextView) convertView.findViewById(R.id.enterprise_list_item_keyword);
                holder.cityName = (TextView) convertView.findViewById(R.id.enterprise_list_text_city_name);

                convertView.setTag(holder);
            } else {
                holder = (CustomHolder) convertView.getTag();
            }

            Favorites favorites = (Favorites) getItem(position);
            holder.companyName.setText(favorites.getCompany());
            holder.level.setImageResource(MemberLevelUtility.switchMemberNum(favorites.getCompanyLevel()));
            holder.major.setText(favorites.getDes());
            holder.cityName.setText(favorites.getCityName());

            return convertView;
        }
    }

    private class CustomHolder {
        TextView companyName;
        ImageView level;
        TextView major;
        TextView cityName;
    }

}
