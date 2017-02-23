package com.tigersapp.bdcricket.activity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.tigersapp.bdcricket.R;
import com.tigersapp.bdcricket.util.Constants;
import com.tigersapp.bdcricket.util.FetchFromWeb;
import com.tigersapp.bdcricket.util.SharedPrefData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import dmax.dialog.SpotsDialog;

/**
 * @author Ripon
 */

public class QuizResultActivity extends AppCompatActivity{

    TextView marks, totalquestion;
    Button ok;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_quiz_result);

        marks = (TextView) findViewById(R.id.score);
        totalquestion = (TextView) findViewById(R.id.totalques);
        ok = (Button) findViewById(R.id.okbutton);

        marks.setText(getIntent().getStringExtra("marks"));
        totalquestion.setText("Total Questions: "+getIntent().getStringExtra("total"));

        final AlertDialog progressDialog = new SpotsDialog(QuizResultActivity.this, R.style.Custom);
        progressDialog.show();
        progressDialog.setCancelable(true);
        RequestParams params = new RequestParams();

        params.put("key", "bl905577");
        params.put("quizid", Constants.QUIZ_ID);
        params.put("name", SharedPrefData.getNickName(QuizResultActivity.this));
        params.put("mobileno", SharedPrefData.getMobileNo(QuizResultActivity.this));
        params.put("marks", getIntent().getStringExtra("marks"));

        String url = Constants.INSERT_QUIZ_MARKS;

        FetchFromWeb.post(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                progressDialog.dismiss();
                try {
                    if (response.getString("msg").equals("Successful")) {
                        Toast.makeText(QuizResultActivity.this, "Score saved successfully", Toast.LENGTH_LONG).show();
                    }

                    Log.d(Constants.TAG, response.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                progressDialog.dismiss();
                Toast.makeText(QuizResultActivity.this, "Failed to save your score", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                progressDialog.dismiss();
                Toast.makeText(QuizResultActivity.this, "Failed to save your score", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                progressDialog.dismiss();
                Toast.makeText(QuizResultActivity.this, "Failed to save your score", Toast.LENGTH_LONG).show();
            }
        });

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
