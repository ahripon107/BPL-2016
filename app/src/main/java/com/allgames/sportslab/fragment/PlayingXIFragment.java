package com.allgames.sportslab.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.allgames.sportslab.R;
import com.allgames.sportslab.activity.PlayerCareerActivity;
import com.allgames.sportslab.adapter.BasicListAdapter;
import com.allgames.sportslab.model.Match;
import com.allgames.sportslab.model.Player;
import com.allgames.sportslab.util.Constants;
import com.allgames.sportslab.util.DefaultMessageHandler;
import com.allgames.sportslab.util.NetworkService;
import com.google.inject.Inject;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import roboguice.fragment.RoboFragment;

/**
 * @author Ripon
 */

public class PlayingXIFragment extends RoboFragment {

    private RecyclerView t1, t2;
    private TextView team1N, team2N;
    private ArrayList<Player> team1;
    private ArrayList<Player> team2;
    private JSONObject response;
    private Match match;
    private Map<String, String> playerIdNameMap;

    @Inject
    private NetworkService networkService;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser) {
            match = (Match) getArguments().getSerializable("summary");
            networkService.fetchMatchDetails(match.getDataPath(), new DefaultMessageHandler(getContext(), false) {
                @Override
                public void onSuccess(Message msg) {
                    String string = (String) msg.obj;

                    try {
                        response = new JSONObject(string);

                        if (response.toString().equals("{}")) {

                        } else {
                            prepareFragment();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }


            });
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_playing_xi, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        t1 = view.findViewById(R.id.list_team1);
        t2 = view.findViewById(R.id.list_team2);
        t1.setNestedScrollingEnabled(false);
        t2.setNestedScrollingEnabled(false);
        team1N = view.findViewById(R.id.tv_team1Name);
        team2N = view.findViewById(R.id.tv_team2Name);
        team1 = new ArrayList<>();
        team2 = new ArrayList<>();
        playerIdNameMap = new HashMap<>();
    }

    public void prepareFragment() {

        try {
            team1.clear();
            team2.clear();
            JSONObject team1Obj = response.getJSONObject("team1");
            JSONObject team2Obj = response.getJSONObject("team2");
            String team1Nam = team1Obj.getString("sName");
            String team2Nam = team2Obj.getString("sName");

            preparePlayerIdNameMap(response.getJSONArray("players"));

            JSONArray team1Array = team1Obj.getJSONArray("squad");
            for (int i = 0; i < team1Array.length(); i++) {
                team1.add(new Player(playerIdNameMap.get(team1Array.getString(i)), "", team1Array.getString(i)));
            }

            JSONArray team2Array = team2Obj.getJSONArray("squad");
            for (int i = 0; i < team2Array.length(); i++) {
                team2.add(new Player(playerIdNameMap.get(team2Array.getString(i)), "", team2Array.getString(i)));
            }

            team1N.setText(team1Nam);
            team2N.setText(team2Nam);
            setPlayingXI();

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void preparePlayerIdNameMap(JSONArray playersArray) {
        try {
            for (int i = 0; i < playersArray.length(); i++) {
                JSONObject playerObject = playersArray.getJSONObject(i);
                playerIdNameMap.put(playerObject.getString("id"), playerObject.getString("fName"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setPlayingXI() {

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
                            .load("http://i.cricketcb.com/stats/img/faceImages/" + team1.get(position).getPersonid() + ".jpg")
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
                            .load("http://i.cricketcb.com/stats/img/faceImages/" + team2.get(position).getPersonid() + ".jpg")
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
        TextView textView;
        ImageView imageView;

        PlayingXIViewHolder(View itemView) {
            super(itemView);
            this.textView = itemView.findViewById(R.id.tv_player_name);
            this.imageView = itemView.findViewById(R.id.civ_player_image);
        }
    }
}
