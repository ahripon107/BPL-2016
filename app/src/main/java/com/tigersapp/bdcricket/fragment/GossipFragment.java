package com.tigersapp.bdcricket.fragment;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.Profile;
import com.google.inject.Inject;
import com.squareup.picasso.Picasso;
import com.tigersapp.bdcricket.R;
import com.tigersapp.bdcricket.activity.LoginActivity;
import com.tigersapp.bdcricket.adapter.BasicListAdapter;
import com.tigersapp.bdcricket.model.Comment;
import com.tigersapp.bdcricket.util.Constants;
import com.tigersapp.bdcricket.util.DefaultMessageHandler;
import com.tigersapp.bdcricket.util.NetworkService;
import com.tigersapp.bdcricket.util.ViewHolder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import roboguice.fragment.RoboFragment;
import roboguice.inject.InjectView;

/**
 * @author Ripon
 */

public class GossipFragment extends RoboFragment implements SwipeRefreshLayout.OnRefreshListener {

    @InjectView(R.id.rvComments)
    private RecyclerView recyclerView;

    @InjectView(R.id.btnSubmitComment)
    private ImageButton sendComment;

    @InjectView(R.id.commentBody)
    private EditText commentBody;

    @InjectView(R.id.refresh)
    private SwipeRefreshLayout swipeRefreshLayout;

    @Inject
    private NetworkService networkService;

    @Inject
    private ArrayList<Comment> comments;

    private Profile profile;
    private Typeface tf;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser) {
            swipeRefreshLayout.setRefreshing(true);
            fetchContents();
        }
    }

    private void fetchContents() {
        networkService.fetchComments("gossip" + getArguments().getString("liveMatchID"), new DefaultMessageHandler(getContext(), false) {
            @Override
            public void onSuccess(Message msg) {
                String string = (String) msg.obj;
                swipeRefreshLayout.setRefreshing(false);
                try {
                    JSONObject response = new JSONObject(string);
                    populateList(response);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure() {
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_news_comment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView.setHasFixedSize(true);

        swipeRefreshLayout.setOnRefreshListener(this);

        tf = Typeface.createFromAsset(getActivity().getAssets(), Constants.SOLAIMAN_LIPI_FONT);
        recyclerView.setAdapter(new BasicListAdapter<Comment, GossipViewHolder>(comments) {
            @Override
            public GossipViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.singlecomment, parent, false);
                return new GossipViewHolder(v);
            }

            @Override
            public void onBindViewHolder(GossipViewHolder holder, int position) {
                holder.comment.setTypeface(tf);
                holder.commenter.setTypeface(tf);
                holder.commenter.setText("কমেন্ট করেছেন:  " + comments.get(position).getName());
                holder.comment.setText(comments.get(position).getComment());
                if (!comments.get(position).getProfileimage().equals("")) {
                    Picasso.with(getContext()).load(comments.get(position).getProfileimage()).into(holder.imageView);
                }
                holder.timestamp.setText(Constants.getTimeAgo(Long.parseLong(comments.get(position).getTimestamp())));
            }

        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

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
                } else {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    getActivity().startActivity(intent);
                }
            }
        });
    }

    public void publishComment(final String comment) {

        networkService.insertComment(comment, profile, "gossip" + getArguments().getString("liveMatchID"), new DefaultMessageHandler(getContext(), true) {
            @Override
            public void onSuccess(Message msg) {
                String string = (String) msg.obj;
                try {
                    JSONObject response = new JSONObject(string);

                    if (response.getString("msg").equals("Successful")) {
                        Toast.makeText(getContext(), "Comment successfully posted", Toast.LENGTH_LONG).show();
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
        });
    }

    public void populateList(JSONObject response) {
        try {
            if (response.getString("msg").equals("Successful")) {
                comments.clear();
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
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);
        fetchContents();

    }

    public static class GossipViewHolder extends RecyclerView.ViewHolder {
        protected TextView commenter;
        protected TextView comment;
        protected TextView timestamp;
        protected ImageView imageView;

        public GossipViewHolder(View v) {
            super(v);
            commenter = ViewHolder.get(v, R.id.tvName);
            comment = ViewHolder.get(v, R.id.tvComment);
            timestamp = ViewHolder.get(itemView, R.id.tv_time_stamp);
            imageView = ViewHolder.get(v, R.id.profile_image);
        }
    }
}
