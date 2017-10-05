package com.allgames.sportslab.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.allgames.sportslab.R;
import com.allgames.sportslab.adapter.BasicListAdapter;
import com.allgames.sportslab.model.Comment;
import com.allgames.sportslab.util.Constants;
import com.allgames.sportslab.util.DefaultMessageHandler;
import com.allgames.sportslab.util.NetworkService;
import com.allgames.sportslab.util.ViewHolder;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.inject.Inject;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * @author Ripon
 */
public class InsertOpinionActivity extends CommonActivity {

    Typeface tf;
    String id;
    Profile profile;

    @Inject
    private ArrayList<Comment> comments;

    private RecyclerView recyclerView;
    private AdView adView;
    private EditText commentBody;
    private ImageButton sendComment;

    private TextView question;
    @Inject
    private NetworkService networkService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initialize();

        recyclerView.setHasFixedSize(true);

        id = getIntent().getStringExtra("opinionid");

        AdRequest adRequest = new AdRequest.Builder().addTestDevice(Constants.ONE_PLUS_TEST_DEVICE)
                .addTestDevice(Constants.XIAOMI_TEST_DEVICE).build();
        adView.loadAd(adRequest);

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

        networkService.fetchOpinionComments(id, new DefaultMessageHandler(this, true) {
            @Override
            public void onSuccess(Message msg) {
                String string = (String) msg.obj;
                try {
                    JSONObject response = new JSONObject(string);
                    if (response.getString("msg").equals("Successful")) {
                        JSONArray jsonArray = response.getJSONArray("content");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            comments.add(new Comment(jsonObject.getString("name"), jsonObject.getString("comment"), jsonObject.getString("profileimage"), jsonObject.getString("timestamp")));
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                recyclerView.getAdapter().notifyDataSetChanged();
                if (comments.size() != 0) {
                    recyclerView.smoothScrollToPosition(comments.size() - 1);
                }
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

        networkService.publishOpinionComment(id, profile.getName(), comment, profile.getProfilePictureUri(50, 50).toString(), new DefaultMessageHandler(this, true) {
            @Override
            public void onSuccess(Message msg) {
                String string = (String) msg.obj;
                try {
                    JSONObject response = new JSONObject(string);
                    if (response.getString("msg").equals("Successful")) {
                        Toast.makeText(InsertOpinionActivity.this, "Comment successfully posted", Toast.LENGTH_LONG).show();
                        comments.add(new Comment(profile.getName(), comment, profile.getProfilePictureUri(50, 50).toString(), System.currentTimeMillis() + ""));
                        recyclerView.getAdapter().notifyDataSetChanged();
                        if (comments.size() != 0) {
                            recyclerView.smoothScrollToPosition(comments.size() - 1);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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

    private void initialize() {
        setContentView(R.layout.activity_opinions);
        FacebookSdk.sdkInitialize(getApplicationContext());

        recyclerView = (RecyclerView) findViewById(R.id.rvComments);
        adView = (AdView) findViewById(R.id.adViewOpinions);
        commentBody = (EditText) findViewById(R.id.commentBody);
        sendComment = (ImageButton) findViewById(R.id.btnSubmitComment);
        question = (TextView) findViewById(R.id.opinion_question);

        tf = Typeface.createFromAsset(getAssets(), Constants.SOLAIMAN_LIPI_FONT);
    }
}
