package com.androxue.login.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;

/**
 * Created by JimCharles on 2017/10/5.
 */

public class ProgressDialogUtils {

    private static ProgressDialog mProgressDialog = null;
    @SuppressLint("StaticFieldLeak")
    private static ProgressDialogUtils mInstance = null;

    public static ProgressDialogUtils getInstance() {
        if (mInstance == null) {
            mInstance = new ProgressDialogUtils();

        }
        return mInstance;
    }

    public void show(Activity activity, String mMessage) {

        try {
            if (activity == null) {
                return;
            }
            // 开始请求是，显示请求对话框
            if (mProgressDialog == null) {
                mProgressDialog = new ProgressDialog(activity, 0);
                mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                mProgressDialog.setMessage(mMessage);
                mProgressDialog.setCanceledOnTouchOutside(false);
                mProgressDialog.setCancelable(true);
                mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        mProgressDialog.dismiss();
                        mProgressDialog = null;
                    }
                });
            }

            if (!activity.isFinishing()) {
                mProgressDialog.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void dismiss() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }
}
