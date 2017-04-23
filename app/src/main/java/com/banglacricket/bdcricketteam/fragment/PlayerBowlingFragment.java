package com.banglacricket.bdcricketteam.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.inject.Inject;
import com.banglacricket.bdcricketteam.R;
import com.banglacricket.bdcricketteam.adapter.BasicListAdapter;
import com.banglacricket.bdcricketteam.model.ProfileBattingBowlingRow;
import com.banglacricket.bdcricketteam.model.ProfileBowling;
import com.banglacricket.bdcricketteam.util.DividerItemDecoration;
import com.banglacricket.bdcricketteam.util.ViewHolder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import roboguice.fragment.RoboFragment;
import roboguice.inject.InjectView;

/**
 * @author Ripon
 */

public class PlayerBowlingFragment extends RoboFragment {


    @InjectView(R.id.recycler_view)
    private RecyclerView recyclerView;

    @Inject
    private ArrayList<ProfileBattingBowlingRow> profileBattingRows;

    private ProfileBattingBowlingRow matches, innings, wickets, runs, balls, economy, average, strikeRate,
            bbi, bbm, fourWkts, fiveWkts, tenWkts, headline;

    private JSONObject response;
    private String data;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.recycler_view_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        data = getArguments().getString("data");

        matches = new ProfileBattingBowlingRow();
        innings = new ProfileBattingBowlingRow();
        wickets = new ProfileBattingBowlingRow();
        runs = new ProfileBattingBowlingRow();
        balls = new ProfileBattingBowlingRow();
        economy = new ProfileBattingBowlingRow();
        average = new ProfileBattingBowlingRow();
        strikeRate = new ProfileBattingBowlingRow();
        bbi = new ProfileBattingBowlingRow();
        bbm = new ProfileBattingBowlingRow();
        fourWkts = new ProfileBattingBowlingRow();
        fiveWkts = new ProfileBattingBowlingRow();
        tenWkts = new ProfileBattingBowlingRow();
        headline = new ProfileBattingBowlingRow();

        try {
            response = new JSONObject(data);
            JSONObject battingObject = response.getJSONObject("data").getJSONObject("bowling");

            if (battingObject.has("tests")) {
                ProfileBowling profileBat = processProfileBowling(battingObject.getJSONObject("tests"));


                matches.setTest(profileBat.getMat());
                innings.setTest(profileBat.getInns());
                wickets.setTest(profileBat.getWkts());
                runs.setTest(profileBat.getRuns());
                balls.setTest(profileBat.getBalls());
                economy.setTest(profileBat.getEcon());
                average.setTest(profileBat.getAve());
                strikeRate.setTest(profileBat.getSR());
                bbi.setTest(profileBat.getBBI());
                bbm.setTest(profileBat.getBBM());
                fourWkts.setTest(profileBat.getFourWkts());
                fiveWkts.setTest(profileBat.getFiveWkts());
                tenWkts.setTest(profileBat.getTenWkts());
            } else {
                matches.setTest("-");
                innings.setTest("-");
                wickets.setTest("-");
                runs.setTest("-");
                balls.setTest("-");
                economy.setTest("-");
                average.setTest("-");
                strikeRate.setTest("-");
                bbi.setTest("-");
                bbm.setTest("-");
                fourWkts.setTest("-");
                fiveWkts.setTest("-");
                tenWkts.setTest("-");
            }

            if (battingObject.has("ODIs")) {
                ProfileBowling profileBat = processProfileBowling(battingObject.getJSONObject("ODIs"));

                matches.setOdi(profileBat.getMat());
                innings.setOdi(profileBat.getInns());
                wickets.setOdi(profileBat.getWkts());
                runs.setOdi(profileBat.getRuns());
                balls.setOdi(profileBat.getBalls());
                economy.setOdi(profileBat.getEcon());
                average.setOdi(profileBat.getAve());
                strikeRate.setOdi(profileBat.getSR());
                bbi.setOdi(profileBat.getBBI());
                bbm.setOdi(profileBat.getBBM());
                fourWkts.setOdi(profileBat.getFourWkts());
                fiveWkts.setOdi(profileBat.getFiveWkts());
                tenWkts.setOdi(profileBat.getTenWkts());
            } else {
                matches.setOdi("-");
                innings.setOdi("-");
                wickets.setOdi("-");
                runs.setOdi("-");
                balls.setOdi("-");
                economy.setOdi("-");
                average.setOdi("-");
                strikeRate.setOdi("-");
                bbi.setOdi("-");
                bbm.setOdi("-");
                fourWkts.setOdi("-");
                fiveWkts.setOdi("-");
                tenWkts.setOdi("-");
            }
            if (battingObject.has("T20Is")) {
                ProfileBowling profileBat = processProfileBowling(battingObject.getJSONObject("T20Is"));

                matches.setT20i(profileBat.getMat());
                innings.setT20i(profileBat.getInns());
                wickets.setT20i(profileBat.getWkts());
                runs.setT20i(profileBat.getRuns());
                balls.setT20i(profileBat.getBalls());
                economy.setT20i(profileBat.getEcon());
                average.setT20i(profileBat.getAve());
                strikeRate.setT20i(profileBat.getSR());
                bbi.setT20i(profileBat.getBBI());
                bbm.setT20i(profileBat.getBBM());
                fourWkts.setT20i(profileBat.getFourWkts());
                fiveWkts.setT20i(profileBat.getFiveWkts());
                tenWkts.setT20i(profileBat.getTenWkts());
            } else {
                matches.setT20i("-");
                innings.setT20i("-");
                wickets.setT20i("-");
                runs.setT20i("-");
                balls.setT20i("-");
                economy.setT20i("-");
                average.setT20i("-");
                strikeRate.setT20i("-");
                bbi.setT20i("-");
                bbm.setT20i("-");
                fourWkts.setT20i("-");
                fiveWkts.setT20i("-");
                tenWkts.setT20i("-");
            }
            if (battingObject.has("firstClass")) {
                ProfileBowling profileBat = processProfileBowling(battingObject.getJSONObject("firstClass"));

                matches.setFirstClass(profileBat.getMat());
                innings.setFirstClass(profileBat.getInns());
                wickets.setFirstClass(profileBat.getWkts());
                runs.setFirstClass(profileBat.getRuns());
                balls.setFirstClass(profileBat.getBalls());
                economy.setFirstClass(profileBat.getEcon());
                average.setFirstClass(profileBat.getAve());
                strikeRate.setFirstClass(profileBat.getSR());
                bbi.setFirstClass(profileBat.getBBI());
                bbm.setFirstClass(profileBat.getBBM());
                fourWkts.setFirstClass(profileBat.getFourWkts());
                fiveWkts.setFirstClass(profileBat.getFiveWkts());
                tenWkts.setFirstClass(profileBat.getTenWkts());
            } else {
                matches.setFirstClass("-");
                innings.setFirstClass("-");
                wickets.setFirstClass("-");
                runs.setFirstClass("-");
                balls.setFirstClass("-");
                economy.setFirstClass("-");
                average.setFirstClass("-");
                strikeRate.setFirstClass("-");
                bbi.setFirstClass("-");
                bbm.setFirstClass("-");
                fourWkts.setFirstClass("-");
                fiveWkts.setFirstClass("-");
                tenWkts.setFirstClass("-");
            }
            if (battingObject.has("listA")) {
                ProfileBowling profileBat = processProfileBowling(battingObject.getJSONObject("listA"));

                matches.setListA(profileBat.getMat());
                innings.setListA(profileBat.getInns());
                wickets.setListA(profileBat.getWkts());
                runs.setListA(profileBat.getRuns());
                balls.setListA(profileBat.getBalls());
                economy.setListA(profileBat.getEcon());
                average.setListA(profileBat.getAve());
                strikeRate.setListA(profileBat.getSR());
                bbi.setListA(profileBat.getBBI());
                bbm.setListA(profileBat.getBBM());
                fourWkts.setListA(profileBat.getFourWkts());
                fiveWkts.setListA(profileBat.getFiveWkts());
                tenWkts.setListA(profileBat.getTenWkts());
            } else {
                matches.setListA("-");
                innings.setListA("-");
                wickets.setListA("-");
                runs.setListA("-");
                balls.setListA("-");
                economy.setListA("-");
                average.setListA("-");
                strikeRate.setListA("-");
                bbi.setListA("-");
                bbm.setListA("-");
                fourWkts.setListA("-");
                fiveWkts.setListA("-");
                tenWkts.setListA("-");
            }
            if (battingObject.has("twenty20")) {
                ProfileBowling profileBat = processProfileBowling(battingObject.getJSONObject("twenty20"));

                matches.setTwenty20(profileBat.getMat());
                innings.setTwenty20(profileBat.getInns());
                wickets.setTwenty20(profileBat.getWkts());
                runs.setTwenty20(profileBat.getRuns());
                balls.setTwenty20(profileBat.getBalls());
                economy.setTwenty20(profileBat.getEcon());
                average.setTwenty20(profileBat.getAve());
                strikeRate.setTwenty20(profileBat.getSR());
                bbi.setTwenty20(profileBat.getBBI());
                bbm.setTwenty20(profileBat.getBBM());
                fourWkts.setTwenty20(profileBat.getFourWkts());
                fiveWkts.setTwenty20(profileBat.getFiveWkts());
                tenWkts.setTwenty20(profileBat.getTenWkts());
            } else {
                matches.setTwenty20("-");
                innings.setTwenty20("-");
                wickets.setTwenty20("-");
                runs.setTwenty20("-");
                balls.setTwenty20("-");
                economy.setTwenty20("-");
                average.setTwenty20("-");
                strikeRate.setTwenty20("-");
                bbi.setTwenty20("-");
                bbm.setTwenty20("-");
                fourWkts.setTwenty20("-");
                fiveWkts.setTwenty20("-");
                tenWkts.setTwenty20("-");
            }

            matches.setProperty("Matches");
            innings.setProperty("Innings");
            wickets.setProperty("Wickets");
            runs.setProperty("Runs");
            balls.setProperty("Balls");
            economy.setProperty("Economy");
            average.setProperty("Average");
            strikeRate.setProperty("SR");
            bbi.setProperty("BBI");
            bbm.setProperty("BBM");
            fourWkts.setProperty("4WI");
            fiveWkts.setProperty("5WI");
            tenWkts.setProperty("10WM");

            headline.setProperty("");
            headline.setTest("Test");
            headline.setOdi("ODI");
            headline.setT20i("T20I");
            headline.setFirstClass("FC");
            headline.setListA("ListA");
            headline.setTwenty20("T20");

            profileBattingRows.add(headline);
            profileBattingRows.add(matches);
            profileBattingRows.add(innings);
            profileBattingRows.add(wickets);
            profileBattingRows.add(runs);
            profileBattingRows.add(balls);
            profileBattingRows.add(economy);
            profileBattingRows.add(average);
            profileBattingRows.add(strikeRate);
            profileBattingRows.add(bbi);
            profileBattingRows.add(bbm);
            profileBattingRows.add(fourWkts);
            profileBattingRows.add(fiveWkts);
            profileBattingRows.add(tenWkts);


            recyclerView.setAdapter(new BasicListAdapter<ProfileBattingBowlingRow, ProfileBowlingViewHolder>(profileBattingRows) {
                @Override
                public ProfileBowlingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_profile_batting_bowling, parent, false);
                    return new ProfileBowlingViewHolder(view);
                }

                @Override
                public void onBindViewHolder(ProfileBowlingViewHolder holder, int position) {
                    ProfileBattingBowlingRow profileBtng = profileBattingRows.get(position);
                    holder.property.setText(profileBtng.getProperty());
                    holder.test.setText(profileBtng.getTest());
                    holder.odi.setText(profileBtng.getOdi());
                    holder.t20i.setText(profileBtng.getT20i());
                    holder.fc.setText(profileBtng.getFirstClass());
                    holder.lista.setText(profileBtng.getListA());
                    holder.t20.setText(profileBtng.getTwenty20());

                    if (position == 0) {
                        holder.linearlayout.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.batsmanbowlerbackground));
                    }
                }
            });

            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.addItemDecoration(new DividerItemDecoration(getContext()));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private static class ProfileBowlingViewHolder extends RecyclerView.ViewHolder {
        protected TextView property;
        protected TextView test;
        protected TextView odi;
        protected TextView t20i;
        protected TextView fc;
        protected TextView lista;
        protected TextView t20;
        protected LinearLayout linearlayout;

        public ProfileBowlingViewHolder(View itemView) {
            super(itemView);
            property = ViewHolder.get(itemView, R.id.game_type);
            test = ViewHolder.get(itemView, R.id.tv_test);
            odi = ViewHolder.get(itemView, R.id.tv_odi);
            t20i = ViewHolder.get(itemView, R.id.tv_t20i);
            fc = ViewHolder.get(itemView, R.id.tv_fc);
            lista = ViewHolder.get(itemView, R.id.tv_lista);
            t20 = ViewHolder.get(itemView, R.id.tv_t20);
            linearlayout = ViewHolder.get(itemView, R.id.layout);
        }
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
            String tenWkts = jsonObject.getString("10");
            profileBowling = new ProfileBowling(Mat, Inns, balls, Runs, wkts, BBI, BBM, Ave, Econ, SR, fourWkts, fiveWkts, tenWkts);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return profileBowling;
    }
}
