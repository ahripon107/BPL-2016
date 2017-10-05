package com.allgames.sportslab.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.allgames.sportslab.R;
import com.allgames.sportslab.adapter.MatchDetailsViewPagerAdapter;
import com.allgames.sportslab.fragment.FragmentMatchSummary;
import com.allgames.sportslab.fragment.FragmentScoreBoard;
import com.allgames.sportslab.fragment.GossipFragment;
import com.allgames.sportslab.fragment.PlayingXIFragment;
import com.allgames.sportslab.util.Constants;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import org.json.JSONArray;

/**
 * @author ripon
 */
public class ActivityMatchDetails extends CommonActivity {


    private ViewPager viewPager;
    private AdView adView;
    private TabLayout tabLayout;

    private String liveMatchID;
    private MatchDetailsViewPagerAdapter matchDetailsViewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initialize();

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

    private void initialize() {
        setContentView(R.layout.activity_match_details);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        adView = (AdView) findViewById(R.id.adViewMatchDetails);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
    }
}