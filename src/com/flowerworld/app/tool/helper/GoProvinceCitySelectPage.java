package com.flowerworld.app.tool.helper;

import android.app.Activity;
import android.content.Intent;
import android.widget.Button;
import com.flowerworld.app.R;
import com.flowerworld.app.dao.bean.GlobalConstant;
import com.flowerworld.app.tool.util.FileUtility;
import com.flowerworld.app.ui.activity.list.base.SingleSelectListPageActivity;

import java.util.ArrayList;
import java.util.List;

public class GoProvinceCitySelectPage {

    public static void startSingleSelect(Activity activity, int requestCode) {
        ArrayList<String> location = new ArrayList<String>();
        location.add(readProvinceStr(activity));
        location.add(readCityStr(activity));

        ArrayList<Integer> title = new ArrayList<Integer>();
        title.add(R.string.province_select_title);
        title.add(R.string.city_select_title);

        ArrayList<String> itemName = new ArrayList<String>();
        itemName.add("name");
        itemName.add("name");

        ArrayList<String> itemId = new ArrayList<String>();
        itemId.add("id");
        itemId.add("id");

        Intent intent = new Intent(activity, SingleSelectListPageActivity.class);
        intent.putStringArrayListExtra(SingleSelectListPageActivity.KEY_DATA_LIST, location);
        intent.putIntegerArrayListExtra(SingleSelectListPageActivity.KEY_TITLE_LIST, title);
        intent.putStringArrayListExtra(SingleSelectListPageActivity.KEY_ITEM_NAME, itemName);
        intent.putStringArrayListExtra(SingleSelectListPageActivity.KEY_ITEM_ID, itemId);
        activity.startActivityForResult(intent, requestCode);
    }

    public static List<String> onActivityResultSingle(Intent data, int id, Activity activity) {
        ArrayList<String> list = data.getStringArrayListExtra(SingleSelectListPageActivity.KEY_RESULT_NAME_LIST);
        String text = list.get(0) + " " + list.get(1);
        ((Button) activity.findViewById(id)).setText(text);
        return data.getStringArrayListExtra(SingleSelectListPageActivity.KEY_RESULT_ID_LIST);
    }

    public static String readProvinceStr(Activity activity) {
        return FileUtility.readFileInData(activity, GlobalConstant.province + GlobalConstant.FILE_TYPE);
    }

    public static String readCityStr(Activity activity) {
        return FileUtility.readFileInData(activity, GlobalConstant.city + GlobalConstant.FILE_TYPE);
    }

}
