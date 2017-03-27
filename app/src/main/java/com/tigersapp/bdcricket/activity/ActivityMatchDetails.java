package com.tigersapp.bdcricket.activity;

import android.os.Bundle;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.gson.Gson;
import com.google.inject.Inject;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.tigersapp.bdcricket.R;
import com.tigersapp.bdcricket.adapter.MatchDetailsViewPagerAdapter;
import com.tigersapp.bdcricket.fragment.FragmentMatchSummary;
import com.tigersapp.bdcricket.fragment.FragmentScoreBoard;
import com.tigersapp.bdcricket.fragment.GossipFragment;
import com.tigersapp.bdcricket.fragment.PlayingXIFragment;
import com.tigersapp.bdcricket.model.Commentry;
import com.tigersapp.bdcricket.model.Summary;
import com.tigersapp.bdcricket.util.Constants;
import com.tigersapp.bdcricket.util.DefaultMessageHandler;
import com.tigersapp.bdcricket.util.Dialogs;
import com.tigersapp.bdcricket.util.FetchFromWeb;
import com.tigersapp.bdcricket.util.NetworkService;
import com.tigersapp.bdcricket.util.RoboAppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

/**
 * @author ripon
 */
@ContentView(R.layout.activity_match_details)
public class ActivityMatchDetails extends RoboAppCompatActivity {

    @InjectView(R.id.viewPager)
    private ViewPager viewPager;

    @InjectView(R.id.adViewMatchDetails)
    private AdView adView;

    @InjectView(R.id.tabLayout)
    private TabLayout tabLayout;

    private String liveMatchID;
    private MatchDetailsViewPagerAdapter matchDetailsViewPagerAdapter;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.liveMatchID = getIntent().getStringExtra("matchID");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewPager.setOffscreenPageLimit(4);
        setupViewPage(this.viewPager);
        tabLayout.setupWithViewPager(viewPager);

        AdRequest adRequest = new AdRequest.Builder().addTestDevice(Constants.ONE_PLUS_TEST_DEVICE)
                .addTestDevice(Constants.XIAOMI_TEST_DEVICE).build();
        adView.loadAd(adRequest);
    }


    public final void setupViewPage(ViewPager viewPager) {
        this.matchDetailsViewPagerAdapter = new MatchDetailsViewPagerAdapter(getSupportFragmentManager());
        Bundle bundle = new Bundle();
        bundle.putString("liveMatchID", liveMatchID);

        FragmentScoreBoard fragmentScoreBoard = new FragmentScoreBoard();
        fragmentScoreBoard.setArguments(bundle);
        this.matchDetailsViewPagerAdapter.addFragment(fragmentScoreBoard, "স্কোর বোর্ড");
        FragmentMatchSummary fragmentMatchSummary = new FragmentMatchSummary();

        fragmentMatchSummary.setArguments(bundle);
        this.matchDetailsViewPagerAdapter.addFragment(fragmentMatchSummary, "কমেন্ট্রি");
        this.matchDetailsViewPagerAdapter.addFragment(new PlayingXIFragment(),"একাদশ");
        GossipFragment fragment = new GossipFragment();
        fragment.setArguments(bundle);
        this.matchDetailsViewPagerAdapter.addFragment(fragment, "আড্ডা");
        viewPager.setAdapter(this.matchDetailsViewPagerAdapter);
    }

    public void setPlayingXI(JSONArray batTeam1,JSONArray dnbTeam1,JSONArray batTeam2,JSONArray dnbTeam2,String team1Name,String team2Name) {
        ((PlayingXIFragment) ActivityMatchDetails.this.matchDetailsViewPagerAdapter.getItem(2)).setPlayingXI(batTeam1, dnbTeam1, batTeam2, dnbTeam2, team1Name, team2Name);
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