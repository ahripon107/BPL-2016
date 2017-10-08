package com.allgames.sportslab.activity;

import android.os.Bundle;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.allgames.sportslab.R;
import com.allgames.sportslab.fragment.PlayerBasicInfoFragment;
import com.allgames.sportslab.fragment.PlayerBattingFragment;
import com.allgames.sportslab.fragment.PlayerBowlingFragment;
import com.allgames.sportslab.util.Constants;
import com.allgames.sportslab.util.DefaultMessageHandler;
import com.allgames.sportslab.util.NetworkService;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.inject.Inject;

/**
 * @author Ripon
 */
public class PlayerCareerActivity extends CommonActivity {

    private ViewPager mPager;
    private TabLayout mTabLayout;
    private AdView adView;

    private PlayerCareerPagerAdapter mAdapter;
    private CharSequence Titles[] = {"INFO", "BATTING", "BOWLING"};
    private String data;

    @Inject
    private NetworkService networkService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initialize();

        networkService.fetchPlayerProfile(getIntent().getStringExtra("playerID"), new DefaultMessageHandler(this, true) {
            @Override
            public void onSuccess(Message msg) {
                data = (String) msg.obj;
                mAdapter = new PlayerCareerPagerAdapter(getSupportFragmentManager(), Titles, data);
                mPager.setAdapter(mAdapter);
                mPager.setOffscreenPageLimit(3);
                mTabLayout.setupWithViewPager(mPager);
            }
        });

        AdRequest adRequest = new AdRequest.Builder().addTestDevice(Constants.ONE_PLUS_TEST_DEVICE)
                .addTestDevice(Constants.XIAOMI_TEST_DEVICE).build();
        adView.loadAd(adRequest);
    }

    private void initialize() {
        setContentView(R.layout.activity_match_details);
        mPager = (ViewPager) findViewById(R.id.viewPager);
        mTabLayout = (TabLayout) findViewById(R.id.tabLayout);
        adView = (AdView) findViewById(R.id.adViewMatchDetails);
    }

    private class PlayerCareerPagerAdapter extends FragmentPagerAdapter {
        private CharSequence[] Titles;
        private String data;

        public PlayerCareerPagerAdapter(FragmentManager fm, CharSequence[] Titles, String data) {
            super(fm);
            this.Titles = Titles;
            this.data = data;
        }

        @Override
        public Fragment getItem(int position) {

            if (position == 0) {
                PlayerBasicInfoFragment playerBasicInfoFragment = new PlayerBasicInfoFragment();
                Bundle bundle = new Bundle();
                bundle.putString("data", data);
                playerBasicInfoFragment.setArguments(bundle);
                return playerBasicInfoFragment;
            } else if (position == 1) {
                PlayerBattingFragment playerBattingFragment = new PlayerBattingFragment();
                Bundle bundle = new Bundle();
                bundle.putString("data", data);
                playerBattingFragment.setArguments(bundle);
                return playerBattingFragment;
            } else {
                PlayerBowlingFragment playerBattingFragment = new PlayerBowlingFragment();
                Bundle bundle = new Bundle();
                bundle.putString("data", data);
                playerBattingFragment.setArguments(bundle);
                return playerBattingFragment;
            }
        }

        @Override
        public int getCount() {
            return Titles.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return Titles[position];
        }
    }
}
