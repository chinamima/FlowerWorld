package com.flowerworld.app.ui.activity.list;

import android.app.Activity;
import android.content.Intent;
import android.view.ViewGroup;
import com.flowerworld.app.R;
import com.flowerworld.app.dao.bean.GlobalConstant;
import com.flowerworld.app.tool.util.FileUtility;
import com.flowerworld.app.ui.activity.list.base.SingleSelectListPageActivity;
import com.flowerworld.app.ui.activity.search.SearchAdvancedMainPageActivity;
import com.flowerworld.app.ui.widget.Banner;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;

public class ProductTypeSingleSelectActivity extends SingleSelectListPageActivity {

    private static final int REQUEST_CODE_SHOW = 0x123;


    public static final void goHere(Activity activity) {
        ArrayList<String> dataList = new ArrayList<String>();
        dataList.add(readProductType(activity));
        dataList.add("");

        ArrayList<Integer> titleIdList = new ArrayList<Integer>();
        titleIdList.add(R.string.product_type);
        titleIdList.add(R.string.product_type);

        ArrayList<String> itemNameList = new ArrayList<String>();
        itemNameList.add("name");
        itemNameList.add("name");

        ArrayList<String> itemIdList = new ArrayList<String>();
        itemIdList.add("id");
        itemIdList.add("id");

        Intent intent = new Intent(activity, ProductTypeSingleSelectActivity.class);
        intent.putStringArrayListExtra(SingleSelectListPageActivity.KEY_DATA_LIST, dataList);
        intent.putIntegerArrayListExtra(SingleSelectListPageActivity.KEY_TITLE_LIST, titleIdList);
        intent.putStringArrayListExtra(SingleSelectListPageActivity.KEY_ITEM_NAME, itemNameList);
        intent.putStringArrayListExtra(SingleSelectListPageActivity.KEY_ITEM_ID, itemIdList);
        activity.startActivityForResult(intent, REQUEST_CODE_SHOW);
    }

    public static String readProductType(Activity activity) {
        return FileUtility.readFileInData(activity, GlobalConstant.productType + GlobalConstant.FILE_TYPE);
    }

    @Override
    protected void dealClick(SingleSelectAdapter adapter, int position, String dataId, String dataName) {
        String id = ((JsonObject) adapter.getItem(position)).get(dataId).getAsString();
        resultNameList.add(((JsonObject) adapter.getItem(position)).get(dataName).getAsString());
        resultIdList.add(id);

        if (count >= dataList.size()) {
            Intent intent = new Intent(this, SearchAdvancedMainPageActivity.class);
            intent.putStringArrayListExtra(KEY_RESULT_NAME_LIST, resultNameList);
            intent.putStringArrayListExtra(KEY_RESULT_ID_LIST, resultIdList);
            startActivityForResult(intent, REQUEST_CODE_SHOW);
            return;
        }

        JsonArray arr = ((JsonObject) adapter.getItem(position)).get("children").getAsJsonArray();
        adapter.setData(arr, itemName.get(count), itemId.get(count));
        adapter.notifyDataSetChanged();
        ((Banner) ((ViewGroup) findViewById(R.id.banner_layout)).getChildAt(0)).getTitleView().setText(titleList.get(count));
        count++;

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (REQUEST_CODE_SHOW == requestCode) {
            finish(false);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
