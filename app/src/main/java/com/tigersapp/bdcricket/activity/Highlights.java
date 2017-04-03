package com.tigersapp.bdcricket.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.gson.Gson;
import com.google.inject.Inject;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.tigersapp.bdcricket.R;
import com.tigersapp.bdcricket.adapter.BasicListAdapter;
import com.tigersapp.bdcricket.model.LivestreamAndHighlights;
import com.tigersapp.bdcricket.util.Constants;
import com.tigersapp.bdcricket.util.Dialogs;
import com.tigersapp.bdcricket.util.DividerItemDecoration;
import com.tigersapp.bdcricket.util.FetchFromWeb;
import com.tigersapp.bdcricket.util.RoboAppCompatActivity;
import com.tigersapp.bdcricket.util.ViewHolder;
import com.tigersapp.bdcricket.videoplayers.DMPlayerActivity;
import com.tigersapp.bdcricket.videoplayers.FrameStream;
import com.tigersapp.bdcricket.videoplayers.HighlightsVids;
import com.tigersapp.bdcricket.videoplayers.LiveStreamView;

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
@ContentView(R.layout.highlights)
public class Highlights extends RoboAppCompatActivity {

    @InjectView(R.id.lvHighlights)
    RecyclerView recyclerView;

    @InjectView(R.id.adViewHighlights)
    AdView adView;

    ArrayList<LivestreamAndHighlights> objects;
    InterstitialAd mInterstitialAd;

    @Inject
    Gson gson;
    String cause, url;

    Dialogs dialogs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        objects = new ArrayList<>();
        cause = getIntent().getStringExtra("cause");
        dialogs = new Dialogs(this);
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-9201945236996508/3198106475");

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

                if (cause.equals("highlights")) {
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
                        } else if (objects.get(position).getType().equals("dmplayer")) {
                            Intent intent = new Intent(Highlights.this, DMPlayerActivity.class);
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
            setTitle("Live Streaming");
        } else {
            url = "http://apisea.xyz/Cricket/apis/v1/fetchHighlights.php?key=bl905577";
            fetchFromWeb(url);
            setTitle("Highlights");
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

        dialogs.showDialog();

        FetchFromWeb.get(url, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                dialogs.dismissDialog();
                objects.clear();

                try {
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
                Log.d(Constants.TAG, response.toString());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                dialogs.dismissDialog();
                Toast.makeText(Highlights.this, "Failed", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                dialogs.dismissDialog();
                Toast.makeText(Highlights.this, "Failed", Toast.LENGTH_LONG).show();
            }
        });
    }
}
