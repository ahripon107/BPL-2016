package com.sfuronlabs.bpl2016.activity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.gson.Gson;
import com.google.inject.Inject;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.sfuronlabs.bpl2016.R;
import com.sfuronlabs.bpl2016.adapter.BasicListAdapter;
import com.sfuronlabs.bpl2016.model.TrollPost;
import com.sfuronlabs.bpl2016.util.Constants;
import com.sfuronlabs.bpl2016.util.FetchFromWeb;
import com.sfuronlabs.bpl2016.util.RoboAppCompatActivity;
import com.sfuronlabs.bpl2016.util.ViewHolder;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import dmax.dialog.SpotsDialog;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

/**
 * @author ripon
 */
@ContentView(R.layout.fixture)
public class TrollPostListActivity extends RoboAppCompatActivity {

    @InjectView(R.id.adViewFixture)
    AdView adView;

    @InjectView(R.id.recycler_view)
    RecyclerView recyclerView;

    @Inject
    ArrayList<TrollPost> trollPosts;

    @Inject
    Gson gson;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        recyclerView.setAdapter(new BasicListAdapter<TrollPost, TrollPostViewHolder>(trollPosts) {
            @Override
            public TrollPostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_troll_posts, parent, false);
                return new TrollPostViewHolder(view);
            }

            @Override
            public void onBindViewHolder(TrollPostViewHolder holder, int position) {
                holder.courtesy.setText(trollPosts.get(position).getCourtesy());
                holder.title.setText(trollPosts.get(position).getImagetext());
                Picasso.with(TrollPostListActivity.this)
                        .load((trollPosts.get(position).getImageurl()))
                        .placeholder(R.drawable.default_image)
                        .into(holder.imageView);
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        String url = "http://apisea.xyz/Cricket/apis/v1/FetchFunPosts.php?key=bl905577";
        Log.d(Constants.TAG, url);

        final AlertDialog progressDialog = new SpotsDialog(TrollPostListActivity.this, R.style.Custom);
        progressDialog.show();
        progressDialog.setCancelable(true);

        FetchFromWeb.get(url, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                progressDialog.dismiss();
                try {
                    JSONArray jsonArray = response.getJSONArray("content");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject obj = jsonArray.getJSONObject(i);
                        TrollPost trollPost = gson.fromJson(String.valueOf(obj), TrollPost.class);
                        trollPosts.add(trollPost);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                recyclerView.getAdapter().notifyDataSetChanged();
                Log.d(Constants.TAG, response.toString());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                progressDialog.dismiss();
                Toast.makeText(TrollPostListActivity.this, "Failed", Toast.LENGTH_LONG).show();
            }
        });

        AdRequest adRequest = new AdRequest.Builder().addTestDevice(Constants.ONE_PLUS_TEST_DEVICE)
                .addTestDevice(Constants.XIAOMI_TEST_DEVICE).build();
        adView.loadAd(adRequest);
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

    private static class TrollPostViewHolder extends RecyclerView.ViewHolder {
        protected ImageView imageView;
        protected TextView title;
        protected TextView courtesy;

        public TrollPostViewHolder(View itemView) {
            super(itemView);
            imageView = ViewHolder.get(itemView, R.id.img_troll_post);
            title = ViewHolder.get(itemView, R.id.tv_troll_post_title);
            courtesy = ViewHolder.get(itemView, R.id.tv_troll_post_courtesy);
        }
    }
}
