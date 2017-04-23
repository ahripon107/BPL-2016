package com.banglacricket.bdcricketteam.activity;

import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.gson.Gson;
import com.google.inject.Inject;
import com.banglacricket.bdcricketteam.R;
import com.banglacricket.bdcricketteam.adapter.BasicListAdapter;
import com.banglacricket.bdcricketteam.model.PointsTable;
import com.banglacricket.bdcricketteam.model.PointsTableElement;
import com.banglacricket.bdcricketteam.util.Constants;
import com.banglacricket.bdcricketteam.util.DefaultMessageHandler;
import com.banglacricket.bdcricketteam.util.NetworkService;
import com.banglacricket.bdcricketteam.util.ViewHolder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

/**
 * @author Ripon
 */
@ContentView(R.layout.activity_points_table)
public class PointsTableActivity extends CommonActivity {

    @InjectView(R.id.adViewpointsTable)
    private AdView adView;
    @InjectView(R.id.recycler_view)
    private RecyclerView recyclerView;
    @InjectView(R.id.spn_tournaments)
    private Spinner spinner;

    @Inject
    private ArrayList<PointsTable> pointTables;
    @Inject
    private ArrayList<PointsTableElement> pointTableElements;
    @Inject
    private Gson gson;
    @Inject
    private NetworkService networkService;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AdRequest adRequest = new AdRequest.Builder().addTestDevice(Constants.ONE_PLUS_TEST_DEVICE)
                .addTestDevice(Constants.XIAOMI_TEST_DEVICE).build();
        adView.loadAd(adRequest);

        final List<String> categories = new ArrayList<String>();
        final ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                networkService.fetchSpecificPointTable(pointTables.get(position).getUrl(), new DefaultMessageHandler(PointsTableActivity.this, true) {
                    @Override
                    public void onSuccess(Message msg) {
                        String string = (String) msg.obj;
                        try {
                            JSONObject response = new JSONObject(string);
                            processData(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        networkService.fetchAllPointTables(Constants.POINT_TABLE_URL, new DefaultMessageHandler(this, true) {
            @Override
            public void onSuccess(Message msg) {
                String string = (String) msg.obj;
                try {
                    JSONObject response = new JSONObject(string);

                    JSONArray jsonArray = response.getJSONArray("pointsTable");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        pointTables.add(gson.fromJson(String.valueOf(jsonArray.get(i)), PointsTable.class));
                        categories.add(pointTables.get(i).getSeriesName());
                    }
                    dataAdapter.notifyDataSetChanged();

                    if (pointTables.size() > 0) {

                        networkService.fetchSpecificPointTable(pointTables.get(0).getUrl(), new DefaultMessageHandler(PointsTableActivity.this, true) {
                            @Override
                            public void onSuccess(Message msg) {
                                String string1 = (String) msg.obj;
                                try {
                                    JSONObject response1 = new JSONObject(string1);
                                    processData(response1);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void processData(JSONObject jsonObject) {

        pointTableElements.clear();
        pointTableElements.add(new PointsTableElement("Team", "Played", "Wins", "Loss", "NR", "Pts", "NRR"));
        try {
            jsonObject = jsonObject.getJSONObject("group");
            Iterator<?> keys = jsonObject.keys();

            while (keys.hasNext()) {
                String key = (String) keys.next();
                JSONArray jsonArray = jsonObject.getJSONArray(key);
                for (int i = 0; i < jsonArray.length(); i++) {
                    pointTableElements.add(gson.fromJson(String.valueOf(jsonArray.get(i)), PointsTableElement.class));
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        recyclerView.setAdapter(new BasicListAdapter<PointsTableElement, PointsTableViewHolder>(pointTableElements) {
            @Override
            public PointsTableViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_points_table, parent, false);
                return new PointsTableViewHolder(view);
            }

            @Override
            public void onBindViewHolder(PointsTableViewHolder holder, int position) {
                holder.teamName.setText(pointTableElements.get(position).getTeamName());
                holder.played.setText(pointTableElements.get(position).getPlayed());
                holder.wins.setText(pointTableElements.get(position).getWon());
                holder.losses.setText(pointTableElements.get(position).getLost());
                holder.NR.setText(pointTableElements.get(position).getNoresults());
                holder.points.setText(pointTableElements.get(position).getPointsscored());
                holder.nrr.setText(pointTableElements.get(position).getRunrate());
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private static class PointsTableViewHolder extends RecyclerView.ViewHolder {
        protected TextView teamName;
        protected TextView played;
        protected TextView wins;
        protected TextView losses;
        protected TextView NR;
        protected TextView points;
        protected TextView nrr;

        public PointsTableViewHolder(View itemView) {
            super(itemView);
            teamName = ViewHolder.get(itemView, R.id.tv_team_name);
            played = ViewHolder.get(itemView, R.id.tv_played);
            wins = ViewHolder.get(itemView, R.id.tv_wins);
            losses = ViewHolder.get(itemView, R.id.tv_loss);
            NR = ViewHolder.get(itemView, R.id.tv_nr);
            points = ViewHolder.get(itemView, R.id.tv_points);
            nrr = ViewHolder.get(itemView, R.id.tv_nrr);
        }
    }
}
