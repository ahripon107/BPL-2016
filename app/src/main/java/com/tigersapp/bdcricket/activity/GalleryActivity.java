package com.tigersapp.bdcricket.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;
import com.tigersapp.bdcricket.R;
import com.tigersapp.bdcricket.adapter.BasicListAdapter;
import com.tigersapp.bdcricket.model.Gallery;
import com.tigersapp.bdcricket.util.Constants;
import com.tigersapp.bdcricket.util.Dialogs;
import com.tigersapp.bdcricket.util.FetchFromWeb;
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

public class GalleryActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<Gallery> galleries;
    Dialogs dialogs;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setContentView(R.layout.playersfragment);
        galleries = new ArrayList<>();
        dialogs = new Dialogs(this);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        String galleryUrl = "http://mapps.cricbuzz.com/cricbuzz-android/gallery/";
        Log.d(Constants.TAG, galleryUrl);

        dialogs.showDialog();

        FetchFromWeb.get(galleryUrl,null,new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                dialogs.dismissDialog();
                try {
                    JSONArray jsonArray = response.getJSONArray("Match Details");
                    for (int i=0;i<jsonArray.length();i++) {
                        JSONObject obj = jsonArray.getJSONObject(i);
                        galleries.add(new Gallery(obj.getString("name"),response.getJSONObject("Index").getString("match")+obj.getString("url"),obj.getString("dt"),response.getJSONObject("Index").getString("img")+obj.getString("img")));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                recyclerView.getAdapter().notifyDataSetChanged();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                dialogs.dismissDialog();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                dialogs.dismissDialog();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                dialogs.dismissDialog();
            }
        });

        recyclerView.setAdapter(new BasicListAdapter<Gallery,GalleryViewHolder>(galleries) {

            @Override
            public GalleryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.singlenews,parent,false);
                return new GalleryViewHolder(view);
            }

            @Override
            public void onBindViewHolder(GalleryViewHolder holder, int position) {
                final Gallery gallery = galleries.get(position);
                holder.matchTitle.setText(gallery.getName());
                Picasso.with(GalleryActivity.this)
                        .load((gallery.getImg()))
                        .placeholder(R.drawable.default_image)
                        .into(holder.imageView);
                holder.matchDate.setText(gallery.getDate());
                holder.author.setVisibility(View.GONE);

                holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(GalleryActivity.this,GalleryOfMatchActivity.class);
                        intent.putExtra("url",gallery.getUrl());
                        startActivity(intent);
                    }
                });
            }

            @Override
            public int getItemViewType(int position) {
                return position;
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
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

    private static class GalleryViewHolder extends RecyclerView.ViewHolder {

        protected TextView matchTitle;
        protected ImageView imageView;
        protected TextView matchDate;
        protected TextView author;
        protected LinearLayout linearLayout;

        public GalleryViewHolder(View itemView) {
            super(itemView);

            matchTitle = ViewHolder.get(itemView,R.id.tv_headline);
            imageView = ViewHolder.get(itemView,R.id.civ_news_thumb);
            matchDate = ViewHolder.get(itemView,R.id.tv_times_ago);
            author = ViewHolder.get(itemView,R.id.tv_author);
            linearLayout = ViewHolder.get(itemView,R.id.news_item_container);
        }
    }
}
