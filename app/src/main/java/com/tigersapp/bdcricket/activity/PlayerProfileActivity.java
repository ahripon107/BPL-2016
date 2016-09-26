package com.tigersapp.bdcricket.activity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.gson.Gson;
import com.google.inject.Inject;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.tigersapp.bdcricket.R;
import com.tigersapp.bdcricket.adapter.BasicListAdapter;
import com.tigersapp.bdcricket.model.ProfileBatting;
import com.tigersapp.bdcricket.model.ProfileBowling;
import com.tigersapp.bdcricket.util.Constants;
import com.tigersapp.bdcricket.util.FetchFromWeb;
import com.tigersapp.bdcricket.util.RoboAppCompatActivity;
import com.tigersapp.bdcricket.util.ViewHolder;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import dmax.dialog.SpotsDialog;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

/**
 * @author Ripon
 */
@ContentView(R.layout.activity_player_profile)
public class PlayerProfileActivity extends RoboAppCompatActivity {
    @InjectView(R.id.tv_player_name)
    TextView playerName;

    @InjectView(R.id.tv_player_born)
    TextView playerBorn;

    @InjectView(R.id.tv_player_age)
    TextView playerAge;

    @InjectView(R.id.tv_player_major_teams)
    TextView majorTeams;

    @InjectView(R.id.tv_player_playing_role)
    TextView playingRole;

    @InjectView(R.id.tv_player_batting_style)
    TextView battingStyle;

    @InjectView(R.id.tv_player_bowling_style)
    TextView bowlingStyle;

    @InjectView(R.id.player_image)
    ImageView imageView;

    @InjectView(R.id.batting_record_list)
    RecyclerView battingRecord;

    @InjectView(R.id.bowling_record_list)
    RecyclerView bowlingRecord;

    @InjectView(R.id.tv_player_profile)
    TextView playerProfile;

    @InjectView(R.id.adViewPlayerProfile)
    AdView adView;

    @Inject
    Gson gson;

    @Inject
    ArrayList<ProfileBatting> profileBattings;

    @Inject
    ArrayList<ProfileBowling> profileBowlings;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        String playerID = getIntent().getStringExtra("playerID");

        String url = "http://cricapi.com/api/playerStats?pid="+playerID+"&apikey=MScPVINvZoYtOmeNSY7aDVtaa4H2";
        Log.d(Constants.TAG, url);

        final AlertDialog progressDialog = new SpotsDialog(PlayerProfileActivity.this, R.style.Custom);
        progressDialog.show();
        progressDialog.setCancelable(true);
        FetchFromWeb.get(url, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                progressDialog.dismiss();
                try {
                    playerName.setText(response.getString("name"));

                    if (response.has("born")) playerBorn.setText(Html.fromHtml("<b>Born:</b> "+response.getString("born")));
                    else playerBorn.setText("");

                    if (response.has("currentAge"))playerAge.setText(Html.fromHtml("<b>Current Age:</b> "+response.getString("currentAge")));
                    else playerAge.setText("");

                    if (response.has("majorTeams")) majorTeams.setText(Html.fromHtml("<b>Major Teams:</b> "+response.getString("majorTeams")));
                    else majorTeams.setText("");

                    if (response.has("playingRole")) playingRole.setText(Html.fromHtml("<b>Playing Role:</b> "+response.getString("playingRole")));
                    else playingRole.setText("");

                    if (response.has("battingStyle")) battingStyle.setText(Html.fromHtml("<b>Batting Style:</b> "+response.getString("battingStyle")));
                    else battingStyle.setText("");

                    if (response.has("bowlingStyle")) bowlingStyle.setText(Html.fromHtml("<b>Bowling Style:</b> "+response.getString("bowlingStyle")));
                    else bowlingStyle.setText("");

                    if (response.has("profile")) playerProfile.setText(response.getString("profile"));
                    else playerProfile.setText("");

                    if (response.has("imageURL")) {
                        Picasso.with(PlayerProfileActivity.this)
                                .load(response.getString("imageURL"))
                                .placeholder(R.drawable.default_image)
                                .into(imageView);
                    }


                    JSONObject battingObject = response.getJSONObject("data").getJSONObject("batting");

                    if (battingObject.has("tests")) {
                        ProfileBatting profileBat = processProfileBatting(battingObject.getJSONObject("tests"));
                        if (profileBat!=null) profileBat.setGametype("Tests");
                        profileBattings.add(profileBat);
                    }
                    if (battingObject.has("ODIs")) {
                        ProfileBatting profileBat = processProfileBatting(battingObject.getJSONObject("ODIs"));
                        if (profileBat!=null) profileBat.setGametype("ODIs");
                        profileBattings.add(profileBat);
                    }
                    if (battingObject.has("T20Is")) {
                        ProfileBatting profileBat = processProfileBatting(battingObject.getJSONObject("T20Is"));
                        if (profileBat!=null) profileBat.setGametype("T20Is");
                        profileBattings.add(profileBat);
                    }
                    if (battingObject.has("firstClass")) {
                        ProfileBatting profileBat = processProfileBatting(battingObject.getJSONObject("firstClass"));
                        if (profileBat!=null) profileBat.setGametype("FirstClass");
                        profileBattings.add(profileBat);
                    }
                    if (battingObject.has("listA")) {
                        ProfileBatting profileBat = processProfileBatting(battingObject.getJSONObject("listA"));
                        if (profileBat!=null) profileBat.setGametype("ListA");
                        profileBattings.add(profileBat);
                    }
                    if (battingObject.has("twenty20")) {
                        ProfileBatting profileBat = processProfileBatting(battingObject.getJSONObject("twenty20"));
                        if (profileBat!=null) profileBat.setGametype("Twenty20");
                        profileBattings.add(profileBat);
                    }

                    battingRecord.setAdapter(new BasicListAdapter<ProfileBatting,ProfileBattingViewHolder>(profileBattings) {
                        @Override
                        public ProfileBattingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_profile_batting,parent,false);
                            return new ProfileBattingViewHolder(view);
                        }

                        @Override
                        public void onBindViewHolder(ProfileBattingViewHolder holder, int position) {
                            ProfileBatting profileBtng = profileBattings.get(position);
                            holder.gameType.setText(profileBtng.getGametype());
                            holder.matches.setText(profileBtng.getMat());
                            holder.runs.setText(profileBtng.getRuns());
                            holder.highestScore.setText(profileBtng.getHS());
                            holder.average.setText(profileBtng.getAve());
                            holder.strikeRate.setText(profileBtng.getSR());
                            holder.fifties.setText(profileBtng.getFifty());
                            holder.hundreds.setText(profileBtng.getHundred());
                        }
                    });

                    battingRecord.setLayoutManager(new LinearLayoutManager(PlayerProfileActivity.this));

                    JSONObject bowlingObject = response.getJSONObject("data").getJSONObject("bowling");

                    if (bowlingObject.has("tests")) {
                        ProfileBowling profileBowl = processProfileBowling(bowlingObject.getJSONObject("tests"));
                        if (profileBowl!=null) profileBowl.setGameType("tests");
                        profileBowlings.add(profileBowl);
                    }

                    if (bowlingObject.has("ODIs")) {
                        ProfileBowling profileBowl = processProfileBowling(bowlingObject.getJSONObject("ODIs"));
                        if (profileBowl!=null) profileBowl.setGameType("ODIs");
                        profileBowlings.add(profileBowl);
                    }
                    if (bowlingObject.has("T20Is")) {
                        ProfileBowling profileBowl = processProfileBowling(bowlingObject.getJSONObject("T20Is"));
                        if (profileBowl!=null) profileBowl.setGameType("T20Is");
                        profileBowlings.add(profileBowl);
                    }
                    if (bowlingObject.has("firstClass")) {
                        ProfileBowling profileBowl = processProfileBowling(bowlingObject.getJSONObject("firstClass"));
                        if (profileBowl!=null) profileBowl.setGameType("firstClass");
                        profileBowlings.add(profileBowl);
                    }
                    if (bowlingObject.has("listA")) {
                        ProfileBowling profileBowl = processProfileBowling(bowlingObject.getJSONObject("listA"));
                        if (profileBowl!=null) profileBowl.setGameType("listA");
                        profileBowlings.add(profileBowl);
                    }
                    if (bowlingObject.has("twenty20")) {
                        ProfileBowling profileBowl = processProfileBowling(bowlingObject.getJSONObject("twenty20"));
                        if (profileBowl!=null) profileBowl.setGameType("twenty20");
                        profileBowlings.add(profileBowl);
                    }


                    bowlingRecord.setAdapter(new BasicListAdapter<ProfileBowling,ProfileBowlingViewHolder>(profileBowlings) {
                        @Override
                        public ProfileBowlingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_profile_bowling,parent,false);
                            return new ProfileBowlingViewHolder(view);
                        }

                        @Override
                        public void onBindViewHolder(ProfileBowlingViewHolder holder, int position) {
                            ProfileBowling profileBowling = profileBowlings.get(position);
                            holder.gameType.setText(profileBowling.getGameType());
                            holder.matches.setText(profileBowling.getMat());
                            holder.wickets.setText(profileBowling.getWkts());
                            holder.bbi.setText(profileBowling.getBBI());
                            holder.average.setText(profileBowling.getAve());
                            holder.economy.setText(profileBowling.getEcon());
                            holder.strikeRate.setText(profileBowling.getSR());
                            holder.fiveWickets.setText(profileBowling.getFiveWkts());
                        }
                    });

                    bowlingRecord.setLayoutManager(new LinearLayoutManager(PlayerProfileActivity.this));

                    Log.d(Constants.TAG, response.toString());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                progressDialog.dismiss();
                Toast.makeText(PlayerProfileActivity.this, "Failed", Toast.LENGTH_LONG).show();
            }
        });

        AdRequest adRequest = new AdRequest.Builder().addTestDevice(Constants.ONE_PLUS_TEST_DEVICE)
                .addTestDevice(Constants.XIAOMI_TEST_DEVICE).build();
        adView.loadAd(adRequest);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public ProfileBatting processProfileBatting(JSONObject jsonObject) {
        ProfileBatting profileBatting = null;
        try {
            String fifty = jsonObject.getString("50");
            String hundred = jsonObject.getString("100");
            String Mat = jsonObject.getString("Mat");
            String Inns = jsonObject.getString("Inns");
            String NO = jsonObject.getString("NO");
            String Runs = jsonObject.getString("Runs");
            String HS = jsonObject.getString("HS");
            String Ave = jsonObject.getString("Ave");
            String BF = jsonObject.getString("BF");
            String SR = jsonObject.getString("SR");
            String fours = jsonObject.getString("4s");
            String sixes = jsonObject.getString("6s");
            profileBatting = new ProfileBatting(fifty,hundred,Mat,Inns,NO,Runs,HS,Ave,BF,SR,fours,sixes);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return profileBatting;
    }

    public ProfileBowling processProfileBowling(JSONObject jsonObject) {
        ProfileBowling profileBowling = null;
        try {
            String Mat = jsonObject.getString("Mat");
            String Inns = jsonObject.getString("Inns");
            String balls = jsonObject.getString("Balls");
            String Runs = jsonObject.getString("Runs");
            String wkts = jsonObject.getString("Wkts");
            String BBI = jsonObject.getString("BBI");
            String BBM = jsonObject.getString("BBM");
            String Ave = jsonObject.getString("Ave");
            String Econ = jsonObject.getString("Econ");
            String SR = jsonObject.getString("SR");
            String fourWkts = jsonObject.getString("4w");
            String fiveWkts = jsonObject.getString("5w");
            profileBowling = new ProfileBowling(Mat,Inns,balls,Runs,wkts,BBI,BBM,Ave,Econ,SR,fourWkts,fiveWkts);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return profileBowling;
    }

    private static class ProfileBattingViewHolder extends RecyclerView.ViewHolder {
        protected TextView gameType;
        protected TextView matches;
        protected TextView runs;
        protected TextView highestScore;
        protected TextView average;
        protected TextView strikeRate;
        protected TextView fifties;
        protected TextView hundreds;

        public ProfileBattingViewHolder(View itemView) {
            super(itemView);
            gameType = ViewHolder.get(itemView,R.id.tv_game_type);
            matches = ViewHolder.get(itemView,R.id.tv_matches);
            runs = ViewHolder.get(itemView,R.id.tv_runs);
            highestScore = ViewHolder.get(itemView,R.id.tv_highest_score);
            average = ViewHolder.get(itemView,R.id.tv_average);
            strikeRate = ViewHolder.get(itemView,R.id.tv_strike_rate);
            fifties = ViewHolder.get(itemView,R.id.tv_fifties);
            hundreds = ViewHolder.get(itemView,R.id.tv_hundreds);
        }
    }

    private static class ProfileBowlingViewHolder extends RecyclerView.ViewHolder {
        protected TextView gameType;
        protected TextView matches;
        protected TextView wickets;
        protected TextView bbi;
        protected TextView average;
        protected TextView economy;
        protected TextView strikeRate;
        protected TextView fiveWickets;

        public ProfileBowlingViewHolder(View itemView) {
            super(itemView);
            gameType = ViewHolder.get(itemView,R.id.game_type);
            matches = ViewHolder.get(itemView,R.id.tv_matches);
            wickets = ViewHolder.get(itemView,R.id.tv_wickets);
            bbi = ViewHolder.get(itemView,R.id.tv_bbi);
            average = ViewHolder.get(itemView,R.id.tv_average);
            economy = ViewHolder.get(itemView,R.id.tv_economy);
            strikeRate = ViewHolder.get(itemView,R.id.tv_strike_rate);
            fiveWickets = ViewHolder.get(itemView,R.id.tv_five_wickets);
        }
    }
}
