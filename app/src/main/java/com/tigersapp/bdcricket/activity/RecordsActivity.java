package com.tigersapp.bdcricket.activity;

import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.gson.Gson;
import com.google.inject.Inject;
import com.tigersapp.bdcricket.R;
import com.tigersapp.bdcricket.adapter.MatchDetailsViewPagerAdapter;
import com.tigersapp.bdcricket.fragment.RecordsFragment;
import com.tigersapp.bdcricket.util.Constants;
import com.tigersapp.bdcricket.util.DefaultMessageHandler;
import com.tigersapp.bdcricket.util.NetworkService;
import com.tigersapp.bdcricket.util.RoboAppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import roboguice.inject.ContentView;

/**
 * @author Ripon
 */
@ContentView(R.layout.activity_match_details)
public class RecordsActivity extends RoboAppCompatActivity {

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

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
