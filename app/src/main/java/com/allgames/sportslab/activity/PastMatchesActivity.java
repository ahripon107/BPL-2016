package com.allgames.sportslab.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.allgames.sportslab.util.RecyclerItemClickListener;
import com.allgames.sportslab.util.ViewHolder;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.inject.Inject;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

/**
 * @author ripon
 */
@ContentView(R.layout.fixture)
public class PastMatchesActivity extends CommonActivity {

    @InjectView(R.id.recycler_view)
    private RecyclerView recyclerView;
    @Inject
    private ArrayList<Match> data;
    @InjectView(R.id.adViewFixture)
    private AdView adView;

    @Inject
    private NetworkService networkService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
                holder.time.setText(data.get(position).getMatchStatus());
                holder.seriesName.setText(data.get(position).getSeriesName());
                holder.matchNo.setText(data.get(position).getMatchNo());

            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, final int position) {
                        Intent intent = new Intent(PastMatchesActivity.this, ActivityMatchDetails.class);
                        intent.putExtra("summary", data.get(position));
                        startActivity(intent);
                    }
                })
        );

        networkService.fetchPastMatches(new DefaultMessageHandler(this, true) {
            @Override
            public void onSuccess(Message msg) {
                String result = (String) msg.obj;
                try {
                    JSONArray matchJsonArray = new JSONArray(result);
                    for (int i = 0; i < matchJsonArray.length(); i++) {
                        JSONObject matchJsonObject = matchJsonArray.getJSONObject(i);
                        String team1 = matchJsonObject.getJSONObject("team1").getString("sName");
                        String team2 = matchJsonObject.getJSONObject("team2").getString("sName");
                        String venue = matchJsonObject.getJSONObject("header").getString("grnd") + ", " +
                                matchJsonObject.getJSONObject("header").getString("vcity") + ", " +
                                matchJsonObject.getJSONObject("header").getString("vcountry");
                        String time = matchJsonObject.getJSONObject("header").getString("status");
                        String seriesName = matchJsonObject.getString("srs");
                        String matchNo = matchJsonObject.getJSONObject("header").getString("mnum");
                        String matchId = matchJsonObject.getString("matchId");
                        String dataPath = matchJsonObject.getString("datapath");

                        data.add(new Match(team1, team2, venue, time, seriesName, matchNo, matchId, dataPath));
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

    private static class PastMatchesViewHolder extends RecyclerView.ViewHolder {
        CircleImageView imgteam1;
        CircleImageView imgteam2;
        TextView textteam1;
        TextView textteam2;
        TextView venue;
        TextView time;
        TextView seriesName;
        TextView matchNo;
        LinearLayout linearLayout;

        PastMatchesViewHolder(View itemView) {
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
