package com.tigersapp.bdcricket.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.tigersapp.bdcricket.R;
import com.tigersapp.bdcricket.adapter.TeamProfileAdapter;
import com.tigersapp.bdcricket.util.Constants;

import java.util.ArrayList;

/**
 * @author ripon
 */
public class TeamProfile extends AppCompatActivity {

    ArrayList<String> teams, teamImages;
    TeamProfileAdapter teamProfileAdapter;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.playersfragment);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setTitle("টিম প্রোফাইল");

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
}
