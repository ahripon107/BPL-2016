package com.allgames.sportslab.fragment;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.allgames.sportslab.R;
import com.allgames.sportslab.adapter.BasicListAdapter;
import com.allgames.sportslab.model.RankingPlayer;
import com.allgames.sportslab.model.RankingTeam;
import com.allgames.sportslab.util.Constants;
import com.allgames.sportslab.util.ViewHolder;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

/**
 * @author Ripon
 */
public class RankingFragment extends Fragment {

    private RecyclerView recyclerView;
    private ArrayList<RankingPlayer> rankingPlayers = new ArrayList<>();
    private ArrayList<RankingTeam> rankingTeams = new ArrayList<>();
    private LinearLayout teamLayout;
    private LinearLayout playerLayout;
    private Gson gson = new Gson();
    private Typeface tf;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_ranking, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        teamLayout = (LinearLayout) view.findViewById(R.id.team_layout);
        playerLayout = (LinearLayout) view.findViewById(R.id.player_layout);
        tf = Typeface.createFromAsset(getContext().getAssets(), Constants.SOLAIMAN_LIPI_FONT);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
    }

    private static class PlayerViewHolder extends RecyclerView.ViewHolder {
        TextView rank;
        TextView playerName;
        TextView playerAverage;
        TextView playerRating;
        ImageView playerImage;
        ImageView countryImage;

        PlayerViewHolder(View itemView) {
            super(itemView);
            rank = ViewHolder.get(itemView, R.id.tv_player_rank);
            playerName = ViewHolder.get(itemView, R.id.tv_player_name);
            playerAverage = ViewHolder.get(itemView, R.id.tv_player_average);
            playerRating = ViewHolder.get(itemView, R.id.tv_player_rating);
            playerImage = ViewHolder.get(itemView, R.id.player_image);
            countryImage = ViewHolder.get(itemView, R.id.player_country_image);
        }
    }

    private static class TeamViewHolder extends RecyclerView.ViewHolder {
        TextView teamRank;
        TextView teamName;
        TextView teamMatches;
        TextView teamPoint;
        TextView teamRating;
        ImageView teamImage;

        TeamViewHolder(View itemView) {
            super(itemView);
            teamRank = ViewHolder.get(itemView, R.id.iv_team_rank);
            teamName = ViewHolder.get(itemView, R.id.tv_team_name);
            teamMatches = ViewHolder.get(itemView, R.id.tv_team_matches);
            teamPoint = ViewHolder.get(itemView, R.id.tv_team_point);
            teamRating = ViewHolder.get(itemView, R.id.tv_team_rating);
            teamImage = ViewHolder.get(itemView, R.id.team_image);
        }
    }

    public void populatePlayerList(JSONArray jsonArray) {
        playerLayout.setVisibility(View.VISIBLE);
        teamLayout.setVisibility(View.GONE);
        rankingPlayers.clear();
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                rankingPlayers.add(gson.fromJson(String.valueOf(jsonArray.get(i)), RankingPlayer.class));
            }

            recyclerView.setAdapter(new BasicListAdapter<RankingPlayer, PlayerViewHolder>(rankingPlayers) {
                @Override
                public PlayerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                    View view1 = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_player_ranking, parent, false);
                    return new PlayerViewHolder(view1);
                }

                @Override
                public void onBindViewHolder(PlayerViewHolder holder, int position) {
                    holder.rank.setText(rankingPlayers.get(position).getRank());
                    holder.playerName.setText(rankingPlayers.get(position).getName());
                    holder.playerName.setTypeface(tf);
                    holder.playerAverage.setTypeface(tf);
                    holder.playerAverage.setText("গড়: " + rankingPlayers.get(position).getAvg());
                    holder.playerRating.setText(rankingPlayers.get(position).getRating());

                    Picasso.with(getContext())
                            .load(Constants.FACE_IMAGE + rankingPlayers.get(position).getId() + ".jpg")
                            .placeholder(R.drawable.default_image)
                            .into(holder.playerImage);

                    Picasso.with(getContext())
                            .load(Constants.TEAM_IMAGE_FIRST_PART + rankingPlayers.get(position).getCountry_id() + Constants.TEAM_IMAGE_LAST_PART)
                            .placeholder(R.drawable.default_image)
                            .into(holder.countryImage);
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void populateTeamList(JSONArray jsonArray) {
        playerLayout.setVisibility(View.GONE);
        teamLayout.setVisibility(View.VISIBLE);
        rankingTeams.clear();
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                rankingTeams.add(gson.fromJson(String.valueOf(jsonArray.get(i)), RankingTeam.class));
            }

            recyclerView.setAdapter(new BasicListAdapter<RankingTeam, TeamViewHolder>(rankingTeams) {
                @Override
                public TeamViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                    View view1 = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_team_ranking, parent, false);
                    return new TeamViewHolder(view1);
                }

                @Override
                public void onBindViewHolder(TeamViewHolder holder, int position) {
                    holder.teamRank.setText(rankingTeams.get(position).getRank());
                    holder.teamName.setText(rankingTeams.get(position).getName());
                    holder.teamName.setTypeface(tf);
                    holder.teamMatches.setText("Matches: " + rankingTeams.get(position).getMatches());
                    holder.teamPoint.setText(rankingTeams.get(position).getPoints());
                    holder.teamRating.setText(rankingTeams.get(position).getRating());

                    Picasso.with(getContext())
                            .load(Constants.TEAM_IMAGE_FIRST_PART + rankingTeams.get(position).getId() + Constants.TEAM_IMAGE_LAST_PART)
                            .placeholder(R.drawable.default_image)
                            .into(holder.teamImage);
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
