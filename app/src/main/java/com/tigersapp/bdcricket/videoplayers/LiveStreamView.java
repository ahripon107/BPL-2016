package com.tigersapp.bdcricket.videoplayers;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.tigersapp.bdcricket.R;
import com.tigersapp.bdcricket.util.Constants;

/**
 * @author Ripon
 */
public class LiveStreamView extends AppCompatActivity {

    private VideoView videoView1;
    private MediaController mediaController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.livestreamview);

        videoView1 = (VideoView) findViewById(R.id.videoView);
        // Set the media controller buttons
        if (mediaController == null) {
            mediaController = new MediaController(LiveStreamView.this);

            // Set the videoView that acts as the anchor for the MediaController.
            mediaController.setAnchorView(videoView1);


            // Set MediaController for VideoView
            videoView1.setMediaController(mediaController);
        }


        videoView1.setVideoURI(Uri.parse(getIntent().getStringExtra("url")));
        videoView1.setMediaController(new MediaController(this));
        videoView1.requestFocus();
        videoView1.start();

        AdView adView = (AdView) findViewById(R.id.adViewStreamM3U8);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice(Constants.ONE_PLUS_TEST_DEVICE)
                .addTestDevice(Constants.XIAOMI_TEST_DEVICE).build();
        adView.loadAd(adRequest);

        Toast.makeText(getApplicationContext(), "Please wait while video is loading", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onPause() {
        videoView1.pause();
        super.onPause();
    }

    @Override
    protected void onStop() {
        videoView1.stopPlayback();
        super.onStop();
    }
}
