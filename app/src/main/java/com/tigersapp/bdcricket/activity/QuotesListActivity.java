package com.tigersapp.bdcricket.activity;

import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.inject.Inject;
import com.squareup.picasso.Picasso;
import com.tigersapp.bdcricket.R;
import com.tigersapp.bdcricket.adapter.BasicListAdapter;
import com.tigersapp.bdcricket.model.CricketNews;
import com.tigersapp.bdcricket.util.Constants;
import com.tigersapp.bdcricket.util.DefaultMessageHandler;
import com.tigersapp.bdcricket.util.Dialogs;
import com.tigersapp.bdcricket.util.NetworkService;
import com.tigersapp.bdcricket.util.RoboAppCompatActivity;
import com.tigersapp.bdcricket.util.ViewHolder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import roboguice.inject.ContentView;

/**
 * @author Ripon
 */
@ContentView(R.layout.news)
public class QuotesListActivity extends RoboAppCompatActivity {

    RecyclerView recyclerView;
    AdView adView;
    ArrayList<CricketNews> quotes;
    Dialogs dialogs;

    @Inject
    NetworkService networkService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        dialogs = new Dialogs(this);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        adView = (AdView) findViewById(R.id.adViewNews);
        quotes = new ArrayList<>();

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main, menu);
        return true;
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
}
