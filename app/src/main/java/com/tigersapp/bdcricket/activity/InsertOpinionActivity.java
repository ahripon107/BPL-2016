package com.tigersapp.bdcricket.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.inject.Inject;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;
import com.tigersapp.bdcricket.R;
import com.tigersapp.bdcricket.adapter.BasicListAdapter;
import com.tigersapp.bdcricket.model.Comment;
import com.tigersapp.bdcricket.util.Constants;
import com.tigersapp.bdcricket.util.Dialogs;
import com.tigersapp.bdcricket.util.FetchFromWeb;
import com.tigersapp.bdcricket.util.RoboAppCompatActivity;
import com.tigersapp.bdcricket.util.ViewHolder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

/**
 * @author Ripon
 */
@ContentView(R.layout.activity_opinions)
public class InsertOpinionActivity extends RoboAppCompatActivity {

    @Inject
    private ArrayList<Comment> comments;

    @InjectView(R.id.rvComments)
    private RecyclerView recyclerView;

    @InjectView(R.id.adViewOpinions)
    private AdView adView;

    @InjectView(R.id.commentBody)
    private EditText commentBody;

    @InjectView(R.id.btnSubmitComment)
    private ImageButton sendComment;

    @InjectView(R.id.opinion_question)
    private TextView question;

    String url;
    Typeface tf;
    String id;
    Dialogs dialogs;
    Profile profile;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recyclerView.setHasFixedSize(true);

        id = getIntent().getStringExtra("opinionid");

        dialogs = new Dialogs(this);

        AdRequest adRequest = new AdRequest.Builder().addTestDevice(Constants.ONE_PLUS_TEST_DEVICE)
                .addTestDevice(Constants.XIAOMI_TEST_DEVICE).build();
        adView.loadAd(adRequest);

        tf = Typeface.createFromAsset(getAssets(), Constants.SOLAIMAN_LIPI_FONT);

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
                if (!comments.get(position).getProfileimage().equals("")) {
                    Picasso.with(InsertOpinionActivity.this).load(comments.get(position).getProfileimage()).into(holder.imageView);
                }
                holder.timestamp.setText(Constants.getTimeAgo(Long.parseLong(comments.get(position).getTimestamp())));
            }

        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        dialogs.showDialog();
        RequestParams requestParams = new RequestParams();

        requestParams.add("key", "bl905577");
        requestParams.add("newsid", "opinion" + id);
        url = Constants.FETCH_NEWS_COMMENT_URL;
        Log.d(Constants.TAG, url);

        FetchFromWeb.get(url, requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                dialogs.dismissDialog();
                try {
                    if (response.getString("msg").equals("Successful")) {
                        JSONArray jsonArray = response.getJSONArray("content");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            comments.add(new Comment(jsonObject.getString("name"), jsonObject.getString("comment"), jsonObject.getString("profileimage"), jsonObject.getString("timestamp")));
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
                dialogs.dismissDialog();
                Toast.makeText(InsertOpinionActivity.this, "Failed", Toast.LENGTH_LONG).show();
            }
        });


        question = (TextView) findViewById(R.id.opinion_question);
        question.setTypeface(tf);
        question.setText(getIntent().getStringExtra("question"));

        sendComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profile = Profile.getCurrentProfile();
                if (profile != null) {
                    String comment = commentBody.getText().toString().trim();
                    if (!comment.equals("")) {
                        publishComment(comment);
                        commentBody.getText().clear();
                    } else {
                        Toast.makeText(getApplicationContext(), "Please write something", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Intent intent = new Intent(InsertOpinionActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    public void publishComment(final String comment) {
        dialogs.showDialog();
        RequestParams params = new RequestParams();

        params.put("key", "bl905577");
        params.put("newsid", "opinion" + id);
        params.put("name", profile.getName());
        params.put("comment", comment);
        params.put("profileimage", profile.getProfilePictureUri(50, 50).toString());
        params.put("timestamp", System.currentTimeMillis() + "");

        url = Constants.INSERT_NEWS_COMMENT_URL;

        FetchFromWeb.post(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                dialogs.dismissDialog();
                try {
                    if (response.getString("msg").equals("Successful")) {
                        Toast.makeText(InsertOpinionActivity.this, "Comment successfully posted", Toast.LENGTH_LONG).show();
                        comments.add(new Comment(profile.getName(), comment, profile.getProfilePictureUri(50, 50).toString(), System.currentTimeMillis() + ""));
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
                dialogs.dismissDialog();
                Toast.makeText(InsertOpinionActivity.this, "Failed", Toast.LENGTH_LONG).show();
            }
        });
    }

    public static class OpinionViewHolder extends RecyclerView.ViewHolder {
        protected TextView commenter;
        protected TextView comment;
        protected TextView timestamp;
        protected ImageView imageView;

        public OpinionViewHolder(View v) {
            super(v);
            commenter = ViewHolder.get(v, R.id.tvName);
            comment = ViewHolder.get(v, R.id.tvComment);
            timestamp = ViewHolder.get(v, R.id.tv_time_stamp);
            imageView = ViewHolder.get(v, R.id.profile_image);
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
