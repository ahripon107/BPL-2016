package com.allgames.sportslab.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.allgames.sportslab.R;
import com.allgames.sportslab.adapter.BasicListAdapter;
import com.allgames.sportslab.model.RecordModel;
import com.allgames.sportslab.util.DefaultMessageHandler;
import com.allgames.sportslab.util.NetworkService;
import com.allgames.sportslab.util.ViewHolder;
import com.google.gson.Gson;
import com.google.inject.Inject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ripon
 */
public class SeriesStatsActivity extends CommonActivity {

    Spinner spinner;
    RecyclerView recyclerView;
    List<String> currentSeries, seriesIds;
    ArrayList<RecordModel> seriesStatsModels;
    Gson gson;

    @Inject
    NetworkService networkService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_series_stats);

        spinner = (Spinner) findViewById(R.id.spinner);
        recyclerView = (RecyclerView) findViewById(R.id.series_stats);

        currentSeries = new ArrayList<>();
        seriesIds = new ArrayList<>();
        seriesStatsModels = new ArrayList<>();
        gson = new Gson();


        fetchData();
    }

    public void fetchData() {

        networkService.fetchSeriesStats(new DefaultMessageHandler(this, true) {
            @Override
            public void onSuccess(Message msg) {
                String string = (String) msg.obj;
                try {
                    JSONObject response = new JSONObject(string);
                    response = response.getJSONObject("series-stats");
                    JSONArray jsonArray = response.getJSONArray("seriesDetails");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        currentSeries.add(jsonObject.getString("seriesName"));
                        seriesIds.add(jsonObject.getString("seriesId"));
                    }
                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(SeriesStatsActivity.this, android.R.layout.simple_spinner_item, currentSeries);
                    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner.setAdapter(dataAdapter);

                    jsonArray = response.getJSONArray("statsType");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        seriesStatsModels.add(gson.fromJson(String.valueOf(jsonArray.getJSONObject(i)), RecordModel.class));
                    }

                    recyclerView.setAdapter(new BasicListAdapter<RecordModel, SeriesStatsViewHolder>(seriesStatsModels) {
                        @Override
                        public SeriesStatsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_records, parent, false);
                            return new SeriesStatsViewHolder(view);
                        }

                        @Override
                        public void onBindViewHolder(SeriesStatsViewHolder holder, final int position) {
                            holder.textView.setText(seriesStatsModels.get(position).getHeader());
                            holder.recordTypeLayout.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(SeriesStatsActivity.this, SeriesStatsDetailsActivity.class);
                                    intent.putExtra("title", seriesStatsModels.get(position).getHeader());
                                    intent.putExtra("seriesId", seriesIds.get(spinner.getSelectedItemPosition()));
                                    intent.putExtra("url", seriesStatsModels.get(position).getUrl());
                                    startActivity(intent);
                                }
                            });
                        }
                    });
                    recyclerView.setLayoutManager(new LinearLayoutManager(SeriesStatsActivity.this));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private static class SeriesStatsViewHolder extends RecyclerView.ViewHolder {
        protected TextView textView;
        protected LinearLayout recordTypeLayout;

        public SeriesStatsViewHolder(View itemView) {
            super(itemView);
            textView = ViewHolder.get(itemView, R.id.tv_record_type);
            recordTypeLayout = ViewHolder.get(itemView, R.id.record_type_layout);
        }
    }
}
