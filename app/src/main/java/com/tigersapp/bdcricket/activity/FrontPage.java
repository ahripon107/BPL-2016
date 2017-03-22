package com.tigersapp.bdcricket.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.tigersapp.bdcricket.R;
import com.tigersapp.bdcricket.adapter.MatchDetailsViewPagerAdapter;
import com.tigersapp.bdcricket.fragment.BlogFragment;
import com.tigersapp.bdcricket.fragment.LiveScoreFragment;
import com.tigersapp.bdcricket.fragment.OpinionFragment;
import com.tigersapp.bdcricket.util.Constants;
import com.tigersapp.bdcricket.util.Dialogs;
import com.tigersapp.bdcricket.util.FetchFromWeb;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class FrontPage extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private MatchDetailsViewPagerAdapter matchDetailsViewPagerAdapter;
    private ViewPager viewPager;
    AdView adView;

    Dialogs dialogs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_front_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        adView = (AdView) findViewById(R.id.adViewFontPage);
        setSupportActionBar(toolbar);

        dialogs = new Dialogs(this);

        this.viewPager = (ViewPager) findViewById(R.id.viewPager);

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
        this.matchDetailsViewPagerAdapter.addFragment(new BlogFragment(), "চ্যাটিং");
        this.matchDetailsViewPagerAdapter.addFragment(new OpinionFragment(),"মতামত");
        //this.matchDetailsViewPagerAdapter.addFragment(new QuizFragment(),"Quiz");
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
            String isAllowedUrl = Constants.ACCESS_CHECKER_URL;
            Log.d(Constants.TAG, isAllowedUrl);

            dialogs.showDialog();

            RequestParams params = new RequestParams();
            params.add("key", "bl905577");

            FetchFromWeb.get(isAllowedUrl, params, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    dialogs.dismissDialog();
                    try {
                        if (response.getString("msg").equals("Successful")) {
                            String source = response.getJSONArray("content").getJSONObject(0).getString("livestream");
                            if (source.equals("true")) {
                                Intent intent = new Intent(FrontPage.this, Highlights.class);
                                intent.putExtra("cause", "livestream");
                                startActivity(intent);
                            } else {
                                Toast.makeText(FrontPage.this, "Failed", Toast.LENGTH_SHORT).show();
                            }
                        }

                        Log.d(Constants.TAG, response.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    dialogs.dismissDialog();
                    Toast.makeText(FrontPage.this, "Failed", Toast.LENGTH_LONG).show();
                }
            });

        } else if (id == R.id.nav_sports_news) {
            String isAllowedUrl = Constants.ACCESS_CHECKER_URL;
            Log.d(Constants.TAG, isAllowedUrl);

            dialogs.showDialog();
            RequestParams params = new RequestParams();
            params.add("key", "bl905577");

            FetchFromWeb.get(isAllowedUrl, params, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    dialogs.dismissDialog();
                    try {
                        if (response.getString("msg").equals("Successful")) {
                            String source = response.getJSONArray("content").getJSONObject(0).getString("news");
                            if (source.equals("true")) {
                                Intent intent = new Intent(FrontPage.this, CricketNewsListActivity.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(FrontPage.this, "Failed", Toast.LENGTH_SHORT).show();
                            }
                        }

                        Log.d(Constants.TAG, response.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    dialogs.dismissDialog();
                    Toast.makeText(FrontPage.this, "Failed", Toast.LENGTH_LONG).show();
                }
            });
        } else if (id == R.id.nav_highlights) {
            String isAllowedUrl = Constants.ACCESS_CHECKER_URL;
            Log.d(Constants.TAG, isAllowedUrl);

            dialogs.showDialog();
            RequestParams params = new RequestParams();
            params.add("key", "bl905577");

            FetchFromWeb.get(isAllowedUrl, params, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    dialogs.dismissDialog();
                    try {
                        if (response.getString("msg").equals("Successful")) {
                            String source = response.getJSONArray("content").getJSONObject(0).getString("highlights");
                            if (source.equals("true")) {
                                Intent intent = new Intent(FrontPage.this, Highlights.class);
                                intent.putExtra("cause", "highlights");
                                startActivity(intent);
                            } else {
                                Toast.makeText(FrontPage.this, "Failed", Toast.LENGTH_SHORT).show();
                            }
                        }

                        Log.d(Constants.TAG, response.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    dialogs.dismissDialog();
                    Toast.makeText(FrontPage.this, "Failed", Toast.LENGTH_LONG).show();
                }
            });

        } else if (id == R.id.nav_fixture) {
            Intent intent = new Intent(FrontPage.this, FixtureActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_past_matches) {
            Intent intent = new Intent(FrontPage.this, PastMatchesActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_gallery) {
            String isAllowedUrl = Constants.ACCESS_CHECKER_URL;
            Log.d(Constants.TAG, isAllowedUrl);

            dialogs.showDialog();
            RequestParams params = new RequestParams();
            params.add("key", "bl905577");

            FetchFromWeb.get(isAllowedUrl, params, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    dialogs.dismissDialog();
                    try {
                        if (response.getString("msg").equals("Successful")) {
                            String source = response.getJSONArray("content").getJSONObject(0).getString("gallery");
                            if (source.equals("true")) {
                                Intent intent = new Intent(FrontPage.this, GalleryActivity.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(FrontPage.this, "Failed", Toast.LENGTH_SHORT).show();
                            }
                        }

                        Log.d(Constants.TAG, response.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    dialogs.dismissDialog();
                    Toast.makeText(FrontPage.this, "Failed", Toast.LENGTH_LONG).show();
                }
            });


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
            String isAllowedUrl = Constants.ACCESS_CHECKER_URL;
            Log.d(Constants.TAG, isAllowedUrl);

            dialogs.showDialog();
            RequestParams params = new RequestParams();
            params.add("key", "bl905577");

            FetchFromWeb.get(isAllowedUrl, params, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    dialogs.dismissDialog();
                    try {
                        if (response.getString("msg").equals("Successful")) {
                            String source = response.getJSONArray("content").getJSONObject(0).getString("quotes");
                            if (source.equals("true")) {
                                Intent intent = new Intent(FrontPage.this, QuotesListActivity.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(FrontPage.this, "Failed", Toast.LENGTH_SHORT).show();
                            }
                        }

                        Log.d(Constants.TAG, response.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    dialogs.dismissDialog();
                    Toast.makeText(FrontPage.this, "Failed", Toast.LENGTH_LONG).show();
                }
            });

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
}
