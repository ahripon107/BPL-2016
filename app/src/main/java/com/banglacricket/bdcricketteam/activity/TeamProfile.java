package com.banglacricket.bdcricketteam.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.banglacricket.bdcricketteam.R;
import com.banglacricket.bdcricketteam.adapter.TeamProfileAdapter;
import com.banglacricket.bdcricketteam.util.Constants;

import java.util.ArrayList;

import roboguice.inject.ContentView;

/**
 * @author ripon
 */
@ContentView(R.layout.playersfragment)
public class TeamProfile extends CommonActivity {

    ArrayList<String> teams, teamImages;
    TeamProfileAdapter teamProfileAdapter;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        teams = new ArrayList<>();
        teams.add("অস্ট্রেলিয়া");
        teams.add("বাংলাদেশ");
        teams.add("ইংল্যান্ড");
        teams.add("ভারত");
        teams.add("নিউজিল্যান্ড");
        teams.add("পাকিস্তান");
        teams.add("সাউথ আফ্রিকা");
        teams.add("শ্রীলঙ্কা");
        teams.add("ওয়েস্ট ইন্ডিজ");
        teams.add("জিম্বাবুয়ে");
        teams.add("বারমুডা");
        teams.add("কানাডা");
        teams.add("আয়ারল্যান্ড");
        teams.add("কেনিয়া");
        teams.add("নেদারল্যান্ড");
        teams.add("স্কটল্যান্ড");

        teamImages = new ArrayList<>();
        teamImages.add(Constants.AUS_TEAM_LOGO_URL);
        teamImages.add(Constants.BD_TEAM_LOGO_URL);
        teamImages.add(Constants.ENG_TEAM_LOGO_URL);
        teamImages.add(Constants.IND_TEAm_LOGO_URL);
        teamImages.add(Constants.NZ_TEAM_LOGO_URL);
        teamImages.add(Constants.PAK_TEAM_LOGO_URL);
        teamImages.add(Constants.SA_TEAM_LOGO_URL);
        teamImages.add(Constants.SL_TEAM_LOGO_URL);
        teamImages.add(Constants.WI_TEAM_LOGO_URL);
        teamImages.add(Constants.ZIM_TEAM_LOGO_URL);
        teamImages.add(Constants.BER_TEAM_LOGO_URL);
        teamImages.add(Constants.CAN_TEAM_LOGO_URL);
        teamImages.add(Constants.IRE_TEAM_LOGO_URL);
        teamImages.add(Constants.KEN_TEAM_LOGO_URL);
        teamImages.add(Constants.NED_TEAM_LOGO_URL);
        teamImages.add(Constants.SCO_TEAM_LOGO_URL);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        teamProfileAdapter = new TeamProfileAdapter(this, teams, teamImages);
        recyclerView.setAdapter(teamProfileAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
