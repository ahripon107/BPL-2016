package com.allgames.sportslab.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.inject.Inject;
import com.allgames.sportslab.R;
import com.allgames.sportslab.adapter.BasicListAdapter;
import com.allgames.sportslab.model.ProfileBattingBowlingRow;
import com.allgames.sportslab.model.ProfileBowling;
import com.allgames.sportslab.util.ViewHolder;

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
            JSONObject battingObject = response.getJSONObject("bowling");
            if (battingObject.has("stats")) {
                battingObject = battingObject.getJSONObject("stats");
            }

            if (battingObject.has("test")) {
                ProfileBowling profileBat = processProfileBowling(battingObject.getJSONObject("test"));


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

            if (battingObject.has("odi")) {
                ProfileBowling profileBat = processProfileBowling(battingObject.getJSONObject("odi"));

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
            if (battingObject.has("t20")) {
                ProfileBowling profileBat = processProfileBowling(battingObject.getJSONObject("t20"));

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
            if (battingObject.has("ipl")) {
                ProfileBowling profileBat = processProfileBowling(battingObject.getJSONObject("ipl"));

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
            if (battingObject.has("cl")) {
                ProfileBowling profileBat = processProfileBowling(battingObject.getJSONObject("cl"));

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
            headline.setFirstClass("IPL");
            headline.setListA("CL");

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
                    holder.ipl.setText(profileBtng.getFirstClass());
                    holder.cl.setText(profileBtng.getListA());

                    if (position == 0) {
                        holder.linearlayout.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.batsmanbowlerbackground));
                    }
                }
            });

            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private static class ProfileBowlingViewHolder extends RecyclerView.ViewHolder {
        TextView property;
        TextView test;
        TextView odi;
        TextView t20i;
        TextView ipl;
        TextView cl;
        LinearLayout linearlayout;

        ProfileBowlingViewHolder(View itemView) {
            super(itemView);
            property = ViewHolder.get(itemView, R.id.game_type);
            test = ViewHolder.get(itemView, R.id.tv_test);
            odi = ViewHolder.get(itemView, R.id.tv_odi);
            t20i = ViewHolder.get(itemView, R.id.tv_t20i);
            ipl = ViewHolder.get(itemView, R.id.tv_ipl);
            cl = ViewHolder.get(itemView, R.id.tv_cl);
            linearlayout = ViewHolder.get(itemView, R.id.layout);
        }
    }

    private ProfileBowling processProfileBowling(JSONObject jsonObject) {
        ProfileBowling profileBowling = null;
        try {
            String Mat = jsonObject.getString("matches");
            String Inns = jsonObject.getString("innings");
            String balls = jsonObject.getString("balls");
            String Runs = jsonObject.getString("runs");
            String wkts = jsonObject.getString("wickets");
            String BBI = jsonObject.getString("bbi");
            String BBM = jsonObject.getString("bbm");
            String Ave = jsonObject.getString("avg");
            String Econ = jsonObject.getString("eco");
            String SR = jsonObject.getString("sr");
            String fourWkts = jsonObject.getString("4w");
            String fiveWkts = jsonObject.getString("5w");
            String tenWkts = jsonObject.getString("10w");
            profileBowling = new ProfileBowling(Mat, Inns, balls, Runs, wkts, BBI, BBM, Ave, Econ, SR, fourWkts, fiveWkts, tenWkts);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return profileBowling;
    }
}
