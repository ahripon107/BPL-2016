package com.banglacricket.bdcricketteam.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.inject.Inject;
import com.banglacricket.bdcricketteam.R;
import com.banglacricket.bdcricketteam.adapter.MatchDetailsViewPagerAdapter;
import com.banglacricket.bdcricketteam.fragment.LiveScoreFragment;
import com.banglacricket.bdcricketteam.fragment.OpinionFragment;
import com.banglacricket.bdcricketteam.util.Constants;
import com.banglacricket.bdcricketteam.util.DefaultMessageHandler;
import com.banglacricket.bdcricketteam.util.NetworkService;
import com.banglacricket.bdcricketteam.util.RoboAppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

@ContentView(R.layout.activity_front_page)
public class FrontPage extends RoboAppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private MatchDetailsViewPagerAdapter matchDetailsViewPagerAdapter;
    @InjectView(R.id.viewPager)
    private ViewPager viewPager;
    @InjectView(R.id.adViewFontPage)
    private AdView adView;
    @InjectView(R.id.toolbar)
    private Toolbar toolbar;

    @Inject
    NetworkService networkService;

    private boolean result = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setSupportActionBar(toolbar);

        this.viewPager.setOffscreenPageLimit(2);
        setupViewPage(this.viewPager);
        ((TabLayout) findViewById(R.id.tabLayout)).setupWithViewPager(this.viewPager);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        drawer.openDrawer(Gravity.LEFT);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        AdRequest adRequest1 = new AdRequest.Builder().addTestDevice(Constants.ONE_PLUS_TEST_DEVICE)
                .addTestDevice(Constants.XIAOMI_TEST_DEVICE).build();
        adView.loadAd(adRequest1);


    }

    public final void setupViewPage(ViewPager viewPager) {
        this.matchDetailsViewPagerAdapter = new MatchDetailsViewPagerAdapter(getSupportFragmentManager());
        this.matchDetailsViewPagerAdapter.addFragment(new LiveScoreFragment(), "লাইভ স্কোর");
        this.matchDetailsViewPagerAdapter.addFragment(new OpinionFragment(), "মতামত");
        viewPager.setAdapter(this.matchDetailsViewPagerAdapter);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_live_streaming) {
            if (checkIsAllowed("livestream")) {
                Intent intent = new Intent(FrontPage.this, Highlights.class);
                intent.putExtra("cause", "livestream");
                startActivity(intent);
            } else {
                Toast.makeText(FrontPage.this, "Failed", Toast.LENGTH_SHORT).show();
            }
        } else if (id == R.id.nav_live_streaming_football) {
            if (checkIsAllowed("livestream")) {
                Intent intent = new Intent(FrontPage.this, Highlights.class);
                intent.putExtra("cause", "livestreamfootball");
                startActivity(intent);
            } else {
                Toast.makeText(FrontPage.this, "Failed", Toast.LENGTH_SHORT).show();
            }
        } else if (id == R.id.nav_sports_news) {
            if (checkIsAllowed("news")) {
                Intent intent = new Intent(FrontPage.this, CricketNewsListActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(FrontPage.this, "Failed", Toast.LENGTH_SHORT).show();
            }
        } else if (id == R.id.nav_highlights) {
            if (checkIsAllowed("highlights")) {
                Intent intent = new Intent(FrontPage.this, Highlights.class);
                intent.putExtra("cause", "highlights");
                startActivity(intent);
            } else {
                Toast.makeText(FrontPage.this, "Failed", Toast.LENGTH_SHORT).show();
            }
        } else if (id == R.id.nav_highlights_football) {
            if (checkIsAllowed("highlights")) {
                Intent intent = new Intent(FrontPage.this, Highlights.class);
                intent.putExtra("cause", "highlightsfootball");
                startActivity(intent);
            } else {
                Toast.makeText(FrontPage.this, "Failed", Toast.LENGTH_SHORT).show();
            }
        } else if (id == R.id.nav_fixture) {
            Intent intent = new Intent(FrontPage.this, FixtureActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_past_matches) {
            Intent intent = new Intent(FrontPage.this, PastMatchesActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_gallery) {
            if (checkIsAllowed("gallery")) {
                Intent intent = new Intent(FrontPage.this, GalleryActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(FrontPage.this, "Failed", Toast.LENGTH_SHORT).show();
            }
        } else if (id == R.id.nav_series_stats) {
            Intent intent = new Intent(FrontPage.this, SeriesStatsActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_ranking) {
            Intent intent = new Intent(FrontPage.this, RankingActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_records) {
            Intent intent = new Intent(FrontPage.this, RecordsActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_points_table) {
            Intent intent = new Intent(FrontPage.this, PointsTableActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_quotes) {
            if (checkIsAllowed("quotes")) {
                Intent intent = new Intent(FrontPage.this, QuotesListActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(FrontPage.this, "Failed", Toast.LENGTH_SHORT).show();
            }
        } else if (id == R.id.nav_team_profile) {
            Intent intent = new Intent(FrontPage.this, TeamProfile.class);
            startActivity(intent);
        } else if (id == R.id.nav_login_logout) {
            Intent intent = new Intent(FrontPage.this, LoginActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_update_app) {
            String appPackageName = getPackageName();
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
            } catch (android.content.ActivityNotFoundException anfe) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private boolean checkIsAllowed(final String item) {

        networkService.fetchIsAllowed(new DefaultMessageHandler(this, true) {
            @Override
            public void onSuccess(Message msg) {
                super.onSuccess(msg);
                String string = (String) msg.obj;
                try {
                    JSONObject response = new JSONObject(string);
                    if (response.getString("msg").equals("Successful")) {
                        String source = response.getJSONArray("content").getJSONObject(0).getString(item);
                        if (source.equals("true")) {
                            result = true;
                        } else {
                            result = false;
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        return result;
    }
}
