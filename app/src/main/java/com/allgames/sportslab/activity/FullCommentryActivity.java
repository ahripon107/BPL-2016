package com.allgames.sportslab.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.allgames.sportslab.R;
import com.allgames.sportslab.adapter.MatchDetailsViewPagerAdapter;
import com.allgames.sportslab.fragment.FullCommentryFragment;
import com.allgames.sportslab.util.Constants;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

/**
 * @author Ripon
 */

public class FullCommentryActivity extends CommonActivity {

    int numberOfInnings;
    String id;
    AdView adView;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private MatchDetailsViewPagerAdapter matchDetailsViewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initialize();

        numberOfInnings = getIntent().getIntExtra("numberofinnings", 0);
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
        for (int i = 1; i <= numberOfInnings; i++) {
            FullCommentryFragment fullCommentryFragment = new FullCommentryFragment();
            Bundle bundle = new Bundle();
            bundle.putString("url", "http://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20cricket.commentary%20where%20match_id=" + id + "%20and%20innings_id=" + i + "&format=json&diagnostics=false&env=store%3A%2F%2F0TxIGQMQbObzvU4Apia0V0&callback=");
            fullCommentryFragment.setArguments(bundle);
            this.matchDetailsViewPagerAdapter.addFragment(fullCommentryFragment, "Innings " + i);
        }
        viewPager.setAdapter(this.matchDetailsViewPagerAdapter);
    }

    private void initialize() {
        setContentView(R.layout.activity_match_details);
        adView = (AdView) findViewById(R.id.adViewMatchDetails);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
    }
}
