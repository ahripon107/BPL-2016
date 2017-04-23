package com.banglacricket.bdcricketteam.activity;

import android.os.Bundle;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.inject.Inject;
import com.banglacricket.bdcricketteam.R;
import com.banglacricket.bdcricketteam.fragment.PlayerBasicInfoFragment;
import com.banglacricket.bdcricketteam.fragment.PlayerBattingFragment;
import com.banglacricket.bdcricketteam.fragment.PlayerBowlingFragment;
import com.banglacricket.bdcricketteam.util.Constants;
import com.banglacricket.bdcricketteam.util.DefaultMessageHandler;
import com.banglacricket.bdcricketteam.util.NetworkService;

import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

/**
 * @author Ripon
 */
@ContentView(R.layout.activity_match_details)
public class PlayerCareerActivity extends CommonActivity {

    @InjectView(R.id.viewPager)
    private ViewPager mPager;

    private PlayerCareerPagerAdapter mAdapter;

    @InjectView(R.id.tabLayout)
    private TabLayout mTabLayout;

    private CharSequence Titles[] = {"INFO", "BATTING", "BOWLING"};

    @InjectView(R.id.adViewMatchDetails)
    private AdView adView;

    @Inject
    private NetworkService networkService;

    private String data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
