package com.sfuronlabs.bpl2016.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sfuronlabs.bpl2016.R;
import com.sfuronlabs.bpl2016.model.RecordVsOthers;
import com.sfuronlabs.bpl2016.util.ViewHolder;

import java.util.ArrayList;


/**
 * @author Ripon
 */
public class TeamRecordAdapter extends RecyclerView.Adapter<TeamRecordAdapter.TeamRecordViewHolder> {

    Context context;
    ArrayList<RecordVsOthers> players;
    LayoutInflater layoutInflater;

    public TeamRecordAdapter(Context context, ArrayList<RecordVsOthers> players) {
        this.context = context;
        this.players = players;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public TeamRecordViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.singleteamrecord,parent,false);
        return new TeamRecordViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TeamRecordViewHolder holder, int position) {

        holder.vsWhom.setText("Against " + players.get(position).getAgainst());
        holder.played.setText("Played: " + players.get(position).getPlayed());
        holder.wins.setText("Wins: " + players.get(position).getWins());
        holder.loss.setText("Losses: " + players.get(position).getLoss());
        holder.draw.setText("Tie/NR: " + players.get(position).getDraw());
        holder.highestInnings.setText("Highest Innings: " + players.get(position).getHighestInnings());
        holder.BBI.setText("Best Bowling: " + players.get(position).getBestBBI());
        holder.bestIndividual.setText("Best Innings: " + players.get(position).getBestInning());
        holder.mostWkts.setText("Most Wickets: " + players.get(position).getMaxWkts());
        holder.mostRuns.setText("Most Runs: " + players.get(position).getMaxRuns());
    }

    @Override
    public int getItemCount() {
        return players.size();
    }


    static class TeamRecordViewHolder extends RecyclerView.ViewHolder {

        protected TextView vsWhom;
        protected TextView played;
        protected TextView wins;
        protected TextView loss;
        protected TextView draw;
        protected TextView highestInnings;
        protected TextView BBI;
        protected TextView bestIndividual;
        protected TextView mostWkts;
        protected TextView mostRuns;

        public TeamRecordViewHolder(View itemView) {
            super(itemView);

            vsWhom = ViewHolder.get(itemView, R.id.tvAgainst);
            played = ViewHolder.get(itemView,R.id.tvPlayed);
            wins = ViewHolder.get(itemView,R.id.tvWins);
            loss = ViewHolder.get(itemView,R.id.tvLoss);
            draw = ViewHolder.get(itemView,R.id.tvDraw);
            highestInnings = ViewHolder.get(itemView,R.id.tvHighestInnings);
            BBI = ViewHolder.get(itemView,R.id.tvBBI);
            bestIndividual = ViewHolder.get(itemView,R.id.tvBestIndividual);
            mostWkts = ViewHolder.get(itemView,R.id.tvMostWickets);
            mostRuns = ViewHolder.get(itemView,R.id.tvMostRuns);
        }
    }
}