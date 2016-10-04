package com.tigersapp.bdcricket.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.batch.android.Batch;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;
import com.tigersapp.bdcricket.R;
import com.tigersapp.bdcricket.adapter.BasicListAdapter;
import com.tigersapp.bdcricket.adapter.SlideShowViewPagerAdapter;
import com.tigersapp.bdcricket.model.Match;
import com.tigersapp.bdcricket.util.CircleImageView;
import com.tigersapp.bdcricket.util.Constants;
import com.tigersapp.bdcricket.util.FetchFromWeb;
import com.tigersapp.bdcricket.util.RecyclerItemClickListener;
import com.tigersapp.bdcricket.util.ViewHolder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import dmax.dialog.SpotsDialog;

public class FrontPage extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    TextView[] dots1;

    TextView welcomeText;

    ArrayList<String> imageUrls, texts;
    ViewPager viewPager;

    SlideShowViewPagerAdapter viewPagerAdapter;

    LinearLayout placeImageDotsLayout, cardContainer;

    RecyclerView recyclerView;

    Typeface typeface;

    ArrayList<Match> datas;
    AdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_front_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        adView = (AdView) findViewById(R.id.adViewFontPage);
        setSupportActionBar(toolbar);
        datas = new ArrayList<>();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        typeface = Typeface.createFromAsset(getAssets(), Constants.SOLAIMAN_LIPI_FONT);

        AdRequest adRequest = new AdRequest.Builder().addTestDevice(Constants.ONE_PLUS_TEST_DEVICE)
                .addTestDevice(Constants.XIAOMI_TEST_DEVICE).build();
        adView.loadAd(adRequest);


        welcomeText = (TextView) findViewById(R.id.tv_welcome_text);

        viewPager = (ViewPager) findViewById(R.id.placeViewPagerImageSlideShow);
        placeImageDotsLayout = (LinearLayout) findViewById(R.id.placeImageDots);
        cardContainer = (LinearLayout) findViewById(R.id.placecardcontainer);
        recyclerView = (RecyclerView) findViewById(R.id.live_matches);

        imageUrls = new ArrayList<>();
        texts = new ArrayList<>();

        viewPagerAdapter = new SlideShowViewPagerAdapter(this, imageUrls, texts);
        viewPager.setAdapter(viewPagerAdapter);

        String welcomeTextUrl = "http://apisea.xyz/BPL2016/apis/v2/welcometext.php?key=bl905577";
        Log.d(Constants.TAG, welcomeTextUrl);

        FetchFromWeb.get(welcomeTextUrl, null, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, final JSONObject response) {
                try {
                    welcomeText.setText(response.getJSONArray("content").getJSONObject(0).getString("description"));
                    if (response.getJSONArray("content").getJSONObject(0).getString("clickable").equals("true")) {
                        welcomeText.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                try {
                                    Uri uriUrl = Uri.parse(String.valueOf(response.getJSONArray("content").getJSONObject(0).getString("link")));
                                    Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
                                    startActivity(launchBrowser);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }

                    String url = "https://skysportsapi.herokuapp.com/sky/getnews/cricket/v1.0/";
                    Log.d(Constants.TAG, url);

                    if (isNetworkAvailable() && response.getJSONArray("content").getJSONObject(0).getString("image").equals("true")) {
                        cardContainer.setVisibility(View.VISIBLE);
                        FetchFromWeb.get(url, null, new JsonHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {

                                try {

                                    for (int i = 0; i < response.length(); i++) {
                                        JSONObject jsonObject = response.getJSONObject(i);
                                        imageUrls.add(jsonObject.getString("imgsrc"));
                                        texts.add(jsonObject.getString("title"));

                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                viewPagerAdapter.notifyDataSetChanged();
                                addBottomDots(0);
                                Log.d(Constants.TAG, response.toString());
                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {

                            }
                        });
                    } else {
                        cardContainer.setVisibility(View.GONE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d(Constants.TAG, response.toString());
            }
        });


        String idMatcherURL = "http://apisea.xyz/BPL2016/apis/v2/livescoresource.php";
        Log.d(Constants.TAG, idMatcherURL);

        final AlertDialog progressDialog = new SpotsDialog(FrontPage.this, R.style.Custom);
        progressDialog.show();
        progressDialog.setCancelable(true);
        RequestParams params = new RequestParams();
        params.add("key", "bl905577");

        FetchFromWeb.get(idMatcherURL, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                progressDialog.dismiss();
                try {
                    if (response.getString("msg").equals("Successful")) {
                        final String source = response.getJSONArray("content").getJSONObject(0).getString("scoresource");
                        String url = response.getJSONArray("content").getJSONObject(0).getString("url");
                        if (source.equals("myself") || source.equals("cricinfo") || source.equals("webview")) {
                            recyclerView.setAdapter(new BasicListAdapter<Match, FrontPage.LiveScoreViewHolder>(datas) {
                                @Override
                                public FrontPage.LiveScoreViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                                    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_live_score, parent, false);
                                    return new FrontPage.LiveScoreViewHolder(view);
                                }

                                @Override
                                public void onBindViewHolder(FrontPage.LiveScoreViewHolder holder, final int position) {
                                    Picasso.with(FrontPage.this)
                                            .load(Constants.resolveLogo(datas.get(position).getTeam1()))
                                            .placeholder(R.drawable.default_image)
                                            .into(holder.imgteam1);

                                    Picasso.with(FrontPage.this)
                                            .load(Constants.resolveLogo(datas.get(position).getTeam2()))
                                            .placeholder(R.drawable.default_image)
                                            .into(holder.imgteam2);

                                    holder.textteam1.setText(datas.get(position).getTeam1());
                                    holder.textteam2.setText(datas.get(position).getTeam2());
                                    holder.venue.setText(Html.fromHtml(datas.get(position).getVenue()));
                                    holder.time.setText(datas.get(position).getTime());
                                }
                            });
                            recyclerView.setLayoutManager(new LinearLayoutManager(FrontPage.this));

                            recyclerView.addOnItemTouchListener(
                                    new RecyclerItemClickListener(FrontPage.this, new RecyclerItemClickListener.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(View view, int position) {
                                            if (source.equals("webview")) {
                                                Intent intent = new Intent(FrontPage.this, LiveScore.class);
                                                intent.putExtra("url", "http://www.criconly.com/ipl/2013/get__summary.php?id=" + datas.get(position).getMatchId());
                                                startActivity(intent);
                                            } else {
                                                Intent intent = new Intent(FrontPage.this, ActivityMatchDetails.class);
                                                intent.putExtra("matchID", datas.get(position).getMatchId());
                                                startActivity(intent);
                                            }

                                        }
                                    })
                            );

                            if (source.equals("cricinfo")) {
                                //String url = "http://cricinfo-mukki.rhcloud.com/api/match/live";
                                Log.d(Constants.TAG, url);

                                final AlertDialog progressDialog = new SpotsDialog(FrontPage.this, R.style.Custom);
                                progressDialog.show();
                                progressDialog.setCancelable(true);
                                FetchFromWeb.get(url, null, new JsonHttpResponseHandler() {
                                    @Override
                                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                        progressDialog.dismiss();
                                        try {
                                            Log.d(Constants.TAG, response.toString());
                                            JSONArray jsonArray = response.getJSONArray("items");
                                            for (int i = 0; i < jsonArray.length(); i++) {
                                                JSONObject obj = jsonArray.getJSONObject(i);

                                                datas.add(new Match(obj.getJSONObject("team1").getString("teamName"), obj.getJSONObject("team2").getString("teamName"),
                                                        obj.getString("matchDescription"), "", "", "", obj.getString("matchId")));
                                            }
                                            recyclerView.getAdapter().notifyDataSetChanged();
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }

                                    @Override
                                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                                        progressDialog.dismiss();
                                        Toast.makeText(FrontPage.this, "Failed", Toast.LENGTH_LONG).show();
                                    }

                                    @Override
                                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                                        progressDialog.dismiss();
                                        Toast.makeText(FrontPage.this, "Failed", Toast.LENGTH_LONG).show();
                                    }
                                });

                            } else if (source.equals("myself")) {
                                //String url = "http://apisea.xyz/Cricket/apis/v1/fetchMyLiveScores.php?key=bl905577";
                                Log.d(Constants.TAG, url);

                                final AlertDialog progressDialog = new SpotsDialog(FrontPage.this, R.style.Custom);
                                progressDialog.show();
                                progressDialog.setCancelable(true);
                                FetchFromWeb.get(url, null, new JsonHttpResponseHandler() {
                                    @Override
                                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                        progressDialog.dismiss();
                                        try {
                                            Log.d(Constants.TAG, response.toString());
                                            if (response.getString("msg").equals("Successful")) {
                                                JSONArray jsonArray = response.getJSONArray("content");
                                                for (int i = 0; i < jsonArray.length(); i++) {
                                                    JSONObject obj = jsonArray.getJSONObject(i);
                                                    datas.add(new Match(obj.getString("team1"), obj.getString("team2"),
                                                            obj.getString("status"), "", "", "", obj.getString("matchId")));
                                                }
                                            }
                                            recyclerView.getAdapter().notifyDataSetChanged();
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }

                                    @Override
                                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                                        progressDialog.dismiss();
                                        Toast.makeText(FrontPage.this, "Failed", Toast.LENGTH_LONG).show();
                                    }

                                    @Override
                                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                                        progressDialog.dismiss();
                                        Toast.makeText(FrontPage.this, "Failed", Toast.LENGTH_LONG).show();
                                    }
                                });

                            } else if (source.equals("webview")) {
                                //String url = "http://www.criconly.com/ipl/2013/html/iphone_home_json.json";
                                Log.d(Constants.TAG, url);

                                final AlertDialog progressDialog = new SpotsDialog(FrontPage.this, R.style.Custom);
                                progressDialog.show();
                                progressDialog.setCancelable(true);

                                FetchFromWeb.get(url, null, new JsonHttpResponseHandler() {

                                    @Override
                                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                        progressDialog.dismiss();
                                        try {

                                            JSONArray jsonArray = response.getJSONArray("live");
                                            for (int i = 0; i < jsonArray.length(); i++) {
                                                JSONObject obj = jsonArray.getJSONObject(i);
                                                datas.add(new Match(obj.getString("team1_sname"), obj.getString("team2_sname"),
                                                        obj.getString("result"), "", "", "", obj.getString("match_id")));
                                            }

                                            recyclerView.getAdapter().notifyDataSetChanged();
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        Log.d(Constants.TAG, response.toString());
                                    }

                                    @Override
                                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                                        progressDialog.dismiss();
                                    }

                                    @Override
                                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                                        progressDialog.dismiss();
                                    }

                                    @Override
                                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                                        progressDialog.dismiss();
                                    }
                                });
                            }
                        } else {
                            Toast.makeText(FrontPage.this, "Please Update App", Toast.LENGTH_SHORT).show();
                        }
                    }

                    Log.d(Constants.TAG, response.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                progressDialog.dismiss();
                Toast.makeText(FrontPage.this, "Failed", Toast.LENGTH_LONG).show();
            }
        });


        //cardContainer.setVisibility(View.GONE);


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position1) {
                addBottomDots(position1);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
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
            String isAllowedUrl = "http://apisea.xyz/BPL2016/apis/v2/accessChecker.php";
            Log.d(Constants.TAG, isAllowedUrl);

            final AlertDialog progressDialog = new SpotsDialog(FrontPage.this, R.style.Custom);
            progressDialog.show();
            progressDialog.setCancelable(true);
            RequestParams params = new RequestParams();
            params.add("key", "bl905577");

            FetchFromWeb.get(isAllowedUrl, params, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    progressDialog.dismiss();
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
                    progressDialog.dismiss();
                    Toast.makeText(FrontPage.this, "Failed", Toast.LENGTH_LONG).show();
                }
            });

        } else if (id == R.id.nav_sports_news) {
            String isAllowedUrl = "http://apisea.xyz/BPL2016/apis/v2/accessChecker.php";
            Log.d(Constants.TAG, isAllowedUrl);

            final AlertDialog progressDialog = new SpotsDialog(FrontPage.this, R.style.Custom);
            progressDialog.show();
            progressDialog.setCancelable(true);
            RequestParams params = new RequestParams();
            params.add("key", "bl905577");

            FetchFromWeb.get(isAllowedUrl, params, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    progressDialog.dismiss();
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
                    progressDialog.dismiss();
                    Toast.makeText(FrontPage.this, "Failed", Toast.LENGTH_LONG).show();
                }
            });
        } else if (id == R.id.nav_highlights) {
            String isAllowedUrl = "http://apisea.xyz/BPL2016/apis/v2/accessChecker.php";
            Log.d(Constants.TAG, isAllowedUrl);

            final AlertDialog progressDialog = new SpotsDialog(FrontPage.this, R.style.Custom);
            progressDialog.show();
            progressDialog.setCancelable(true);
            RequestParams params = new RequestParams();
            params.add("key", "bl905577");

            FetchFromWeb.get(isAllowedUrl, params, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    progressDialog.dismiss();
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
                    progressDialog.dismiss();
                    Toast.makeText(FrontPage.this, "Failed", Toast.LENGTH_LONG).show();
                }
            });

        } else if (id == R.id.nav_fixture) {
            Intent intent = new Intent(FrontPage.this, FixtureActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_past_matches) {
            Intent intent = new Intent(FrontPage.this, PastMatchesActivity.class);
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
            String isAllowedUrl = "http://apisea.xyz/BPL2016/apis/v2/accessChecker.php";
            Log.d(Constants.TAG, isAllowedUrl);

            final AlertDialog progressDialog = new SpotsDialog(FrontPage.this, R.style.Custom);
            progressDialog.show();
            progressDialog.setCancelable(true);
            RequestParams params = new RequestParams();
            params.add("key", "bl905577");

            FetchFromWeb.get(isAllowedUrl, params, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    progressDialog.dismiss();
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
                    progressDialog.dismiss();
                    Toast.makeText(FrontPage.this, "Failed", Toast.LENGTH_LONG).show();
                }
            });

        } else if (id == R.id.nav_team_profile) {
            Intent intent = new Intent(FrontPage.this, TeamProfile.class);
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

    private void addBottomDots(int currentPage) {
        dots1 = new TextView[imageUrls.size()];

        int colorsActive = getResources().getColor(R.color.DarkGreen);
        int colorsInactive = getResources().getColor(R.color.MediumSpringGreen);

        placeImageDotsLayout.removeAllViews();
        for (int i = 0; i < dots1.length; i++) {
            dots1[i] = new TextView(this);
            dots1[i].setText(Html.fromHtml("&#8226;"));
            dots1[i].setTextSize(35);
            dots1[i].setTextColor(colorsInactive);
            placeImageDotsLayout.addView(dots1[i]);
        }
        if (dots1.length > 0)
            dots1[currentPage].setTextColor(colorsActive);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Batch.onStart(this);
    }

    @Override
    protected void onStop() {
        Batch.onStop(this);

        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Batch.onDestroy(this);

        super.onDestroy();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        Batch.onNewIntent(this, intent);

        super.onNewIntent(intent);
    }


    private static class LiveScoreViewHolder extends RecyclerView.ViewHolder {
        protected CircleImageView imgteam1;
        protected CircleImageView imgteam2;
        protected TextView textteam1;
        protected TextView textteam2;
        protected TextView venue;
        protected TextView time;

        public LiveScoreViewHolder(final View itemView) {
            super(itemView);

            imgteam1 = ViewHolder.get(itemView, R.id.civTeam1);
            imgteam2 = ViewHolder.get(itemView, R.id.civTeam2);
            textteam1 = ViewHolder.get(itemView, R.id.tvTeam1);
            textteam2 = ViewHolder.get(itemView, R.id.tvTeam2);
            venue = ViewHolder.get(itemView, R.id.tvVenue);
            time = ViewHolder.get(itemView, R.id.tvTime);
        }
    }
}
