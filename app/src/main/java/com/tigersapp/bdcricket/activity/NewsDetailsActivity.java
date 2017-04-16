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
import com.tigersapp.bdcricket.fragment.NewsCommentsFragment;
import com.tigersapp.bdcricket.fragment.NewsDetailsFragment;
import com.tigersapp.bdcricket.util.Constants;
import com.tigersapp.bdcricket.util.RoboAppCompatActivity;

import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

/**
 * @author ripon
 */
@ContentView(R.layout.newsdetails)
public class NewsDetailsActivity extends RoboAppCompatActivity {

    public static final String EXTRA_NEWS_OBJECT = "newsobject";

    @InjectView(R.id.adViewNewsDetails)
    private AdView adView;

    @InjectView(R.id.viewPager)
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle("বিস্তারিত");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        this.viewPager.setOffscreenPageLimit(2);
        setupViewPage(this.viewPager);
        ((TabLayout) findViewById(R.id.tabLayout)).setupWithViewPager(this.viewPager);

        AdRequest adRequest = new AdRequest.Builder().addTestDevice(Constants.ONE_PLUS_TEST_DEVICE)
                .addTestDevice(Constants.XIAOMI_TEST_DEVICE).build();
        adView.loadAd(adRequest);
    }

    public final void setupViewPage(ViewPager viewPager) {
        MatchDetailsViewPagerAdapter matchDetailsViewPagerAdapter = new MatchDetailsViewPagerAdapter(getSupportFragmentManager());
        matchDetailsViewPagerAdapter.addFragment(new NewsDetailsFragment(), "বিস্তারিত");
        matchDetailsViewPagerAdapter.addFragment(new NewsCommentsFragment(), "কমেন্ট");
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
