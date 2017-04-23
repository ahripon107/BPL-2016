package com.banglacricket.bdcricketteam.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.banglacricket.bdcricketteam.R;
import com.banglacricket.bdcricketteam.activity.PlayerCareerActivity;
import com.banglacricket.bdcricketteam.model.Bowler;
import com.banglacricket.bdcricketteam.util.CircleImageView;
import com.banglacricket.bdcricketteam.util.Constants;
import com.banglacricket.bdcricketteam.util.ViewHolder;

import java.util.ArrayList;

/**
 * @author Ripon
 */
public class BowlerAdapter extends RecyclerView.Adapter<BowlerAdapter.BowlerViewHolder> {

    private Context context;
    private ArrayList<Bowler> bowlers;


    public BowlerAdapter(Context context, ArrayList<Bowler> bowlers) {
        this.context = context;
        this.bowlers = bowlers;
    }


    @Override
    public BowlerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BowlerViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.singlebowler, parent, false));
    }

    @Override
    public void onBindViewHolder(BowlerViewHolder holder, final int position) {
        holder.name.setText(bowlers.get(position).getName());
        holder.overs.setText(bowlers.get(position).getOver());
        holder.maidens.setText(bowlers.get(position).getMaiden());
        holder.runs.setText(bowlers.get(position).getRun());
        holder.wickets.setText(bowlers.get(position).getWicket());
        if (Constants.SHOW_PLAYER_IMAGE.equals("true")) {
            Picasso.with(context)
                    .load("http://cdn.cricapi.com/players/" + bowlers.get(position).getPlayerId() + ".jpg")
                    .placeholder(R.drawable.player_thumb)
                    .into(holder.playerImage);
        }
        holder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PlayerCareerActivity.class);
                intent.putExtra("playerID", bowlers.get(position).getPlayerId());
                context.startActivity(intent);
            }
        });

        if (position%2 == 1) {
            holder.linearLayout.setBackgroundColor(ContextCompat.getColor(context,R.color.batsmanbowlerbackground));
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }


    @Override
    public int getItemCount() {
        return bowlers.size();
    }

    static class BowlerViewHolder extends RecyclerView.ViewHolder {
        protected TextView name;
        protected TextView overs;
        protected TextView maidens;
        protected TextView runs;
        protected TextView wickets;
        protected LinearLayout linearLayout;
        protected CircleImageView playerImage;

        public BowlerViewHolder(View itemView) {
            super(itemView);

            name = ViewHolder.get(itemView, R.id.bowl_Name);
            overs = ViewHolder.get(itemView, R.id.bowl_overs);
            maidens = ViewHolder.get(itemView, R.id.bowl_maiden);
            runs = ViewHolder.get(itemView, R.id.bowl_runs);
            wickets = ViewHolder.get(itemView, R.id.bowl_wickets);
            linearLayout = ViewHolder.get(itemView,R.id.batsmanScoredetails);
            playerImage = ViewHolder.get(itemView, R.id.civ_player_image);
        }
    }
}
