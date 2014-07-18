package com.flowerworld.app.tool.helper;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;
import com.flowerworld.app.R;
import com.flowerworld.app.dao.bean.CreateFormBean;
import com.flowerworld.app.listener.NotEmptyTextWatcher;
import com.flowerworld.app.tool.util.UnitUtil;

public class CreateFromPageHelper {
    public static final String TAG = CreateFromPageHelper.class.getSimpleName();

    public static Button addSelect(final Activity activity, ViewGroup parent, String fieldName, String id, boolean must,
            boolean tips, final String[] data, final OnItemClickListener itemClickListener) {
        View v = activity.getLayoutInflater().inflate(R.layout.form_item_select, null);
        v.findViewById(R.id.form_item_text_must).setVisibility(must ? View.VISIBLE : View.GONE);
        v.findViewById(R.id.form_item_text_tips).setVisibility(tips ? View.VISIBLE : View.GONE);
        ((TextView) v.findViewById(R.id.form_item_text_select)).setText(fieldName);
        final Button btn = (Button) v.findViewById(R.id.form_item_button_select);
        btn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(activity, R.style.Theme_Dialog_NoTitle_NoActionBar);

                ListView list = new ListView(activity);
                list.setAdapter(new ArrayAdapter<String>(activity, android.R.layout.simple_list_item_1, data));
                list.setOnItemClickListener(new OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        ((CreateFormBean) btn.getTag()).v = data[position];
                        btn.setText(data[position]);
                        if (null != itemClickListener) {
                            itemClickListener.onItemClick(parent, view, position, id);
                        }
                        dialog.dismiss();
                    }
                });
                list.setCacheColorHint(Color.TRANSPARENT);
                list.setBackgroundColor(Color.WHITE);

                dialog.setContentView(list, new ViewGroup.LayoutParams(UnitUtil.transformDipToPx(300), LayoutParams.WRAP_CONTENT));
                dialog.setCancelable(true);
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();
            }
        });
        CreateFormBean bean = new CreateFormBean();
        bean.pp = id;
        btn.setTag(bean);

        parent.addView(v);
        return btn;
    }

    public static EditText addEdit(final Activity activity, ViewGroup parent, String fieldName, String id, boolean must,
            boolean tips, String tipsStr) {
        View v = activity.getLayoutInflater().inflate(R.layout.form_item_edit, null);
        v.findViewById(R.id.form_item_text_must).setVisibility(must ? View.VISIBLE : View.GONE);
        v.findViewById(R.id.form_item_text_tips).setVisibility(tips ? View.VISIBLE : View.GONE);
        if (tips) {
            ((TextView) v.findViewById(R.id.form_item_text_tips)).setText(tipsStr);
        }

        ((TextView) v.findViewById(R.id.form_item_text_field)).setText(fieldName);
        final EditText edit = (EditText) v.findViewById(R.id.form_item_edit_field);
        edit.setInputType(InputType.TYPE_CLASS_TEXT);
        CreateFormBean bean = new CreateFormBean();
        bean.pp = id;
        edit.setTag(bean);

        if (must) {
            edit.addTextChangedListener(new NotEmptyTextWatcher(edit));
            edit.setError("不能为空");
        }

        parent.addView(v);
        return edit;
    }
}
