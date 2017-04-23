package com.banglacricket.bdcricketteam.activity;

import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
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
import com.banglacricket.bdcricketteam.util.ViewHolder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

/**
 * @author Ripon
 */
@ContentView(R.layout.news)
public class QuotesListActivity extends CommonActivity {

    @InjectView(R.id.recycler_view)
    RecyclerView recyclerView;
    @InjectView(R.id.adViewNews)
    AdView adView;

    @Inject
    ArrayList<CricketNews> quotes;

    @Inject
    NetworkService networkService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AdRequest adRequest = new AdRequest.Builder().addTestDevice(Constants.ONE_PLUS_TEST_DEVICE)
                .addTestDevice(Constants.XIAOMI_TEST_DEVICE).build();
        adView.loadAd(adRequest);

        recyclerView.setAdapter(new BasicListAdapter<CricketNews, QuotesViewHolder>(quotes) {
            @Override
            public QuotesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_quotes, parent, false);
                return new QuotesViewHolder(view);
            }

            @Override
            public void onBindViewHolder(QuotesViewHolder holder, int position) {
                holder.headline.setText(quotes.get(position).getTitle());
                holder.author.setText(Html.fromHtml(quotes.get(position).getSourceBangla()));

                holder.time.setVisibility(View.GONE);
                Picasso.with(QuotesListActivity.this)
                        .load(quotes.get(position).getImageUrl())
                        .placeholder(R.drawable.default_image)
                        .into(holder.circleImageView);
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        networkService.fetchQuotes(new DefaultMessageHandler(this, true) {
            @Override
            public void onSuccess(Message msg) {
                String string = (String) msg.obj;
                try {
                    JSONObject response = new JSONObject(string);
                    JSONArray jsonArray = response.getJSONArray("quotes");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        CricketNews cricketNews = new CricketNews("", jsonObject.getString("image"),
                                "", jsonObject.getString("comment"),
                                "", "banglanews", jsonObject.getString("context"));
                        quotes.add(cricketNews);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                recyclerView.getAdapter().notifyDataSetChanged();
            }
        });
    }

    private static class QuotesViewHolder extends RecyclerView.ViewHolder {
        protected TextView headline;
        protected TextView author;
        protected TextView time;
        protected ImageView circleImageView;

        public QuotesViewHolder(View itemView) {
            super(itemView);

            headline = ViewHolder.get(itemView, R.id.tv_headline);
            author = ViewHolder.get(itemView, R.id.tv_author);
            time = ViewHolder.get(itemView, R.id.tv_times_ago);
            circleImageView = ViewHolder.get(itemView, R.id.civ_news_thumb);
        }
    }
}
