package com.allgames.sportslab.activity;

import android.os.Bundle;
import android.os.Message;
import android.text.Html;
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

    @InjectView(R.id.image)
    private ImageView imageView;

    @InjectView(R.id.tv_about)
    private TextView about;

    @InjectView(R.id.tv_title)
    private TextView title;

    @InjectView(R.id.tv_details)
    private TextView details;
    private CricketNews cricketNews;

    @Inject
    private NetworkService networkService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        cricketNews = (CricketNews) getIntent().getSerializableExtra(EXTRA_NEWS_OBJECT);

        about.setText(cricketNews.getAbout());
        title.setText(cricketNews.getTitle());

        if (!cricketNews.getImageUrl().equals("")) {
            Picasso.with(this)
                    .load(cricketNews.getImageUrl())
                    .placeholder(R.drawable.default_image)
                    .into(imageView);
        }

        networkService.fetchNewsDetails(cricketNews.getId(), new DefaultMessageHandler(this, true) {
            @Override
            public void onSuccess(Message msg) {
                String string = (String) msg.obj;

                try {
                    JSONObject response = new JSONObject(string);
                    details.setText(Html.fromHtml(response.getString("text")));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

//        AdRequest adRequest = new AdRequest.Builder().addTestDevice(Constants.ONE_PLUS_TEST_DEVICE)
//                .addTestDevice(Constants.XIAOMI_TEST_DEVICE).build();
//        adView.loadAd(adRequest);
    }
}
