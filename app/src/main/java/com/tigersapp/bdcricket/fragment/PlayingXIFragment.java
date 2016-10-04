package com.tigersapp.bdcricket.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tigersapp.bdcricket.R;
import com.tigersapp.bdcricket.adapter.BasicListAdapter;
import com.tigersapp.bdcricket.util.ViewHolder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * @author Ripon
 */

public class PlayingXIFragment extends Fragment {

    private RecyclerView t1,t2;
    private TextView team1N,team2N;
    ArrayList<String> team1,team2;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_playing_xi,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        t1 = (RecyclerView) view.findViewById(R.id.list_team1);
        t2 = (RecyclerView) view.findViewById(R.id.list_team2);
        team1N = (TextView) view.findViewById(R.id.tv_team1Name);
        team2N = (TextView) view.findViewById(R.id.tv_team2Name);
        team1 = new ArrayList<>();
        team2 = new ArrayList<>();
    }

    public void setPlayingXI (JSONArray batTeam1,JSONArray dnbTeam1,JSONArray batTeam2,JSONArray dnbTeam2,String team1Name,String team2Name) {
        team1N.setText(team1Name);
        team2N.setText(team2Name);


        for (int i=0;i<dnbTeam1.length();i++) {
            try {
                JSONObject jsonObject = dnbTeam1.getJSONObject(i);
                team1.add(jsonObject.getString("playerName"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        for (int i=0;i<dnbTeam2.length();i++) {
            try {
                JSONObject jsonObject = dnbTeam2.getJSONObject(i);
                team2.add(jsonObject.getString("playerName"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        t1.setAdapter(new BasicListAdapter<String,PlayingXIViewHolder>(team1) {
            @Override
            public PlayingXIViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_playing_xi,parent,false);
                return new PlayingXIViewHolder(view);
            }

            @Override
            public void onBindViewHolder(PlayingXIViewHolder holder, int position) {
                holder.textView.setText(team1.get(position));
            }
        });
        t1.setLayoutManager(new LinearLayoutManager(getContext()));

        t2.setAdapter(new BasicListAdapter<String,PlayingXIViewHolder>(team2) {
            @Override
            public PlayingXIViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_playing_xi,parent,false);
                return new PlayingXIViewHolder(view);
            }

            @Override
            public void onBindViewHolder(PlayingXIViewHolder holder, int position) {
                holder.textView.setText(team2.get(position));
            }
        });
        t2.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private static class PlayingXIViewHolder extends RecyclerView.ViewHolder {

        protected TextView textView;

        public PlayingXIViewHolder(View itemView) {
            super(itemView);
            textView = ViewHolder.get(itemView,R.id.tv_player_name);
        }
    }
}
