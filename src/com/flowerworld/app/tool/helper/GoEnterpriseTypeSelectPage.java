package com.flowerworld.app.tool.helper;

import android.app.Activity;
import android.content.Intent;
import com.flowerworld.app.R;
import com.flowerworld.app.dao.bean.GlobalConstant;
import com.flowerworld.app.tool.util.FileUtility;
import com.flowerworld.app.ui.activity.list.EnterpriseTypeOneSelectPageActivity;
import com.flowerworld.app.ui.activity.list.base.SingleSelectListPageActivity;

import java.util.ArrayList;

public class GoEnterpriseTypeSelectPage {
    public static void goEnterpriseTypeSelectPage(Activity activity, int requestCode) {
//		Intent intent = new Intent(activity, MultiSelectListPageActivity.class);
//		intent.putExtra(MultiSelectListPageActivity.KEY_DATA, readCompanyType(activity));
//		intent.putExtra(MultiSelectListPageActivity.KEY_ITEM_NAME, "name");
//		intent.putExtra(MultiSelectListPageActivity.KEY_ITEM_ID, "id");
//		intent.putExtra(MultiSelectListPageActivity.KEY_TITLE, R.string.enterprise_type_title);
//		activity.startActivityForResult(intent, requestCode);

        ArrayList<String> data = new ArrayList<String>();
        data.add(readCompanyType(activity));

        ArrayList<Integer> title = new ArrayList<Integer>();
        title.add(R.string.enterprise_type_title);

        ArrayList<String> itemName = new ArrayList<String>();
        itemName.add("name");

        ArrayList<String> itemId = new ArrayList<String>();
        itemId.add("id");

        Intent intent = new Intent(activity, EnterpriseTypeOneSelectPageActivity.class);
        intent.putStringArrayListExtra(SingleSelectListPageActivity.KEY_DATA_LIST, data);
        intent.putIntegerArrayListExtra(SingleSelectListPageActivity.KEY_TITLE_LIST, title);
        intent.putStringArrayListExtra(SingleSelectListPageActivity.KEY_ITEM_NAME, itemName);
        intent.putStringArrayListExtra(SingleSelectListPageActivity.KEY_ITEM_ID, itemId);
        activity.startActivityForResult(intent, requestCode);

    }

    public static String readCompanyType(Activity activity) {
        return FileUtility.readFileInData(activity, GlobalConstant.companyType + GlobalConstant.FILE_TYPE);
    }
}
