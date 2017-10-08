package com.allgames.sportslab.activity;

import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.allgames.sportslab.R;
import com.allgames.sportslab.adapter.MatchDetailsViewPagerAdapter;
import com.allgames.sportslab.fragment.RecordsFragment;
import com.allgames.sportslab.util.Constants;
import com.allgames.sportslab.util.DefaultMessageHandler;
import com.allgames.sportslab.util.NetworkService;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.gson.Gson;
import com.google.inject.Inject;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Ripon
 */
public class RecordsActivity extends CommonActivity {

    MatchDetailsViewPagerAdapter matchDetailsViewPagerAdapter;
    ViewPager viewPager;
    Gson gson;
    TabLayout tabLayout;
    AdView adView;
    RecordsFragment battingRecordsFragment, bowlingRecordsFragment, fastestRecordsFragment;

    @Inject
    private NetworkService networkService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_details);

        battingRecordsFragment = new RecordsFragment();
        bowlingRecordsFragment = new RecordsFragment();
        fastestRecordsFragment = new RecordsFragment();

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        adView = (AdView) findViewById(R.id.adViewMatchDetails);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);

        gson = new Gson();
        viewPager.setOffscreenPageLimit(2);
        setUpViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);

        AdRequest adRequest = new AdRequest.Builder().addTestDevice(Constants.ONE_PLUS_TEST_DEVICE)
                .addTestDevice(Constants.XIAOMI_TEST_DEVICE).build();
        adView.loadAd(adRequest);

        networkService.fetchRecords(new DefaultMessageHandler(this, true) {
            @Override
            public void onSuccess(Message msg) {
                String string = (String) msg.obj;
                try {
                    JSONObject response = new JSONObject(string);
                    if (battingRecordsFragment.isAdded()) {
                        battingRecordsFragment.populateFragment(response.getJSONObject("top-stats").getJSONArray("battingStats"));
                    }
                    if (bowlingRecordsFragment.isAdded()) {
                        bowlingRecordsFragment.populateFragment(response.getJSONObject("top-stats").getJSONArray("bowlingStats"));
                    }
                    if (fastestRecordsFragment.isAdded()) {
                        fastestRecordsFragment.populateFragment(response.getJSONObject("top-stats").getJSONArray("fastestStats"));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void setUpViewPager(ViewPager viewPager) {
        matchDetailsViewPagerAdapter = new MatchDetailsViewPagerAdapter(getSupportFragmentManager());

        Bundle bundle = new Bundle();
        bundle.putString("recordtype", "batting");
        battingRecordsFragment.setArguments(bundle);
        matchDetailsViewPagerAdapter.addFragment(battingRecordsFragment, "Batting Records");

        Bundle bundle1 = new Bundle();
        bundle1.putString("recordtype", "bowling");
        bowlingRecordsFragment.setArguments(bundle1);
        matchDetailsViewPagerAdapter.addFragment(bowlingRecordsFragment, "Bowling Records");

        Bundle bundle2 = new Bundle();
        bundle2.putString("recordtype", "fastest");
        fastestRecordsFragment.setArguments(bundle2);
        matchDetailsViewPagerAdapter.addFragment(fastestRecordsFragment, "Fastest Records");

        viewPager.setAdapter(matchDetailsViewPagerAdapter);
    }
}
