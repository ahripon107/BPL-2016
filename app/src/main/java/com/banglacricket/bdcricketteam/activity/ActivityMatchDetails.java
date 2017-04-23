package com.banglacricket.bdcricketteam.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.banglacricket.bdcricketteam.R;
import com.banglacricket.bdcricketteam.adapter.MatchDetailsViewPagerAdapter;
import com.banglacricket.bdcricketteam.fragment.FragmentMatchSummary;
import com.banglacricket.bdcricketteam.fragment.FragmentScoreBoard;
import com.banglacricket.bdcricketteam.fragment.GossipFragment;
import com.banglacricket.bdcricketteam.fragment.PlayingXIFragment;
import com.banglacricket.bdcricketteam.util.Constants;

import org.json.JSONArray;

import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

/**
 * @author ripon
 */
@ContentView(R.layout.activity_match_details)
public class ActivityMatchDetails extends CommonActivity {

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
        this.matchDetailsViewPagerAdapter.addFragment(new PlayingXIFragment(), "একাদশ");
        GossipFragment fragment = new GossipFragment();
        fragment.setArguments(bundle);
        this.matchDetailsViewPagerAdapter.addFragment(fragment, "আড্ডা");
        viewPager.setAdapter(this.matchDetailsViewPagerAdapter);
    }

    public void setPlayingXI(JSONArray batTeam1, JSONArray dnbTeam1, JSONArray batTeam2, JSONArray dnbTeam2, String team1Name, String team2Name) {
        ((PlayingXIFragment) ActivityMatchDetails.this.matchDetailsViewPagerAdapter.getItem(2)).setPlayingXI(batTeam1, dnbTeam1, batTeam2, dnbTeam2, team1Name, team2Name);
    }
}