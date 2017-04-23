package com.banglacricket.bdcricketteam.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.inject.Inject;
import com.squareup.picasso.Picasso;
import com.banglacricket.bdcricketteam.R;
import com.banglacricket.bdcricketteam.adapter.BasicListAdapter;
import com.banglacricket.bdcricketteam.model.CricketNews;
import com.banglacricket.bdcricketteam.util.Constants;
import com.banglacricket.bdcricketteam.util.DefaultMessageHandler;
import com.banglacricket.bdcricketteam.util.NetworkService;
import com.banglacricket.bdcricketteam.util.RecyclerItemClickListener;
import com.banglacricket.bdcricketteam.util.ViewHolder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

/**
 * @author ripon
 */
@ContentView(R.layout.news)
public class CricketNewsListActivity extends CommonActivity {

    @InjectView(R.id.recycler_view)
    private RecyclerView recyclerView;

    @InjectView(R.id.adViewNews)
    private AdView adView;

    @Inject
    private ArrayList<CricketNews> cricketNewses;

    @Inject
    private NetworkService networkService;

    private Typeface typeface;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        typeface = Typeface.createFromAsset(getAssets(), Constants.SOLAIMAN_LIPI_FONT);

        recyclerView.setAdapter(new BasicListAdapter<CricketNews, NewsViewHolder>(cricketNewses) {
            @Override
            public NewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_quotes, parent, false);
                return new NewsViewHolder(view);
            }

            @Override
            public void onBindViewHolder(NewsViewHolder holder, final int position) {

                holder.headline.setTypeface(typeface);
                holder.author.setTypeface(typeface);
                holder.headline.setText(cricketNewses.get(position).getTitle());
                holder.author.setVisibility(View.GONE);

                holder.time.setText(cricketNewses.get(position).getDate());
                Picasso.with(CricketNewsListActivity.this)
                        .load(cricketNewses.get(position).getImageUrl())
                        .placeholder(R.drawable.default_image)
                        .into(holder.circleImageView);
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Intent intent = new Intent(CricketNewsListActivity.this, NewsDetailsActivity.class);
                        intent.putExtra(NewsDetailsActivity.EXTRA_NEWS_OBJECT, cricketNewses.get(position));
                        CricketNewsListActivity.this.startActivity(intent);
                    }
                })
        );

        networkService.fetchBanglanews(new DefaultMessageHandler(this, true) {
            @Override
            public void onSuccess(Message msg) {
                String string = (String) msg.obj;
                try {
                    JSONArray response = new JSONArray(string);
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject jsonObject = response.getJSONObject(i);
                        CricketNews cricketNews = new CricketNews(jsonObject.getString("ContentID"), "http://www.banglanews24.com/media/imgAll/" + jsonObject.getString("ImageSMPath"),
                                "http://www.banglanews24.com/api/details/" + jsonObject.getString("ContentID"), jsonObject.getString("ContentHeading"),
                                jsonObject.getString("updated_at"), "banglanews", "");
                        cricketNewses.add(cricketNews);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Collections.sort(cricketNewses);
                recyclerView.getAdapter().notifyDataSetChanged();
            }
        });

        networkService.fetchBdProtidin(new DefaultMessageHandler(this) {
            @Override
            public void onSuccess(Message msg) {
                String string = (String) msg.obj;
                try {
                    JSONArray response = new JSONArray(string);
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject jsonObject = response.getJSONObject(i);
                        CricketNews cricketNews = new CricketNews(jsonObject.getString("item_id"), jsonObject.getString("featured_image"),
                                jsonObject.getString("main_news_url"), jsonObject.getString("title"),
                                jsonObject.getString("datetime"), "bdprotidin", "");
                        cricketNewses.add(cricketNews);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Collections.sort(cricketNewses);
                recyclerView.getAdapter().notifyDataSetChanged();
            }
        });

        networkService.fetchRisingBd(new DefaultMessageHandler(this) {
            @Override
            public void onSuccess(Message msg) {
                String string = (String) msg.obj;
                try {
                    JSONArray response = new JSONArray(string);
                    response = response.getJSONObject(0).getJSONArray("data");
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject jsonObject = response.getJSONObject(i);
                        CricketNews cricketNews = new CricketNews(jsonObject.getString("NewsId"), "http://cdn.risingbd.com/assets/" + jsonObject.getString("ImageSMPath"),
                                "http://api.risingbd.com/index.php/News/Details?id=" + jsonObject.getString("NewsId"), jsonObject.getString("NewsHeading"),
                                jsonObject.getString("DateTimeInserted"), "risingbd", "");
                        cricketNewses.add(cricketNews);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Collections.sort(cricketNewses);
                recyclerView.getAdapter().notifyDataSetChanged();
            }
        });

        AdRequest adRequest = new AdRequest.Builder().addTestDevice(Constants.ONE_PLUS_TEST_DEVICE)
                .addTestDevice(Constants.XIAOMI_TEST_DEVICE).build();
        adView.loadAd(adRequest);
    }

    private static class NewsViewHolder extends RecyclerView.ViewHolder {
        protected TextView headline;
        protected TextView author;
        protected TextView time;
        protected ImageView circleImageView;

        public NewsViewHolder(View itemView) {
            super(itemView);

            headline = ViewHolder.get(itemView, R.id.tv_headline);
            author = ViewHolder.get(itemView, R.id.tv_author);
            time = ViewHolder.get(itemView, R.id.tv_times_ago);
            circleImageView = ViewHolder.get(itemView, R.id.civ_news_thumb);
        }
    }
}
