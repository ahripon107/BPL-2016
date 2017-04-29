package com.allgames.sportslab.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.gson.Gson;
import com.google.inject.Inject;
import com.allgames.sportslab.R;
import com.allgames.sportslab.adapter.BasicListAdapter;
import com.allgames.sportslab.model.LivestreamAndHighlights;
import com.allgames.sportslab.util.Constants;
import com.allgames.sportslab.util.DefaultMessageHandler;
import com.allgames.sportslab.util.DividerItemDecoration;
import com.allgames.sportslab.util.NetworkService;
import com.allgames.sportslab.util.RoboAppCompatActivity;
import com.allgames.sportslab.util.ViewHolder;
import com.allgames.sportslab.videoplayers.FrameStream;
import com.allgames.sportslab.videoplayers.HighlightsVids;
import com.allgames.sportslab.videoplayers.LiveStreamView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

/**
 * @author ripon
 */
@ContentView(R.layout.highlights)
public class Highlights extends RoboAppCompatActivity {

    @InjectView(R.id.lvHighlights)
    RecyclerView recyclerView;

    @InjectView(R.id.adViewHighlights)
    AdView adView;

    @InjectView(R.id.empty_view)
    TextView emptyView;

    ArrayList<LivestreamAndHighlights> objects;
    InterstitialAd mInterstitialAd;

    @Inject
    Gson gson;

    @Inject
    NetworkService networkService;
    String cause, url;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        objects = new ArrayList<>();
        cause = getIntent().getStringExtra("cause");
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-9201945236996508/1636846478");

        AdRequest adRequestInterstitial = new AdRequest.Builder()
                .addTestDevice(Constants.ONE_PLUS_TEST_DEVICE).addTestDevice(Constants.XIAOMI_TEST_DEVICE)
                .build();
        mInterstitialAd.loadAd(adRequestInterstitial);

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                }
            }
        });

        recyclerView.setAdapter(new BasicListAdapter<LivestreamAndHighlights, HighlightsViewHolder>(objects) {
            @Override
            public HighlightsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_livestream, parent, false);
                return new HighlightsViewHolder(view);
            }

            @Override
            public void onBindViewHolder(HighlightsViewHolder holder, final int position) {
                holder.teamName.setText(objects.get(position).getTitle());

                if (cause.equals("highlights") || cause.equals("highlightsfootball")) {
                    holder.watchLive.setText("Watch");
                } else {
                    holder.watchLive.setText("Watch Live");
                }

                holder.watchLive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (objects.get(position).getType().equals("youtube")) {
                            Intent intent = new Intent(Highlights.this, HighlightsVids.class);
                            intent.putExtra("url", objects.get(position).getUrl());
                            startActivity(intent);
                        } else if (objects.get(position).getType().equals("other")) {
                            Intent intent = new Intent(Highlights.this, FrameStream.class);
                            intent.putExtra("url", objects.get(position).getUrl());
                            startActivity(intent);
                        } else if (objects.get(position).getType().equals("m3u8")) {
                            Intent intent = new Intent(Highlights.this, LiveStreamView.class);
                            intent.putExtra("url", objects.get(position).getUrl());
                            startActivity(intent);
                        }
                    }
                });
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this));

        if (cause.equals("livestream")) {
            url = "http://apisea.xyz/Cricket/apis/v1/fetchLiveStrams.php?key=bl905577";
            fetchFromWeb(url);
            setTitle("ক্রিকেট লাইভ");
        } else if (cause.equals("livestreamfootball")) {
            url = "http://apisea.xyz/Cricket/apis/v1/fetchLiveStramsFootball.php?key=bl905577";
            fetchFromWeb(url);
            setTitle("ফুটবল লাইভ");
        } else if (cause.equals("highlights")) {
            url = "http://apisea.xyz/Cricket/apis/v1/fetchHighlights.php?key=bl905577";
            fetchFromWeb(url);
            setTitle("হাইলাইটস");
        } else {
            url = "http://apisea.xyz/Cricket/apis/v1/fetchHighlightsFootball.php?key=bl905577";
            fetchFromWeb(url);
            setTitle("হাইলাইটস");
        }
        AdRequest adRequest = new AdRequest.Builder().addTestDevice(Constants.ONE_PLUS_TEST_DEVICE)
                .addTestDevice(Constants.XIAOMI_TEST_DEVICE).build();
        adView.loadAd(adRequest);
    }

    private static class HighlightsViewHolder extends RecyclerView.ViewHolder {
        protected TextView teamName;
        protected LinearLayout linearLayout;
        protected Button watchLive;

        public HighlightsViewHolder(View itemView) {
            super(itemView);
            teamName = ViewHolder.get(itemView, R.id.tv_link_title);
            linearLayout = ViewHolder.get(itemView, R.id.linear_layout);
            watchLive = ViewHolder.get(itemView, R.id.btn_watch);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.refresh_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_load:
                fetchFromWeb(url);
                return true;
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void fetchFromWeb(String url) {

        networkService.fetchHighlights(url, new DefaultMessageHandler(this, true) {
            @Override
            public void onSuccess(Message msg) {
                objects.clear();
                String string = (String) msg.obj;
                try {
                    JSONObject response = new JSONObject(string);
                    JSONArray jsonArray = response.getJSONArray("content");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject obj = jsonArray.getJSONObject(i);
                        LivestreamAndHighlights livestreamAndHighlights = gson.fromJson(String.valueOf(obj), LivestreamAndHighlights.class);
                        objects.add(livestreamAndHighlights);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                recyclerView.getAdapter().notifyDataSetChanged();

                if (objects.size() > 0) {
                    emptyView.setVisibility(View.GONE);
                } else {
                    emptyView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure() {
                emptyView.setVisibility(View.VISIBLE);
            }
        });
    }
}
