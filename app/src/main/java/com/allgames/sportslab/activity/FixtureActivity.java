package com.allgames.sportslab.activity;

import android.os.Bundle;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.allgames.sportslab.R;
import com.allgames.sportslab.adapter.BasicListAdapter;
import com.allgames.sportslab.model.Match;
import com.allgames.sportslab.util.CircleImageView;
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
public class FixtureActivity extends CommonActivity {

    private RecyclerView recyclerView;
    private AdView adView;

    @Inject
    private ArrayList<Match> data;

    @Inject
    private NetworkService networkService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initialize();

        recyclerView.setAdapter(new BasicListAdapter<Match, FixtureViewHolder>(data) {
            @Override
            public FixtureViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.match, parent, false);
                return new FixtureViewHolder(view);
            }

            @Override
            public void onBindViewHolder(FixtureViewHolder holder, int position) {
                if (Constants.SHOW_PLAYER_IMAGE.equals("true")) {
                    holder.imgteam1.setVisibility(View.VISIBLE);
                    holder.imgteam2.setVisibility(View.VISIBLE);

                    Picasso.with(FixtureActivity.this)
                            .load(Constants.resolveLogo(data.get(position).getTeam1()))
                            .placeholder(R.drawable.default_image)
                            .into(holder.imgteam1);

                    Picasso.with(FixtureActivity.this)
                            .load(Constants.resolveLogo(data.get(position).getTeam2()))
                            .placeholder(R.drawable.default_image)
                            .into(holder.imgteam2);
                } else {
                    holder.imgteam1.setVisibility(View.GONE);
                    holder.imgteam2.setVisibility(View.GONE);
                }


                holder.textteam1.setText(data.get(position).getTeam1());
                holder.textteam2.setText(data.get(position).getTeam2());
                holder.venue.setText(data.get(position).getVenue());

                //String timeparts[] = data.get(position).getTime().split("T");
                //holder.time.setText(timeparts[0] + "  " + timeparts[1]);
                holder.seriesName.setText(data.get(position).getSeriesName());
                holder.matchNo.setText(data.get(position).getMatchNo());

            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        String url = Constants.FIXTURE_URL;
        Log.d(Constants.TAG, url);

        networkService.fetchFixture(new DefaultMessageHandler(this, true) {
            @Override
            public void onSuccess(Message msg) {
                String string = (String) msg.obj;
                try {
                    JSONObject response = new JSONObject(string);

                    String team1, team2, venue, time, seriesName, matcNo;
                    response = response.getJSONObject("schedule");
                    JSONArray jsonArray = response.getJSONArray("matches");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject obj = jsonArray.getJSONObject(i);
                        seriesName = obj.getString("name");
                        matcNo = obj.getString("id");

                        team1 = obj.getString("team1");
                        team2 = obj.getString("team2");

                        venue = obj.getString("location");
                        time = obj.getString("result");
                        Match match = new Match(team1, team2, venue, time, seriesName, matcNo, "");
                        data.add(match);
                    }

                    recyclerView.getAdapter().notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });


        AdRequest adRequest = new AdRequest.Builder().addTestDevice(Constants.ONE_PLUS_TEST_DEVICE)
                .addTestDevice(Constants.XIAOMI_TEST_DEVICE).build();
        adView.loadAd(adRequest);
    }

    private static class FixtureViewHolder extends RecyclerView.ViewHolder {
        protected CircleImageView imgteam1;
        protected CircleImageView imgteam2;
        protected TextView textteam1;
        protected TextView textteam2;
        protected TextView venue;
        protected TextView time;
        protected TextView seriesName;
        protected TextView matchNo;
        protected LinearLayout linearLayout;

        public FixtureViewHolder(View itemView) {
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

    private void initialize() {
        setContentView(R.layout.fixture);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        adView = (AdView) findViewById(R.id.adViewFixture);
    }
}
