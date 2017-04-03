package com.tigersapp.bdcricket.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.tigersapp.bdcricket.R;
import com.tigersapp.bdcricket.fragment.BasicInfoFragment;
import com.tigersapp.bdcricket.fragment.PlayersFragment;
import com.tigersapp.bdcricket.fragment.RecordFragment;
import com.tigersapp.bdcricket.util.Constants;

/**
 * @author Ripon
 */
public class TeamDetailsActivity extends AppCompatActivity {

    private ViewPager mPager;
    private SectionsPagerAdapter mAdapter;
    private TabLayout mTabLayout;
    private CharSequence Titles[] = {"BASIC INFO", "TEST RECORD", "ODI RECORD", "T20 RECORD", "PLAYERS"};
    private AdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teamdetailsactivity);
        adView = (AdView) findViewById(R.id.adViewTeamProfieDetails);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String data = getIntent().getStringExtra("data");
        mTabLayout = (TabLayout) findViewById(R.id.hoteltab_layout);
        mAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), Titles, data);
        mPager = (ViewPager) findViewById(R.id.view_pager);
        mPager.setAdapter(mAdapter);

        mTabLayout.setupWithViewPager(mPager);

        AdRequest adRequest = new AdRequest.Builder().addTestDevice(Constants.ONE_PLUS_TEST_DEVICE)
                .addTestDevice(Constants.XIAOMI_TEST_DEVICE).build();
        adView.loadAd(adRequest);
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

    private class SectionsPagerAdapter extends FragmentPagerAdapter {
        private CharSequence[] titles;
        private String data;

        public SectionsPagerAdapter(FragmentManager fm, CharSequence[] titles, String data) {
            super(fm);
            this.titles = titles;
            this.data = data;
        }


        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return BasicInfoFragment.newInstanceOfDescriptionFragment(data);
            } else if (position == 1) {
                return RecordFragment.newInstanceOfRecordFragment(data, "Test");
            } else if (position == 2) {
                return RecordFragment.newInstanceOfRecordFragment(data, "ODI");
            } else if (position == 3) {
                return RecordFragment.newInstanceOfRecordFragment(data, "T20");
            } else {
                return PlayersFragment.newInstanceOfPlayersFragment(data);
            }
        }

        @Override
        public int getCount() {
            return titles.length;
        }
    }
}