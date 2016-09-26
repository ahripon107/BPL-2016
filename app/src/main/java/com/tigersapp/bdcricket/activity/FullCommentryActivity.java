package com.tigersapp.bdcricket.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.tigersapp.bdcricket.R;
import com.tigersapp.bdcricket.adapter.MatchDetailsViewPagerAdapter;
import com.tigersapp.bdcricket.fragment.FullCommentryFragment;
import com.tigersapp.bdcricket.util.Constants;
import com.tigersapp.bdcricket.util.RoboAppCompatActivity;

import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

/**
 * @author Ripon
 */
@ContentView(R.layout.activity_match_details)
public class FullCommentryActivity extends RoboAppCompatActivity {

    @InjectView(R.id.viewPager)
    private ViewPager viewPager;

    @InjectView(R.id.tabLayout)
    private TabLayout tabLayout;

    int numberOfInnings;
    String id;

    @InjectView(R.id.adViewMatchDetails)
    AdView adView;

    private MatchDetailsViewPagerAdapter matchDetailsViewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        numberOfInnings = getIntent().getIntExtra("numberofinnings",0);
        id = getIntent().getStringExtra("id");
        this.viewPager.setOffscreenPageLimit(3);
        setupViewPage(this.viewPager);
        tabLayout.setupWithViewPager(viewPager);

        AdRequest adRequest = new AdRequest.Builder().addTestDevice(Constants.ONE_PLUS_TEST_DEVICE)
                .addTestDevice(Constants.XIAOMI_TEST_DEVICE).build();
        adView.loadAd(adRequest);
    }

    public void setupViewPage(ViewPager viewPager) {
        this.matchDetailsViewPagerAdapter = new MatchDetailsViewPagerAdapter(getSupportFragmentManager());
        for (int i=1;i<=numberOfInnings;i++) {
            FullCommentryFragment fullCommentryFragment = new FullCommentryFragment();
            Bundle bundle = new Bundle();
            bundle.putString("url","http://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20cricket.commentary%20where%20match_id="+id+"%20and%20innings_id="+i+"&format=json&diagnostics=false&env=store%3A%2F%2F0TxIGQMQbObzvU4Apia0V0&callback=");
            fullCommentryFragment.setArguments(bundle);
            this.matchDetailsViewPagerAdapter.addFragment(fullCommentryFragment,"Innings "+i);
        }
        viewPager.setAdapter(this.matchDetailsViewPagerAdapter);
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
