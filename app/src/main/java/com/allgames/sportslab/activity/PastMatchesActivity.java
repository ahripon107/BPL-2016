package com.allgames.sportslab.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.allgames.sportslab.R;
import com.allgames.sportslab.adapter.BasicListAdapter;
import com.allgames.sportslab.model.match.Header;
import com.allgames.sportslab.model.match.Match;
import com.allgames.sportslab.model.match.MiniScore;
import com.allgames.sportslab.util.Constants;
import com.allgames.sportslab.util.DefaultMessageHandler;
import com.allgames.sportslab.util.NetworkService;
import com.allgames.sportslab.util.RecyclerItemClickListener;
import com.allgames.sportslab.util.ViewHolder;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.inject.Inject;
import com.squareup.picasso.Picasso;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.JavaType;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_match, parent, false);
                return new PastMatchesViewHolder(view);
            }

            @Override
            public void onBindViewHolder(PastMatchesViewHolder holder, int position) {
                Match match = data.get(position);
                Picasso.with(PastMatchesActivity.this)
                        .load(Constants.resolveLogo(match.getTeam1().getId()))
                        .placeholder(R.drawable.default_image)
                        .into(holder.imgteam1);

                Picasso.with(PastMatchesActivity.this)
                        .load(Constants.resolveLogo(match.getTeam2().getId()))
                        .placeholder(R.drawable.default_image)
                        .into(holder.imgteam2);

                MiniScore miniScore = match.getMiniScore();
                if (miniScore != null) {
                    if (miniScore.getBattingTeamId() == match.getTeam1().getId()) {
                        holder.textteam1.setText(match.getTeam1().getShortName()+"  "+miniScore.getBattingTeamScore());
                        holder.textteam2.setText(match.getTeam2().getShortName() + "  "+ miniScore.getBowlingTeamScore());
                    } else {
                        holder.textteam1.setText(match.getTeam1().getShortName()+"  "+miniScore.getBowlingTeamScore());
                        holder.textteam2.setText(match.getTeam2().getShortName() + "  "+ miniScore.getBattingTeamScore());
                    }
                } else {
                    holder.textteam1.setText(match.getTeam1().getShortName());
                    holder.textteam2.setText(match.getTeam2().getShortName());
                }
                Header header = match.getHeader();
                holder.venue.setText(header.getGround()+", "+header.getCity()+", "+header.getCountry());

                holder.time.setText(header.getStatus());
                holder.seriesName.setText(match.getSrs()+", "+header.getMatchNumber());
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, final int position) {
                        Intent intent = new Intent(PastMatchesActivity.this, MatchDetailsActivity.class);
                        intent.putExtra("summary", data.get(position));
                        startActivity(intent);
                    }
                })
        );

        networkService.fetchPastMatches(new DefaultMessageHandler(this, true) {
            @Override
            public void onSuccess(Message msg) {
                ObjectMapper objectMapper = new ObjectMapper();
                JavaType valueType = objectMapper.getTypeFactory().constructCollectionType(List.class, Match.class);
                try {
                    data.addAll((ArrayList<Match>) objectMapper.readValue((String) msg.obj, valueType));
                    recyclerView.getAdapter().notifyDataSetChanged();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });


//        AdRequest adRequest = new AdRequest.Builder().addTestDevice(Constants.ONE_PLUS_TEST_DEVICE)
//                .addTestDevice(Constants.XIAOMI_TEST_DEVICE).build();
//        adView.loadAd(adRequest);
    }

    private static class PastMatchesViewHolder extends RecyclerView.ViewHolder {
        ImageView imgteam1;
        ImageView imgteam2;
        TextView textteam1;
        TextView textteam2;
        TextView venue;
        TextView time;
        TextView seriesName;
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
            linearLayout = ViewHolder.get(itemView, R.id.match_layout);
        }
    }
}
