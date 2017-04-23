package com.banglacricket.bdcricketteam.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.banglacricket.bdcricketteam.R;
import com.banglacricket.bdcricketteam.adapter.MatchDetailsViewPagerAdapter;
import com.banglacricket.bdcricketteam.fragment.NewsCommentsFragment;
import com.banglacricket.bdcricketteam.fragment.NewsDetailsFragment;
import com.banglacricket.bdcricketteam.util.Constants;

import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

/**
 * @author ripon
 */
@ContentView(R.layout.newsdetails)
public class NewsDetailsActivity extends CommonActivity {

    public static final String EXTRA_NEWS_OBJECT = "newsobject";

    @InjectView(R.id.adViewNewsDetails)
    private AdView adView;

    @InjectView(R.id.viewPager)
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
}
