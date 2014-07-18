package com.flowerworld.app.ui.activity.list.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;
import com.flowerworld.app.R;
import com.flowerworld.app.tool.util.LOG;
import com.flowerworld.app.tool.util.ToastUtil;
import com.flowerworld.app.ui.base.BaseActivity;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

public class MultiSelectListPageActivity extends BaseActivity {

    public static final void goHere(Activity activity, int requestCode, String dataStr, String itemName, String itemId,
            int titleId, boolean buttonShow, String tipsStr) {
        Intent intent = new Intent(activity, MultiSelectListPageActivity.class);
        intent.putExtra(MultiSelectListPageActivity.KEY_DATA, dataStr);
        intent.putExtra(MultiSelectListPageActivity.KEY_ITEM_NAME, itemName);
        intent.putExtra(MultiSelectListPageActivity.KEY_ITEM_ID, itemId);
        intent.putExtra(MultiSelectListPageActivity.KEY_TITLE, titleId);
        intent.putExtra(MultiSelectListPageActivity.KEY_BUTTON_SHOW, buttonShow);
        intent.putExtra(MultiSelectListPageActivity.KEY_TIPS, tipsStr);
        activity.startActivityForResult(intent, requestCode);
    }

    public static final String KEY_DATA = "data";
    public static final String KEY_TITLE = "title";
    public static final String KEY_ITEM_NAME = "itemName";
    public static final String KEY_ITEM_ID = "itemId";
    public static final String KEY_RESULT_NAME_LIST = "resultNameList";
    public static final String KEY_RESULT_ID_LIST = "resultIdList";
    public static final String KEY_TIPS = "tips";
    public static final String KEY_BUTTON_SHOW = "button_show";

    protected MultiSelectAdapter adapter = null;
    protected String dataList = null;
    protected String itemName = null;
    protected String itemId = null;
    protected int title = View.NO_ID;
    protected String resultNameList = null;
    protected String resultIdList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.multi_select_page);

        initView();
    }

    private void initView() {
        title = getIntent().getIntExtra(KEY_TITLE, View.NO_ID);
        dataList = getIntent().getStringExtra(KEY_DATA);
        itemName = getIntent().getStringExtra(KEY_ITEM_NAME);
        itemId = getIntent().getStringExtra(KEY_ITEM_ID);

        initBanner(R.id.banner_layout, R.drawable.banner, /* R.string.signup_newenterprise_title */title, 0, R.string.go_back,
                R.drawable.button_corner_rectangle_selector, 0, 0, new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                }, null);
        initList();
        findViewById(R.id.multi_select_button_confirm).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                dealConfirm(adapter);
            }
        });
        initTextAndButton();
    }

    protected void dealConfirm(MultiSelectAdapter adapter) {
        JsonArray ids = adapter.getSelectedIds();
        if (null == ids || 1 > ids.size()) {
            ToastUtil.show(MultiSelectListPageActivity.this, "请选择至少一项");
            return;
        }
        resultNameList = adapter.getSelectedNames().toString();
        resultIdList = ids.toString();

        Intent intent = new Intent();
        intent.putExtra(KEY_RESULT_NAME_LIST, resultNameList);
        intent.putExtra(KEY_RESULT_ID_LIST, resultIdList);
        setResult(RESULT_OK, intent);
        finish();
    }

//	private String listdata = "[{\"name\":\"广东\",\"ID\":\"001\"},{\"name\":\"北京\",\"ID\":\"002\"}]";
//	private String listdata = "[{\"name\":\"广东\",\"ID\":\"001\"},{\"name\":\"广东\",\"ID\":\"001\"},{\"name\":\"广东\",\"ID\":\"001\"},{\"name\":\"广东\",\"ID\":\"001\"},{\"name\":\"广东\",\"ID\":\"001\"},{\"name\":\"广东\",\"ID\":\"001\"},{\"name\":\"广东\",\"ID\":\"001\"},{\"name\":\"广东\",\"ID\":\"001\"},{\"name\":\"广东\",\"ID\":\"001\"},{\"name\":\"广东\",\"ID\":\"001\"},{\"name\":\"广东\",\"ID\":\"001\"},{\"name\":\"广东\",\"ID\":\"001\"},{\"name\":\"广东\",\"ID\":\"001\"},{\"name\":\"广东\",\"ID\":\"001\"},{\"name\":\"广东\",\"ID\":\"001\"},{\"name\":\"北京\",\"ID\":\"002\"}]";

    private void initList() {
        JsonArray arr = new JsonParser().parse(dataList).getAsJsonArray();
//		Type listType = new TypeToken<List<Map<String, String>>>()
//		{}.getType();
//
//		List<Map<String, String>> listMap = new Gson().fromJson(listdata, listType);
//
        ListView list = (ListView) findViewById(R.id.multi_select_list);
//		SimpleAdapter adapter = new SimpleAdapter(this, listMap, R.layout.multi_select_list_item, new String[] { "name" },
//				new int[] { R.id.list_item_left });
        adapter = new MultiSelectAdapter();
        adapter.setData(arr, itemName, itemId);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LOG.w(TAG, "===========onItemClick=position: " + position);
            }
        });
    }

    private void initTextAndButton() {
        String text = getIntent().getStringExtra(KEY_TIPS);
        ((TextView) findViewById(R.id.multi_select_text)).setText(null == text ? "" : text);

        boolean show = getIntent().getBooleanExtra(KEY_BUTTON_SHOW, false);
        Button all = (Button) findViewById(R.id.multi_select_button_all);
        all.setVisibility(show ? View.VISIBLE : View.GONE);
        all.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                adapter.selectAll();
            }
        });
    }

    public class MultiSelectAdapter extends BaseAdapter {
        private JsonArray dataArr = null;
        private String dataName = null;
        private String dataId = null;
        private LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        private SparseArray<String> selectNameMap = null;
        private SparseArray<String> selectIdMap = null;

        public void setData(JsonArray data, String name, String id) {
            dataArr = data;
            dataName = name;
            dataId = id;
            selectNameMap = new SparseArray<String>();
            selectIdMap = new SparseArray<String>();
        }

        @Override
        public int getCount() {
            return dataArr.size();
        }

        @Override
        public Object getItem(int position) {
            return dataArr.get(position).getAsJsonObject();
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (null == convertView) {
                convertView = (RelativeLayout) inflater.inflate(R.layout.multi_select_list_item, null);
                ((ViewGroup) convertView).setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);
            }

            ((TextView) convertView.findViewById(R.id.list_item_left)).setText(((JsonObject) getItem(position)).get(dataName)
                    .getAsString());
            final CheckBox chk = (CheckBox) convertView.findViewById(R.id.list_item_right);
            chk.setClickable(true);
            convertView.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (chk.isChecked()) {
                        chk.setChecked(false);
                        selectNameMap.remove(position);
                        selectIdMap.remove(position);
                    } else {
                        chk.setChecked(true);
                        selectNameMap.put(position, ((JsonObject) getItem(position)).get(dataName).getAsString());
                        selectIdMap.put(position, ((JsonObject) getItem(position)).get(dataId).getAsString());
                    }
                }
            });
            chk.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (((CheckBox) v).isChecked()) {
                        selectNameMap.put(position, ((JsonObject) getItem(position)).get(dataName).getAsString());
                        selectIdMap.put(position, ((JsonObject) getItem(position)).get(dataId).getAsString());
                    } else {
                        selectNameMap.remove(position);
                        selectIdMap.remove(position);
                    }
                }
            });

            //control select
            int selected = selectNameMap.indexOfKey(position);
            if (-1 < selected) {
                chk.setChecked(true);
            } else {
                chk.setChecked(false);
            }

            return convertView;
        }

        public JsonArray getSelectedNames() {
            JsonArray arr = new JsonArray();
            for (int i = 0; i < selectNameMap.size(); i++) {
                arr.add(new JsonPrimitive(selectNameMap.get(selectNameMap.keyAt(i))));
            }
            return arr;
        }

        public JsonArray getSelectedIds() {
            JsonArray arr = new JsonArray();
            for (int i = 0; i < selectIdMap.size(); i++) {
                arr.add(new JsonPrimitive(selectIdMap.get(selectIdMap.keyAt(i))));
            }
            return arr;
        }

        public void selectAll() {
            selectIdMap.clear();
            selectNameMap.clear();
            for (int i = 0; i < getCount(); i++) {
                selectNameMap.put(i, ((JsonObject) getItem(i)).get(dataName).getAsString());
                selectIdMap.put(i, ((JsonObject) getItem(i)).get(dataId).getAsString());
            }
            notifyDataSetChanged();
        }
    }

}
