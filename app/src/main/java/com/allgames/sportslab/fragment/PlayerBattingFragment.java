package com.allgames.sportslab.fragment;

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
import com.allgames.sportslab.R;
import com.allgames.sportslab.adapter.BasicListAdapter;
import com.allgames.sportslab.model.ProfileBatting;
import com.allgames.sportslab.model.ProfileBattingBowlingRow;
import com.allgames.sportslab.util.DividerItemDecoration;
import com.allgames.sportslab.util.ViewHolder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import roboguice.fragment.RoboFragment;
import roboguice.inject.InjectView;

/**
 * @author Ripon
 */

public class PlayerBattingFragment extends RoboFragment {

    @InjectView(R.id.recycler_view)
    private RecyclerView recyclerView;

    @Inject
    private ArrayList<ProfileBattingBowlingRow> profileBattingRows;

    private ProfileBattingBowlingRow matches, innings, notouts, runs, balls, highestScore, average, strikeRate,
            fours, sixes, fifties, hundreds, headline;

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
        notouts = new ProfileBattingBowlingRow();
        runs = new ProfileBattingBowlingRow();
        balls = new ProfileBattingBowlingRow();
        highestScore = new ProfileBattingBowlingRow();
        average = new ProfileBattingBowlingRow();
        strikeRate = new ProfileBattingBowlingRow();
        fours = new ProfileBattingBowlingRow();
        sixes = new ProfileBattingBowlingRow();
        fifties = new ProfileBattingBowlingRow();
        hundreds = new ProfileBattingBowlingRow();
        headline = new ProfileBattingBowlingRow();

        try {
            response = new JSONObject(data);
            JSONObject battingObject = response.getJSONObject("batting");
            if (battingObject.has("stats")) {
                battingObject = battingObject.getJSONObject("stats");
            }

            if (battingObject.has("test")) {
                ProfileBatting profileBat = processProfileBatting(battingObject.getJSONObject("test"));
                if (profileBat != null) profileBat.setGametype("Tests");

                matches.setTest(profileBat.getMat());
                innings.setTest(profileBat.getInns());
                notouts.setTest(profileBat.getNO());
                runs.setTest(profileBat.getRuns());
                balls.setTest(profileBat.getBF());
                highestScore.setTest(profileBat.getHS());
                average.setTest(profileBat.getAve());
                strikeRate.setTest(profileBat.getSR());
                fours.setTest(profileBat.getFours());
                sixes.setTest(profileBat.getSixes());
                fifties.setTest(profileBat.getFifty());
                hundreds.setTest(profileBat.getHundred());
            } else {
                matches.setTest("-");
                innings.setTest("-");
                notouts.setTest("-");
                runs.setTest("-");
                balls.setTest("-");
                highestScore.setTest("-");
                average.setTest("-");
                strikeRate.setTest("-");
                fours.setTest("-");
                sixes.setTest("-");
                fifties.setTest("-");
                hundreds.setTest("-");
            }

            if (battingObject.has("odi")) {
                ProfileBatting profileBat = processProfileBatting(battingObject.getJSONObject("odi"));
                if (profileBat != null) profileBat.setGametype("ODIs");

                matches.setOdi(profileBat.getMat());
                innings.setOdi(profileBat.getInns());
                notouts.setOdi(profileBat.getNO());
                runs.setOdi(profileBat.getRuns());
                balls.setOdi(profileBat.getBF());
                highestScore.setOdi(profileBat.getHS());
                average.setOdi(profileBat.getAve());
                strikeRate.setOdi(profileBat.getSR());
                fours.setOdi(profileBat.getFours());
                sixes.setOdi(profileBat.getSixes());
                fifties.setOdi(profileBat.getFifty());
                hundreds.setOdi(profileBat.getHundred());
            } else {
                matches.setOdi("-");
                innings.setOdi("-");
                notouts.setOdi("-");
                runs.setOdi("-");
                balls.setOdi("-");
                highestScore.setOdi("-");
                average.setOdi("-");
                strikeRate.setOdi("-");
                fours.setOdi("-");
                sixes.setOdi("-");
                fifties.setOdi("-");
                hundreds.setOdi("-");
            }
            if (battingObject.has("t20")) {
                ProfileBatting profileBat = processProfileBatting(battingObject.getJSONObject("t20"));
                if (profileBat != null) profileBat.setGametype("T20Is");

                matches.setT20i(profileBat.getMat());
                innings.setT20i(profileBat.getInns());
                notouts.setT20i(profileBat.getNO());
                runs.setT20i(profileBat.getRuns());
                balls.setT20i(profileBat.getBF());
                highestScore.setT20i(profileBat.getHS());
                average.setT20i(profileBat.getAve());
                strikeRate.setT20i(profileBat.getSR());
                fours.setT20i(profileBat.getFours());
                sixes.setT20i(profileBat.getSixes());
                fifties.setT20i(profileBat.getFifty());
                hundreds.setT20i(profileBat.getHundred());
            } else {
                matches.setT20i("-");
                innings.setT20i("-");
                notouts.setT20i("-");
                runs.setT20i("-");
                balls.setT20i("-");
                highestScore.setT20i("-");
                average.setT20i("-");
                strikeRate.setT20i("-");
                fours.setT20i("-");
                sixes.setT20i("-");
                fifties.setT20i("-");
                hundreds.setT20i("-");
            }
            if (battingObject.has("ipl")) {
                ProfileBatting profileBat = processProfileBatting(battingObject.getJSONObject("ipl"));
                if (profileBat != null) profileBat.setGametype("IPL");

                matches.setFirstClass(profileBat.getMat());
                innings.setFirstClass(profileBat.getInns());
                notouts.setFirstClass(profileBat.getNO());
                runs.setFirstClass(profileBat.getRuns());
                balls.setFirstClass(profileBat.getBF());
                highestScore.setFirstClass(profileBat.getHS());
                average.setFirstClass(profileBat.getAve());
                strikeRate.setFirstClass(profileBat.getSR());
                fours.setFirstClass(profileBat.getFours());
                sixes.setFirstClass(profileBat.getSixes());
                fifties.setFirstClass(profileBat.getFifty());
                hundreds.setFirstClass(profileBat.getHundred());
            } else {
                matches.setFirstClass("-");
                innings.setFirstClass("-");
                notouts.setFirstClass("-");
                runs.setFirstClass("-");
                balls.setFirstClass("-");
                highestScore.setFirstClass("-");
                average.setFirstClass("-");
                strikeRate.setFirstClass("-");
                fours.setFirstClass("-");
                sixes.setFirstClass("-");
                fifties.setFirstClass("-");
                hundreds.setFirstClass("-");
            }
            if (battingObject.has("cl")) {
                ProfileBatting profileBat = processProfileBatting(battingObject.getJSONObject("cl"));
                if (profileBat != null) profileBat.setGametype("CL");

                matches.setListA(profileBat.getMat());
                innings.setListA(profileBat.getInns());
                notouts.setListA(profileBat.getNO());
                runs.setListA(profileBat.getRuns());
                balls.setListA(profileBat.getBF());
                highestScore.setListA(profileBat.getHS());
                average.setListA(profileBat.getAve());
                strikeRate.setListA(profileBat.getSR());
                fours.setListA(profileBat.getFours());
                sixes.setListA(profileBat.getSixes());
                fifties.setListA(profileBat.getFifty());
                hundreds.setListA(profileBat.getHundred());
            } else {
                matches.setListA("-");
                innings.setListA("-");
                notouts.setListA("-");
                runs.setListA("-");
                balls.setListA("-");
                highestScore.setListA("-");
                average.setListA("-");
                strikeRate.setListA("-");
                fours.setListA("-");
                sixes.setListA("-");
                fifties.setListA("-");
                hundreds.setListA("-");
            }

            matches.setProperty("Matches");
            innings.setProperty("Innings");
            notouts.setProperty("NotOuts");
            runs.setProperty("Runs");
            balls.setProperty("Balls");
            highestScore.setProperty("Highest");
            average.setProperty("Average");
            strikeRate.setProperty("SR");
            fours.setProperty("Fours");
            sixes.setProperty("Sixes");
            fifties.setProperty("50S");
            hundreds.setProperty("100S");

            headline.setProperty("");
            headline.setTest("Test");
            headline.setOdi("ODI");
            headline.setT20i("T20I");
            headline.setFirstClass("IPL");
            headline.setListA("CL");

            profileBattingRows.add(headline);
            profileBattingRows.add(matches);
            profileBattingRows.add(innings);
            profileBattingRows.add(notouts);
            profileBattingRows.add(runs);
            profileBattingRows.add(balls);
            profileBattingRows.add(highestScore);
            profileBattingRows.add(average);
            profileBattingRows.add(strikeRate);
            profileBattingRows.add(fours);
            profileBattingRows.add(sixes);
            profileBattingRows.add(fifties);
            profileBattingRows.add(hundreds);


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
            recyclerView.addItemDecoration(new DividerItemDecoration(getContext()));
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

    private ProfileBatting processProfileBatting(JSONObject jsonObject) {
        ProfileBatting profileBatting = null;
        try {
            String fifty = jsonObject.getString("50s");
            String hundred = jsonObject.getString("100s");
            String Mat = jsonObject.getString("matches");
            String Inns = jsonObject.getString("innings");
            String NO = jsonObject.getString("no");
            String Runs = jsonObject.getString("runs");
            String HS = jsonObject.getString("highest");
            String Ave = jsonObject.getString("avg");
            String BF = jsonObject.getString("balls");
            String SR = jsonObject.getString("sr");
            String fours = jsonObject.getString("4s");
            String sixes = jsonObject.getString("6s");
            profileBatting = new ProfileBatting(fifty, hundred, Mat, Inns, NO, Runs, HS, Ave, BF, SR, fours, sixes);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return profileBatting;
    }
}
