package com.flowerworld.app.ui.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;
import com.flowerworld.app.R;
import com.flowerworld.app.tool.helper.FriendOperationHelper;
import com.flowerworld.app.tool.util.ToastUtil;
import com.flowerworld.app.ui.base.BaseActivity;

import java.util.List;
import java.util.Map;

public class FriendOperationDialog extends BaseActivity implements OnClickListener {
    public static final int REQUEST_CODE_THIS = 0X12341;

    public static final String INTENT_KEY_SOURCE_GROUP_ID = "source_group_id";
    public static final String INTENT_KEY_FRIEND_COUNT = "friend_count";

    public static void goHere(BaseActivity activity, String srcGid, String friendCount) {
        Intent intent = new Intent(activity, FriendOperationDialog.class);
        intent.putExtra(INTENT_KEY_SOURCE_GROUP_ID, srcGid);
        intent.putExtra(INTENT_KEY_FRIEND_COUNT, friendCount);
        activity.startActivityForResult(intent, REQUEST_CODE_THIS, false);
    }

    private Button btnAllTransfer = null;
    private Button btnDelete = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initialize();
    }

    private void initialize() {
        initView();
        initEvent();
    }

    private void initView() {
        setContentView(R.layout.dialog_friend_operation);

        btnAllTransfer = (Button) findViewById(R.id.dialog_friend_operation_button_all_transfer);
        btnDelete = (Button) findViewById(R.id.dialog_friend_operation_button_delete);
    }

    private void initEvent() {
        btnAllTransfer.setOnClickListener(this);
        btnDelete.setOnClickListener(this);

        findViewById(R.id.dialog_friend_operation_button_goback).setOnClickListener(this);
        findViewById(R.id.dialog_friend_operation_button_rename).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.dialog_friend_operation_button_goback:
            finish(false);
            break;

        case R.id.dialog_friend_operation_button_all_transfer:
            showOtherGroupSelection();
            break;

        case R.id.dialog_friend_operation_button_rename:
            showRenameDialog();
            break;

        case R.id.dialog_friend_operation_button_delete:
            FriendOperationHelper.deleteFriendGroup(this, getIntent().getStringExtra(INTENT_KEY_SOURCE_GROUP_ID), new Runnable() {

                @Override
                public void run() {
                    ToastUtil.show(getBaseContext(), "删除成功");
                    setResult(RESULT_OK);
                    finish(false);
                }
            });
            break;

        default:
            break;
        }
    }

    private void showOtherGroupSelection() {
        ListView selectList = new ListView(this);
        selectList.setBackgroundResource(R.drawable.dialog_corner_rectangle);
        final List<Map<String, String>> listData = FriendOperationHelper.getFriendGroupWithoutSrcGid(getIntent().getStringExtra(
                INTENT_KEY_SOURCE_GROUP_ID));
        selectList.setAdapter(new SimpleAdapter(this, listData, R.layout.dialog_item_transfer_friend_group,
                new String[] { "name" }, new int[] { R.id.dialog_item_transfer_friend_group_text }));

        final Dialog dialog = new Dialog(this, R.style.Theme_Dialog_NoTitle_NoActionBar);
        selectList.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dialog.dismiss();
                Map<String, String> m = listData.get(position);
                String targetGid = m.get("id");

                FriendOperationHelper.doAllTransfer(FriendOperationDialog.this, targetGid,
                        getIntent().getStringExtra(INTENT_KEY_SOURCE_GROUP_ID), new Runnable() {

                            @Override
                            public void run() {
                                ToastUtil.show(getBaseContext(), "转移成功");
                                setResult(RESULT_OK);
                                finish(false);
                            }
                        });
                ;
            }
        });

        dialog.setContentView(selectList);
        dialog.show();

//		PopupWindow popup = new PopupWindow(selectList, 200, 300);
//		popup.setTouchable(true);
//		popup.setBackgroundDrawable(getResources().getDrawable(R.drawable.dialog_corner_rectangle));
//		popup.setOutsideTouchable(false);
//		popup.showAtLocation(this.getWindow().getDecorView(), Gravity.CENTER, 0, 0);
////		popup.showAsDropDown(btnAllTransfer);
    }

    private void showRenameDialog() {
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setTitle(getString(R.string.dialog_friend_operation_rename));
        final EditText rename = new EditText(this);
        b.setView(rename);
        b.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                FriendOperationHelper.renameFriendGroup(FriendOperationDialog.this,
                        getIntent().getStringExtra(INTENT_KEY_SOURCE_GROUP_ID), rename.getText().toString(), new Runnable() {
                            public void run() {
                                ToastUtil.show(getBaseContext(), "重命名成功");
                                setResult(RESULT_OK);
                                finish(false);
                            }
                        });

            }
        });
        b.setNegativeButton(R.string.cancle, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        b.create().show();
    }
}
