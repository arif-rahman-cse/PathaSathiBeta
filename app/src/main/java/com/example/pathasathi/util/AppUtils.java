package com.example.pathasathi.util;

import android.content.Context;
import android.graphics.Color;

import cn.pedant.SweetAlert.SweetAlertDialog;

public final class AppUtils {
    static SweetAlertDialog pDialog;

    public static void showProgress(Context context) {
        pDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#80c12c"));
        pDialog.setTitleText("Loading ...");
        pDialog.setCancelable(false);
        pDialog.show();
    }

    public static void hideProgress() {
        pDialog.dismissWithAnimation();
    }
}
