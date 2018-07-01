package com.allgames.sportslab.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
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

import com.allgames.sportslab.R;
import com.allgames.sportslab.activity.ActivityMatchDetails;
import com.allgames.sportslab.adapter.BasicListAdapter;
import com.allgames.sportslab.model.Match;
import com.allgames.sportslab.util.CircleImageView;
import com.allgames.sportslab.util.Constants;
import com.allgames.sportslab.util.DefaultMessageHandler;
import com.allgames.sportslab.util.NetworkService;
import com.allgames.sportslab.util.RecyclerItemClickListener;
import com.allgames.sportslab.util.ViewHolder;
import com.google.inject.Inject;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import roboguice.fragment.RoboFragment;
import roboguice.inject.InjectView;

/**
 * @author Ripon
 */

public class LiveScoreFragment extends RoboFragment {

    @InjectView(R.id.tv_welcome_text)
    private TextView welcomeText;
    @InjectView(R.id.live_matches)
    private RecyclerView recyclerView;
    @InjectView(R.id.tour_image)
    private ImageView imageView;
    @InjectView(R.id.tv_empty_view)
    private TextView emptyView;

    @Inject
    private ArrayList<Match> datas;
    @Inject
    private NetworkService networkService;

    private PackageInfo pInfo;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_front_page, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        try {
            pInfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        String welcomeTextUrl = Constants.WELCOME_TEXT_URL;
        Log.d(Constants.TAG, welcomeTextUrl);

        networkService.fetchWelcomeText(new DefaultMessageHandler(getContext(), true) {
            @Override
            public void onSuccess(Message msg) {
                String string = (String) msg.obj;
                try {
                    final JSONObject response = new JSONObject(string);

                    Constants.SHOW_PLAYER_IMAGE = response.getJSONArray("content").getJSONObject(0).getString("playerimage");
                    welcomeText.setText(Html.fromHtml(response.getJSONArray("content").getJSONObject(0).getString("description")));
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

                    if (isNetworkAvailable() && response.getJSONArray("content").getJSONObject(0).getString("appimage").equals("true")) {
                        imageView.setVisibility(View.VISIBLE);
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

                    if (!response.getJSONArray("content").getJSONObject(0).getString("checkversion").contains(pInfo.versionName)) {
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
                        alertDialogBuilder.setMessage(response.getJSONArray("content").getJSONObject(0).getString("popupmessage"));
                        alertDialogBuilder.setPositiveButton("Yes",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface arg0, int arg1) {
                                        try {
                                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(response.getJSONArray("content").getJSONObject(0).getString("popuplink"))));
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                    }
                                });

                        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });

                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();
                    }
                    if (response.getJSONArray("content").getJSONObject(0).getString("showlivescore").equals("false")) {
                        recyclerView.setVisibility(View.GONE);
                    } else {
                        recyclerView.setVisibility(View.VISIBLE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        recyclerView.setAdapter(new BasicListAdapter<Match, LiveScoreViewHolder>(datas) {
            @Override
            public LiveScoreViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.match, parent, false);
                return new LiveScoreViewHolder(view);
            }

            @Override
            public void onBindViewHolder(LiveScoreViewHolder holder, final int position) {
                //if (Constants.SHOW_PLAYER_IMAGE.equals("true")) {
                Picasso.with(getContext())
                        .load(Constants.resolveLogo(datas.get(position).getTeam1()))
                        .placeholder(R.drawable.default_image)
                        .into(holder.imgteam1);

                Picasso.with(getContext())
                        .load(Constants.resolveLogo(datas.get(position).getTeam2()))
                        .placeholder(R.drawable.default_image)
                        .into(holder.imgteam2);
                //}

                holder.textteam1.setText(datas.get(position).getTeam1());
                holder.textteam2.setText(datas.get(position).getTeam2());
                holder.venue.setText(Html.fromHtml(datas.get(position).getVenue()));
                holder.time.setText(datas.get(position).getMatchStatus());

                holder.seriesName.setText(datas.get(position).getSeriesName());
                holder.matchNo.setText(datas.get(position).getMatchNo());
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getContext(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Intent intent = new Intent(getActivity(), ActivityMatchDetails.class);
                        intent.putExtra("summary", datas.get(position));
                        startActivity(intent);
                    }
                })
        );

        networkService.fetchLiveScore(new DefaultMessageHandler(getContext(), true) {
            @Override
            public void onSuccess(Message msg) {
                String result = (String) msg.obj;
                try {
                    JSONArray matchJsonArray = new JSONArray(result);
                    for (int i = 0; i < matchJsonArray.length(); i++) {
                        JSONObject matchJsonObject = matchJsonArray.getJSONObject(i);
                        String team1 = matchJsonObject.getJSONObject("team1").getString("sName");
                        String team2 = matchJsonObject.getJSONObject("team2").getString("sName");
                        String venue = matchJsonObject.getJSONObject("header").getString("grnd") + ", " +
                                matchJsonObject.getJSONObject("header").getString("vcity") + ", " +
                                matchJsonObject.getJSONObject("header").getString("vcountry");
                        String time = matchJsonObject.getJSONObject("header").getString("status");
                        String seriesName = matchJsonObject.getString("srs");
                        String matchNo = matchJsonObject.getJSONObject("header").getString("mnum");
                        String matchId = matchJsonObject.getString("matchId");
                        String dataPath = matchJsonObject.getString("datapath");

                        datas.add(new Match(team1, team2, venue, time, seriesName, matchNo, matchId, dataPath));
                    }

                    recyclerView.getAdapter().notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

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


    private static class LiveScoreViewHolder extends RecyclerView.ViewHolder {
        CircleImageView imgteam1;
        CircleImageView imgteam2;
        TextView textteam1;
        TextView textteam2;
        TextView venue;
        TextView time;
        TextView seriesName;
        TextView matchNo;
        LinearLayout linearLayout;

        LiveScoreViewHolder(final View itemView) {
            super(itemView);

            imgteam1 = ViewHolder.get(itemView, R.id.civTeam1);
            imgteam2 = ViewHolder.get(itemView, R.id.civTeam2);
            textteam1 = ViewHolder.get(itemView, R.id.tvTeam1);
            textteam2 = ViewHolder.get(itemView, R.id.tvTeam2);
            venue = ViewHolder.get(itemView, R.id.tvVenue);
            time = ViewHolder.get(itemView, R.id.tvTime);
            seriesName = ViewHolder.get(itemView, R.id.tvSeriesname);
            matchNo = ViewHolder.get(itemView, R.id.tvMatchNo);
            linearLayout = ViewHolder.get(itemView, R.id.match_layout);
        }
    }
}
