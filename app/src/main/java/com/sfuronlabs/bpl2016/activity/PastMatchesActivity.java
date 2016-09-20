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
import com.google.inject.Inject;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.sfuronlabs.bpl2016.R;
import com.sfuronlabs.bpl2016.adapter.BasicListAdapter;
import com.sfuronlabs.bpl2016.model.Match;
import com.sfuronlabs.bpl2016.util.CircleImageView;
import com.sfuronlabs.bpl2016.util.Constants;
import com.sfuronlabs.bpl2016.util.FetchFromWeb;
import com.sfuronlabs.bpl2016.util.RecyclerItemClickListener;
import com.sfuronlabs.bpl2016.util.RoboAppCompatActivity;
import com.sfuronlabs.bpl2016.util.ViewHolder;
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
@ContentView(R.layout.fixture)
public class PastMatchesActivity extends RoboAppCompatActivity {

    @InjectView(R.id.recycler_view)
    RecyclerView recyclerView;

    @Inject
    ArrayList<Match> data;

    @InjectView(R.id.adViewFixture)
    AdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView.setAdapter(new BasicListAdapter<Match, PastMatchesViewHolder>(data) {
            @Override
            public PastMatchesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.match, parent, false);
                return new PastMatchesViewHolder(view);
            }

            @Override
            public void onBindViewHolder(PastMatchesViewHolder holder, int position) {
                Picasso.with(PastMatchesActivity.this)
                        .load(Constants.resolveLogo(data.get(position).getTeam1()))
                        .placeholder(R.drawable.default_image)
                        .into(holder.imgteam1);

                Picasso.with(PastMatchesActivity.this)
                        .load(Constants.resolveLogo(data.get(position).getTeam2()))
                        .placeholder(R.drawable.default_image)
                        .into(holder.imgteam2);

                holder.textteam1.setText(data.get(position).getTeam1());
                holder.textteam2.setText(data.get(position).getTeam2());
                holder.venue.setText(data.get(position).getVenue());

                holder.time.setTextSize(20f);
                holder.time.setText(data.get(position).getTime());
                holder.seriesName.setText(data.get(position).getSeriesName());
                holder.matchNo.setText(data.get(position).getMatchNo());

            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, final int position) {
                        String idMatcherURL = "http://apisea.xyz/Cricket/apis/v1/fetxchCrictinfoMatchID.php";
                        Log.d(Constants.TAG, idMatcherURL);

                        final AlertDialog progressDialog = new SpotsDialog(PastMatchesActivity.this, R.style.Custom);
                        progressDialog.show();
                        progressDialog.setCancelable(true);
                        RequestParams params = new RequestParams();
                        params.add("key", "bl905577");
                        params.add("yahoo", data.get(position).getMatchId());

                        FetchFromWeb.get(idMatcherURL, params, new JsonHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                progressDialog.dismiss();
                                try {
                                    if (response.getString("msg").equals("Successful")) {
                                        String cricinfoID = response.getJSONArray("content").getJSONObject(0).getString("cricinfoID");
                                        Intent intent = new Intent(PastMatchesActivity.this, ActivityMatchDetails.class);
                                        intent.putExtra("matchID", cricinfoID);
                                        startActivity(intent);
                                    } else {
                                        Toast.makeText(PastMatchesActivity.this,"Scorecard not available",Toast.LENGTH_SHORT).show();
                                    }

                                    Log.d(Constants.TAG, response.toString());
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                                progressDialog.dismiss();
                                Toast.makeText(PastMatchesActivity.this, "Failed", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                })
        );

        String url = Constants.PAST_MATCHES_URL;
        Log.d(Constants.TAG, url);


        final AlertDialog progressDialog = new SpotsDialog(PastMatchesActivity.this, R.style.Custom);
        progressDialog.show();
        progressDialog.setCancelable(true);

        FetchFromWeb.get(url, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                progressDialog.dismiss();
                try {
                    String team1, team2, venue, time="", seriesName, matcNo,matchID;
                    response = response.getJSONObject("query").getJSONObject("results");
                    JSONArray jsonArray = response.getJSONArray("Match");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject obj = jsonArray.getJSONObject(i);
                        JSONArray array = obj.getJSONArray("Team");
                        seriesName = obj.getString("series_name");
                        matcNo = obj.getString("MatchNo");

                        team1 = array.getJSONObject(0).getString("Team");
                        team2 = array.getJSONObject(1).getString("Team");

                        venue = obj.getJSONObject("Venue").getString("content");

                        String by = obj.getJSONObject("Result").getString("by");
                        String how = obj.getJSONObject("Result").getString("how");
                        JSONArray temp  = obj.getJSONObject("Result").getJSONArray("Team");
                        if (temp.getJSONObject(0).getString("matchwon").equals("yes") && temp.getJSONObject(1).getString("matchwon").equals("no")) {
                            time = team1 + " won by " + by +" " + how;
                        } else if (temp.getJSONObject(0).getString("matchwon").equals("no") && temp.getJSONObject(1).getString("matchwon").equals("yes")) {
                            time = team2 + " won by " + by +" " + how;
                        } else if (temp.getJSONObject(0).getString("matchwon").equals("no") && temp.getJSONObject(1).getString("matchwon").equals("no")) {
                            time = how;
                        }
                        matchID = obj.getString("matchid");
                        Match match = new Match(team1, team2, venue, time, seriesName, matcNo, matchID);
                        data.add(match);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                recyclerView.getAdapter().notifyDataSetChanged();
                Log.d(Constants.TAG, response.toString());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                progressDialog.dismiss();
                Toast.makeText(PastMatchesActivity.this, "Failed", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                progressDialog.dismiss();
                Toast.makeText(PastMatchesActivity.this, "Failed", Toast.LENGTH_LONG).show();
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

    private static class PastMatchesViewHolder extends RecyclerView.ViewHolder {
        protected CircleImageView imgteam1;
        protected CircleImageView imgteam2;
        protected TextView textteam1;
        protected TextView textteam2;
        protected TextView venue;
        protected TextView time;
        protected TextView seriesName;
        protected TextView matchNo;
        protected LinearLayout linearLayout;

        public PastMatchesViewHolder(View itemView) {
            super(itemView);

            imgteam1 = ViewHolder.get(itemView, R.id.civTeam1);
            imgteam2 = ViewHolder.get(itemView, R.id.civTeam2);
            textteam1 = ViewHolder.get(itemView, R.id.tvTeam1);
            textteam2 = ViewHolder.get(itemView, R.id.tvTeam2);
            venue = ViewHolder.get(itemView, R.id.tvVenue);
            time = ViewHolder.get(itemView, R.id.tvTime);
            seriesName = ViewHolder.get(itemView, R.id.tvSeriesname);
            matchNo = ViewHolder.get(itemView, R.id.tvMatchNo);
            linearLayout = ViewHolder.get(itemView, R.id.match_layout);
        }
    }
}
