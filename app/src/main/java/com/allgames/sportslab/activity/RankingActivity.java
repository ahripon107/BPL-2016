package com.allgames.sportslab.activity;

import android.os.Bundle;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.allgames.sportslab.R;
import com.allgames.sportslab.adapter.MatchDetailsViewPagerAdapter;
import com.allgames.sportslab.fragment.RankingFragment;
import com.allgames.sportslab.util.Constants;
import com.allgames.sportslab.util.DefaultMessageHandler;
import com.allgames.sportslab.util.NetworkService;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.inject.Inject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import roboguice.inject.ContentView;

/**
 * @author Ripon
 */
@ContentView(R.layout.activity_ranking)
public class RankingActivity extends CommonActivity {

    JSONObject response;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private Spinner spinner;
    private AdView adView;
    private MatchDetailsViewPagerAdapter matchDetailsViewPagerAdapter;
    private RankingFragment testFragment;
    private RankingFragment odiFragment;
    private RankingFragment T20Fragment;
    @Inject
    private NetworkService networkService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tabLayout = findViewById(R.id.tab_ranking);
        viewPager = findViewById(R.id.view_pager_ranking);
        spinner = findViewById(R.id.spinner);
        adView = findViewById(R.id.adViewMatchDetails);
        testFragment = new RankingFragment();
        odiFragment = new RankingFragment();
        T20Fragment = new RankingFragment();

        List<String> categories = new ArrayList<String>();
        categories.add("Top Teams");
        categories.add("Top Batsmen");
        categories.add("Top Bowlers");
        categories.add("Top Allrounders");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);

        setupViewPage(viewPager);
        viewPager.setOffscreenPageLimit(2);
        tabLayout.setupWithViewPager(viewPager);
        String url = Constants.RANKING_URL;
        Log.d(Constants.TAG, url);

        networkService.fetchRanking(new DefaultMessageHandler(this, true) {
            @Override
            public void onSuccess(Message msg) {
                try {
                    response = new JSONObject((String) msg.obj);
                    if (testFragment.isAdded())
                        testFragment.populateTeamList(response.getJSONObject("Team").getJSONArray("TEST"));
                    if (odiFragment.isAdded())
                        odiFragment.populateTeamList(response.getJSONObject("Team").getJSONArray("ODI"));
                    if (T20Fragment.isAdded())
                        T20Fragment.populateTeamList(response.getJSONObject("Team").getJSONArray("T20"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if (position == 0) {
                            try {
                                testFragment.populateTeamList(response.getJSONObject("Team").getJSONArray("TEST"));
                                odiFragment.populateTeamList(response.getJSONObject("Team").getJSONArray("ODI"));
                                T20Fragment.populateTeamList(response.getJSONObject("Team").getJSONArray("T20"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else if (position == 1) {
                            try {
                                testFragment.populatePlayerList(response.getJSONObject("Player").getJSONObject("TEST").getJSONArray("batting"));
                                odiFragment.populatePlayerList(response.getJSONObject("Player").getJSONObject("ODI").getJSONArray("batting"));
                                T20Fragment.populatePlayerList(response.getJSONObject("Player").getJSONObject("T20").getJSONArray("batting"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else if (position == 2) {
                            try {
                                testFragment.populatePlayerList(response.getJSONObject("Player").getJSONObject("TEST").getJSONArray("bowling"));
                                odiFragment.populatePlayerList(response.getJSONObject("Player").getJSONObject("ODI").getJSONArray("bowling"));
                                T20Fragment.populatePlayerList(response.getJSONObject("Player").getJSONObject("T20").getJSONArray("bowling"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else if (position == 3) {
                            try {
                                testFragment.populatePlayerList(response.getJSONObject("Player").getJSONObject("TEST").getJSONArray("allrounder"));
                                odiFragment.populatePlayerList(response.getJSONObject("Player").getJSONObject("ODI").getJSONArray("allrounder"));
                                T20Fragment.populatePlayerList(response.getJSONObject("Player").getJSONObject("T20").getJSONArray("allrounder"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                Log.d(Constants.TAG, response.toString());
            }
        });

        AdRequest adRequest = new AdRequest.Builder().addTestDevice(Constants.ONE_PLUS_TEST_DEVICE)
                .addTestDevice(Constants.XIAOMI_TEST_DEVICE).build();
        adView.loadAd(adRequest);
    }

    private void setupViewPage(ViewPager viewPager) {
        this.matchDetailsViewPagerAdapter = new MatchDetailsViewPagerAdapter(getSupportFragmentManager());
        this.matchDetailsViewPagerAdapter.addFragment(testFragment, "Test");
        this.matchDetailsViewPagerAdapter.addFragment(odiFragment, "ODI");
        this.matchDetailsViewPagerAdapter.addFragment(T20Fragment, "T20I");
        viewPager.setAdapter(this.matchDetailsViewPagerAdapter);
    }
}
