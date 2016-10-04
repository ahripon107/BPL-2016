package com.tigersapp.bdcricket.activity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.tigersapp.bdcricket.R;
import com.tigersapp.bdcricket.adapter.MatchDetailsViewPagerAdapter;
import com.tigersapp.bdcricket.fragment.FragmentMatchSummary;
import com.tigersapp.bdcricket.fragment.FragmentScoreBoard;
import com.tigersapp.bdcricket.fragment.PlayingXIFragment;
import com.tigersapp.bdcricket.model.Commentry;
import com.tigersapp.bdcricket.model.Summary;
import com.tigersapp.bdcricket.util.Constants;
import com.tigersapp.bdcricket.util.FetchFromWeb;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import dmax.dialog.SpotsDialog;

/**
 * @author ripon
 */
public class ActivityMatchDetails extends AppCompatActivity {
    private String liveMatchID;
    private MatchDetailsViewPagerAdapter matchDetailsViewPagerAdapter;
    private ViewPager viewPager;
    private Gson gson;
    ArrayList<Commentry> commentry = new ArrayList<>();

    AdView adView;

    public int numberOfInnings;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_details);
        this.liveMatchID = getIntent().getStringExtra("matchID");
        numberOfInnings = 0;

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        this.viewPager = (ViewPager) findViewById(R.id.viewPager);
        adView = (AdView) findViewById(R.id.adViewMatchDetails);
        this.viewPager.setOffscreenPageLimit(3);
        setupViewPage(this.viewPager);
        ((TabLayout) findViewById(R.id.tabLayout)).setupWithViewPager(this.viewPager);
        gson = new Gson();
        sendRequestForLiveMatchDetails();

        AdRequest adRequest = new AdRequest.Builder().addTestDevice(Constants.ONE_PLUS_TEST_DEVICE)
                .addTestDevice(Constants.XIAOMI_TEST_DEVICE).build();
        adView.loadAd(adRequest);
    }


    public final void setupViewPage(ViewPager viewPager) {
        this.matchDetailsViewPagerAdapter = new MatchDetailsViewPagerAdapter(getSupportFragmentManager());
        this.matchDetailsViewPagerAdapter.addFragment(new FragmentScoreBoard(), "Score Board");
        this.matchDetailsViewPagerAdapter.addFragment(new FragmentMatchSummary(), "Commentry");
        //this.matchDetailsViewPagerAdapter.addFragment(new PlayingXIFragment(),"Playing XI");
        viewPager.setAdapter(this.matchDetailsViewPagerAdapter);
    }

    private void sendRequestForLiveMatchDetails() {
        String url = "http://37.187.95.220:8080/cricket-api/api/match/" + this.liveMatchID;
        Log.d(Constants.TAG, url);

        final AlertDialog progressDialog = new SpotsDialog(ActivityMatchDetails.this, R.style.Custom);
        progressDialog.show();
        progressDialog.setCancelable(true);

        FetchFromWeb.get(url, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                progressDialog.dismiss();
                try {
                    JSONObject sumry = response.getJSONObject("summary");
                    Summary summary = gson.fromJson(String.valueOf(sumry), Summary.class);


                    ((FragmentScoreBoard) ActivityMatchDetails.this.matchDetailsViewPagerAdapter.getItem(0)).setMatchSummary(summary);
                    ((FragmentScoreBoard) ActivityMatchDetails.this.matchDetailsViewPagerAdapter.getItem(0)).loadEachTeamScore(liveMatchID);

                    /*if (response.has("innings1") && response.has("innings2")) {
                        ((PlayingXIFragment) ActivityMatchDetails.this.matchDetailsViewPagerAdapter.getItem(2)).setPlayingXI(response.getJSONObject("innings1").getJSONArray("batting"),response.getJSONObject("innings1").getJSONArray("dnb"),response.getJSONObject("innings2").getJSONArray("batting"),response.getJSONObject("innings2").getJSONArray("dnb"),summary.getTeam1(),summary.getTeam2());
                    }*/

                    if (response.has("innings1")) {

                        if (response.getJSONObject("innings1").has("summary")) {
                            numberOfInnings = 1;
                            ((FragmentScoreBoard) ActivityMatchDetails.this.matchDetailsViewPagerAdapter.getItem(0)).setFirstInningsBattingList(response.getJSONObject("innings1").getJSONArray("batting"));
                            ((FragmentScoreBoard) ActivityMatchDetails.this.matchDetailsViewPagerAdapter.getItem(0)).setFirstInningsBowlingList(response.getJSONObject("innings1").getJSONArray("bowling"));
                            ((FragmentScoreBoard) ActivityMatchDetails.this.matchDetailsViewPagerAdapter.getItem(0)).setFirstInningsSummary(response.getJSONObject("innings1").getJSONObject("summary"));
                            ((FragmentScoreBoard) ActivityMatchDetails.this.matchDetailsViewPagerAdapter.getItem(0)).setFirstInningsFOW(response.getJSONObject("innings1").getJSONArray("fow"));
                            ((FragmentScoreBoard) ActivityMatchDetails.this.matchDetailsViewPagerAdapter.getItem(0)).setFirstInningsDNB(response.getJSONObject("innings1").getJSONArray("dnb"));

                        } else {
                            ((FragmentScoreBoard) ActivityMatchDetails.this.matchDetailsViewPagerAdapter.getItem(0)).hideFirstInnings();
                        }

                    } else {
                        ((FragmentScoreBoard) ActivityMatchDetails.this.matchDetailsViewPagerAdapter.getItem(0)).hideFirstInnings();
                    }
                    if (response.has("innings2")) {
                        if (response.getJSONObject("innings2").has("summary")) {
                            numberOfInnings = 2;
                            ((FragmentScoreBoard) ActivityMatchDetails.this.matchDetailsViewPagerAdapter.getItem(0)).setSecondInningsBattingList(response.getJSONObject("innings2").getJSONArray("batting"));
                            ((FragmentScoreBoard) ActivityMatchDetails.this.matchDetailsViewPagerAdapter.getItem(0)).setSecondInningsBowlingList(response.getJSONObject("innings2").getJSONArray("bowling"));
                            ((FragmentScoreBoard) ActivityMatchDetails.this.matchDetailsViewPagerAdapter.getItem(0)).setSecondInningsSummary(response.getJSONObject("innings2").getJSONObject("summary"));
                            ((FragmentScoreBoard) ActivityMatchDetails.this.matchDetailsViewPagerAdapter.getItem(0)).setSecondInningsFOW(response.getJSONObject("innings2").getJSONArray("fow"));
                            ((FragmentScoreBoard) ActivityMatchDetails.this.matchDetailsViewPagerAdapter.getItem(0)).setSecondInningsDNB(response.getJSONObject("innings2").getJSONArray("dnb"));

                        } else {
                            ((FragmentScoreBoard) ActivityMatchDetails.this.matchDetailsViewPagerAdapter.getItem(0)).hideSecondInnings();
                        }

                    } else {
                        ((FragmentScoreBoard) ActivityMatchDetails.this.matchDetailsViewPagerAdapter.getItem(0)).hideSecondInnings();
                    }
                    if (response.has("innings3")) {
                        if (response.getJSONObject("innings3").has("summary")) {
                            numberOfInnings = 3;
                            ((FragmentScoreBoard) ActivityMatchDetails.this.matchDetailsViewPagerAdapter.getItem(0)).setThirdInningsBattingList(response.getJSONObject("innings3").getJSONArray("batting"));
                            ((FragmentScoreBoard) ActivityMatchDetails.this.matchDetailsViewPagerAdapter.getItem(0)).setThirdInningsBowlingList(response.getJSONObject("innings3").getJSONArray("bowling"));
                            ((FragmentScoreBoard) ActivityMatchDetails.this.matchDetailsViewPagerAdapter.getItem(0)).setThirdInningsSummary(response.getJSONObject("innings3").getJSONObject("summary"));
                            ((FragmentScoreBoard) ActivityMatchDetails.this.matchDetailsViewPagerAdapter.getItem(0)).setThirdInningsFOW(response.getJSONObject("innings3").getJSONArray("fow"));
                            ((FragmentScoreBoard) ActivityMatchDetails.this.matchDetailsViewPagerAdapter.getItem(0)).setThirdInningsDNB(response.getJSONObject("innings3").getJSONArray("dnb"));
                        } else {
                            ((FragmentScoreBoard) ActivityMatchDetails.this.matchDetailsViewPagerAdapter.getItem(0)).hideThirdInnings();
                        }
                    } else {
                        ((FragmentScoreBoard) ActivityMatchDetails.this.matchDetailsViewPagerAdapter.getItem(0)).hideThirdInnings();
                    }
                    if (response.has("innings4")) {
                        if (response.getJSONObject("innings4").has("summary")) {
                            numberOfInnings = 4;
                            ((FragmentScoreBoard) ActivityMatchDetails.this.matchDetailsViewPagerAdapter.getItem(0)).setFourthInningsBattingList(response.getJSONObject("innings4").getJSONArray("batting"));
                            ((FragmentScoreBoard) ActivityMatchDetails.this.matchDetailsViewPagerAdapter.getItem(0)).setFourthInningsBowlingList(response.getJSONObject("innings4").getJSONArray("bowling"));
                            ((FragmentScoreBoard) ActivityMatchDetails.this.matchDetailsViewPagerAdapter.getItem(0)).setFourthInningsSummary(response.getJSONObject("innings4").getJSONObject("summary"));
                            ((FragmentScoreBoard) ActivityMatchDetails.this.matchDetailsViewPagerAdapter.getItem(0)).setFourthInningsFOW(response.getJSONObject("innings4").getJSONArray("fow"));
                            ((FragmentScoreBoard) ActivityMatchDetails.this.matchDetailsViewPagerAdapter.getItem(0)).setFourthInningsDNB(response.getJSONObject("innings4").getJSONArray("dnb"));
                        } else {
                            ((FragmentScoreBoard) ActivityMatchDetails.this.matchDetailsViewPagerAdapter.getItem(0)).hideFourthInnings();
                        }
                    } else {
                        ((FragmentScoreBoard) ActivityMatchDetails.this.matchDetailsViewPagerAdapter.getItem(0)).hideFourthInnings();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d(Constants.TAG, response.toString());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                progressDialog.dismiss();
                Toast.makeText(ActivityMatchDetails.this, "Failed", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                progressDialog.dismiss();
                Toast.makeText(ActivityMatchDetails.this, "Failed", Toast.LENGTH_LONG).show();
            }
        });

        String idMatcherURL = "http://apisea.xyz/Cricket/apis/v1/fetchIDMatcher.php";
        Log.d(Constants.TAG, idMatcherURL);

        RequestParams params = new RequestParams();
        params.add("key", "bl905577");
        params.add("cricinfo", liveMatchID);
        FetchFromWeb.get(idMatcherURL, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    if (response.getString("msg").equals("Successful")) {
                        String yahooID = response.getJSONArray("content").getJSONObject(0).getString("yahooID");
                        ((FragmentMatchSummary) ActivityMatchDetails.this.matchDetailsViewPagerAdapter.getItem(1)).setMatchID(yahooID);
                        loadCommentry(yahooID);
                    } else {
                        ((FragmentMatchSummary) ActivityMatchDetails.this.matchDetailsViewPagerAdapter.getItem(1)).setNoCommentry();
                    }

                    Log.d(Constants.TAG, response.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {

            }
        });
    }

    public void loadCommentry(final String yahooID) {

        //String url = "http://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20cricket.commentary%20where%20match_id="+yahooID+"%20and%20innings_id=1&format=json&diagnostics=false&env=store%3A%2F%2F0TxIGQMQbObzvU4Apia0V0&callback=";
        String url = "http://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20cricket.commentary%20where%20match_id="+yahooID+"%20limit%205&format=json&diagnostics=false&env=store%3A%2F%2F0TxIGQMQbObzvU4Apia0V0&callback=";
        //11980
        FetchFromWeb.get(url, null, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                commentry.clear();

                try {
                    Object object = response.getJSONObject("query").getJSONObject("results").get("Over");
                    if (object instanceof JSONArray) {
                        JSONArray jsonArray = (JSONArray) object;
                        for (int p = 0; p < jsonArray.length(); p++) {
                            object = jsonArray.getJSONObject(p).get("Ball");
                            if (object instanceof JSONArray) {
                                JSONArray jsonArray1 = (JSONArray) object;
                                for (int i = 0; i < jsonArray1.length(); i++) {
                                    JSONObject obj = jsonArray1.getJSONObject(i);
                                    if (obj.getString("type").equals("ball")) {
                                        String string = "";
                                        String ov = (Integer.parseInt(obj.getString("ov"))-1) + "." + obj.getString("n") + " ";
                                        string += obj.getString("shc") + " - ";
                                        string += obj.getString("r") + " run; ";
                                        string += obj.getString("c");
                                        if (obj.has("dmsl")) {
                                            commentry.add(new Commentry("ball","W",ov,string));
                                        } else {
                                            commentry.add(new Commentry("ball",obj.getString("r"),ov,string));
                                        }

                                    } else {
                                        commentry.add(new Commentry("nonball","","",obj.getString("c")));
                                    }
                                }
                            } else if (object instanceof JSONObject) {
                                JSONObject obj = (JSONObject) object;
                                if (obj.getString("type").equals("ball")) {
                                    String string = "";
                                    String ov = (Integer.parseInt(obj.getString("ov"))-1) + "." + obj.getString("n") + " ";
                                    string += obj.getString("shc") + " - ";
                                    string += obj.getString("r") + " run; ";
                                    string += obj.getString("c");
                                    if (obj.has("dmsl")) {
                                        commentry.add(new Commentry("ball","W",ov,string));
                                    } else {
                                        commentry.add(new Commentry("ball",obj.getString("r"),ov,string));
                                    }
                                } else {
                                    commentry.add(new Commentry("nonball","","",obj.getString("c")));
                                }
                            }
                            //commentry.add("-------------------------------------------");
                        }
                    } else if (object instanceof JSONObject) {
                        JSONObject objt = (JSONObject) object;
                        JSONArray jsonArray1 = objt.getJSONArray("Ball");
                        for (int i = 0; i < jsonArray1.length(); i++) {
                            JSONObject obj = jsonArray1.getJSONObject(i);
                            if (obj.getString("type").equals("ball")) {
                                String string = "";
                                String ov = (Integer.parseInt(obj.getString("ov"))-1) + "." + obj.getString("n") + " ";
                                string += obj.getString("shc") + " - ";
                                string += obj.getString("r") + " run; ";
                                string += obj.getString("c");
                                if (obj.has("dmsl")) {
                                    commentry.add(new Commentry("ball","W",ov,string));
                                } else {
                                    commentry.add(new Commentry("ball",obj.getString("r"),ov,string));
                                }
                            } else {
                                commentry.add(new Commentry("nonball","","",obj.getString("c")));
                            }
                        }

                    }

                    ((FragmentMatchSummary) ActivityMatchDetails.this.matchDetailsViewPagerAdapter.getItem(1)).setCommentry(commentry);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d(Constants.TAG, response.toString());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Toast.makeText(ActivityMatchDetails.this, "Failed", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.refresh_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_load:
                sendRequestForLiveMatchDetails();
                return true;
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}