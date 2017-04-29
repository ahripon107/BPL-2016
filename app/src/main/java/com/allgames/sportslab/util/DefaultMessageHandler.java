package com.allgames.sportslab.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.allgames.sportslab.R;

/**
 * @author Ripon
 */

public class DefaultMessageHandler extends Handler {
    private Context context;
    private ProgressDialog progressDialog;

    public DefaultMessageHandler(Context context) {
        this(context, false);
    }

    public DefaultMessageHandler(Context context, boolean showProgressDialog) {
        this(context, showProgressDialog, null);
    }

    public DefaultMessageHandler(Context context, boolean showProgressDialog, String message) {
        this.context = context;

        if (showProgressDialog) {
            progressDialog = new ProgressDialog(context, R.style.Theme_Tour_Dialog);
            progressDialog.setMessage(message);
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.show();
            //progressDialog.setContentView must be called after progressDialog.show()
            progressDialog.setContentView(R.layout.loading_spinner);
        }
    }

    @Override
    public void handleMessage(Message msg) {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }

        if (msg.what == Constants.SUCCESS) {
            onSuccess(msg);
        } else {
            onFailure();
        }
    }

    public void onSuccess(Message msg) {
    }

    public void onFailure() {
        Toast.makeText(context, "Failed", Toast.LENGTH_LONG).show();
    }
}
