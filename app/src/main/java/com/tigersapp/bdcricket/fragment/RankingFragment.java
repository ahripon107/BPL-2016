package com.tigersapp.bdcricket.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tigersapp.bdcricket.R;
import com.tigersapp.bdcricket.adapter.BasicListAdapter;
import com.tigersapp.bdcricket.model.RankingPlayer;
import com.tigersapp.bdcricket.model.RankingTeam;
import com.tigersapp.bdcricket.util.Constants;
import com.tigersapp.bdcricket.util.ViewHolder;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * @author Ripon
 */
public class RankingFragment extends Fragment {

    RecyclerView recyclerView;

    ArrayList<RankingPlayer> rankingPlayers = new ArrayList<>();

    ArrayList<RankingTeam> rankingTeams = new ArrayList<>();

    LinearLayout teamLayout,playerLayout;

    Gson gson = new Gson();

    public RankingFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ranking,container,false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        teamLayout = (LinearLayout) view.findViewById(R.id.team_layout);
        playerLayout = (LinearLayout) view.findViewById(R.id.player_layout);
        return view;
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private static class PlayerViewHolder extends RecyclerView.ViewHolder {
        protected TextView rank;
        protected TextView playerName;
        protected TextView playerAverage;
        protected TextView playerRating;
        protected ImageView playerImage;
        protected ImageView countryImage;
        public PlayerViewHolder(View itemView) {
            super(itemView);
            rank = ViewHolder.get(itemView, R.id.tv_player_rank);
            playerName = ViewHolder.get(itemView,R.id.tv_player_name);
            playerAverage = ViewHolder.get(itemView,R.id.tv_player_average);
            playerRating = ViewHolder.get(itemView,R.id.tv_player_rating);
            playerImage = ViewHolder.get(itemView,R.id.player_image);
            countryImage = ViewHolder.get(itemView,R.id.player_country_image);
        }
    }

    private static class TeamViewHolder extends RecyclerView.ViewHolder {
        protected TextView teamRank;
        protected TextView teamName;
        protected TextView teamMatches;
        protected TextView teamPoint;
        protected TextView teamRating;
        protected ImageView teamImage;
        public TeamViewHolder(View itemView) {
            super(itemView);
            teamRank = ViewHolder.get(itemView,R.id.iv_team_rank);
            teamName = ViewHolder.get(itemView,R.id.tv_team_name);
            teamMatches = ViewHolder.get(itemView,R.id.tv_team_matches);
            teamPoint = ViewHolder.get(itemView,R.id.tv_team_point);
            teamRating = ViewHolder.get(itemView,R.id.tv_team_rating);
            teamImage = ViewHolder.get(itemView,R.id.team_image);
        }
    }

    public void populatePlayerList(JSONObject jsonObject) {
        playerLayout.setVisibility(View.VISIBLE);
        teamLayout.setVisibility(View.GONE);
        rankingPlayers.clear();
        try {
            JSONArray jsonArray = jsonObject.getJSONArray("playerCurrentRanks");
            for (int i=0;i<jsonArray.length();i++) {
                rankingPlayers.add(gson.fromJson(String.valueOf(jsonArray.get(i)),RankingPlayer.class));
            }

            recyclerView.setAdapter(new BasicListAdapter<RankingPlayer,PlayerViewHolder>(rankingPlayers) {
                @Override
                public PlayerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                    View view1 = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_player_ranking,parent,false);
                    return new PlayerViewHolder(view1);
                }

                @Override
                public void onBindViewHolder(PlayerViewHolder holder, int position) {
                    holder.rank.setText(rankingPlayers.get(position).getCurrentRank());
                    holder.playerName.setText(rankingPlayers.get(position).getName());
                    holder.playerAverage.setText("Average: "+rankingPlayers.get(position).getAvg());
                    holder.playerRating.setText(rankingPlayers.get(position).getCurrentRating());

                    Picasso.with(getContext())
                            .load(Constants.FACE_IMAGE+rankingPlayers.get(position).getId()+".jpg")
                            .placeholder(R.drawable.default_image)
                            .into(holder.playerImage);

                    Picasso.with(getContext())
                            .load(Constants.TEAM_IMAGE_FIRST_PART+rankingPlayers.get(position).getCountryId()+Constants.TEAM_IMAGE_LAST_PART)
                            .placeholder(R.drawable.default_image)
                            .into(holder.countryImage);
                }
            });
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void populateTeamList(JSONObject jsonObject) {
        playerLayout.setVisibility(View.GONE);
        teamLayout.setVisibility(View.VISIBLE);
        rankingTeams.clear();
        try {
            JSONArray jsonArray = jsonObject.getJSONArray("teamCurrentRanks");
            for (int i=0;i<jsonArray.length();i++) {
                rankingTeams.add(gson.fromJson(String.valueOf(jsonArray.get(i)),RankingTeam.class));
            }

            recyclerView.setAdapter(new BasicListAdapter<RankingTeam,TeamViewHolder>(rankingTeams) {
                @Override
                public TeamViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                    View view1 = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_team_ranking,parent,false);
                    return new TeamViewHolder(view1);
                }

                @Override
                public void onBindViewHolder(TeamViewHolder holder, int position) {
                    holder.teamRank.setText(rankingTeams.get(position).getCurrentRank());
                    holder.teamName.setText(rankingTeams.get(position).getName());
                    holder.teamMatches.setText("Matches: "+rankingTeams.get(position).getMatches());
                    holder.teamPoint.setText(rankingTeams.get(position).getCurrentPoints());
                    holder.teamRating.setText(rankingTeams.get(position).getCurrentRating());

                    Picasso.with(getContext())
                            .load(Constants.TEAM_IMAGE_FIRST_PART+rankingTeams.get(position).getId()+Constants.TEAM_IMAGE_LAST_PART)
                            .placeholder(R.drawable.default_image)
                            .into(holder.teamImage);
                }
            });
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
