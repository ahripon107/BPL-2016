package com.tigersapp.bdcricket.util;

import android.app.ProgressDialog;
import android.content.Context;

import com.tigersapp.bdcricket.R;

/**
 * @author Ripon
 */

public class Dialogs {
    Context context;
    ProgressDialog progressDialog;

    public Dialogs(Context context) {
        this.context = context;
    }

    public void showDialog() {
        progressDialog = new ProgressDialog(context, R.style.Theme_Cricket_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.show();
        //progressDialog.setContentView must be called after progressDialog.show()
        progressDialog.setContentView(R.layout.loading_spinner);
    }

    public void dismissDialog() {
        if (progressDialog != null && progressDialog.isShowing())
        progressDialog.dismiss();
    }
}
