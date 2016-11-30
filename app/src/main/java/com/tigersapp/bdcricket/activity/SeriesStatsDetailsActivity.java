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

public class SeriesStatsDetailsActivity extends AppCompatActivity{

    TabLayout tabLayout;
    ViewPager viewPager;
    Spinner spinner;
    AdView adView;
    MatchDetailsViewPagerAdapter matchDetailsViewPagerAdapter;
    String seriesId, url;
    List<String> currentSeries, seriesIds;
    Dialogs dialogs;

    RecordsDetailsFragment testFragment, odiFragment, t20Fragment;
    ArrayAdapter<String> dataAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);
        setTitle(getIntent().getStringExtra("title"));
        seriesId = getIntent().getStringExtra("seriesId");
        url = getIntent().getStringExtra("url");
        currentSeries = new ArrayList<>();
        seriesIds = new ArrayList<>();
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


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //setupViewPager(viewPager);
        viewPager.setOffscreenPageLimit(2);


        dataAdapter = new ArrayAdapter<String>(SeriesStatsDetailsActivity.this, android.R.layout.simple_spinner_item, currentSeries);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);

        fetchData(url+"/"+seriesId);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d(Constants.TAG, "onItemSelected: called");
                fetchData(url+"/"+seriesIds.get(position));
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
                    currentSeries.clear();
                    seriesIds.clear();
                    JSONArray jsonArray1 = response.getJSONObject("series-stats").getJSONArray("relatedSeries");
                    for (int i = 0; i < jsonArray1.length(); i++) {
                        JSONObject jsonObject = jsonArray1.getJSONObject(i);
                        currentSeries.add(jsonObject.getString("seriesName"));
                        seriesIds.add(jsonObject.getString("seriesId"));
                    }
                    dataAdapter.notifyDataSetChanged();
                    matchDetailsViewPagerAdapter = new MatchDetailsViewPagerAdapter(getSupportFragmentManager());
                    if (response.getJSONObject("series-stats").getJSONArray("Test").length() > 0)
                        matchDetailsViewPagerAdapter.addFragment(testFragment, "Test");
                    if (response.getJSONObject("series-stats").getJSONArray("Odi").length() > 0)
                        matchDetailsViewPagerAdapter.addFragment(odiFragment, "ODI");
                    if (response.getJSONObject("series-stats").getJSONArray("T20").length() > 0)
                        matchDetailsViewPagerAdapter.addFragment(t20Fragment, "T20I");

                    viewPager.setAdapter(matchDetailsViewPagerAdapter);
                    tabLayout.setupWithViewPager(viewPager);

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
