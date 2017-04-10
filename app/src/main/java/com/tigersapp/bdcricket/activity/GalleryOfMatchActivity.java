package com.tigersapp.bdcricket.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
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
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.gson.Gson;
import com.google.inject.Inject;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;
import com.tigersapp.bdcricket.R;
import com.tigersapp.bdcricket.adapter.BasicListAdapter;
import com.tigersapp.bdcricket.model.Gallery;
import com.tigersapp.bdcricket.model.ImageHolder;
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
 * @author ripon
 */
@ContentView(R.layout.fixture)
public class GalleryOfMatchActivity extends RoboAppCompatActivity {

    @InjectView(R.id.adViewFixture)
    AdView adView;

    @InjectView(R.id.recycler_view)
    RecyclerView recyclerView;

    @Inject
    ArrayList<Gallery> galleries;

    @Inject
    Gson gson;

    @Inject
    private ImageHolder imageHolder;

    Dialogs dialogs;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        dialogs = new Dialogs(this);
        recyclerView.setAdapter(new BasicListAdapter<Gallery, GalleryMatchViewHolder>(galleries) {
            @Override
            public GalleryMatchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_troll_posts, parent, false);
                return new GalleryMatchViewHolder(view);
            }

            @Override
            public void onBindViewHolder(GalleryMatchViewHolder holder, final int position) {
                holder.courtesy.setVisibility(View.GONE);
                holder.title.setText(galleries.get(position).getName());
                Picasso.with(GalleryOfMatchActivity.this)
                        .load((galleries.get(position).getImg()))
                        .placeholder(R.drawable.default_image)
                        .into(holder.imageView);
            }

            @Override
            public int getItemViewType(int position) {
                return position;
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        String url = getIntent().getStringExtra("url");
        Log.d(Constants.TAG, url);

        dialogs.showDialog();

        FetchFromWeb.get(url, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                dialogs.dismissDialog();
                try {
                    JSONArray jsonArray = response.getJSONArray("Image Details");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject obj = jsonArray.getJSONObject(i);
                        Gallery gallery = new Gallery(obj.getString("cap"), "", "", response.getJSONObject("Event Details").getString("base") + obj.getString("urlLarge"));
                        galleries.add(gallery);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                recyclerView.getAdapter().notifyDataSetChanged();
                Log.d(Constants.TAG, response.toString());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                dialogs.dismissDialog();
                Toast.makeText(GalleryOfMatchActivity.this, "Failed", Toast.LENGTH_LONG).show();
            }
        });

        AdRequest adRequest = new AdRequest.Builder().addTestDevice(Constants.ONE_PLUS_TEST_DEVICE)
                .addTestDevice(Constants.XIAOMI_TEST_DEVICE).build();
        adView.loadAd(adRequest);
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

    private static class GalleryMatchViewHolder extends RecyclerView.ViewHolder {
        protected ImageView imageView;
        protected TextView title;
        protected TextView courtesy;
        protected LinearLayout linearLayout;

        public GalleryMatchViewHolder(View itemView) {
            super(itemView);
            imageView = ViewHolder.get(itemView, R.id.img_troll_post);
            title = ViewHolder.get(itemView, R.id.tv_troll_post_title);
            courtesy = ViewHolder.get(itemView, R.id.tv_troll_post_courtesy);
            linearLayout = ViewHolder.get(itemView, R.id.image_container);
        }
    }
}
