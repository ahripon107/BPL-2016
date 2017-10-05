package com.allgames.sportslab.activity;

import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.allgames.sportslab.R;
import com.allgames.sportslab.adapter.BasicListAdapter;
import com.allgames.sportslab.model.Gallery;
import com.allgames.sportslab.util.Constants;
import com.allgames.sportslab.util.DefaultMessageHandler;
import com.allgames.sportslab.util.NetworkService;
import com.allgames.sportslab.util.ViewHolder;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.inject.Inject;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * @author ripon
 */
public class GalleryOfMatchActivity extends CommonActivity {

    private AdView adView;
    private RecyclerView recyclerView;

    @Inject
    ArrayList<Gallery> galleries;

    @Inject
    private NetworkService networkService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initialize();

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

        networkService.fetchGalleryOfMatch(getIntent().getStringExtra("url"), new DefaultMessageHandler(this, true) {
            @Override
            public void onSuccess(Message msg) {
                String string = (String) msg.obj;
                try {
                    JSONObject response = new JSONObject(string);
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
            }
        });

        AdRequest adRequest = new AdRequest.Builder().addTestDevice(Constants.ONE_PLUS_TEST_DEVICE)
                .addTestDevice(Constants.XIAOMI_TEST_DEVICE).build();
        adView.loadAd(adRequest);
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

    private void initialize() {
        setContentView(R.layout.fixture);
        adView = (AdView) findViewById(R.id.adViewFixture);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
    }
}
