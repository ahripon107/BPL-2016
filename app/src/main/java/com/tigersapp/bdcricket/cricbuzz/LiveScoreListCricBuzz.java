package com.tigersapp.bdcricket.cricbuzz;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.inject.Inject;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;
import com.tigersapp.bdcricket.R;
import com.tigersapp.bdcricket.activity.ActivityMatchDetails;
import com.tigersapp.bdcricket.adapter.BasicListAdapter;
import com.tigersapp.bdcricket.model.Match;
import com.tigersapp.bdcricket.util.CircleImageView;
import com.tigersapp.bdcricket.util.Constants;
import com.tigersapp.bdcricket.util.FetchFromWeb;
import com.tigersapp.bdcricket.util.RecyclerItemClickListener;
import com.tigersapp.bdcricket.util.RoboAppCompatActivity;
import com.tigersapp.bdcricket.util.ViewHolder;

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
@ContentView(R.layout.activity_live_score)
public class LiveScoreListCricBuzz extends RoboAppCompatActivity {

    @InjectView(R.id.rv_live_score)
    RecyclerView recyclerView;

    @Inject
    ArrayList<Match> datas;

    @InjectView(R.id.adViewLiveScoreList)
    AdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Live Score");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView.setAdapter(new BasicListAdapter<Match, LiveScoreListCricBuzz.LiveScoreViewHolder>(datas) {
            @Override
            public LiveScoreListCricBuzz.LiveScoreViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_live_score, parent, false);
                return new LiveScoreListCricBuzz.LiveScoreViewHolder(view);
            }

            @Override
            public void onBindViewHolder(LiveScoreListCricBuzz.LiveScoreViewHolder holder, final int position) {
                Picasso.with(LiveScoreListCricBuzz.this)
                        .load(Constants.resolveLogo(datas.get(position).getTeam1()))
                        .placeholder(R.drawable.default_image)
                        .into(holder.imgteam1);

                Picasso.with(LiveScoreListCricBuzz.this)
                        .load(Constants.resolveLogo(datas.get(position).getTeam2()))
                        .placeholder(R.drawable.default_image)
                        .into(holder.imgteam2);

                holder.textteam1.setText(datas.get(position).getTeam1());
                holder.textteam2.setText(datas.get(position).getTeam2());
                holder.venue.setText(Html.fromHtml(datas.get(position).getVenue()));
                holder.time.setText(datas.get(position).getTime());
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Intent intent = new Intent(LiveScoreListCricBuzz.this, ActivityMatchDetails.class);
                        intent.putExtra("matchID", datas.get(position).getMatchId());
                        startActivity(intent);
                    }
                })
        );

        String url = "http://cricinfo-mukki.rhcloud.com/api/match/live";
        Log.d(Constants.TAG, url);

        final AlertDialog progressDialog = new SpotsDialog(LiveScoreListCricBuzz.this, R.style.Custom);
        progressDialog.show();
        progressDialog.setCancelable(true);
        FetchFromWeb.get(url, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                progressDialog.dismiss();
                try {
                    Log.d(Constants.TAG, response.toString());
                    JSONArray jsonArray = response.getJSONArray("items");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject obj = jsonArray.getJSONObject(i);

                        datas.add(new Match(obj.getJSONObject("team1").getString("teamName"), obj.getJSONObject("team2").getString("teamName"),
                                obj.getString("matchDescription"), "", "", "", obj.getString("matchId")));
                    }
                    recyclerView.getAdapter().notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                progressDialog.dismiss();
                Toast.makeText(LiveScoreListCricBuzz.this, "Failed", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                progressDialog.dismiss();
                Toast.makeText(LiveScoreListCricBuzz.this, "Failed", Toast.LENGTH_LONG).show();
            }
        });

        AdRequest adRequest = new AdRequest.Builder().addTestDevice(Constants.ONE_PLUS_TEST_DEVICE)
                .addTestDevice(Constants.XIAOMI_TEST_DEVICE).build();
        adView.loadAd(adRequest);
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

    private static class LiveScoreViewHolder extends RecyclerView.ViewHolder {
        protected CircleImageView imgteam1;
        protected CircleImageView imgteam2;
        protected TextView textteam1;
        protected TextView textteam2;
        protected TextView venue;
        protected TextView time;

        public LiveScoreViewHolder(final View itemView) {
            super(itemView);

            imgteam1 = ViewHolder.get(itemView, R.id.civTeam1);
            imgteam2 = ViewHolder.get(itemView, R.id.civTeam2);
            textteam1 = ViewHolder.get(itemView, R.id.tvTeam1);
            textteam2 = ViewHolder.get(itemView, R.id.tvTeam2);
            venue = ViewHolder.get(itemView, R.id.tvVenue);
            time = ViewHolder.get(itemView, R.id.tvTime);
        }
    }
}
