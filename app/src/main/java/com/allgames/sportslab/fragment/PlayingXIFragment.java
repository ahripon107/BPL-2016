package com.allgames.sportslab.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.allgames.sportslab.R;
import com.allgames.sportslab.activity.PlayerCareerActivity;
import com.allgames.sportslab.adapter.BasicListAdapter;
import com.allgames.sportslab.model.Player;
import com.allgames.sportslab.util.Constants;
import com.allgames.sportslab.util.ViewHolder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * @author Ripon
 */

public class PlayingXIFragment extends Fragment {

    private RecyclerView t1, t2;
    private TextView team1N, team2N;
    ArrayList<Player> team1, team2;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_playing_xi, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        t1 = (RecyclerView) view.findViewById(R.id.list_team1);
        t2 = (RecyclerView) view.findViewById(R.id.list_team2);
        t1.setNestedScrollingEnabled(false);
        t2.setNestedScrollingEnabled(false);
        team1N = (TextView) view.findViewById(R.id.tv_team1Name);
        team2N = (TextView) view.findViewById(R.id.tv_team2Name);
        team1 = new ArrayList<>();
        team2 = new ArrayList<>();
    }

    public void setPlayingXI(JSONArray batTeam1, JSONArray dnbTeam1, JSONArray batTeam2, JSONArray dnbTeam2, String team1Name, String team2Name) {
        team1.clear();
        team2.clear();
        team1N.setText(team1Name);
        team2N.setText(team2Name);
        for (int i = 0; i < batTeam1.length(); i++) {
            try {
                JSONObject jsonObject = batTeam1.getJSONObject(i);
                team1.add(new Player(jsonObject.getJSONObject("player").getString("playerName"), "", jsonObject.getJSONObject("player").getString("playerId")));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        for (int i = 0; i < batTeam2.length(); i++) {
            try {
                JSONObject jsonObject = batTeam2.getJSONObject(i);
                team2.add(new Player(jsonObject.getJSONObject("player").getString("playerName"), "", jsonObject.getJSONObject("player").getString("playerId")));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        for (int i = 0; i < dnbTeam1.length(); i++) {
            try {
                JSONObject jsonObject = dnbTeam1.getJSONObject(i);
                team1.add(new Player(jsonObject.getString("playerName"), "", jsonObject.getString("playerId")));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        for (int i = 0; i < dnbTeam2.length(); i++) {
            try {
                JSONObject jsonObject = dnbTeam2.getJSONObject(i);
                team2.add(new Player(jsonObject.getString("playerName"), "", jsonObject.getString("playerId")));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        t1.setAdapter(new BasicListAdapter<Player, PlayingXIViewHolder>(team1) {
            @Override
            public PlayingXIViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_playing_xi, parent, false);
                return new PlayingXIViewHolder(view);
            }

            @Override
            public void onBindViewHolder(PlayingXIViewHolder holder, final int position) {
                holder.textView.setText(team1.get(position).getName());
                if (Constants.SHOW_PLAYER_IMAGE.equals("true")) {
                    Picasso.with(getContext())
                            .load("http://cdn.cricapi.com/players/" + team1.get(position).getPersonid() + ".jpg")
                            .placeholder(R.drawable.default_image)
                            .into(holder.imageView);
                }

                holder.textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getContext(), PlayerCareerActivity.class);
                        intent.putExtra("playerID", team1.get(position).getPersonid());
                        getActivity().startActivity(intent);
                    }
                });
            }
        });
        t1.setLayoutManager(new LinearLayoutManager(getContext()));

        t2.setAdapter(new BasicListAdapter<Player, PlayingXIViewHolder>(team2) {
            @Override
            public PlayingXIViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_playing_xi, parent, false);
                return new PlayingXIViewHolder(view);
            }

            @Override
            public void onBindViewHolder(PlayingXIViewHolder holder, final int position) {
                holder.textView.setText(team2.get(position).getName());
                if (Constants.SHOW_PLAYER_IMAGE.equals("true")) {
                    Picasso.with(getContext())
                            .load("http://cdn.cricapi.com/players/" + team2.get(position).getPersonid() + ".jpg")
                            .placeholder(R.drawable.default_image)
                            .into(holder.imageView);
                }
                holder.textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getContext(), PlayerCareerActivity.class);
                        intent.putExtra("playerID", team2.get(position).getPersonid());
                        getActivity().startActivity(intent);
                    }
                });
            }
        });
        t2.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private static class PlayingXIViewHolder extends RecyclerView.ViewHolder {

        protected TextView textView;
        protected ImageView imageView;

        public PlayingXIViewHolder(View itemView) {
            super(itemView);
            textView = ViewHolder.get(itemView, R.id.tv_player_name);
            imageView = ViewHolder.get(itemView, R.id.civ_player_image);
        }
    }
}
