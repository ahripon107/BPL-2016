package com.tigersapp.bdcricket.activity;

import android.os.Bundle;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.inject.Inject;
import com.tigersapp.bdcricket.R;
import com.tigersapp.bdcricket.fragment.PlayerBasicInfoFragment;
import com.tigersapp.bdcricket.fragment.PlayerBattingFragment;
import com.tigersapp.bdcricket.fragment.PlayerBowlingFragment;
import com.tigersapp.bdcricket.util.Constants;
import com.tigersapp.bdcricket.util.DefaultMessageHandler;
import com.tigersapp.bdcricket.util.NetworkService;
import com.tigersapp.bdcricket.util.RoboAppCompatActivity;

import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

/**
 * @author Ripon
 */
@ContentView(R.layout.activity_match_details)
public class PlayerCareerActivity extends RoboAppCompatActivity {

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

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String playerID = getIntent().getStringExtra("playerID");
        networkService.fetchPlayerProfile(playerID, new DefaultMessageHandler(this, true) {
            @Override
            public void onSuccess(Message msg) {
                data = (String) msg.obj;
                mAdapter = new PlayerCareerPagerAdapter(getSupportFragmentManager(), Titles,data);
                mPager.setAdapter(mAdapter);
                mPager.setOffscreenPageLimit(3);
                mTabLayout.setupWithViewPager(mPager);
            }
        });

        AdRequest adRequest = new AdRequest.Builder().addTestDevice(Constants.ONE_PLUS_TEST_DEVICE)
                .addTestDevice(Constants.XIAOMI_TEST_DEVICE).build();
        adView.loadAd(adRequest);
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
