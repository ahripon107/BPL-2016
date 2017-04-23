package com.banglacricket.bdcricketteam.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.banglacricket.bdcricketteam.R;
import com.banglacricket.bdcricketteam.adapter.TeamRecordAdapter;
import com.banglacricket.bdcricketteam.model.RecordVsOthers;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * @author ripon
 */
public class RecordFragment extends Fragment {

    RecyclerView recyclerView;
    ArrayList<RecordVsOthers> pl;
    TeamRecordAdapter teamRecordAdapter;


    public static RecordFragment newInstanceOfRecordFragment(String text, String what) {
        RecordFragment myFragment = new RecordFragment();
        Bundle arguments = new Bundle();

        arguments.putString("details", text);
        arguments.putString("what", what);

        myFragment.setArguments(arguments);
        return myFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.playersfragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        pl = new ArrayList<>();
        teamRecordAdapter = new TeamRecordAdapter(this.getActivity(), pl);
        recyclerView.setAdapter(teamRecordAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        String string = getArguments().getString("details");
        String what = getArguments().getString("what");

        try {
            JSONObject jsonObject = new JSONObject(string);

            jsonObject = jsonObject.getJSONObject("query");
            jsonObject = jsonObject.getJSONObject("results");
            jsonObject = jsonObject.getJSONObject("TeamProfile");

            JSONArray jsonArray = jsonObject.getJSONArray("Tally");

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                if (obj.getString("mtype").equals(what)) {
                    String against = obj.getString("Name");
                    String wins = obj.getString("Wins");
                    String lass = obj.getString("Losses");
                    String draw = obj.getString("NoResults");
                    String played = obj.getString("Played");
                    String highestInnings = "";
                    JSONObject obj1;
                    if (obj.has("HighestInningSummary")) {
                        obj1 = obj.getJSONObject("HighestInningSummary");
                        obj1 = obj1.getJSONObject("Score");
                        highestInnings = obj1.getString("runs") + " / " + obj1.getString("wickets");
                    }

                    obj1 = obj.getJSONObject("Tops").getJSONObject("Bowling").getJSONObject("Player");
                    String as = obj1.getString("FirstName") + " " + obj1.getString("LastName") + " - " + obj.getJSONObject("Tops").getJSONObject("Bowling").getString("RunsGiven") + " / " + obj.getJSONObject("Tops").getJSONObject("Bowling").getString("WicketsTaken");

                    obj1 = obj.getJSONObject("Tops").getJSONObject("Batting").getJSONObject("Player");
                    String bs = obj1.getString("FirstName") + " " + obj1.getString("LastName") + " - " + obj.getJSONObject("Tops").getJSONObject("Batting").getJSONObject("RunsScored").getString("content") + " (" + obj.getJSONObject("Tops").getJSONObject("Batting").getString("BallsFaced") + ")";

                    obj1 = obj.getJSONObject("Prolific").getJSONObject("Bowling").getJSONObject("Player");
                    String cs = obj1.getString("FirstName") + " " + obj1.getString("LastName") + " - " + obj.getJSONObject("Prolific").getJSONObject("Bowling").getString("WicketsTaken") + " wickets";

                    obj1 = obj.getJSONObject("Prolific").getJSONObject("Batting").getJSONObject("Player");
                    String ds = obj1.getString("FirstName") + " " + obj1.getString("LastName") + " - " + obj.getJSONObject("Prolific").getJSONObject("Batting").getString("RunsScored") + " runs";

                    RecordVsOthers recordVsOthers = new RecordVsOthers(against, played, wins, lass, draw, highestInnings, as, bs, cs, ds);
                    pl.add(recordVsOthers);
                }
            }
            teamRecordAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
