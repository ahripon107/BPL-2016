package com.allgames.sportslab.videoplayers;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.allgames.sportslab.R;
import com.allgames.sportslab.util.Constants;

/**
 * @author Ripon
 */
public class FrameStream extends AppCompatActivity {

    private WebView webView;
    private String html;
    private AdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.framestream);

        adView = (AdView) findViewById(R.id.adViewFrameStream);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice(Constants.ONE_PLUS_TEST_DEVICE)
                .addTestDevice(Constants.XIAOMI_TEST_DEVICE).build();
        adView.loadAd(adRequest);
        Intent intent = getIntent();
        String str = intent.getStringExtra("url");

        html = "<html>\n" +
                "    <head>\n" +
                "     <meta name=\"viewport\" content=\"width=device-width, target-densitydpi=device-dpi, initial-scale=0, maximum-scale=1, user-scalable=yes\" />\n" +
                "        <script type=\"text/javascript\">\n" +
                "            function changeIframeSize(height, width){\n" +
                "                var iframe = document.getElementById(\"iframe1\");\n" +
                "                iframe.height = height;\n" +
                "                iframe.width = width;\n" +
                "            }\n" +
                "        </script>\n" +
                "    </head>\n" +
                "    <body style=\"margin: 0; padding: 0\"> \n" +
                "        \n" +
                "       " + str + "  \n" +
                "    </body>\n" +
                "</html>";
        webView = (WebView) findViewById(R.id.wvframe);
        webView.getSettings().setJavaScriptEnabled(true);

        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);

        webView.setWebChromeClient(new WebChromeClient());
        webView.loadDataWithBaseURL("", html, "text/html", "UTF-8", "");

        Toast.makeText(getApplicationContext(), "Zoom in or out to adjust with device screen", Toast.LENGTH_LONG).show();


    }

    @Override
    public void onPause() {
        super.onPause();
        webView.onPause();
        webView.pauseTimers();
    }

    @Override
    public void onResume() {
        super.onResume();
        webView.resumeTimers();
        webView.onResume();
    }


    @Override
    protected void onDestroy() {
        webView.destroy();
        webView = null;
        super.onDestroy();
    }
}
