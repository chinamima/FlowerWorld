package com.flowerworld.app.ui.activity.list.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;
import com.flowerworld.app.R;
import com.flowerworld.app.tool.util.GsonJsonUtil;
import com.flowerworld.app.tool.util.LOG;
import com.flowerworld.app.ui.base.BaseActivity;
import com.flowerworld.app.ui.widget.Banner;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;

public class SingleSelectListPageActivity extends BaseActivity {
    public static final void goHere(Activity activity, int requestCode, ArrayList<String> dataList,
            ArrayList<String> itemNameList, ArrayList<String> itemIdList, ArrayList<Integer> titleIdList) {
        Intent intent = new Intent(activity, SingleSelectListPageActivity.class);
        intent.putStringArrayListExtra(SingleSelectListPageActivity.KEY_DATA_LIST, dataList);
        intent.putIntegerArrayListExtra(SingleSelectListPageActivity.KEY_TITLE_LIST, titleIdList);
        intent.putStringArrayListExtra(SingleSelectListPageActivity.KEY_ITEM_NAME, itemNameList);
        intent.putStringArrayListExtra(SingleSelectListPageActivity.KEY_ITEM_ID, itemIdList);
        activity.startActivityForResult(intent, requestCode);
    }

    public static final String KEY_DATA_LIST = "dataList";
    public static final String KEY_TITLE_LIST = "titleList";
    public static final String KEY_RESULT_NAME_LIST = "resultNameList";
    public static final String KEY_RESULT_ID_LIST = "resultIdList";
    public static final String KEY_ITEM_NAME = "itemName";
    public static final String KEY_ITEM_ID = "itemId";

    protected SingleSelectAdapter adapter = null;
    protected List<String> dataList = null;
    protected List<Integer> titleList = null;
    protected List<String> itemName = null;
    protected List<String> itemId = null;
    protected ArrayList<String> resultNameList = new ArrayList<String>();
    protected ArrayList<String> resultIdList = new ArrayList<String>();
    protected int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location_select_page);

        initView();
    }

    protected void initView() {
        dataList = getIntent().getStringArrayListExtra(KEY_DATA_LIST);
        titleList = getIntent().getIntegerArrayListExtra(KEY_TITLE_LIST);
        itemName = getIntent().getStringArrayListExtra(KEY_ITEM_NAME);
        itemId = getIntent().getStringArrayListExtra(KEY_ITEM_ID);

        initBanner(R.id.banner_layout, R.drawable.banner, titleList.get(count), 0, R.string.go_back,
                R.drawable.button_corner_rectangle_selector, 0, 0, new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                }, null);
        initList();
        count++;
    }

//	protected String listdata = "[{\"name\":\"广东\",\"ID\":\"001\"},{\"name\":\"北京\",\"ID\":\"002\"}]";
//
//	protected String listdata1 = "[{\"name\":\"广东\",\"ID\":\"001\"},{\"name\":\"广东\",\"ID\":\"001\"},{\"name\":\"广东\",\"ID\":\"001\"},{\"name\":\"广东\",\"ID\":\"001\"},{\"name\":\"广东\",\"ID\":\"001\"},{\"name\":\"广东\",\"ID\":\"001\"},{\"name\":\"广东\",\"ID\":\"001\"},{\"name\":\"广东\",\"ID\":\"001\"},{\"name\":\"广东\",\"ID\":\"001\"},{\"name\":\"广东\",\"ID\":\"001\"},{\"name\":\"广东\",\"ID\":\"001\"},{\"name\":\"广东\",\"ID\":\"001\"},{\"name\":\"广东\",\"ID\":\"001\"},{\"name\":\"广东\",\"ID\":\"001\"},{\"name\":\"广东\",\"ID\":\"001\"},{\"name\":\"北京\",\"ID\":\"002\"}]";

    protected void initList() {
        JsonArray arr = new JsonParser().parse(dataList.get(count)).getAsJsonArray();
        ListView list = (ListView) findViewById(R.id.location_select_single_list);
        adapter = new SingleSelectAdapter();
        adapter.setData(arr, itemName.get(count), itemId.get(count));
        list.setAdapter(adapter);
        list.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LOG.w(TAG, "===========onItemClick=position: " + position);
            }
        });
    }

    public class SingleSelectAdapter extends BaseAdapter {
        protected JsonArray dataArr = null;
        protected LayoutInflater inflater = null;

        protected String dataName = null;
        protected String dataId = null;

        public void setData(JsonArray data, String name, String id) {
            dataArr = data;
            inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            dataName = name;
            dataId = id;
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
                convertView = (RelativeLayout) inflater.inflate(R.layout.location_select_list_item, null);
            }

            ((TextView) convertView.findViewById(R.id.list_item_left)).setText(((JsonObject) getItem(position)).get(dataName)
                    .getAsString());
            OnClickListener listener = new OnClickListener() {

                @Override
                public void onClick(View v) {
                    dealClick(adapter, position, dataId, dataName);
                }
            };
            convertView.setOnClickListener(listener);

            return convertView;
        }
    }

    protected void dealClick(SingleSelectAdapter adapter, int position, String dataId, String dataName) {
        String id = ((JsonObject) adapter.getItem(position)).get(dataId).getAsString();
        resultNameList.add(((JsonObject) adapter.getItem(position)).get(dataName).getAsString());
        resultIdList.add(id);

        if (count >= dataList.size()) {
            Intent intent = new Intent();
            intent.putStringArrayListExtra(KEY_RESULT_NAME_LIST, resultNameList);
            intent.putStringArrayListExtra(KEY_RESULT_ID_LIST, resultIdList);
            setResult(RESULT_OK, intent);
            finish();
            return;
        }

        JsonObject obj = GsonJsonUtil.parse(dataList.get(count)).getAsJsonObject();
        JsonArray arr = obj.get(id).getAsJsonArray();
        adapter.setData(arr, itemName.get(count), itemId.get(count));
        adapter.notifyDataSetChanged();
        ((Banner) ((ViewGroup) findViewById(R.id.banner_layout)).getChildAt(0)).getTitleView().setText(titleList.get(count));
        count++;
    }

}
