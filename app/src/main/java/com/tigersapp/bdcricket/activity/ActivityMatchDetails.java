package com.tigersapp.bdcricket.activity;

import android.os.Bundle;
import android.os.Message;
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
import com.google.inject.Inject;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.tigersapp.bdcricket.R;
import com.tigersapp.bdcricket.adapter.MatchDetailsViewPagerAdapter;
import com.tigersapp.bdcricket.fragment.FragmentMatchSummary;
import com.tigersapp.bdcricket.fragment.FragmentScoreBoard;
import com.tigersapp.bdcricket.fragment.GossipFragment;
import com.tigersapp.bdcricket.fragment.PlayingXIFragment;
import com.tigersapp.bdcricket.model.Commentry;
import com.tigersapp.bdcricket.model.Summary;
import com.tigersapp.bdcricket.util.Constants;
import com.tigersapp.bdcricket.util.DefaultMessageHandler;
import com.tigersapp.bdcricket.util.Dialogs;
import com.tigersapp.bdcricket.util.FetchFromWeb;
import com.tigersapp.bdcricket.util.NetworkService;
import com.tigersapp.bdcricket.util.RoboAppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

/**
 * @author ripon
 */
@ContentView(R.layout.activity_match_details)
public class ActivityMatchDetails extends RoboAppCompatActivity {

    @InjectView(R.id.viewPager)
    private ViewPager viewPager;

    @InjectView(R.id.adViewMatchDetails)
    private AdView adView;

    @InjectView(R.id.tabLayout)
    private TabLayout tabLayout;

    @Inject
    private Gson gson;

    @Inject
    private ArrayList<Commentry> commentry;

    @Inject
    private NetworkService networkService;

    private String liveMatchID;
    private MatchDetailsViewPagerAdapter matchDetailsViewPagerAdapter;
    public int numberOfInnings;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.liveMatchID = getIntent().getStringExtra("matchID");
        GossipFragment.matchId = liveMatchID;
        numberOfInnings = 0;

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewPager.setOffscreenPageLimit(4);
        setupViewPage(this.viewPager);
        tabLayout.setupWithViewPager(viewPager);

        sendRequestForLiveMatchDetails();

        AdRequest adRequest = new AdRequest.Builder().addTestDevice(Constants.ONE_PLUS_TEST_DEVICE)
                .addTestDevice(Constants.XIAOMI_TEST_DEVICE).build();
        adView.loadAd(adRequest);
    }


    public final void setupViewPage(ViewPager viewPager) {
        this.matchDetailsViewPagerAdapter = new MatchDetailsViewPagerAdapter(getSupportFragmentManager());
        this.matchDetailsViewPagerAdapter.addFragment(new FragmentScoreBoard(), "স্কোর বোর্ড");
        this.matchDetailsViewPagerAdapter.addFragment(new FragmentMatchSummary(), "কমেন্ট্রি");
        this.matchDetailsViewPagerAdapter.addFragment(new PlayingXIFragment(),"একাদশ");
        this.matchDetailsViewPagerAdapter.addFragment(new GossipFragment(), "আড্ডা");
        viewPager.setAdapter(this.matchDetailsViewPagerAdapter);
    }

    private void sendRequestForLiveMatchDetails() {

        networkService.fetchMatchDetails(liveMatchID, new DefaultMessageHandler(this, true) {
            @Override
            public void onSuccess(Message msg) {
                String string = (String) msg.obj;

                try {
                    JSONObject response = new JSONObject(string);

                    JSONObject sumry = response.getJSONObject("summary");
                    Summary summary = gson.fromJson(String.valueOf(sumry), Summary.class);


                    ((FragmentScoreBoard) ActivityMatchDetails.this.matchDetailsViewPagerAdapter.getItem(0)).setMatchSummary(summary);
                    //((FragmentScoreBoard) ActivityMatchDetails.this.matchDetailsViewPagerAdapter.getItem(0)).loadEachTeamScore(liveMatchID);

                    if (response.has("innings1") && response.has("innings2")) {
                        ((PlayingXIFragment) ActivityMatchDetails.this.matchDetailsViewPagerAdapter.getItem(2)).setPlayingXI(response.getJSONObject("innings1").getJSONArray("batting"),response.getJSONObject("innings1").getJSONArray("dnb"),response.getJSONObject("innings2").getJSONArray("batting"),response.getJSONObject("innings2").getJSONArray("dnb"),summary.getTeam1(),summary.getTeam2());
                    }

                    if (response.has("innings1")) {

                        if (response.getJSONObject("innings1").has("summary")) {
                            numberOfInnings = 1;
                        } else {
                            ((FragmentScoreBoard) ActivityMatchDetails.this.matchDetailsViewPagerAdapter.getItem(0)).hideFirstInnings();
                        }
                    } else {
                        ((FragmentScoreBoard) ActivityMatchDetails.this.matchDetailsViewPagerAdapter.getItem(0)).hideFirstInnings();
                    }
                    if (response.has("innings2")) {
                        if (response.getJSONObject("innings2").has("summary")) {
                            numberOfInnings = 2;
                        }
                    }
                    if (response.has("innings3")) {
                        if (response.getJSONObject("innings3").has("summary")) {
                            numberOfInnings = 3;
                        }
                    }
                    if (response.has("innings4")) {
                        if (response.getJSONObject("innings4").has("summary")) {
                            numberOfInnings = 4;
                        }
                    }

                    ((FragmentScoreBoard) ActivityMatchDetails.this.matchDetailsViewPagerAdapter.getItem(0)).setResponse(response, numberOfInnings);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        String url = "http://37.187.95.220:8080/cricket-api/api/match/" + this.liveMatchID;
        Log.d(Constants.TAG, url);


        String idMatcherURL = "http://apisea.xyz/Cricket/apis/v1/fetchIDMatcher.php";
        Log.d(Constants.TAG, idMatcherURL);

        networkService.fetchMatchIdMatcher(liveMatchID, new DefaultMessageHandler(this, false){
            @Override
            public void onSuccess(Message msg) {
                String string = (String) msg.obj;
                try {
                    JSONObject response = new JSONObject(string);

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
        });

        networkService.fetchComments("gossip"+liveMatchID, new DefaultMessageHandler(this, false){
            @Override
            public void onSuccess(Message msg) {
                String string = (String) msg.obj;

                try {
                    JSONObject response = new JSONObject(string);
                    ((GossipFragment) ActivityMatchDetails.this.matchDetailsViewPagerAdapter.getItem(3)).populateList(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void loadCommentry(String yahooID) {

        networkService.loadCommentryFromYahoo(yahooID, new DefaultMessageHandler(this, false){
            @Override
            public void onSuccess(Message msg) {
                commentry.clear();

                String string1 = (String) msg.obj;

                try {
                    JSONObject response = new JSONObject(string1);

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