package com.tigersapp.bdcricket.fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
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
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.tigersapp.bdcricket.R;
import com.tigersapp.bdcricket.activity.CricketNewsListActivity;
import com.tigersapp.bdcricket.activity.MainActivity;
import com.tigersapp.bdcricket.adapter.BasicListAdapter;
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
                Toast.makeText(getContext(), "Failed", Toast.LENGTH_LONG).show();
            }
        });


        Button sendComment = (Button) view.findViewById(R.id.btnSubmitComment);

        sendComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                View promptsView = LayoutInflater.from(getContext()).inflate(R.layout.addnewcomment, null, false);
                final EditText writeComment = (EditText) promptsView.findViewById(R.id.etYourComment);
                final EditText yourName = (EditText) promptsView.findViewById(R.id.etYourName);
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setView(promptsView);
                builder.setTitle("মন্তব্য").setPositiveButton("SUBMIT", null).setNegativeButton("CANCEL", null);

                final AlertDialog alertDialog = builder.create();
                alertDialog.show();

                Button okButton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
                okButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (Validator.validateNotEmpty(yourName, "Required") && Validator.validateNotEmpty(writeComment, "Required")) {
                            final String comment = writeComment.getText().toString().trim();
                            final String name = yourName.getText().toString().trim();

                            final AlertDialog progressDialog = new SpotsDialog(getContext(), R.style.Custom);
                            progressDialog.show();
                            progressDialog.setCancelable(true);
                            RequestParams params = new RequestParams();

                            params.put("key", "bl905577");
                            params.put("newsid", cricketNews.getSource()+cricketNews.getId());
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
                                            Toast.makeText(getContext(), "Comment successfully posted", Toast.LENGTH_LONG).show();
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
                                    Toast.makeText(getContext(), "Failed", Toast.LENGTH_LONG).show();
                                }
                            });

                            alertDialog.dismiss();
                        }
                    }
                });
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
