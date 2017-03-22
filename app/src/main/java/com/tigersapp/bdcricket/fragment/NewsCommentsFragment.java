package com.tigersapp.bdcricket.fragment;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.Profile;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.tigersapp.bdcricket.R;
import com.tigersapp.bdcricket.activity.LoginActivity;
import com.tigersapp.bdcricket.adapter.BasicListAdapter;
import com.tigersapp.bdcricket.model.Comment;
import com.tigersapp.bdcricket.model.CricketNews;
import com.tigersapp.bdcricket.util.Constants;
import com.tigersapp.bdcricket.util.Dialogs;
import com.tigersapp.bdcricket.util.FetchFromWeb;
import com.tigersapp.bdcricket.util.ViewHolder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

import static com.tigersapp.bdcricket.activity.NewsDetailsActivity.EXTRA_NEWS_OBJECT;

/**
 * @author Ripon
 */

public class NewsCommentsFragment extends Fragment {

    ArrayList<Comment> comments;
    String url;
    RecyclerView recyclerView;
    Typeface tf;
    CricketNews cricketNews;
    Dialogs dialogs;
    ImageButton sendComment;
    EditText commentBody;
    Profile profile;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_news_comment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        recyclerView = (RecyclerView) view.findViewById(R.id.rvComments);
        recyclerView.setHasFixedSize(true);
        cricketNews = (CricketNews) getActivity().getIntent().getSerializableExtra(EXTRA_NEWS_OBJECT);
        dialogs = new Dialogs(getContext());

        tf = Typeface.createFromAsset(getActivity().getAssets(), Constants.SOLAIMAN_LIPI_FONT);

        comments = new ArrayList<>();

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
                holder.commenter.setText("কমেন্ট করেছেন:  " + comments.get(position).getName());
                holder.comment.setText(comments.get(position).getComment());
                holder.timestamp.setText(Constants.getTimeAgo(Long.parseLong(comments.get(position).getTimestamp())));
            }

        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        RequestParams requestParams = new RequestParams();

        requestParams.add("key", "bl905577");
        requestParams.add("newsid", cricketNews.getSource()+cricketNews.getId());
        url = Constants.FETCH_NEWS_COMMENT_URL;

        FetchFromWeb.get(url, requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                try {
                    if (response.getString("msg").equals("Successful")) {
                        JSONArray jsonArray = response.getJSONArray("content");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            comments.add(new Comment(jsonObject.getString("name"), jsonObject.getString("comment"),jsonObject.getString("profileimage"), jsonObject.getString("timestamp")));
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
                Toast.makeText(getContext(), "Failed", Toast.LENGTH_LONG).show();
            }
        });


        sendComment = (ImageButton) view.findViewById(R.id.btnSubmitComment);
        commentBody = (EditText) view.findViewById(R.id.commentBody);

        sendComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profile = Profile.getCurrentProfile();
                if (profile != null) {
                    String comment = commentBody.getText().toString().trim();
                    if (!comment.equals("")) {
                        publishComment(comment);
                        commentBody.getText().clear();
                    }
                }
                else {
                    Intent intent = new Intent(getActivity(),LoginActivity.class);
                    getActivity().startActivity(intent);
                }
            }
        });
    }

    public void publishComment(final String comment) {
        dialogs.showDialog();
        RequestParams params = new RequestParams();

        params.put("key", "bl905577");
        params.put("newsid", cricketNews.getSource()+cricketNews.getId());
        params.put("name", profile.getName());
        params.put("comment", comment);
        params.put("profileimage", profile.getProfilePictureUri(50,50).toString());
        params.put("timestamp", System.currentTimeMillis() + "");

        url = Constants.INSERT_NEWS_COMMENT_URL;

        FetchFromWeb.post(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                dialogs.dismissDialog();
                try {
                    if (response.getString("msg").equals("Successful")) {
                        Toast.makeText(getContext(), "Comment successfully posted", Toast.LENGTH_LONG).show();
                        comments.add(new Comment(profile.getName(), comment,profile.getProfilePictureUri(50,50).toString(), System.currentTimeMillis() + ""));
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
                Toast.makeText(getContext(), "Failed", Toast.LENGTH_LONG).show();
            }
        });
    }

    public static class CommentViewHolder extends RecyclerView.ViewHolder {
        protected TextView commenter;
        protected TextView comment;
        protected TextView timestamp;

        public CommentViewHolder(View v) {
            super(v);
            commenter = ViewHolder.get(v, R.id.tvName);
            comment = ViewHolder.get(v, R.id.tvComment);
            timestamp = ViewHolder.get(itemView, R.id.tv_time_stamp);
        }
    }
}
