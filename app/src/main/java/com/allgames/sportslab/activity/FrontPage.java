package com.allgames.sportslab.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.allgames.sportslab.R;
import com.allgames.sportslab.adapter.BasicListAdapter;
import com.allgames.sportslab.model.match.Header;
import com.allgames.sportslab.model.match.Match;
import com.allgames.sportslab.model.match.MiniScore;
import com.allgames.sportslab.util.Constants;
import com.allgames.sportslab.util.DefaultMessageHandler;
import com.allgames.sportslab.util.NetworkService;
import com.allgames.sportslab.util.RecyclerItemClickListener;
import com.allgames.sportslab.util.RoboAppCompatActivity;
import com.allgames.sportslab.util.ViewHolder;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.inject.Inject;
import com.squareup.picasso.Picasso;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.JavaType;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

@ContentView(R.layout.activity_front_page)
public class FrontPage extends RoboAppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @InjectView(R.id.adViewFontPage)
    private AdView adView;
    @InjectView(R.id.toolbar)
    private Toolbar toolbar;

    @InjectView(R.id.tv_welcome_text)
    private TextView welcomeText;
    @InjectView(R.id.live_matches)
    private RecyclerView recyclerView;
    @InjectView(R.id.tour_image)
    private ImageView imageView;
    @InjectView(R.id.tv_empty_view)
    private TextView emptyView;

    @Inject
    private ArrayList<Match> datas;
    @Inject
    private NetworkService networkService;

    private PackageInfo pInfo;

    private InterstitialAd mInterstitialAd;

    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        drawer.openDrawer(Gravity.LEFT);

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

//        hideAllMenu(navigationView);

//        networkService.fetchIsAllowed(new DefaultMessageHandler(this, true) {
//            @Override
//            public void onSuccess(Message msg) {
//                super.onSuccess(msg);
//                String string = (String) msg.obj;
//                try {
//                    JSONObject response = new JSONObject(string);
//                    if (response.getString("msg").equals("Successful")) {
//                        String source = response.getJSONArray("content").getJSONObject(0).getString("showmenu");
//                        if (source.equals("true")) {
//                            showAllMenu(navigationView);
//                        }
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        });

//        AdRequest adRequest1 = new AdRequest.Builder().addTestDevice(Constants.ONE_PLUS_TEST_DEVICE)
//                .addTestDevice(Constants.XIAOMI_TEST_DEVICE).build();
//        adView.loadAd(adRequest1);
//
//        mInterstitialAd = new InterstitialAd(this);
//        mInterstitialAd.setAdUnitId("ca-app-pub-9201945236996508/7683380073");
//
//        AdRequest adRequestInterstitial = new AdRequest.Builder()
//                .addTestDevice(Constants.ONE_PLUS_TEST_DEVICE).addTestDevice(Constants.XIAOMI_TEST_DEVICE)
//                .build();
//        mInterstitialAd.loadAd(adRequestInterstitial);
//
//        mInterstitialAd.setAdListener(new AdListener() {
//            @Override
//            public void onAdLoaded() {
//                super.onAdLoaded();
//                if (mInterstitialAd.isLoaded()) {
//                    mInterstitialAd.show();
//                }
//            }
//        });

        try {
            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        String welcomeTextUrl = Constants.WELCOME_TEXT_URL;
        Log.d(Constants.TAG, welcomeTextUrl);

        networkService.fetchWelcomeText(new DefaultMessageHandler(this, true) {
            @Override
            public void onSuccess(Message msg) {
                String string = (String) msg.obj;
                try {
                    final JSONObject response = new JSONObject(string);

                    Constants.SHOW_PLAYER_IMAGE = response.getString("playerimage");
                    welcomeText.setText(Html.fromHtml(response.getString("description")));
                    if (response.getString("clickable").equals("true")) {
                        welcomeText.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                try {
                                    Uri uriUrl = Uri.parse(String.valueOf(response.getString("link")));
                                    Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
                                    startActivity(launchBrowser);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }

                    if (isNetworkAvailable() && response.getString("appimage").equals("true")) {
                        imageView.setVisibility(View.VISIBLE);
                        Picasso.with(FrontPage.this)
                                .load(response.getString("appimageurl"))
                                .placeholder(R.drawable.default_image)
                                .into(imageView);
                        imageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                try {
                                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(response.getString("applink"))));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    } else {
                        imageView.setVisibility(View.GONE);
                    }

                    if (!response.getString("checkversion").contains(pInfo.versionName)) {
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(FrontPage.this);
                        alertDialogBuilder.setMessage(response.getString("popupmessage"));
                        alertDialogBuilder.setPositiveButton("Yes",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface arg0, int arg1) {
                                        try {
                                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(response.getString("popuplink"))));
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });

                        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });

                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();
                    }
                    if (response.getString("showlivescore").equals("false")) {
                        recyclerView.setVisibility(View.GONE);
                    } else {
                        recyclerView.setVisibility(View.VISIBLE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        recyclerView.setAdapter(new BasicListAdapter<Match, LiveScoreViewHolder>(datas) {
            @Override
            public LiveScoreViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_match, parent, false);
                return new LiveScoreViewHolder(view);
            }

            @Override
            public void onBindViewHolder(LiveScoreViewHolder holder, final int position) {
                Match match = datas.get(position);
                Picasso.with(FrontPage.this)
                        .load(Constants.resolveLogo(match.getTeam1().getId()))
                        .placeholder(R.drawable.default_image)
                        .into(holder.imgteam1);

                Picasso.with(FrontPage.this)
                        .load(Constants.resolveLogo(match.getTeam2().getId()))
                        .placeholder(R.drawable.default_image)
                        .into(holder.imgteam2);

                MiniScore miniScore = match.getMiniScore();
                if (miniScore != null) {
                    if (miniScore.getBattingTeamId() == match.getTeam1().getId()) {
                        holder.textteam1.setText(match.getTeam1().getShortName()+"  "+miniScore.getBattingTeamScore());
                        holder.textteam2.setText(match.getTeam2().getShortName() + "  "+ miniScore.getBowlingTeamScore());
                    } else {
                        holder.textteam1.setText(match.getTeam1().getShortName()+"  "+miniScore.getBowlingTeamScore());
                        holder.textteam2.setText(match.getTeam2().getShortName() + "  "+ miniScore.getBattingTeamScore());
                    }
                } else {
                    holder.textteam1.setText(match.getTeam1().getShortName());
                    holder.textteam2.setText(match.getTeam2().getShortName());
                }


                Header header = match.getHeader();
                holder.venue.setText(header.getGround() + ", " + header.getCity() + ", " + header.getCountry());
                holder.status.setText(header.getStatus());
                holder.seriesName.setText(match.getSrs() + ", " + header.getMatchNumber());
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Intent intent = new Intent(FrontPage.this, MatchDetailsActivity.class);
                        intent.putExtra("summary", datas.get(position));
                        startActivity(intent);
                    }
                })
        );

        networkService.fetchLiveScore(new DefaultMessageHandler(this, true) {
            @Override
            public void onSuccess(Message msg) {
                ObjectMapper objectMapper = new ObjectMapper();
                JavaType valueType = objectMapper.getTypeFactory().constructCollectionType(List.class, Match.class);
                try {
                    datas.addAll((ArrayList<Match>) objectMapper.readValue((String) msg.obj, valueType));
                    recyclerView.getAdapter().notifyDataSetChanged();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        recyclerView.setNestedScrollingEnabled(false);
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
                Intent i = new Intent(android.content.Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(android.content.Intent.EXTRA_SUBJECT, "Share App");
                i.putExtra(android.content.Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=" + getPackageName());
                startActivity(Intent.createChooser(i, "Share via"));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
            Intent intent = new Intent(FrontPage.this, CricketNewsListActivity.class);
            startActivity(intent);
            //checkIsAllowed("activity_news", "com.allgames.sportslab.activity.CricketNewsListActivity", null);

        } else if (id == R.id.nav_highlights) {
            checkIsAllowed("highlights", "com.allgames.sportslab.activity.Highlights", "highlights");

        } else if (id == R.id.nav_highlights_football) {
            checkIsAllowed("highlights", "com.allgames.sportslab.activity.Highlights", "highlightsfootball");

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
        menu.findItem(R.id.nav_past_matches).setVisible(false);
    }

    private void showAllMenu(NavigationView navigationView) {
        Menu menu = navigationView.getMenu();
        menu.findItem(R.id.nav_live_streaming).setVisible(true);
        menu.findItem(R.id.nav_live_streaming_football).setVisible(true);
        menu.findItem(R.id.nav_sports_news).setVisible(true);
        menu.findItem(R.id.nav_highlights).setVisible(true);
        menu.findItem(R.id.nav_highlights_football).setVisible(true);
        menu.findItem(R.id.nav_gallery).setVisible(true);
        menu.findItem(R.id.nav_series_stats).setVisible(true);
        menu.findItem(R.id.nav_ranking).setVisible(true);
        menu.findItem(R.id.nav_records).setVisible(true);
        menu.findItem(R.id.nav_points_table).setVisible(true);
        menu.findItem(R.id.nav_login_logout).setVisible(true);
        menu.findItem(R.id.nav_update_app).setVisible(true);
        menu.findItem(R.id.nav_past_matches).setVisible(true);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    private static class LiveScoreViewHolder extends RecyclerView.ViewHolder {
        ImageView imgteam1;
        ImageView imgteam2;
        TextView textteam1;
        TextView textteam2;
        TextView venue;
        TextView status;
        TextView seriesName;
        LinearLayout linearLayout;

        LiveScoreViewHolder(final View itemView) {
            super(itemView);

            imgteam1 = ViewHolder.get(itemView, R.id.civTeam1);
            imgteam2 = ViewHolder.get(itemView, R.id.civTeam2);
            textteam1 = ViewHolder.get(itemView, R.id.tvTeam1);
            textteam2 = ViewHolder.get(itemView, R.id.tvTeam2);
            venue = ViewHolder.get(itemView, R.id.tvVenue);
            status = ViewHolder.get(itemView, R.id.tvTime);
            seriesName = ViewHolder.get(itemView, R.id.tvSeriesname);
            linearLayout = ViewHolder.get(itemView, R.id.match_layout);
        }
    }
}
