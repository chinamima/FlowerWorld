package com.flowerworld.app.ui.activity.list;

import android.content.Intent;
import com.flowerworld.app.R;
import com.flowerworld.app.tool.util.GsonJsonUtil;
import com.flowerworld.app.ui.activity.list.base.MultiSelectListPageActivity;
import com.flowerworld.app.ui.activity.list.base.SingleSelectListPageActivity;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class EnterpriseTypeOneSelectPageActivity extends SingleSelectListPageActivity {

    protected static final int REQUEST_CODE_TYPE_2 = 0x123566;

    public static final String KEY_RESULT_CHILD_ID_LIST = "resultChildIdList";

    @Override
    protected void dealClick(SingleSelectAdapter adapter, int position, String dataId, String dataName) {
        String id = ((JsonObject) adapter.getItem(position)).get(dataId).getAsString();
        resultNameList.add(((JsonObject) adapter.getItem(position)).get(dataName).getAsString());
        resultIdList.add(id);

        if (count >= dataList.size()) {
            String data = ((JsonObject) adapter.getItem(position)).get("children").getAsJsonArray().toString();

            Intent intent = new Intent(EnterpriseTypeOneSelectPageActivity.this, MultiSelectListPageActivity.class);
            intent.putExtra(MultiSelectListPageActivity.KEY_DATA, data);
            intent.putExtra(MultiSelectListPageActivity.KEY_ITEM_NAME, "name");
            intent.putExtra(MultiSelectListPageActivity.KEY_ITEM_ID, "id");
            intent.putExtra(MultiSelectListPageActivity.KEY_TITLE, R.string.enterprise_type_title);
            intent.putExtra(MultiSelectListPageActivity.KEY_BUTTON_SHOW, true);
            intent.putExtra(MultiSelectListPageActivity.KEY_TIPS, "注意：以下类别可以多选。");
            startActivityForResult(intent, REQUEST_CODE_TYPE_2);
            return;
        }

//		JsonObject obj = GsonJsonUtil.parse(dataList.get(count)).getAsJsonObject();
//		JsonArray arr = obj.get(id).getAsJsonArray();
//		adapter.setData(arr, itemName.get(count), itemId.get(count));
//		adapter.notifyDataSetChanged();
//		((Banner) ((ViewGroup) findViewById(R.id.banner_layout)).getChildAt(0)).getTitleView().setText(titleList.get(count));
        count++;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (RESULT_OK == resultCode) {
            switch (requestCode) {
            case REQUEST_CODE_TYPE_2:
                Intent intent = new Intent();
                String ids = dealMultiSelect(data, 0);
                intent.putStringArrayListExtra(KEY_RESULT_NAME_LIST, resultNameList);
                intent.putStringArrayListExtra(KEY_RESULT_ID_LIST, resultIdList);
                intent.putExtra(KEY_RESULT_CHILD_ID_LIST, ids);
                setResult(RESULT_OK, intent);
                finish();
                break;

            default:
                break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);

    }

    private String dealMultiSelect(Intent data, int id) {
        //name
//		String text = data.getStringExtra(MultiSelectListPageActivity.KEY_RESULT_NAME_LIST);
//		JsonArray arr = GsonJsonUtil.parse(text).getAsJsonArray();
//		text = "";
//		for (int i = 0; i < arr.size(); i++)
//		{
//			text += "," + arr.get(i).getAsString() ;
//		}
//		text = text.substring(1);
//		if (7 < text.length())
//		{
//			text = text.substring(0, 6) + "...";
//		}
//		((TextView) findViewById(id)).setText(text);

        //id
        String text = data.getStringExtra(MultiSelectListPageActivity.KEY_RESULT_ID_LIST);
        JsonArray arr = GsonJsonUtil.parse(text).getAsJsonArray();
        text = "";
        for (int i = 0; i < arr.size(); i++) {
            text += "|" + arr.get(i).getAsString();
        }
        return text.substring(1);
    }
}
