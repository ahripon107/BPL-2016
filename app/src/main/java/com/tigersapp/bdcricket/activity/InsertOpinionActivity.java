package com.tigersapp.bdcricket.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.tigersapp.bdcricket.R;
import com.tigersapp.bdcricket.adapter.BasicListAdapter;
import com.tigersapp.bdcricket.fragment.NewsCommentsFragment;
import com.tigersapp.bdcricket.model.Comment;
import com.tigersapp.bdcricket.model.CricketNews;
import com.tigersapp.bdcricket.util.Constants;
import com.tigersapp.bdcricket.util.FetchFromWeb;
import com.tigersapp.bdcricket.util.Validator;
import com.tigersapp.bdcricket.util.ViewHolder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import dmax.dialog.SpotsDialog;

/**
 * @author Ripon
 */

public class InsertOpinionActivity extends AppCompatActivity{

    ArrayList<Comment> comments;
    String url;
    RecyclerView recyclerView;
    Typeface tf;
    String id;
    AdView adView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opinions);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recyclerView = (RecyclerView) findViewById(R.id.rvComments);
        recyclerView.setHasFixedSize(true);
        id =  getIntent().getStringExtra("opinionid");
        adView = (AdView) findViewById(R.id.adViewOpinions);

        AdRequest adRequest = new AdRequest.Builder().addTestDevice(Constants.ONE_PLUS_TEST_DEVICE)
                .addTestDevice(Constants.XIAOMI_TEST_DEVICE).build();
        adView.loadAd(adRequest);

        tf = Typeface.createFromAsset(getAssets(), Constants.SOLAIMAN_LIPI_FONT);

        comments = new ArrayList<>();

        recyclerView.setAdapter(new BasicListAdapter<Comment, OpinionViewHolder>(comments) {
            @Override
            public OpinionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.singlecomment, parent, false);
                return new OpinionViewHolder(v);
            }

            @Override
            public void onBindViewHolder(OpinionViewHolder holder, int position) {
                holder.comment.setTypeface(tf);
                holder.commenter.setTypeface(tf);
                holder.commenter.setText("কমেন্ট করেছেন:  " + comments.get(position).getName());
                holder.comment.setText(comments.get(position).getComment());
                holder.timestamp.setText(Constants.getTimeAgo(Long.parseLong(comments.get(position).getTimestamp())));
            }

        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        final AlertDialog progressDialog = new SpotsDialog(InsertOpinionActivity.this, R.style.Custom);
        progressDialog.show();
        progressDialog.setCancelable(true);
        RequestParams requestParams = new RequestParams();

        requestParams.add("key", "bl905577");
        requestParams.add("newsid", "opinion"+id);
        url = Constants.FETCH_NEWS_COMMENT_URL;

        FetchFromWeb.get(url, requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                progressDialog.dismiss();
                try {
                    if (response.getString("msg").equals("Successful")) {
                        JSONArray jsonArray = response.getJSONArray("content");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            comments.add(new Comment(jsonObject.getString("name"), jsonObject.getString("comment"), jsonObject.getString("timestamp")));
                        }
                    }

                    Log.d(Constants.TAG, response.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                recyclerView.getAdapter().notifyDataSetChanged();
                if (comments.size() != 0) {
                    recyclerView.smoothScrollToPosition(comments.size() - 1);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                progressDialog.dismiss();
                Toast.makeText(InsertOpinionActivity.this, "Failed", Toast.LENGTH_LONG).show();
            }
        });


        Button sendComment = (Button) findViewById(R.id.btnSubmitComment);
        sendComment.setText("মতামত দিন");
        TextView question = (TextView) findViewById(R.id.opinion_question);
        question.setTypeface(tf);
        question.setText(getIntent().getStringExtra("question"));

        sendComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                View promptsView = LayoutInflater.from(InsertOpinionActivity.this).inflate(R.layout.addnewcomment, null, false);
                final EditText writeComment = (EditText) promptsView.findViewById(R.id.etYourComment);
                final EditText yourName = (EditText) promptsView.findViewById(R.id.etYourName);
                AlertDialog.Builder builder = new AlertDialog.Builder(InsertOpinionActivity.this);
                builder.setView(promptsView);
                builder.setTitle("মতামত").setPositiveButton("SUBMIT", null).setNegativeButton("CANCEL", null);

                final AlertDialog alertDialog = builder.create();
                alertDialog.show();

                Button okButton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
                okButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (Validator.validateNotEmpty(yourName, "Required") && Validator.validateNotEmpty(writeComment, "Required")) {
                            final String comment = writeComment.getText().toString().trim();
                            final String name = yourName.getText().toString().trim();

                            final AlertDialog progressDialog = new SpotsDialog(InsertOpinionActivity.this, R.style.Custom);
                            progressDialog.show();
                            progressDialog.setCancelable(true);
                            RequestParams params = new RequestParams();

                            params.put("key", "bl905577");
                            params.put("newsid", "opinion"+id);
                            params.put("name", name);
                            params.put("comment", comment);
                            params.put("timestamp", System.currentTimeMillis() + "");

                            url = Constants.INSERT_NEWS_COMMENT_URL;

                            FetchFromWeb.post(url, params, new JsonHttpResponseHandler() {
                                @Override
                                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                    progressDialog.dismiss();
                                    try {
                                        if (response.getString("msg").equals("Successful")) {
                                            Toast.makeText(InsertOpinionActivity.this, "Comment successfully posted", Toast.LENGTH_LONG).show();
                                            comments.add(new Comment(name, comment, System.currentTimeMillis() + ""));
                                            recyclerView.getAdapter().notifyDataSetChanged();
                                            if (comments.size() != 0) {
                                                recyclerView.smoothScrollToPosition(comments.size() - 1);
                                            }
                                        }

                                        Log.d(Constants.TAG, response.toString());
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                                @Override
                                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                                    progressDialog.dismiss();
                                    Toast.makeText(InsertOpinionActivity.this, "Failed", Toast.LENGTH_LONG).show();
                                }
                            });

                            alertDialog.dismiss();
                        }
                    }
                });
            }
        });
    }

    public static class OpinionViewHolder extends RecyclerView.ViewHolder {
        protected TextView commenter;
        protected TextView comment;
        protected TextView timestamp;

        public OpinionViewHolder(View v) {
            super(v);
            commenter = ViewHolder.get(v, R.id.tvName);
            comment = ViewHolder.get(v, R.id.tvComment);
            timestamp = ViewHolder.get(itemView, R.id.tv_time_stamp);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
