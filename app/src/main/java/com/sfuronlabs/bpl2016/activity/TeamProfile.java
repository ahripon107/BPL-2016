package com.sfuronlabs.bpl2016.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.sfuronlabs.bpl2016.R;
import com.sfuronlabs.bpl2016.adapter.TeamProfileAdapter;
import com.sfuronlabs.bpl2016.util.Constants;

import java.util.ArrayList;

/**
 * @author ripon
 */
public class TeamProfile extends AppCompatActivity {

    ArrayList<String> teams,teamImages;
    TeamProfileAdapter teamProfileAdapter;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.playersfragment);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setTitle("Team Profile");

        teams = new ArrayList<>();
        teams.add("Australia");
        teams.add("Bangladesh");
        teams.add("England");
        teams.add("India");
        teams.add("New Zealand");
        teams.add("Pakistan");
        teams.add("South Africa");
        teams.add("Sri Lanka");
        teams.add("West Indies");
        teams.add("Zimbabwe");
        teams.add("Bermuda");
        teams.add("Canada");
        teams.add("Ireland");
        teams.add("Kenya");
        teams.add("Netherlands");
        teams.add("Scotland");

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
        teamProfileAdapter = new TeamProfileAdapter(this, teams,teamImages);
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
