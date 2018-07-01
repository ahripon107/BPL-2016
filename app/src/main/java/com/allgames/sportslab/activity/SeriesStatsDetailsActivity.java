package com.allgames.sportslab.activity;

import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.allgames.sportslab.R;
import com.allgames.sportslab.adapter.MatchDetailsViewPagerAdapter;
import com.allgames.sportslab.fragment.RecordsDetailsFragment;
import com.allgames.sportslab.model.RecordDetailsModel1;
import com.allgames.sportslab.model.RecordDetailsModel2;
import com.allgames.sportslab.model.RecordDetailsModel3;
import com.allgames.sportslab.util.Constants;
import com.allgames.sportslab.util.DefaultMessageHandler;
import com.allgames.sportslab.util.NetworkService;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.inject.Inject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import roboguice.inject.ContentView;

/**
 * @author Ripon
 */
@ContentView(R.layout.activity_ranking)
public class SeriesStatsDetailsActivity extends CommonActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private Spinner spinner;
    private AdView adView;
    private MatchDetailsViewPagerAdapter matchDetailsViewPagerAdapter;
    private String seriesId;
    private String url;
    private List<String> currentSeries;
    private List<String> seriesIds;

    private RecordsDetailsFragment testFragment;
    private RecordsDetailsFragment odiFragment;
    private RecordsDetailsFragment t20Fragment;
    private ArrayAdapter<String> dataAdapter;

    @Inject
    private NetworkService networkService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle(getIntent().getStringExtra("title"));
        seriesId = getIntent().getStringExtra("seriesId");
        url = getIntent().getStringExtra("url");
        currentSeries = new ArrayList<>();
        seriesIds = new ArrayList<>();

        tabLayout = findViewById(R.id.tab_ranking);
        viewPager = findViewById(R.id.view_pager_ranking);
        spinner = findViewById(R.id.spinner);
        adView = findViewById(R.id.adViewMatchDetails);

        AdRequest adRequest = new AdRequest.Builder().addTestDevice(Constants.ONE_PLUS_TEST_DEVICE)
                .addTestDevice(Constants.XIAOMI_TEST_DEVICE).build();
        adView.loadAd(adRequest);

        testFragment = new RecordsDetailsFragment();
        odiFragment = new RecordsDetailsFragment();
        t20Fragment = new RecordsDetailsFragment();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        viewPager.setOffscreenPageLimit(2);

        dataAdapter = new ArrayAdapter<String>(SeriesStatsDetailsActivity.this, android.R.layout.simple_spinner_item, currentSeries);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);

        fetchData(url + "/" + seriesId);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d(Constants.TAG, "onItemSelected: called");
                fetchData(url + "/" + seriesIds.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void fetchData(String url) {
        networkService.fetch(url, new DefaultMessageHandler(this, true) {
            @Override
            public void onSuccess(Message msg) {
                try {
                    JSONObject response = new JSONObject((String) msg.obj);
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
                            sixElementModels.add(new RecordDetailsModel1(a[0], a[1], a[2], a[3], a[4], a[5]));
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject obj = jsonArray.getJSONObject(i);
                                sixElementModels.add(new RecordDetailsModel1(obj.getString(arr[0]), obj.getString(arr[1]), obj.getString(arr[2]), obj.getString(arr[3]), obj.getString(arr[4]), obj.getString(arr[5])));
                            }
                            testFragment.setUpSixElements(sixElementModels);
                        } else if (arr.length == 4) {
                            fourElementModel.add(new RecordDetailsModel2(a[0], a[1], a[2], a[3]));
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject obj = jsonArray.getJSONObject(i);
                                fourElementModel.add(new RecordDetailsModel2(obj.getString(arr[0]), obj.getString(arr[1]), obj.getString(arr[2]), obj.getString(arr[3])));
                            }
                            testFragment.setUpFourElements(fourElementModel);
                        } else if (arr.length == 3) {
                            threeElementModel.add(new RecordDetailsModel3(a[0], a[1], a[2]));
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject obj = jsonArray.getJSONObject(i);
                                threeElementModel.add(new RecordDetailsModel3(obj.getString(arr[0]), obj.getString(arr[1]), obj.getString(arr[2])));
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
                            sixElementModels.add(new RecordDetailsModel1(a[0], a[1], a[2], a[3], a[4], a[5]));
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject obj = jsonArray.getJSONObject(i);
                                sixElementModels.add(new RecordDetailsModel1(obj.getString(arr[0]), obj.getString(arr[1]), obj.getString(arr[2]), obj.getString(arr[3]), obj.getString(arr[4]), obj.getString(arr[5])));
                            }
                            odiFragment.setUpSixElements(sixElementModels);
                        } else if (arr.length == 4) {
                            fourElementModel.add(new RecordDetailsModel2(a[0], a[1], a[2], a[3]));
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject obj = jsonArray.getJSONObject(i);
                                fourElementModel.add(new RecordDetailsModel2(obj.getString(arr[0]), obj.getString(arr[1]), obj.getString(arr[2]), obj.getString(arr[3])));
                            }
                            odiFragment.setUpFourElements(fourElementModel);
                        } else if (arr.length == 3) {
                            threeElementModel.add(new RecordDetailsModel3(a[0], a[1], a[2]));
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject obj = jsonArray.getJSONObject(i);
                                threeElementModel.add(new RecordDetailsModel3(obj.getString(arr[0]), obj.getString(arr[1]), obj.getString(arr[2])));
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
                            sixElementModels.add(new RecordDetailsModel1(a[0], a[1], a[2], a[3], a[4], a[5]));
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject obj = jsonArray.getJSONObject(i);
                                sixElementModels.add(new RecordDetailsModel1(obj.getString(arr[0]), obj.getString(arr[1]), obj.getString(arr[2]), obj.getString(arr[3]), obj.getString(arr[4]), obj.getString(arr[5])));
                            }
                            t20Fragment.setUpSixElements(sixElementModels);
                        } else if (arr.length == 4) {
                            fourElementModel.add(new RecordDetailsModel2(a[0], a[1], a[2], a[3]));
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject obj = jsonArray.getJSONObject(i);
                                fourElementModel.add(new RecordDetailsModel2(obj.getString(arr[0]), obj.getString(arr[1]), obj.getString(arr[2]), obj.getString(arr[3])));
                            }
                            t20Fragment.setUpFourElements(fourElementModel);
                        } else if (arr.length == 3) {
                            threeElementModel.add(new RecordDetailsModel3(a[0], a[1], a[2]));
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject obj = jsonArray.getJSONObject(i);
                                threeElementModel.add(new RecordDetailsModel3(obj.getString(arr[0]), obj.getString(arr[1]), obj.getString(arr[2])));
                            }
                            t20Fragment.setUpThreeElements(threeElementModel);
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
