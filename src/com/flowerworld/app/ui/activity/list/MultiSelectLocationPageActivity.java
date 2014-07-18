package com.flowerworld.app.ui.activity.list;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.widget.TextView;
import com.flowerworld.app.R;
import com.flowerworld.app.tool.helper.GoProvinceCitySelectPage;
import com.flowerworld.app.tool.util.GsonJsonUtil;
import com.flowerworld.app.ui.activity.list.base.MultiSelectListPageActivity;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class MultiSelectLocationPageActivity extends MultiSelectListPageActivity {
    public static final String RESULT_INTENT_PROVINCE_NAME_ARRAY = "RESULT_INTENT_PROVINCE_NAME_ARRAY";
    public static final String RESULT_INTENT_PROVINCE_ID_ARRAY = "RESULT_INTENT_PROVINCE_ID_ARRAY";
    public static final String RESULT_INTENT_CITY_NAME_ARRAY = "RESULT_INTENT_CITY_NAME_ARRAY";
    public static final String RESULT_INTENT_CITY_ID_ARRAY = "RESULT_INTENT_CITY_ID_ARRAY";

    private int count = 1;

    private JsonArray provinceNameArr = new JsonArray();
    private JsonArray provinceIdArr = new JsonArray();
    private JsonArray cityNameArr = new JsonArray();
    private JsonArray cityIdArr = new JsonArray();

    public static void goHere(Activity activity, int requestCode) {
        String dataStr = GoProvinceCitySelectPage.readProvinceStr(activity);
        String itemName = "name";
        String itemId = "id";
        int titleId = R.string.province_select_title;
        boolean buttonShow = true;
        String tipsStr = "注意：省份可全选，可单选，可多选。";

        Intent intent = new Intent(activity, MultiSelectLocationPageActivity.class);
        intent.putExtra(MultiSelectListPageActivity.KEY_DATA, dataStr);
        intent.putExtra(MultiSelectListPageActivity.KEY_ITEM_NAME, itemName);
        intent.putExtra(MultiSelectListPageActivity.KEY_ITEM_ID, itemId);
        intent.putExtra(MultiSelectListPageActivity.KEY_TITLE, titleId);
        intent.putExtra(MultiSelectListPageActivity.KEY_BUTTON_SHOW, buttonShow);
        intent.putExtra(MultiSelectListPageActivity.KEY_TIPS, tipsStr);
        activity.startActivityForResult(intent, requestCode);
    }

    @Override
    protected void dealConfirm(MultiSelectAdapter adapter) {
        if (1 == count) {
            provinceNameArr = adapter.getSelectedNames();
            provinceIdArr = adapter.getSelectedIds();

            if (1 < provinceIdArr.size()) {
                Intent intent = new Intent();
                intent.putExtra(RESULT_INTENT_PROVINCE_NAME_ARRAY, provinceNameArr.toString());
                intent.putExtra(RESULT_INTENT_PROVINCE_ID_ARRAY, provinceIdArr.toString());
//				intent.putExtra(RESULT_INTENT_CITY_NAME_ARRAY, cityNameArr.toString());
//				intent.putExtra(RESULT_INTENT_CITY_ID_ARRAY, cityIdArr.toString());
                setResult(RESULT_OK, intent);
                finish();
                return;
            }

            String id = provinceIdArr.get(0).getAsString();
            JsonObject obj = GsonJsonUtil.mJsonParser.parse(GoProvinceCitySelectPage.readCityStr(this)).getAsJsonObject();
            adapter.setData(obj.get(id).getAsJsonArray(), itemName, itemId);
            adapter.notifyDataSetChanged();
            getBanner().getTitleView().setText(R.string.city_select_title);
            ((TextView) findViewById(R.id.multi_select_text)).setText("注意：省份可全选，可单选，可多选。");
            count++;

        } else if (2 == count) {
            cityNameArr = adapter.getSelectedNames();
            cityIdArr = adapter.getSelectedIds();

            Intent intent = new Intent();
            intent.putExtra(RESULT_INTENT_PROVINCE_NAME_ARRAY, provinceNameArr.toString());
            intent.putExtra(RESULT_INTENT_PROVINCE_ID_ARRAY, provinceIdArr.toString());
            intent.putExtra(RESULT_INTENT_CITY_NAME_ARRAY, cityNameArr.toString());
            intent.putExtra(RESULT_INTENT_CITY_ID_ARRAY, cityIdArr.toString());
            setResult(RESULT_OK, intent);
            finish();

        } else {
            Log.e(TAG, "no this count: " + count);
        }

    }
}
