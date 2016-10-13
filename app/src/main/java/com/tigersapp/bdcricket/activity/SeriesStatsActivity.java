package com.tigersapp.bdcricket.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.tigersapp.bdcricket.R;
import com.tigersapp.bdcricket.adapter.BasicListAdapter;
import com.tigersapp.bdcricket.model.RecordModel;
import com.tigersapp.bdcricket.util.Constants;
import com.tigersapp.bdcricket.util.FetchFromWeb;
import com.tigersapp.bdcricket.util.ViewHolder;

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

public class SeriesStatsActivity extends AppCompatActivity {

    Spinner spinner;
    RecyclerView recyclerView;
    List<String> currentSeries, seriesIds;
    ArrayList<RecordModel> seriesStatsModels;
    Gson gson;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_series_stats);
        spinner = (Spinner) findViewById(R.id.spinner);
        recyclerView = (RecyclerView) findViewById(R.id.series_stats);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        currentSeries = new ArrayList<>();
        seriesIds = new ArrayList<>();
        seriesStatsModels = new ArrayList<>();
        gson = new Gson();

        fetchData(Constants.SERIES_STATS_URL);

    }

    public void fetchData(String url) {
        final AlertDialog progressDialog = new SpotsDialog(SeriesStatsActivity.this, R.style.Custom);
        progressDialog.show();
        progressDialog.setCancelable(true);
        FetchFromWeb.get(url, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                progressDialog.dismiss();
                try {
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
                        seriesStatsModels.add(gson.fromJson(String.valueOf(jsonArray.getJSONObject(i)),RecordModel.class));
                    }

                    recyclerView.setAdapter(new BasicListAdapter<RecordModel,SeriesStatsViewHolder>(seriesStatsModels) {
                        @Override
                        public SeriesStatsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_records,parent,false);
                            return new SeriesStatsViewHolder(view);
                        }

                        @Override
                        public void onBindViewHolder(SeriesStatsViewHolder holder, final int position) {
                            holder.textView.setText(seriesStatsModels.get(position).getHeader());
                            holder.recordTypeLayout.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(SeriesStatsActivity.this, SeriesStatsDetailsActivity.class);
                                    intent.putExtra("title",seriesStatsModels.get(position).getHeader());
                                    intent.putExtra("seriesId",seriesIds.get(spinner.getSelectedItemPosition()));
                                    intent.putExtra("url",seriesStatsModels.get(position).getUrl());
                                    startActivity(intent);
                                }
                            });
                        }
                    });
                    recyclerView.setLayoutManager(new LinearLayoutManager(SeriesStatsActivity.this));

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
        });
    }

    private static class SeriesStatsViewHolder extends RecyclerView.ViewHolder {
        protected TextView textView;
        protected LinearLayout recordTypeLayout;

        public SeriesStatsViewHolder(View itemView) {
            super(itemView);
            textView = ViewHolder.get(itemView,R.id.tv_record_type);
            recordTypeLayout = ViewHolder.get(itemView,R.id.record_type_layout);
        }
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
