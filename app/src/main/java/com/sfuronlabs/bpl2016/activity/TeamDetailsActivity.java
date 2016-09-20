package com.sfuronlabs.bpl2016.activity;

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
import com.sfuronlabs.bpl2016.R;
import com.sfuronlabs.bpl2016.fragment.BasicInfoFragment;
import com.sfuronlabs.bpl2016.fragment.PlayersFragment;
import com.sfuronlabs.bpl2016.fragment.RecordFragment;
import com.sfuronlabs.bpl2016.util.Constants;

/**
 * @author Ripon
 */
public class TeamDetailsActivity extends AppCompatActivity {

    private ViewPager mPager;
    private YourPagerAdapter mAdapter;
    private TabLayout mTabLayout;
    private CharSequence Titles[] = {"BASIC INFO", "TEST RECORD", "ODI RECORD", "T20 RECORD", "PLAYERS"};
    private int NoOfTabs = 5;
    private AdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teamdetailsactivity);
        adView = (AdView) findViewById(R.id.adViewTeamProfieDetails);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String data = getIntent().getStringExtra("data");
        mTabLayout = (TabLayout) findViewById(R.id.hoteltab_layout);
        mAdapter = new YourPagerAdapter(getSupportFragmentManager(), Titles, NoOfTabs,data);
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
}


class YourPagerAdapter extends FragmentPagerAdapter {
    CharSequence[] Titles;
    int NoOfTabs;
    String data;



    public YourPagerAdapter(FragmentManager fm, CharSequence[] Titles, int NoOfTabs, String data) {
        super(fm);
        this.Titles = Titles;
        this.NoOfTabs = NoOfTabs;
        this.data = data;
    }

    @Override
    public Fragment getItem(int position) {

        if (position == 0) {
            return BasicInfoFragment.newInstanceOfDescriptionFragment(data);
        } else if (position == 1) {
            return RecordFragment.newInstanceOfRecordFragment(data,"Test");
        } else if (position == 2) {
            return RecordFragment.newInstanceOfRecordFragment(data, "ODI");
        } else if (position == 3) {
            return RecordFragment.newInstanceOfRecordFragment(data, "T20");
        } else  {
            return PlayersFragment.newInstanceOfPlayersFragment(data);
        }

    }

    @Override
    public int getCount() {
        return 5;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return Titles[position];
    }


}

