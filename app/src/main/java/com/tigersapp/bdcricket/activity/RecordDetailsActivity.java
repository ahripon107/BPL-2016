package com.tigersapp.bdcricket.activity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.tigersapp.bdcricket.R;
import com.tigersapp.bdcricket.adapter.MatchDetailsViewPagerAdapter;
import com.tigersapp.bdcricket.fragment.RecordsDetailsFragment;
import com.tigersapp.bdcricket.model.RecordDetailsModel1;
import com.tigersapp.bdcricket.model.RecordDetailsModel2;
import com.tigersapp.bdcricket.model.RecordDetailsModel3;
import com.tigersapp.bdcricket.util.Constants;
import com.tigersapp.bdcricket.util.Dialogs;
import com.tigersapp.bdcricket.util.FetchFromWeb;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import dmax.dialog.SpotsDialog;

/**
 * @author Ripon
 */
public class RecordDetailsActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager viewPager;
    Spinner spinner;
    AdView adView;
    MatchDetailsViewPagerAdapter matchDetailsViewPagerAdapter;
    String recordType, url;
    Dialogs dialogs;

    RecordsDetailsFragment testFragment, odiFragment, t20Fragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);
        setTitle(getIntent().getStringExtra("title"));
        recordType = getIntent().getStringExtra("recordtype");
        url = getIntent().getStringExtra("url");
        dialogs = new Dialogs(this);

        tabLayout = (TabLayout) findViewById(R.id.tab_ranking);
        viewPager = (ViewPager) findViewById(R.id.view_pager_ranking);
        spinner = (Spinner) findViewById(R.id.spinner);
        adView = (AdView) findViewById(R.id.adViewMatchDetails);

        AdRequest adRequest = new AdRequest.Builder().addTestDevice(Constants.ONE_PLUS_TEST_DEVICE)
                .addTestDevice(Constants.XIAOMI_TEST_DEVICE).build();
        adView.loadAd(adRequest);

        testFragment = new RecordsDetailsFragment();
        odiFragment = new RecordsDetailsFragment();
        t20Fragment = new RecordsDetailsFragment();

        List<String> categories = new ArrayList<String>();
        if (recordType.equals("batting") || recordType.equals("bowling")) {
            categories.add("All Seasons");
            categories.add("Season 2016");
            categories.add("Season 2015");
            categories.add("Season 2014");
            categories.add("Season 2013");
            categories.add("Season 2012");
            categories.add("Season 2011");
            categories.add("Season 2010");
            categories.add("Season 2009");
            categories.add("Season 2008");
            categories.add("Season 2007");
        } else {
            categories.add("All Seasons");
        }


        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setupViewPager(viewPager);
        viewPager.setOffscreenPageLimit(2);
        tabLayout.setupWithViewPager(viewPager);

        //fetchData(url + "/overallseasons/all");

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    fetchData(url + "/overallseasons/all");
                } else if (position == 1) {
                    fetchData(url + "/2016");
                } else if (position == 2) {
                    fetchData(url + "/2015");
                } else if (position == 3) {
                    fetchData(url + "/2014");
                } else if (position == 4) {
                    fetchData(url + "/2013");
                } else if (position == 5) {
                    fetchData(url + "/2012");
                } else if (position == 6) {
                    fetchData(url + "/2011");
                } else if (position == 7) {
                    fetchData(url + "/2010");
                } else if (position == 8) {
                    fetchData(url + "/2009");
                } else if (position == 9) {
                    fetchData(url + "/2008");
                } else if (position == 10) {
                    fetchData(url + "/2007");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    public final void setupViewPager(ViewPager viewPager) {
        this.matchDetailsViewPagerAdapter = new MatchDetailsViewPagerAdapter(getSupportFragmentManager());
        this.matchDetailsViewPagerAdapter.addFragment(testFragment, "Test");
        this.matchDetailsViewPagerAdapter.addFragment(odiFragment, "ODI");
        this.matchDetailsViewPagerAdapter.addFragment(t20Fragment, "T20I");
        viewPager.setAdapter(this.matchDetailsViewPagerAdapter);
    }

    public void fetchData(String url) {
        dialogs.showDialog();
        FetchFromWeb.get(url, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                dialogs.dismissDialog();
                try {
                    if (testFragment.isAdded()) {
                        ArrayList<RecordDetailsModel1> sixElementModels = new ArrayList<RecordDetailsModel1>();
                        ArrayList<RecordDetailsModel2> fourElementModel = new ArrayList<RecordDetailsModel2>();
                        ArrayList<RecordDetailsModel3> threeElementModel = new ArrayList<RecordDetailsModel3>();

                        String feedHeader = response.getJSONObject("series-stats").getString("feedHeader");
                        String arr[] = feedHeader.split(",");
                        String header = response.getJSONObject("series-stats").getString("header");
                        String a[] = header.split(",");
                        JSONArray jsonArray = response.getJSONObject("series-stats").getJSONArray("Test");
                        if (arr.length == 6) {
                            sixElementModels.add(new RecordDetailsModel1(a[0],a[1],a[2],a[3],a[4],a[5]));
                            for (int i=0;i<jsonArray.length();i++) {
                                JSONObject obj = jsonArray.getJSONObject(i);
                                sixElementModels.add(new RecordDetailsModel1(obj.getString(arr[0]),obj.getString(arr[1]),obj.getString(arr[2]),obj.getString(arr[3]),obj.getString(arr[4]),obj.getString(arr[5])));
                            }
                            testFragment.setUpSixElements(sixElementModels);
                        } else if (arr.length == 4) {
                            fourElementModel.add(new RecordDetailsModel2(a[0],a[1],a[2],a[3]));
                            for (int i=0;i<jsonArray.length();i++) {
                                JSONObject obj = jsonArray.getJSONObject(i);
                                fourElementModel.add(new RecordDetailsModel2(obj.getString(arr[0]),obj.getString(arr[1]),obj.getString(arr[2]),obj.getString(arr[3])));
                            }
                            testFragment.setUpFourElements(fourElementModel);
                        } else if (arr.length == 3) {
                            threeElementModel.add(new RecordDetailsModel3(a[0],a[1],a[2]));
                            for (int i=0;i<jsonArray.length();i++) {
                                JSONObject obj = jsonArray.getJSONObject(i);
                                threeElementModel.add(new RecordDetailsModel3(obj.getString(arr[0]),obj.getString(arr[1]),obj.getString(arr[2])));
                            }
                            testFragment.setUpThreeElements(threeElementModel);
                        }

                    }

                    if (odiFragment.isAdded()) {
                        ArrayList<RecordDetailsModel1> sixElementModels = new ArrayList<RecordDetailsModel1>();
                        ArrayList<RecordDetailsModel2> fourElementModel = new ArrayList<RecordDetailsModel2>();
                        ArrayList<RecordDetailsModel3> threeElementModel = new ArrayList<RecordDetailsModel3>();

                        String feedHeader = response.getJSONObject("series-stats").getString("feedHeader");
                        String arr[] = feedHeader.split(",");
                        String header = response.getJSONObject("series-stats").getString("header");
                        String a[] = header.split(",");
                        JSONArray jsonArray = response.getJSONObject("series-stats").getJSONArray("Odi");
                        if (arr.length == 6) {
                            sixElementModels.add(new RecordDetailsModel1(a[0],a[1],a[2],a[3],a[4],a[5]));
                            for (int i=0;i<jsonArray.length();i++) {
                                JSONObject obj = jsonArray.getJSONObject(i);
                                sixElementModels.add(new RecordDetailsModel1(obj.getString(arr[0]),obj.getString(arr[1]),obj.getString(arr[2]),obj.getString(arr[3]),obj.getString(arr[4]),obj.getString(arr[5])));
                            }
                            odiFragment.setUpSixElements(sixElementModels);
                        } else if (arr.length == 4) {
                            fourElementModel.add(new RecordDetailsModel2(a[0],a[1],a[2],a[3]));
                            for (int i=0;i<jsonArray.length();i++) {
                                JSONObject obj = jsonArray.getJSONObject(i);
                                fourElementModel.add(new RecordDetailsModel2(obj.getString(arr[0]),obj.getString(arr[1]),obj.getString(arr[2]),obj.getString(arr[3])));
                            }
                            odiFragment.setUpFourElements(fourElementModel);
                        } else if (arr.length == 3) {
                            threeElementModel.add(new RecordDetailsModel3(a[0],a[1],a[2]));
                            for (int i=0;i<jsonArray.length();i++) {
                                JSONObject obj = jsonArray.getJSONObject(i);
                                threeElementModel.add(new RecordDetailsModel3(obj.getString(arr[0]),obj.getString(arr[1]),obj.getString(arr[2])));
                            }
                            odiFragment.setUpThreeElements(threeElementModel);
                        }

                    }

                    if (t20Fragment.isAdded()) {
                        ArrayList<RecordDetailsModel1> sixElementModels = new ArrayList<RecordDetailsModel1>();
                        ArrayList<RecordDetailsModel2> fourElementModel = new ArrayList<RecordDetailsModel2>();
                        ArrayList<RecordDetailsModel3> threeElementModel = new ArrayList<RecordDetailsModel3>();

                        String feedHeader = response.getJSONObject("series-stats").getString("feedHeader");
                        String arr[] = feedHeader.split(",");
                        String header = response.getJSONObject("series-stats").getString("header");
                        String a[] = header.split(",");
                        JSONArray jsonArray = response.getJSONObject("series-stats").getJSONArray("T20");
                        if (arr.length == 6) {
                            sixElementModels.add(new RecordDetailsModel1(a[0],a[1],a[2],a[3],a[4],a[5]));
                            for (int i=0;i<jsonArray.length();i++) {
                                JSONObject obj = jsonArray.getJSONObject(i);
                                sixElementModels.add(new RecordDetailsModel1(obj.getString(arr[0]),obj.getString(arr[1]),obj.getString(arr[2]),obj.getString(arr[3]),obj.getString(arr[4]),obj.getString(arr[5])));
                            }
                            t20Fragment.setUpSixElements(sixElementModels);
                        } else if (arr.length == 4) {
                            fourElementModel.add(new RecordDetailsModel2(a[0],a[1],a[2],a[3]));
                            for (int i=0;i<jsonArray.length();i++) {
                                JSONObject obj = jsonArray.getJSONObject(i);
                                fourElementModel.add(new RecordDetailsModel2(obj.getString(arr[0]),obj.getString(arr[1]),obj.getString(arr[2]),obj.getString(arr[3])));
                            }
                            t20Fragment.setUpFourElements(fourElementModel);
                        } else if (arr.length == 3) {
                            threeElementModel.add(new RecordDetailsModel3(a[0],a[1],a[2]));
                            for (int i=0;i<jsonArray.length();i++) {
                                JSONObject obj = jsonArray.getJSONObject(i);
                                threeElementModel.add(new RecordDetailsModel3(obj.getString(arr[0]),obj.getString(arr[1]),obj.getString(arr[2])));
                            }
                            t20Fragment.setUpThreeElements(threeElementModel);
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d(Constants.TAG, response.toString());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                dialogs.dismissDialog();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                dialogs.dismissDialog();
            }
        });
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
