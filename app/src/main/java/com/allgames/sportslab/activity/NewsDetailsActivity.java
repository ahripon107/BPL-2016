package com.allgames.sportslab.activity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Message;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.allgames.sportslab.R;
import com.allgames.sportslab.model.CricketNews;
import com.allgames.sportslab.util.Constants;
import com.allgames.sportslab.util.DefaultMessageHandler;
import com.allgames.sportslab.util.NetworkService;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.inject.Inject;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

/**
 * @author ripon
 */
@ContentView(R.layout.activity_news_details)
public class NewsDetailsActivity extends CommonActivity {

    public static final String EXTRA_NEWS_OBJECT = "newsobject";

    @InjectView(R.id.adViewNewsDetails)
    private AdView adView;

    private ImageView imageView;
    private TextView headline;
    private TextView date;
    private TextView author;
    private TextView details;
    private CricketNews cricketNews;

    private Typeface typeface;

    @Inject
    private NetworkService networkService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        imageView = findViewById(R.id.image);
        headline = findViewById(R.id.text_view_headline);
        date = findViewById(R.id.text_view_date);
        author = findViewById(R.id.text_view_author);
        details = findViewById(R.id.text_view_details);

        cricketNews = (CricketNews) getIntent().getSerializableExtra(EXTRA_NEWS_OBJECT);

        typeface = Typeface.createFromAsset(getAssets(), Constants.SOLAIMAN_LIPI_FONT);

        headline.setTypeface(typeface);
        author.setTypeface(typeface);
        details.setTypeface(typeface);
        headline.setText(cricketNews.getTitle());

        date.setText(cricketNews.getDate());
        //author.setText(cricketNews.getSourceBangla());
        details.setText(cricketNews.getTitle());

        if (!cricketNews.getImageUrl().equals("")) {
            Picasso.with(this)
                    .load(cricketNews.getImageUrl())
                    .placeholder(R.drawable.default_image)
                    .into(imageView);
        }


        if (cricketNews.getSource().equals("prothomalo")) {
            details.setText(Html.fromHtml(cricketNews.getSourceBangla()));
            imageView.setVisibility(View.GONE);
        } else {
            networkService.fetch(cricketNews.getDetailNewsUrl(), new DefaultMessageHandler(this, true) {
                @Override
                public void onSuccess(Message msg) {
                    try {
                        JSONArray response = new JSONArray((String) msg.obj);
                        JSONObject jsonObject = response.getJSONObject(0);
                        if (jsonObject.has("ContentDetails")) {
                            details.setText(Html.fromHtml(jsonObject.getString("ContentDetails")));
                        } else {
                            details.setText(Html.fromHtml(jsonObject.getString("NewsDetails")));
                        }

                    } catch (JSONException e) {
                        try {
                            JSONObject response = new JSONObject((String) msg.obj);
                            if (response.has("getnewsby_id")) {
                                JSONObject jsonObject = response.getJSONObject("getnewsby_id");
                                details.setText(Html.fromHtml(jsonObject.getString("summery")));
                            } else if (response.has("contents")) {
                                response = response.getJSONArray("contents").getJSONObject(0);
                                details.setText(Html.fromHtml(response.getString("news_details")));
                            } else {
                                details.setText(Html.fromHtml(response.getString("ContentDetails")));
                            }


                        } catch (JSONException ea) {
                            ea.printStackTrace();
                        }
                    }

                }
            });
        }

        AdRequest adRequest = new AdRequest.Builder().addTestDevice(Constants.ONE_PLUS_TEST_DEVICE)
                .addTestDevice(Constants.XIAOMI_TEST_DEVICE).build();
        adView.loadAd(adRequest);
    }
}
