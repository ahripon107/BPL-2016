package com.tigersapp.bdcricket.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.batch.android.Batch;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.tigersapp.bdcricket.R;
import com.tigersapp.bdcricket.util.Constants;
import com.tigersapp.bdcricket.util.FetchFromWeb;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import dmax.dialog.SpotsDialog;

public class MainActivity extends AppCompatActivity {

    LinearLayout cricketLive, cricketLiveScore, cricketHighlights, cricketFixture, cricketNews, trollPosts, teamProfile, pastMatches, rate, ranking, records, pointsTable, quotes;

    TextView liveStreaming, liveScore, news, highlights, fixture, pastMatch, rankings, record, pointSTable, trolls, quote, profile, update;

    TextView welcomeText;
    Typeface typeface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        typeface = Typeface.createFromAsset(getAssets(), Constants.SOLAIMAN_LIPI_FONT);

        cricketLive = (LinearLayout) findViewById(R.id.button_cricket_live);
        cricketLiveScore = (LinearLayout) findViewById(R.id.button_cricket_live_score);
        cricketHighlights = (LinearLayout) findViewById(R.id.button_cricket_highlights);
        cricketFixture = (LinearLayout) findViewById(R.id.button_cricket_fixture);
        cricketNews = (LinearLayout) findViewById(R.id.button_cricket_news);
        trollPosts = (LinearLayout) findViewById(R.id.button_troll_posts);
        teamProfile = (LinearLayout) findViewById(R.id.button_team_profile);
        pastMatches = (LinearLayout) findViewById(R.id.button_cricket_past_matches);
        rate = (LinearLayout) findViewById(R.id.button_rate);
        ranking = (LinearLayout) findViewById(R.id.button_ranking);
        records = (LinearLayout) findViewById(R.id.button_records);
        pointsTable = (LinearLayout) findViewById(R.id.button_points_table);
        quotes = (LinearLayout) findViewById(R.id.button_quotes);

        liveStreaming = (TextView) findViewById(R.id.tv_livestreaming);
        liveScore = (TextView) findViewById(R.id.tv_livescore);
        news = (TextView) findViewById(R.id.tv_news);
        highlights = (TextView) findViewById(R.id.tv_highlights);
        fixture = (TextView) findViewById(R.id.tv_fixture);
        pastMatch = (TextView) findViewById(R.id.tv_pastmatches);
        rankings = (TextView) findViewById(R.id.tv_ranking);
        record = (TextView) findViewById(R.id.tv_records);
        pointSTable = (TextView) findViewById(R.id.tv_ptsTable);
        trolls = (TextView) findViewById(R.id.tv_trolls);
        quote = (TextView) findViewById(R.id.tv_quotes);
        profile = (TextView) findViewById(R.id.tv_profiles);
        update = (TextView) findViewById(R.id.tv_update);

        liveStreaming.setTypeface(typeface);
        liveScore.setTypeface(typeface);
        news.setTypeface(typeface);
        highlights.setTypeface(typeface);
        fixture.setTypeface(typeface);
        pastMatch.setTypeface(typeface);
        rankings.setTypeface(typeface);
        record.setTypeface(typeface);
        pointSTable.setTypeface(typeface);
        trolls.setTypeface(typeface);
        quote.setTypeface(typeface);
        profile.setTypeface(typeface);
        update.setTypeface(typeface);

        welcomeText = (TextView) findViewById(R.id.tv_welcome_text);


        String welcomeTextUrl = "http://apisea.xyz/BPL2016/apis/v1/welcometext.php?key=bl905577";
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
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d(Constants.TAG, response.toString());
            }
        });

        cricketLive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Highlights.class);
                intent.putExtra("cause", "livestream");
                startActivity(intent);
            }
        });

        cricketLiveScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String idMatcherURL = "http://apisea.xyz/BPL2016/apis/v2/livescoresource.php";
                Log.d(Constants.TAG, idMatcherURL);

                final AlertDialog progressDialog = new SpotsDialog(MainActivity.this, R.style.Custom);
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
                                String source = response.getJSONArray("content").getJSONObject(0).getString("scoresource");
                                if (source.equals("myself") || source.equals("cricinfo") || source.equals("webview")) {
                                    Intent intent = new Intent(MainActivity.this, LiveScoreListActivity.class);
                                    intent.putExtra("source", source);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(MainActivity.this, "Please Update App", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(MainActivity.this, "Failed", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        cricketHighlights.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Highlights.class);
                intent.putExtra("cause", "highlights");
                startActivity(intent);
            }
        });

        cricketFixture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FixtureActivity.class);
                startActivity(intent);
            }
        });

        cricketNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String isAllowedUrl = "http://apisea.xyz/BPL2016/apis/v1/fetchNewsUrls.php";
                Log.d(Constants.TAG, isAllowedUrl);

                final AlertDialog progressDialog = new SpotsDialog(MainActivity.this, R.style.Custom);
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
                                String source = response.getJSONArray("content").getJSONObject(0).getString("permitted");
                                if (source.equals("true")) {
                                    Intent intent = new Intent(MainActivity.this, CricketNewsListActivity.class);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(MainActivity.this, "Failed", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(MainActivity.this, "Failed", Toast.LENGTH_LONG).show();
                    }
                });

            }
        });

        trollPosts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, GalleryOfMatchActivity.class);
                startActivity(intent);
            }
        });

        teamProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TeamProfile.class);
                startActivity(intent);
            }
        });

        pastMatches.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PastMatchesActivity.class);
                startActivity(intent);
            }
        });

        rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String appPackageName = getPackageName();
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }
            }
        });

        ranking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RankingActivity.class);
                startActivity(intent);
            }
        });

        records.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RecordsActivity.class);
                startActivity(intent);
            }
        });

        pointsTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PointsTableActivity.class);
                startActivity(intent);
            }
        });

        quotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, QuotesListActivity.class);
                startActivity(intent);
            }
        });
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

}
