package com.tigersapp.bdcricket.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdView;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;
import com.tigersapp.bdcricket.R;
import com.tigersapp.bdcricket.activity.ActivityMatchDetails;
import com.tigersapp.bdcricket.activity.FrontPage;
import com.tigersapp.bdcricket.activity.LiveScore;
import com.tigersapp.bdcricket.adapter.BasicListAdapter;
import com.tigersapp.bdcricket.adapter.SlideShowViewPagerAdapter;
import com.tigersapp.bdcricket.model.Match;
import com.tigersapp.bdcricket.util.CircleImageView;
import com.tigersapp.bdcricket.util.Constants;
import com.tigersapp.bdcricket.util.FetchFromWeb;
import com.tigersapp.bdcricket.util.RecyclerItemClickListener;
import com.tigersapp.bdcricket.util.ViewHolder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import dmax.dialog.SpotsDialog;

/**
 * @author Ripon
 */

public class LiveScoreFragment extends Fragment {

    TextView[] dots1;

    TextView welcomeText;

    ArrayList<String> imageUrls, texts;
    ViewPager viewPager;

    SlideShowViewPagerAdapter viewPagerAdapter;

    LinearLayout placeImageDotsLayout, cardContainer;

    RecyclerView recyclerView;

    Typeface typeface;

    ArrayList<Match> datas;
    ImageView imageView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_front_page, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        datas = new ArrayList<>();
        typeface = Typeface.createFromAsset(getActivity().getAssets(), Constants.SOLAIMAN_LIPI_FONT);
        welcomeText = (TextView) view.findViewById(R.id.tv_welcome_text);
        viewPager = (ViewPager) view.findViewById(R.id.placeViewPagerImageSlideShow);
        placeImageDotsLayout = (LinearLayout) view.findViewById(R.id.placeImageDots);
        cardContainer = (LinearLayout) view.findViewById(R.id.placecardcontainer);
        recyclerView = (RecyclerView) view.findViewById(R.id.live_matches);
        imageView = (ImageView) view.findViewById(R.id.tour_image);

        imageUrls = new ArrayList<>();
        texts = new ArrayList<>();

        viewPagerAdapter = new SlideShowViewPagerAdapter(getContext(), imageUrls, texts);
        viewPager.setAdapter(viewPagerAdapter);


        String welcomeTextUrl = "http://apisea.xyz/BPL2016/apis/v4/welcometext.php?key=bl905577";
        Log.d(Constants.TAG, welcomeTextUrl);

        FetchFromWeb.get(welcomeTextUrl, null, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, final JSONObject response) {
                try {
                    Constants.SHOW_PLAYER_IMAGE = response.getJSONArray("content").getJSONObject(0).getString("playerimage");
                    welcomeText.setText(response.getJSONArray("content").getJSONObject(0).getString("description"));
                    if (response.getJSONArray("content").getJSONObject(0).getString("clickable").equals("true")) {
                        welcomeText.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                try {
                                    Uri uriUrl = Uri.parse(String.valueOf(response.getJSONArray("content").getJSONObject(0).getString("link")));
                                    Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
                                    startActivity(launchBrowser);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }

                    String url = "https://skysportsapi.herokuapp.com/sky/getnews/cricket/v1.0/";
                    Log.d(Constants.TAG, url);

                    if (isNetworkAvailable() && response.getJSONArray("content").getJSONObject(0).getString("image").equals("true")) {
                        cardContainer.setVisibility(View.VISIBLE);
                        FetchFromWeb.get(url, null, new JsonHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {

                                try {

                                    for (int i = 0; i < response.length(); i++) {
                                        JSONObject jsonObject = response.getJSONObject(i);
                                        imageUrls.add(jsonObject.getString("imgsrc"));
                                        texts.add(jsonObject.getString("title"));

                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                viewPagerAdapter.notifyDataSetChanged();
                                addBottomDots(0);
                                Log.d(Constants.TAG, response.toString());
                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {

                            }
                        });
                    } else {
                        cardContainer.setVisibility(View.GONE);
                    }
                    if (isNetworkAvailable() && response.getJSONArray("content").getJSONObject(0).getString("appimage").equals("true")) {
                        Picasso.with(getContext())
                                .load(response.getJSONArray("content").getJSONObject(0).getString("appimageurl"))
                                .placeholder(R.drawable.default_image)
                                .into(imageView);
                        imageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                try {
                                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(response.getJSONArray("content").getJSONObject(0).getString("applink"))));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    } else {
                        imageView.setVisibility(View.GONE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d(Constants.TAG, response.toString());
            }
        });


        String idMatcherURL = "http://apisea.xyz/BPL2016/apis/v4/livescoresource.php";
        Log.d(Constants.TAG, idMatcherURL);

        final AlertDialog progressDialog = new SpotsDialog(getContext(), R.style.Custom);
        progressDialog.show();
        progressDialog.setCancelable(true);
        RequestParams params = new RequestParams();
        params.add("key", "bl905577");

        FetchFromWeb.get(idMatcherURL, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                progressDialog.dismiss();
                try {
                    if (response.getString("msg").equals("Successful")) {
                        final String source = response.getJSONArray("content").getJSONObject(0).getString("scoresource");
                        String url = response.getJSONArray("content").getJSONObject(0).getString("url");
                        if (source.equals("myself") || source.equals("cricinfo") || source.equals("webview")) {
                            recyclerView.setAdapter(new BasicListAdapter<Match, LiveScoreViewHolder>(datas) {
                                @Override
                                public LiveScoreViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                                    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_live_score, parent, false);
                                    return new LiveScoreViewHolder(view);
                                }

                                @Override
                                public void onBindViewHolder(LiveScoreViewHolder holder, final int position) {
                                    Picasso.with(getContext())
                                            .load(Constants.resolveLogo(datas.get(position).getTeam1()))
                                            .placeholder(R.drawable.default_image)
                                            .into(holder.imgteam1);

                                    Picasso.with(getContext())
                                            .load(Constants.resolveLogo(datas.get(position).getTeam2()))
                                            .placeholder(R.drawable.default_image)
                                            .into(holder.imgteam2);

                                    holder.textteam1.setText(datas.get(position).getTeam1());
                                    holder.textteam2.setText(datas.get(position).getTeam2());
                                    holder.venue.setText(Html.fromHtml(datas.get(position).getVenue()));
                                    holder.time.setText(datas.get(position).getTime());
                                }
                            });
                            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

                            recyclerView.addOnItemTouchListener(
                                    new RecyclerItemClickListener(getContext(), new RecyclerItemClickListener.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(View view, int position) {
                                            if (source.equals("webview")) {
                                                Intent intent = new Intent(getContext(), LiveScore.class);
                                                intent.putExtra("url", "http://www.criconly.com/ipl/2013/get__summary.php?id=" + datas.get(position).getMatchId());
                                                startActivity(intent);
                                            } else {
                                                Intent intent = new Intent(getContext(), ActivityMatchDetails.class);
                                                intent.putExtra("matchID", datas.get(position).getMatchId());
                                                startActivity(intent);
                                            }

                                        }
                                    })
                            );

                            if (source.equals("cricinfo")) {
                                //String url = "http://cricinfo-mukki.rhcloud.com/api/match/live";
                                Log.d(Constants.TAG, url);

                                final AlertDialog progressDialog = new SpotsDialog(getContext(), R.style.Custom);
                                progressDialog.show();
                                progressDialog.setCancelable(true);
                                FetchFromWeb.get(url, null, new JsonHttpResponseHandler() {
                                    @Override
                                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                        progressDialog.dismiss();
                                        try {
                                            Log.d(Constants.TAG, response.toString());
                                            JSONArray jsonArray = response.getJSONArray("items");
                                            for (int i = 0; i < jsonArray.length(); i++) {
                                                JSONObject obj = jsonArray.getJSONObject(i);

                                                datas.add(new Match(obj.getJSONObject("team1").getString("teamName"), obj.getJSONObject("team2").getString("teamName"),
                                                        obj.getString("matchDescription"), "", "", "", obj.getString("matchId")));
                                            }
                                            recyclerView.getAdapter().notifyDataSetChanged();
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }

                                    @Override
                                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                                        progressDialog.dismiss();
                                        Toast.makeText(getContext(), "Failed", Toast.LENGTH_LONG).show();
                                    }

                                    @Override
                                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                                        progressDialog.dismiss();
                                        Toast.makeText(getContext(), "Failed", Toast.LENGTH_LONG).show();
                                    }
                                });

                            } else if (source.equals("myself")) {
                                //String url = "http://apisea.xyz/Cricket/apis/v1/fetchMyLiveScores.php?key=bl905577";
                                Log.d(Constants.TAG, url);

                                final AlertDialog progressDialog = new SpotsDialog(getContext(), R.style.Custom);
                                progressDialog.show();
                                progressDialog.setCancelable(true);
                                FetchFromWeb.get(url, null, new JsonHttpResponseHandler() {
                                    @Override
                                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                        progressDialog.dismiss();
                                        try {
                                            Log.d(Constants.TAG, response.toString());
                                            if (response.getString("msg").equals("Successful")) {
                                                JSONArray jsonArray = response.getJSONArray("content");
                                                for (int i = 0; i < jsonArray.length(); i++) {
                                                    JSONObject obj = jsonArray.getJSONObject(i);
                                                    datas.add(new Match(obj.getString("team1"), obj.getString("team2"),
                                                            obj.getString("status"), "", "", "", obj.getString("matchId")));
                                                }
                                            }
                                            recyclerView.getAdapter().notifyDataSetChanged();
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }

                                    @Override
                                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                                        progressDialog.dismiss();
                                        Toast.makeText(getContext(), "Failed", Toast.LENGTH_LONG).show();
                                    }

                                    @Override
                                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                                        progressDialog.dismiss();
                                        Toast.makeText(getContext(), "Failed", Toast.LENGTH_LONG).show();
                                    }
                                });

                            } else if (source.equals("webview")) {
                                //String url = "http://www.criconly.com/ipl/2013/html/iphone_home_json.json";
                                Log.d(Constants.TAG, url);

                                final AlertDialog progressDialog = new SpotsDialog(getContext(), R.style.Custom);
                                progressDialog.show();
                                progressDialog.setCancelable(true);

                                FetchFromWeb.get(url, null, new JsonHttpResponseHandler() {

                                    @Override
                                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                        progressDialog.dismiss();
                                        try {

                                            JSONArray jsonArray = response.getJSONArray("live");
                                            for (int i = 0; i < jsonArray.length(); i++) {
                                                JSONObject obj = jsonArray.getJSONObject(i);
                                                datas.add(new Match(obj.getString("team1_sname"), obj.getString("team2_sname"),
                                                        obj.getString("result"), "", "", "", obj.getString("match_id")));
                                            }

                                            recyclerView.getAdapter().notifyDataSetChanged();
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

                                    @Override
                                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                                        progressDialog.dismiss();
                                    }
                                });
                            }
                        } else {
                            Toast.makeText(getContext(), "Please Update App", Toast.LENGTH_SHORT).show();
                        }
                    }

                    Log.d(Constants.TAG, response.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                progressDialog.dismiss();
                Toast.makeText(getContext(), "Failed", Toast.LENGTH_LONG).show();
            }
        });


        //cardContainer.setVisibility(View.GONE);


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position1) {
                addBottomDots(position1);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        recyclerView.setNestedScrollingEnabled(false);

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void addBottomDots(int currentPage) {
        dots1 = new TextView[imageUrls.size()];

        int colorsActive = getResources().getColor(R.color.DarkGreen);
        int colorsInactive = getResources().getColor(R.color.MediumSpringGreen);

        placeImageDotsLayout.removeAllViews();
        for (int i = 0; i < dots1.length; i++) {
            dots1[i] = new TextView(getContext());
            dots1[i].setText(Html.fromHtml("&#8226;"));
            dots1[i].setTextSize(35);
            dots1[i].setTextColor(colorsInactive);
            placeImageDotsLayout.addView(dots1[i]);
        }
        if (dots1.length > 0)
            dots1[currentPage].setTextColor(colorsActive);
    }

    private static class LiveScoreViewHolder extends RecyclerView.ViewHolder {
        protected CircleImageView imgteam1;
        protected CircleImageView imgteam2;
        protected TextView textteam1;
        protected TextView textteam2;
        protected TextView venue;
        protected TextView time;

        public LiveScoreViewHolder(final View itemView) {
            super(itemView);

            imgteam1 = ViewHolder.get(itemView, R.id.civTeam1);
            imgteam2 = ViewHolder.get(itemView, R.id.civTeam2);
            textteam1 = ViewHolder.get(itemView, R.id.tvTeam1);
            textteam2 = ViewHolder.get(itemView, R.id.tvTeam2);
            venue = ViewHolder.get(itemView, R.id.tvVenue);
            time = ViewHolder.get(itemView, R.id.tvTime);
        }
    }
}
