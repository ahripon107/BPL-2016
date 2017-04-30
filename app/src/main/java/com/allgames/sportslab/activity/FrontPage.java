package com.allgames.sportslab.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.allgames.sportslab.adapter.BasicListAdapter;
import com.allgames.sportslab.util.DividerItemDecoration;
import com.allgames.sportslab.util.ViewHolder;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.inject.Inject;
import com.allgames.sportslab.R;
import com.allgames.sportslab.adapter.MatchDetailsViewPagerAdapter;
import com.allgames.sportslab.util.Constants;
import com.allgames.sportslab.util.DefaultMessageHandler;
import com.allgames.sportslab.util.NetworkService;
import com.allgames.sportslab.util.RoboAppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

@ContentView(R.layout.activity_front_page)
public class FrontPage extends RoboAppCompatActivity {

    private MatchDetailsViewPagerAdapter matchDetailsViewPagerAdapter;
    @InjectView(R.id.menu_list)
    private RecyclerView menuList;
    @InjectView(R.id.adViewFontPage)
    private AdView adView;
    @InjectView(R.id.toolbar)
    private Toolbar toolbar;

    @Inject
    NetworkService networkService;

    InterstitialAd mInterstitialAd;

    @Inject
    ArrayList<String> allMenu;

    @Inject
    ArrayList<String> hideMenu;

    @Inject
    ArrayList<String> selectedMenu;

    Typeface typeface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setSupportActionBar(toolbar);

        typeface = Typeface.createFromAsset(this.getAssets(), Constants.SOLAIMAN_LIPI_FONT);

        allMenu.add("ক্রিকেট লাইভ");
        allMenu.add("ফুটবল লাইভ");
        allMenu.add("লাইভ স্কোর");
        allMenu.add("স্পোর্টস নিউজ");
        allMenu.add("ক্রিকেট হাইলাইটস");
        allMenu.add("ফুটবল হাইলাইটস");
        allMenu.add("ফিক্সচার");
        allMenu.add("পূর্বের খেলা");
        allMenu.add("গ্যালারী");
        allMenu.add("সিরিজ পরিসংখ্যান");
        allMenu.add("রাঙ্কিং");
        allMenu.add("ক্রিকেটের যত রেকর্ড");
        allMenu.add("পয়েন্ট টেবিল");
        allMenu.add("উদ্ধৃতি");
        allMenu.add("টিম প্রোফাইল");
        allMenu.add("লগ ইন/লগ আউট");
        allMenu.add("অ্যাপ আপডেট করুন");


        hideMenu.add("ফিক্সচার");
        hideMenu.add("সিরিজ পরিসংখ্যান");
        hideMenu.add("ক্রিকেটের যত রেকর্ড");
        hideMenu.add("পয়েন্ট টেবিল");
        hideMenu.add("টিম প্রোফাইল");
        hideMenu.add("লগ ইন/লগ আউট");
        hideMenu.add("অ্যাপ আপডেট করুন");

        selectedMenu.addAll(hideMenu);

        menuList.setAdapter(new BasicListAdapter<String, HomeViewHolder>(selectedMenu) {
            @Override
            public HomeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_home, parent, false);
                return new HomeViewHolder(view);
            }

            @Override
            public void onBindViewHolder(final HomeViewHolder holder, final int position) {
                holder.textView.setText(selectedMenu.get(position));

                holder.textView.setTypeface(typeface);
                holder.textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (selectedMenu.get(position).equals("ক্রিকেট লাইভ")) {
                            checkIsAllowed("livestream", "com.allgames.sportslab.activity.Highlights", "livestream");
                        } else if (selectedMenu.get(position).equals("ফুটবল লাইভ")) {
                            checkIsAllowed("livestream", "com.allgames.sportslab.activity.Highlights", "livestreamfootball");
                        } else if (selectedMenu.get(position).equals("লাইভ স্কোর")) {
                            Intent intent = new Intent(FrontPage.this, LiveScoreActivity.class);
                            startActivity(intent);
                        } else if (selectedMenu.get(position).equals("স্পোর্টস নিউজ")) {
                            checkIsAllowed("news", "com.allgames.sportslab.activity.CricketNewsListActivity", null);
                        } else if (selectedMenu.get(position).equals("ক্রিকেট হাইলাইটস")) {
                            checkIsAllowed("highlights", "com.allgames.sportslab.activity.Highlights", "highlights");
                        } else if (selectedMenu.get(position).equals("ফুটবল হাইলাইটস")) {
                            checkIsAllowed("highlights", "com.allgames.sportslab.activity.Highlights", "highlightsfootball");
                        } else if (selectedMenu.get(position).equals("ফিক্সচার")) {
                            Intent intent = new Intent(FrontPage.this, FixtureActivity.class);
                            startActivity(intent);
                        } else if (selectedMenu.get(position).equals("পূর্বের খেলা")) {
                            Intent intent = new Intent(FrontPage.this, PastMatchesActivity.class);
                            startActivity(intent);
                        } else if (selectedMenu.get(position).equals("গ্যালারী")) {
                            checkIsAllowed("gallery", "com.allgames.sportslab.activity.GalleryActivity", null);
                        } else if (selectedMenu.get(position).equals("সিরিজ পরিসংখ্যান")) {
                            Intent intent = new Intent(FrontPage.this, SeriesStatsActivity.class);
                            startActivity(intent);
                        } else if (selectedMenu.get(position).equals("রাঙ্কিং")) {
                            Intent intent = new Intent(FrontPage.this, RankingActivity.class);
                            startActivity(intent);
                        } else if (selectedMenu.get(position).equals("ক্রিকেটের যত রেকর্ড")) {
                            Intent intent = new Intent(FrontPage.this, RecordsActivity.class);
                            startActivity(intent);
                        } else if (selectedMenu.get(position).equals("পয়েন্ট টেবিল")) {
                            Intent intent = new Intent(FrontPage.this, PointsTableActivity.class);
                            startActivity(intent);
                        } else if (selectedMenu.get(position).equals("উদ্ধৃতি")) {
                            checkIsAllowed("quotes", "com.allgames.sportslab.activity.QuotesListActivity", null);
                        } else if (selectedMenu.get(position).equals("টিম প্রোফাইল")) {
                            Intent intent = new Intent(FrontPage.this, TeamProfile.class);
                            startActivity(intent);
                        } else if (selectedMenu.get(position).equals("লগ ইন/লগ আউট")) {
                            Intent intent = new Intent(FrontPage.this, LoginActivity.class);
                            startActivity(intent);
                        } else if (selectedMenu.get(position).equals("অ্যাপ আপডেট করুন")) {
                            String appPackageName = getPackageName();
                            try {
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                            } catch (android.content.ActivityNotFoundException anfe) {
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                            }
                        }
                    }
                });
            }
        });

        menuList.setLayoutManager(new LinearLayoutManager(this));
        menuList.addItemDecoration(new DividerItemDecoration(this));

        networkService.fetchIsAllowed(new DefaultMessageHandler(this, true) {
            @Override
            public void onSuccess(Message msg) {
                super.onSuccess(msg);
                String string = (String) msg.obj;
                try {
                    JSONObject response = new JSONObject(string);
                    if (response.getString("msg").equals("Successful")) {
                        String source = response.getJSONArray("content").getJSONObject(0).getString("showmenu");
                        if (source.equals("true")) {
                            selectedMenu.clear();
                            selectedMenu.addAll(allMenu);
                            menuList.getAdapter().notifyDataSetChanged();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        AdRequest adRequest1 = new AdRequest.Builder().addTestDevice(Constants.ONE_PLUS_TEST_DEVICE)
                .addTestDevice(Constants.XIAOMI_TEST_DEVICE).build();
        adView.loadAd(adRequest1);

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-9201945236996508/7683380073");

        AdRequest adRequestInterstitial = new AdRequest.Builder()
                .addTestDevice(Constants.ONE_PLUS_TEST_DEVICE).addTestDevice(Constants.XIAOMI_TEST_DEVICE)
                .build();
        mInterstitialAd.loadAd(adRequestInterstitial);

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.share_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_share:
                Intent i=new Intent(android.content.Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(android.content.Intent.EXTRA_SUBJECT,"Share App");
                i.putExtra(android.content.Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id="+getPackageName());
                startActivity(Intent.createChooser(i,"Share via"));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private static class HomeViewHolder extends RecyclerView.ViewHolder {

        protected TextView textView;
        public HomeViewHolder(View itemView) {
            super(itemView);
            textView = ViewHolder.get(itemView, R.id.menu_item_text);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    private void checkIsAllowed(final String item, final String className, final String cause) {

        networkService.fetchIsAllowed(new DefaultMessageHandler(this, true) {
            @Override
            public void onSuccess(Message msg) {
                super.onSuccess(msg);
                String string = (String) msg.obj;
                try {
                    JSONObject response = new JSONObject(string);
                    if (response.getString("msg").equals("Successful")) {
                        String source = response.getJSONArray("content").getJSONObject(0).getString(item);
                        if (source.equals("true")) {

                            try {
                                Intent intent = new Intent(FrontPage.this, Class.forName(className));
                                if (cause != null) {
                                    intent.putExtra("cause", cause);
                                }
                                startActivity(intent);
                            } catch (ClassNotFoundException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(FrontPage.this, "Failed", Toast.LENGTH_LONG).show();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }
}
