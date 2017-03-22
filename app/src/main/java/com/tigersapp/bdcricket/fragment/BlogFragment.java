package com.tigersapp.bdcricket.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;
import com.tigersapp.bdcricket.R;
import com.tigersapp.bdcricket.activity.BlogDetailsActivity;
import com.tigersapp.bdcricket.adapter.BasicListAdapter;
import com.tigersapp.bdcricket.model.BlogPost;
import com.tigersapp.bdcricket.util.Constants;
import com.tigersapp.bdcricket.util.FetchFromWeb;
import com.tigersapp.bdcricket.util.ViewHolder;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import roboguice.fragment.RoboFragment;
import roboguice.inject.InjectView;

/**
 * @author Ripon
 */

public class BlogFragment extends Fragment {

    private RecyclerView recyclerView;

    private FloatingActionButton fabNewBlog;

    private ArrayList<BlogPost> blogPosts;


    private Typeface tf;

    private BasicListAdapter<BlogPost, BlogViewHolder> tourBlogListAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        blogPosts = new ArrayList<>();
        tourBlogListAdapter = new BasicListAdapter<BlogPost, BlogViewHolder>(blogPosts) {
            @Override
            public BlogViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_blog, parent, false);
                return new BlogViewHolder(view);
            }

            @Override
            public void onBindViewHolder(BlogViewHolder holder, int position) {
                final BlogPost blogPost = blogPosts.get(position);
                holder.name.setTypeface(tf);
                holder.title.setTypeface(tf);
                holder.tags.setTypeface(tf);
                holder.name.setText("লিখেছেন: " + blogPost.getName());
                holder.title.setText(blogPost.getTitle());
                holder.tags.setText(blogPost.getReadtimes()+"  বার পঠিত");
                holder.timestamp.setText(Constants.getTimeAgo(Long.parseLong(blogPost.getTimestamp())));

                Picasso.with(getContext()).load(blogPost.getImage()).placeholder(R.drawable.default_image).into(holder.imageView);

                holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(getActivity(), BlogDetailsActivity.class);
                        i.putExtra("post", blogPost);
                        startActivity(i);
                    }
                });
            }
        };

        loadPosts();
        EventBus.getDefault().register(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.list_with_fab,container,false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tf = Typeface.createFromAsset(getActivity().getAssets(), Constants.SOLAIMAN_LIPI_FONT);
        recyclerView = (RecyclerView) view.findViewById(R.id.list);
        fabNewBlog = (FloatingActionButton) view.findViewById(R.id.fab);
        recyclerView.setAdapter(tourBlogListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

//        fabNewBlog.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(getActivity(), NewTourBlogActivity.class);
//                startActivity(i);
//            }
//        });
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(BlogPost blogPost) {
        loadPosts();
    }


    @Override
    public void onDestroy() {
        Log.d(Constants.TAG, "onDestroy: ");
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    public void loadPosts() {
        RequestParams requestParams = new RequestParams();
        requestParams.add(Constants.KEY,Constants.KEY_VALUE);

        FetchFromWeb.get("http://apisea.xyz/TourBangla/apis/v3/FetchTourBlogs.php", requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                try {
                    blogPosts.clear();

                    JSONArray jsonArray = response.getJSONArray("content");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        Gson gson = new Gson();
                        BlogPost blogPost = gson.fromJson(String.valueOf(jsonObject), BlogPost.class);
                        blogPosts.add(blogPost);
                    }
                    recyclerView.getAdapter().notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Toast.makeText(getContext(), "Failed", Toast.LENGTH_LONG).show();
            }
        });
    }

    private static class BlogViewHolder extends RecyclerView.ViewHolder {
        protected ImageView imageView;
        protected TextView title, name, tags;
        protected TextView timestamp;
        protected LinearLayout linearLayout;
        protected CardView cardView;

        public BlogViewHolder(View itemView) {
            super(itemView);
            imageView = ViewHolder.get(itemView, R.id.imgBlogPost);
            title = ViewHolder.get(itemView, R.id.txtPostTitle);
            name = ViewHolder.get(itemView, R.id.txtPostWriter);
            tags = ViewHolder.get(itemView, R.id.txtPostTags);
            timestamp = ViewHolder.get(itemView, R.id.tv_blog_time_stamp);
            linearLayout = ViewHolder.get(itemView, R.id.blogPostContainer);
            cardView = ViewHolder.get(itemView,R.id.card_bl);
        }
    }



    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}
