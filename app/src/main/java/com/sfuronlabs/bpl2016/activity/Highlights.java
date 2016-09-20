package com.sfuronlabs.bpl2016.activity;

import android.app.AlertDialog;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.gson.Gson;
import com.google.inject.Inject;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.sfuronlabs.bpl2016.R;
import com.sfuronlabs.bpl2016.adapter.BasicListAdapter;
import com.sfuronlabs.bpl2016.model.LivestreamAndHighlights;
import com.sfuronlabs.bpl2016.util.CircleImageView;
import com.sfuronlabs.bpl2016.util.Constants;
import com.sfuronlabs.bpl2016.util.FetchFromWeb;
import com.sfuronlabs.bpl2016.util.RoboAppCompatActivity;
import com.sfuronlabs.bpl2016.util.ViewHolder;
import com.sfuronlabs.bpl2016.videoplayers.DMPlayerActivity;
import com.sfuronlabs.bpl2016.videoplayers.FrameStream;
import com.sfuronlabs.bpl2016.videoplayers.HighlightsVids;
import com.sfuronlabs.bpl2016.videoplayers.LiveStreamView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import dmax.dialog.SpotsDialog;
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

    @Inject
    Gson gson;
    String cause,url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        objects = new ArrayList<>();
        cause = getIntent().getStringExtra("cause");

        recyclerView.setAdapter(new BasicListAdapter<LivestreamAndHighlights,HighlightsViewHolder>(objects) {
            @Override
            public HighlightsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.singleteam,parent,false);
                return new HighlightsViewHolder(view);
            }

            @Override
            public void onBindViewHolder(HighlightsViewHolder holder, final int position) {
                holder.teamName.setText(objects.get(position).getTitle());
                Picasso.with(Highlights.this)
                        .load("http://img.youtube.com/vi/" + objects.get(position).getUrl() + "/0.jpg")
                        .placeholder(R.drawable.default_image)
                        .into(holder.circleImageView);
                holder.linearLayout.setOnClickListener(new View.OnClickListener() {
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
        protected CircleImageView circleImageView;
        protected TextView teamName;
        protected LinearLayout linearLayout;
        public HighlightsViewHolder(View itemView) {
            super(itemView);
            circleImageView = ViewHolder.get(itemView, R.id.civTeams);
            teamName = ViewHolder.get(itemView, R.id.tvTeamName);
            linearLayout = ViewHolder.get(itemView,R.id.team_name_flag_container);
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

        final AlertDialog progressDialog = new SpotsDialog(Highlights.this, R.style.Custom);
        progressDialog.show();
        progressDialog.setCancelable(true);

        FetchFromWeb.get(url, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                progressDialog.dismiss();
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
                progressDialog.dismiss();
                Toast.makeText(Highlights.this, "Failed", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                progressDialog.dismiss();
                Toast.makeText(Highlights.this, "Failed", Toast.LENGTH_LONG).show();
            }
        });
    }
}
