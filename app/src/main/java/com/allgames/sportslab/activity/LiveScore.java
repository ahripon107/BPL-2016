package com.allgames.sportslab.activity;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.allgames.sportslab.R;
import com.allgames.sportslab.util.Constants;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

/**
 * @author ripon
 */
public class LiveScore extends CommonActivity {

    public static final String EXTRA_URL = "url";

    private WebView mWebview;
    private AdView adView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initialize();

        String url = getIntent().getStringExtra(EXTRA_URL);
        mWebview.getSettings().setJavaScriptEnabled(true);
        mWebview.setWebViewClient(new WebViewClient() {
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Toast.makeText(LiveScore.this, description, Toast.LENGTH_SHORT).show();
            }
        });

        mWebview.loadUrl(url);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice(Constants.ONE_PLUS_TEST_DEVICE)
                .addTestDevice(Constants.XIAOMI_TEST_DEVICE).build();
        adView.loadAd(adRequest);
    }

    private void initialize() {
        setContentView(R.layout.livescore);
        mWebview = (WebView) findViewById(R.id.webView);
        adView = (AdView) findViewById(R.id.adViewLivescore);
        setTitle("Details");
    }
}
