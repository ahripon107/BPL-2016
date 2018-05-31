package com.allgames.sportslab.activity;

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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.inject.Inject;
import com.allgames.sportslab.R;
import com.allgames.sportslab.adapter.MatchDetailsViewPagerAdapter;
import com.allgames.sportslab.fragment.LiveScoreFragment;
import com.allgames.sportslab.util.Constants;
import com.allgames.sportslab.util.DefaultMessageHandler;
import com.allgames.sportslab.util.NetworkService;
import com.allgames.sportslab.util.RoboAppCompatActivity;

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
    private
    NetworkService networkService;

    private InterstitialAd mInterstitialAd;

    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setSupportActionBar(toolbar);

        this.viewPager.setOffscreenPageLimit(2);
        setupViewPage(this.viewPager);
        ((TabLayout) findViewById(R.id.tabLayout)).setupWithViewPager(this.viewPager);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        drawer.openDrawer(Gravity.LEFT);

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        hideAllMenu(navigationView);

        networkService.fetchIsAllowed(new DefaultMessageHandler(this, true) {
            @Override
            public void onSuccess(Message msg) {
                super.onSuccess(msg);
                String string = (String) msg.obj;
                try {
                    JSONObject response = new JSONObject(string);
                    if (response.getString("msg").equals("Successful")) {
                        String source = response.getJSONArray("content").getJSONObject(0).getString("showmenu");
                        if (source.equals("true")) {
                            showAllMenu(navigationView);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        AdRequest adRequest1 = new AdRequest.Builder().addTestDevice(Constants.ONE_PLUS_TEST_DEVICE)
                .addTestDevice(Constants.XIAOMI_TEST_DEVICE).build();
        adView.loadAd(adRequest1);

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-9201945236996508/7683380073");

        AdRequest adRequestInterstitial = new AdRequest.Builder()
                .addTestDevice(Constants.ONE_PLUS_TEST_DEVICE).addTestDevice(Constants.XIAOMI_TEST_DEVICE)
                .build();
        mInterstitialAd.loadAd(adRequestInterstitial);

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.share_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_share:
                Intent i=new Intent(android.content.Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(android.content.Intent.EXTRA_SUBJECT,"Share App");
                i.putExtra(android.content.Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id="+getPackageName());
                startActivity(Intent.createChooser(i,"Share via"));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setupViewPage(ViewPager viewPager) {
        this.matchDetailsViewPagerAdapter = new MatchDetailsViewPagerAdapter(getSupportFragmentManager());
        this.matchDetailsViewPagerAdapter.addFragment(new LiveScoreFragment(), "লাইভ স্কোর");
        viewPager.setAdapter(this.matchDetailsViewPagerAdapter);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
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
            checkIsAllowed("livestream", "com.allgames.sportslab.activity.Highlights", "livestream");

        } else if (id == R.id.nav_live_streaming_football) {
            checkIsAllowed("livestream", "com.allgames.sportslab.activity.Highlights", "livestreamfootball");

        } else if (id == R.id.nav_sports_news) {
            checkIsAllowed("news", "com.allgames.sportslab.activity.CricketNewsListActivity", null);

        } else if (id == R.id.nav_highlights) {
            checkIsAllowed("highlights", "com.allgames.sportslab.activity.Highlights", "highlights");

        } else if (id == R.id.nav_highlights_football) {
            checkIsAllowed("highlights", "com.allgames.sportslab.activity.Highlights", "highlightsfootball");

        } else if (id == R.id.nav_fixture) {
            Intent intent = new Intent(FrontPage.this, FixtureActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_past_matches) {
            Intent intent = new Intent(FrontPage.this, PastMatchesActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_gallery) {
            checkIsAllowed("gallery", "com.allgames.sportslab.activity.GalleryActivity", null);

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
            checkIsAllowed("quotes", "com.allgames.sportslab.activity.QuotesListActivity", null);

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

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void checkIsAllowed(final String item, final String className, final String cause) {

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

                            try {
                                Intent intent = new Intent(FrontPage.this, Class.forName(className));
                                if (cause != null) {
                                    intent.putExtra("cause", cause);
                                }
                                startActivity(intent);
                            } catch (ClassNotFoundException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(FrontPage.this, "Failed", Toast.LENGTH_LONG).show();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private void hideAllMenu(NavigationView navigationView) {
        Menu menu = navigationView.getMenu();
        menu.findItem(R.id.nav_live_streaming).setVisible(false);
        menu.findItem(R.id.nav_live_streaming_football).setVisible(false);
        menu.findItem(R.id.nav_sports_news).setVisible(false);
        menu.findItem(R.id.nav_highlights).setVisible(false);
        menu.findItem(R.id.nav_highlights_football).setVisible(false);
        menu.findItem(R.id.nav_gallery).setVisible(false);
        menu.findItem(R.id.nav_ranking).setVisible(false);
        menu.findItem(R.id.nav_quotes).setVisible(false);
        menu.findItem(R.id.nav_past_matches).setVisible(false);
        menu.findItem(R.id.nav_fixture).setVisible(false);
    }

    private void showAllMenu(NavigationView navigationView) {
        Menu menu = navigationView.getMenu();
        menu.findItem(R.id.nav_live_streaming).setVisible(true);
        menu.findItem(R.id.nav_live_streaming_football).setVisible(true);
        menu.findItem(R.id.nav_sports_news).setVisible(true);
        menu.findItem(R.id.nav_highlights).setVisible(true);
        menu.findItem(R.id.nav_highlights_football).setVisible(true);
        menu.findItem(R.id.nav_fixture).setVisible(true);
        menu.findItem(R.id.nav_gallery).setVisible(true);
        menu.findItem(R.id.nav_series_stats).setVisible(true);
        menu.findItem(R.id.nav_ranking).setVisible(true);
        menu.findItem(R.id.nav_records).setVisible(true);
        menu.findItem(R.id.nav_points_table).setVisible(true);
        menu.findItem(R.id.nav_quotes).setVisible(true);
        menu.findItem(R.id.nav_team_profile).setVisible(true);
        menu.findItem(R.id.nav_login_logout).setVisible(true);
        menu.findItem(R.id.nav_update_app).setVisible(true);
        menu.findItem(R.id.nav_past_matches).setVisible(true);
    }
}
