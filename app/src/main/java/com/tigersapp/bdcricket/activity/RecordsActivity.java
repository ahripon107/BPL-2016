package com.tigersapp.bdcricket.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.tigersapp.bdcricket.R;
import com.tigersapp.bdcricket.adapter.MatchDetailsViewPagerAdapter;
import com.tigersapp.bdcricket.fragment.RecordsFragment;
import com.tigersapp.bdcricket.util.Constants;
import com.tigersapp.bdcricket.util.Dialogs;
import com.tigersapp.bdcricket.util.FetchFromWeb;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * @author Ripon
 */
public class RecordsActivity extends AppCompatActivity {

    MatchDetailsViewPagerAdapter matchDetailsViewPagerAdapter;
    ViewPager viewPager;
    Gson gson;
    TabLayout tabLayout;
    AdView adView;
    RecordsFragment battingRecordsFragment, bowlingRecordsFragment, fastestRecordsFragment;
    Dialogs dialogs;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_details);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        dialogs = new Dialogs(this);

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

        String url = Constants.RECORDS_URL;
        Log.d(Constants.TAG, url);

        dialogs.showDialog();
        FetchFromWeb.get(url, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                dialogs.dismissDialog();
                try {
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
                Log.d(Constants.TAG, response.toString());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                dialogs.dismissDialog();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                dialogs.dismissDialog();
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
