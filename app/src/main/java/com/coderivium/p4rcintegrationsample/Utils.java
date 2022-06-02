package com.coderivium.p4rcintegrationsample;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Resources;

public class Utils {

    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    public static ProgressDialog buildLoading(Context context){
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        return progressDialog;

    }
}
