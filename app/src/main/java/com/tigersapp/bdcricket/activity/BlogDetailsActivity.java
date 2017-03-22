package com.tigersapp.bdcricket.activity;

import android.graphics.Typeface;
import android.os.Bundle;
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

import com.google.gson.Gson;
import com.google.inject.Inject;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;
import com.tigersapp.bdcricket.R;
import com.tigersapp.bdcricket.adapter.BasicListAdapter;
import com.tigersapp.bdcricket.model.BlogPost;
import com.tigersapp.bdcricket.model.Comment;
import com.tigersapp.bdcricket.util.Constants;
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
@ContentView(R.layout.activity_blog_details)
public class BlogDetailsActivity extends RoboAppCompatActivity{

    @InjectView(R.id.tvTitle)
    private TextView title;

    @InjectView(R.id.tvWriter)
    private TextView writer;

    @InjectView(R.id.tvDetailsofPost)
    private TextView details;

    @InjectView(R.id.tvReadTimes)
    private TextView readTimes;

    @Inject
    private ArrayList<Comment> comments;

    @InjectView(R.id.rvComments)
    private RecyclerView recyclerView;

    @InjectView(R.id.btnSubmitComment)
    private ImageButton sendComment;

    @InjectView(R.id.et_comment)
    private EditText commentEditText;

    Typeface tf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tf = Typeface.createFromAsset(this.getAssets(), Constants.SOLAIMAN_LIPI_FONT);

        title.setTypeface(tf);
        writer.setTypeface(tf);
        details.setTypeface(tf);
        readTimes.setTypeface(tf);

        RequestParams requestParams = new RequestParams();
        requestParams.add(Constants.KEY,Constants.KEY_VALUE);
        requestParams.put("id",getIntent().getStringExtra("id"));

        FetchFromWeb.get("http://apisea.xyz/TourBangla/apis/v3/FetchBlogDetails.php", requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                try {
                    Gson gson = new Gson();
                    response = response.getJSONArray("details").getJSONObject(0);
                    BlogPost blogPost1 = gson.fromJson(String.valueOf(response), BlogPost.class);
                    title.setText(blogPost1.getTitle());
                    writer.setText("লিখেছেন: " + blogPost1.getName());
                    details.setText(blogPost1.getDetails());
                    readTimes.setText(blogPost1.getReadtimes()+"  বার পঠিত");
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
                Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_LONG).show();
            }
        });

        recyclerView.setAdapter(new BasicListAdapter<Comment, CommentViewHolder>(comments) {
            @Override
            public CommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.singlecomment, parent, false);
                return new CommentViewHolder(v);
            }

            @Override
            public void onBindViewHolder(CommentViewHolder holder, int position) {
                holder.comment.setTypeface(tf);
                holder.commenter.setTypeface(tf);
                holder.commenter.setText("মন্তব্য করেছেন:  " + comments.get(position).getName());
                holder.comment.setText(comments.get(position).getComment());
                if (!comments.get(position).getProfileimage().equals("")) {
                    Picasso.with(BlogDetailsActivity.this).load(comments.get(position).getProfileimage()).into(holder.profileImageView);
                }
                holder.timestamp.setText(Constants.getTimeAgo(Long.parseLong(comments.get(position).getTimestamp())));
            }

        });

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        RequestParams requestParams1 = new RequestParams();
        requestParams1.put(Constants.KEY, Constants.KEY_VALUE);
        requestParams1.put("id", String.valueOf(getIntent().getStringExtra("id")));

        FetchFromWeb.get("http://apisea.xyz/TourBangla/apis/v3/FetchBlogDetails.php", requestParams1, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                try {
                    JSONArray jsonArray = response.getJSONArray("content");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        comments.add(new Comment(jsonObject.getString("name"), jsonObject.getString("comment"),jsonObject.getString("profileimage"), jsonObject.getString("timestamp")));
                    }
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
                Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_LONG).show();
            }
        });

    }

    public static class CommentViewHolder extends RecyclerView.ViewHolder {
        protected TextView commenter;
        protected TextView comment;
        protected TextView timestamp;
        protected ImageView profileImageView;

        public CommentViewHolder(View v) {
            super(v);
            commenter = ViewHolder.get(v, R.id.tvName);
            comment = ViewHolder.get(v, R.id.tvComment);
            timestamp = ViewHolder.get(itemView, R.id.tv_time_stamp);
            profileImageView = ViewHolder.get(itemView,R.id.profile_image);
        }
    }
}
