package com.banglacricket.bdcricketteam.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.banglacricket.bdcricketteam.R;
import com.banglacricket.bdcricketteam.activity.PlayerCareerActivity;
import com.banglacricket.bdcricketteam.model.Player;
import com.banglacricket.bdcricketteam.util.Constants;
import com.banglacricket.bdcricketteam.util.Dialogs;
import com.banglacricket.bdcricketteam.util.FetchFromWeb;
import com.banglacricket.bdcricketteam.util.ViewHolder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * @author Ripon
 */
public class PlayerListAdapter extends RecyclerView.Adapter<PlayerListAdapter.PlayerListViewHolder> {

    Context context;
    ArrayList<Player> players;
    Gson gson;
    Dialogs dialogs;

    public PlayerListAdapter(Context context, ArrayList<Player> players) {
        this.context = context;
        this.players = players;
        gson = new Gson();
        dialogs = new Dialogs(context);
    }

    @Override
    public PlayerListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.singleplayeroflist, parent, false);
        return new PlayerListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PlayerListViewHolder holder, final int position) {
        holder.pname.setText(players.get(position).getName());
        holder.viewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String url = "http://apisea.xyz/Cricket/apis/v1/YahooToCricAPI.php?key=bl905577&yahoo=" + players.get(position).getPersonid();
                dialogs.showDialog();

                FetchFromWeb.get(url, null, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        dialogs.dismissDialog();
                        try {
                            if (response.getString("msg").equals("Successful")) {
                                String playerID = (response.getJSONArray("content").getJSONObject(0).getString("cricapiID"));
                                Intent intent = new Intent(context, PlayerCareerActivity.class);
                                intent.putExtra("playerID", playerID);
                                context.startActivity(intent);
                            } else {
                                Toast.makeText(context, "Profile Not Found", Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.d(Constants.TAG, response.toString());
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        dialogs.dismissDialog();
                        Toast.makeText(context, "Failed", Toast.LENGTH_LONG).show();
                    }
                });


            }
        });
    }

    @Override
    public int getItemCount() {
        return players.size();
    }

    static class PlayerListViewHolder extends RecyclerView.ViewHolder {
        TextView pname;
        LinearLayout linearLayout;
        Button viewProfile;

        public PlayerListViewHolder(View itemView) {
            super(itemView);
            viewProfile = ViewHolder.get(itemView, R.id.btn_view_profile);
            pname = ViewHolder.get(itemView, R.id.tvPlayerName);
            linearLayout = ViewHolder.get(itemView, R.id.player_list_item_card);
        }
    }
}
