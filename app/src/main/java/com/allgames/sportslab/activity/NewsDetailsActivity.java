package com.allgames.sportslab.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.allgames.sportslab.R;
import com.allgames.sportslab.adapter.MatchDetailsViewPagerAdapter;
import com.allgames.sportslab.fragment.NewsCommentsFragment;
import com.allgames.sportslab.fragment.NewsDetailsFragment;
import com.allgames.sportslab.util.Constants;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

/**
 * @author ripon
 */
public class NewsDetailsActivity extends CommonActivity {

    public static final String EXTRA_NEWS_OBJECT = "newsobject";

    private AdView adView;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initialize();

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

    private void initialize() {
        setContentView(R.layout.newsdetails);
        adView = (AdView) findViewById(R.id.adViewNewsDetails);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
    }
}
