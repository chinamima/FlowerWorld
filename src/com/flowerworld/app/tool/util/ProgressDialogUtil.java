package com.flowerworld.app.tool.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.text.TextUtils;
import com.flowerworld.app.R;

public class ProgressDialogUtil {
    public static ProgressDialog progressDialog;
    private static int count = 0;

    public static void show(Context context, String message) {
        count++;
        if (null != progressDialog) {
            return;
        }

        progressDialog = new ProgressDialog(context);

        if (TextUtils.isEmpty(message)) {
            message = context.getResources().getString(R.string.loading);
        }

        progressDialog.setMessage(message);
        progressDialog.setCanceledOnTouchOutside(false);
//		progress.setView(null);
        try {
            progressDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void dismiss() {
        count--;
        if (0 != count) {
            return;
        }

        if (null != progressDialog) {
            progressDialog.dismiss();
        }

        progressDialog = null;
    }
}
