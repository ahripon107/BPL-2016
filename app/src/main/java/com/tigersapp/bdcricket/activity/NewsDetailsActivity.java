package com.tigersapp.bdcricket.activity;

import android.app.AlertDialog;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.tigersapp.bdcricket.R;
import com.tigersapp.bdcricket.model.CricketNews;
import com.tigersapp.bdcricket.util.Constants;
import com.tigersapp.bdcricket.util.FetchFromWeb;
import com.tigersapp.bdcricket.util.RoboAppCompatActivity;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import dmax.dialog.SpotsDialog;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

/**
 * @author ripon
 */
@ContentView(R.layout.newsdetails)
public class NewsDetailsActivity extends RoboAppCompatActivity {

    public static final String EXTRA_NEWS_OBJECT = "newsobject";

    @InjectView(R.id.btn_details_news)
    Button detailsNews;

    @InjectView(R.id.image)
    ImageView imageView;

    @InjectView(R.id.text_view_headline)
    TextView headline;

    @InjectView(R.id.text_view_date)
    TextView date;

    @InjectView(R.id.text_view_author)
    TextView author;

    @InjectView(R.id.text_view_details)
    TextView details;

    @InjectView(R.id.adViewNewsDetails)
    AdView adView;

    CricketNews cricketNews;

    Typeface typeface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle("বিস্তারিত");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        cricketNews = (CricketNews) getIntent().getSerializableExtra(EXTRA_NEWS_OBJECT);

        typeface = Typeface.createFromAsset(getAssets(), Constants.SOLAIMAN_LIPI_FONT);

        headline.setTypeface(typeface);
        author.setTypeface(typeface);
        details.setTypeface(typeface);
        headline.setText(cricketNews.getTitle());

        date.setText(cricketNews.getDate());
        author.setText(cricketNews.getSourceBangla());
        details.setText(cricketNews.getTitle());

        Picasso.with(NewsDetailsActivity.this)
                .load(cricketNews.getImageUrl())
                .placeholder(R.drawable.default_image)
                .into(imageView);
        detailsNews.setVisibility(View.GONE);

        final AlertDialog progressDialog = new SpotsDialog(NewsDetailsActivity.this, R.style.Custom);
        progressDialog.show();
        progressDialog.setCancelable(true);
        FetchFromWeb.get(cricketNews.getDetailNewsUrl(), null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                progressDialog.dismiss();
                try {

                    JSONObject jsonObject = response.getJSONObject(0);
                    if (jsonObject.has("ContentDetails")) {
                        details.setText(Html.fromHtml(jsonObject.getString("ContentDetails")));
                    } else {
                        details.setText(Html.fromHtml(jsonObject.getString("NewsDetails")));
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Log.d(Constants.TAG, response.toString());
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                progressDialog.dismiss();
                try {
                    if (response.has("getnewsby_id")) {
                        JSONObject jsonObject = response.getJSONObject("getnewsby_id");
                        details.setText(Html.fromHtml(jsonObject.getString("summery")));
                    } else {
                        response = response.getJSONArray("contents").getJSONObject(0);
                        details.setText(Html.fromHtml(response.getString("news_details")));
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Log.d(Constants.TAG, response.toString());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                progressDialog.dismiss();
                Toast.makeText(NewsDetailsActivity.this, "Failed", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                progressDialog.dismiss();
                Toast.makeText(NewsDetailsActivity.this, "Failed", Toast.LENGTH_LONG).show();
            }
        });

        AdRequest adRequest = new AdRequest.Builder().addTestDevice(Constants.ONE_PLUS_TEST_DEVICE)
                .addTestDevice(Constants.XIAOMI_TEST_DEVICE).build();
        adView.loadAd(adRequest);
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
