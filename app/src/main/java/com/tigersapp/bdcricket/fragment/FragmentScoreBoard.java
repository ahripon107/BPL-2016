package com.tigersapp.bdcricket.fragment;

import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.inject.Inject;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.tigersapp.bdcricket.R;
import com.tigersapp.bdcricket.activity.ActivityMatchDetails;
import com.tigersapp.bdcricket.adapter.BatsmanAdapter;
import com.tigersapp.bdcricket.adapter.BowlerAdapter;
import com.tigersapp.bdcricket.model.Batsman;
import com.tigersapp.bdcricket.model.Bowler;
import com.tigersapp.bdcricket.model.Summary;
import com.tigersapp.bdcricket.util.Constants;
import com.tigersapp.bdcricket.util.DefaultMessageHandler;
import com.tigersapp.bdcricket.util.FetchFromWeb;
import com.tigersapp.bdcricket.util.NetworkService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import roboguice.fragment.RoboFragment;
import roboguice.inject.InjectView;

/**
 * @author Ripon
 */
public class FragmentScoreBoard extends RoboFragment {

    @InjectView(R.id.lvBattingInnings1)
    private RecyclerView battingInnings1;

    @InjectView(R.id.lvBowlingInnings1)
    private RecyclerView bowlingInnings1;

    @InjectView(R.id.innings1extra)
    private TextView innings1extra;

    @InjectView(R.id.innings1total)
    private TextView innings1total;

    @InjectView(R.id.innings1fow)
    private TextView innings1fallofwickets;

    @InjectView(R.id.innings1DNB)
    private TextView innings1dnb;

    @InjectView(R.id.firstinningscontainer)
    private LinearLayout firstInningsContainer;

    @InjectView(R.id.labelGround)
    private TextView labelGround;

    @InjectView(R.id.labelInfo)
    private TextView labelInfo;

    @InjectView(R.id.labelMatchStatus)
    private TextView labelMatchStatus;

    @InjectView(R.id.labelTeam1)
    private TextView labelTeam1;

    @InjectView(R.id.labelTeam2)
    private TextView labelTeam2;

    @InjectView(R.id.labelTournament)
    private TextView labelTournament;

    @InjectView(R.id.btn_first_inns)
    private Button firstInnings;

    @InjectView(R.id.btn_second_inns)
    private Button secondInnings;

    @InjectView(R.id.btn_third_inns)
    private Button thirdInnings;

    @InjectView(R.id.btn_fourth_inns)
    private Button fourthInnings;

    @Inject
    private NetworkService networkService;

    @InjectView(R.id.refresh)
    private SwipeRefreshLayout swipeRefreshLayout;

    @Inject
    private Gson gson;

    private String liveMatchID;
    private JSONObject response;
    public static int numberOfInnings = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_score_board, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        liveMatchID = getArguments().getString("liveMatchID");

        battingInnings1.setNestedScrollingEnabled(false);
        bowlingInnings1.setNestedScrollingEnabled(false);

        firstInnings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    JSONObject jsonObject = response.getJSONObject("innings1");
                    populateScorecard(jsonObject.getJSONArray("batting"), jsonObject.getJSONArray("bowling"),
                            jsonObject.getJSONObject("summary"), jsonObject.getJSONArray("fow"), jsonObject.getJSONArray("dnb"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                firstInnings.setTextColor(ContextCompat.getColor(getContext(), R.color.cpb_green_dark));
                secondInnings.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
                thirdInnings.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
                fourthInnings.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));

                firstInnings.setTypeface(null, Typeface.BOLD);
                secondInnings.setTypeface(null, Typeface.NORMAL);
                thirdInnings.setTypeface(null, Typeface.NORMAL);
                fourthInnings.setTypeface(null, Typeface.NORMAL);


            }
        });

        secondInnings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    JSONObject jsonObject = response.getJSONObject("innings2");
                    populateScorecard(jsonObject.getJSONArray("batting"), jsonObject.getJSONArray("bowling"),
                            jsonObject.getJSONObject("summary"), jsonObject.getJSONArray("fow"), jsonObject.getJSONArray("dnb"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                firstInnings.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
                secondInnings.setTextColor(ContextCompat.getColor(getContext(), R.color.cpb_green_dark));
                thirdInnings.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
                fourthInnings.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));

                secondInnings.setTypeface(null, Typeface.BOLD);
                firstInnings.setTypeface(null, Typeface.NORMAL);
                thirdInnings.setTypeface(null, Typeface.NORMAL);
                fourthInnings.setTypeface(null, Typeface.NORMAL);
            }
        });

        thirdInnings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    JSONObject jsonObject = response.getJSONObject("innings3");
                    populateScorecard(jsonObject.getJSONArray("batting"), jsonObject.getJSONArray("bowling"),
                            jsonObject.getJSONObject("summary"), jsonObject.getJSONArray("fow"), jsonObject.getJSONArray("dnb"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                firstInnings.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
                secondInnings.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
                thirdInnings.setTextColor(ContextCompat.getColor(getContext(), R.color.cpb_green_dark));
                fourthInnings.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));

                thirdInnings.setTypeface(null, Typeface.BOLD);
                firstInnings.setTypeface(null, Typeface.NORMAL);
                secondInnings.setTypeface(null, Typeface.NORMAL);
                fourthInnings.setTypeface(null, Typeface.NORMAL);
            }
        });

        fourthInnings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    JSONObject jsonObject = response.getJSONObject("innings4");
                    populateScorecard(jsonObject.getJSONArray("batting"), jsonObject.getJSONArray("bowling"),
                            jsonObject.getJSONObject("summary"), jsonObject.getJSONArray("fow"), jsonObject.getJSONArray("dnb"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                firstInnings.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
                secondInnings.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
                thirdInnings.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
                fourthInnings.setTextColor(ContextCompat.getColor(getContext(), R.color.cpb_green_dark));

                fourthInnings.setTypeface(null, Typeface.BOLD);
                firstInnings.setTypeface(null, Typeface.NORMAL);
                secondInnings.setTypeface(null, Typeface.NORMAL);
                thirdInnings.setTypeface(null, Typeface.NORMAL);
            }
        });

        sendRequestForLiveMatchDetails();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                sendRequestForLiveMatchDetails();
            }
        });
    }

    private void sendRequestForLiveMatchDetails() {

        networkService.fetchMatchDetails(liveMatchID, new DefaultMessageHandler(getContext(), true) {
            @Override
            public void onSuccess(Message msg) {
                String string = (String) msg.obj;
                swipeRefreshLayout.setRefreshing(false);
                try {
                    JSONObject response = new JSONObject(string);

                    JSONObject sumry = response.getJSONObject("summary");
                    Summary summary = gson.fromJson(String.valueOf(sumry), Summary.class);


                    setMatchSummary(summary);
                    //((FragmentScoreBoard) ActivityMatchDetails.this.matchDetailsViewPagerAdapter.getItem(0)).loadEachTeamScore(liveMatchID);

                    if (response.has("innings1") && response.has("innings2")) {
                        ((ActivityMatchDetails) getActivity()).setPlayingXI(response.getJSONObject("innings1").getJSONArray("batting"), response.getJSONObject("innings1").getJSONArray("dnb"), response.getJSONObject("innings2").getJSONArray("batting"), response.getJSONObject("innings2").getJSONArray("dnb"), summary.getTeam1(), summary.getTeam2());
                    }

                    if (response.has("innings1")) {

                        if (response.getJSONObject("innings1").has("summary")) {
                            numberOfInnings = 1;
                        } else {
                            hideFirstInnings();
                        }
                    } else {
                        hideFirstInnings();
                    }
                    if (response.has("innings2")) {
                        if (response.getJSONObject("innings2").has("summary")) {
                            numberOfInnings = 2;
                        }
                    }
                    if (response.has("innings3")) {
                        if (response.getJSONObject("innings3").has("summary")) {
                            numberOfInnings = 3;
                        }
                    }
                    if (response.has("innings4")) {
                        if (response.getJSONObject("innings4").has("summary")) {
                            numberOfInnings = 4;
                        }
                    }

                    setResponse(response, numberOfInnings);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        String url = "http://37.187.95.220:8080/cricket-api/api/match/" + this.liveMatchID;
        Log.d(Constants.TAG, url);

    }

    public void setMatchSummary(Summary matchSummary) {
        if (isAdded()) {
            this.labelTournament.setText(matchSummary.getTournament());
            this.labelGround.setText(matchSummary.getGround());
            this.labelInfo.setText(matchSummary.getInfo());
            this.labelTeam1.setText(matchSummary.getTeam1());
            this.labelTeam2.setText(matchSummary.getTeam2());
            this.labelMatchStatus.setText(matchSummary.getMatchStatus());
        }
    }

    private void populateScorecard(JSONArray battingList, JSONArray bowlingList, JSONObject summary, JSONArray fallOfWickets, JSONArray didNotBat) {
        setFirstInningsBattingList(battingList);
        setFirstInningsBowlingList(bowlingList);
        setFirstInningsSummary(summary);
        setFirstInningsFOW(fallOfWickets);
        setFirstInningsDNB(didNotBat);
    }

    public void setResponse(JSONObject response, int numberOfInnings) {
        this.response = response;
        this.numberOfInnings = numberOfInnings;

        if (numberOfInnings == 0) {
            firstInnings.setVisibility(View.GONE);
            secondInnings.setVisibility(View.GONE);
            thirdInnings.setVisibility(View.GONE);
            fourthInnings.setVisibility(View.GONE);
        } else if (numberOfInnings == 1) {
            firstInnings.setTypeface(null, Typeface.BOLD);

            firstInnings.setTextColor(ContextCompat.getColor(getContext(), R.color.cpb_green_dark));
            secondInnings.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
            thirdInnings.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
            fourthInnings.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));

            firstInnings.setVisibility(View.VISIBLE);
            secondInnings.setVisibility(View.GONE);
            thirdInnings.setVisibility(View.GONE);
            fourthInnings.setVisibility(View.GONE);

            try {
                JSONObject jsonObject = response.getJSONObject("innings1");
                populateScorecard(jsonObject.getJSONArray("batting"), jsonObject.getJSONArray("bowling"),
                        jsonObject.getJSONObject("summary"), jsonObject.getJSONArray("fow"), jsonObject.getJSONArray("dnb"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else if (numberOfInnings == 2) {
            secondInnings.setTypeface(null, Typeface.BOLD);

            firstInnings.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
            secondInnings.setTextColor(ContextCompat.getColor(getContext(), R.color.cpb_green_dark));
            thirdInnings.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
            fourthInnings.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));

            firstInnings.setVisibility(View.VISIBLE);
            secondInnings.setVisibility(View.VISIBLE);
            thirdInnings.setVisibility(View.GONE);
            fourthInnings.setVisibility(View.GONE);

            try {
                JSONObject jsonObject = response.getJSONObject("innings2");
                populateScorecard(jsonObject.getJSONArray("batting"), jsonObject.getJSONArray("bowling"),
                        jsonObject.getJSONObject("summary"), jsonObject.getJSONArray("fow"), jsonObject.getJSONArray("dnb"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else if (numberOfInnings == 3) {
            thirdInnings.setTypeface(null, Typeface.BOLD);

            firstInnings.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
            secondInnings.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
            thirdInnings.setTextColor(ContextCompat.getColor(getContext(), R.color.cpb_green_dark));
            fourthInnings.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));

            firstInnings.setVisibility(View.VISIBLE);
            secondInnings.setVisibility(View.VISIBLE);
            thirdInnings.setVisibility(View.VISIBLE);
            fourthInnings.setVisibility(View.GONE);

            try {
                JSONObject jsonObject = response.getJSONObject("innings3");
                populateScorecard(jsonObject.getJSONArray("batting"), jsonObject.getJSONArray("bowling"),
                        jsonObject.getJSONObject("summary"), jsonObject.getJSONArray("fow"), jsonObject.getJSONArray("dnb"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else if (numberOfInnings == 4) {
            fourthInnings.setTypeface(null, Typeface.BOLD);

            firstInnings.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
            secondInnings.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
            thirdInnings.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
            fourthInnings.setTextColor(ContextCompat.getColor(getContext(), R.color.cpb_green_dark));

            firstInnings.setVisibility(View.VISIBLE);
            secondInnings.setVisibility(View.VISIBLE);
            thirdInnings.setVisibility(View.VISIBLE);
            fourthInnings.setVisibility(View.VISIBLE);

            try {
                JSONObject jsonObject = response.getJSONObject("innings4");
                populateScorecard(jsonObject.getJSONArray("batting"), jsonObject.getJSONArray("bowling"),
                        jsonObject.getJSONObject("summary"), jsonObject.getJSONArray("fow"), jsonObject.getJSONArray("dnb"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }


    public void setFirstInningsBattingList(JSONArray jsonArray) {
        BatsmanAdapter batsmanAdapter = new BatsmanAdapter(getContext(), preocessBattingList(jsonArray));
        battingInnings1.setAdapter(batsmanAdapter);
        battingInnings1.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    public void setFirstInningsBowlingList(JSONArray jsonArray) {
        BowlerAdapter bowlerAdapter = new BowlerAdapter(getContext(), processBowlingList(jsonArray));
        bowlingInnings1.setAdapter(bowlerAdapter);
        bowlingInnings1.setLayoutManager(new LinearLayoutManager(getContext()));
    }


    public void setFirstInningsSummary(JSONObject jsonObject) {
        try {
            if (jsonObject.getJSONObject("extra").has("details")) {
                innings1extra.setText(jsonObject.getJSONObject("extra").getString("details") + " --- " + jsonObject.getJSONObject("extra").getString("total"));
            } else {
                innings1extra.setText("0");
            }
            if (jsonObject.getJSONObject("total").has("wickets")) {
                innings1total.setText("(" + jsonObject.getJSONObject("total").getString("overs") + " overs)   " + jsonObject.getJSONObject("total").getString("score") + " for " + jsonObject.getJSONObject("total").getString("wickets"));
            } else {
                innings1total.setText("(" + jsonObject.getJSONObject("total").getString("overs") + " overs)   " + jsonObject.getJSONObject("total").getString("score"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setFirstInningsFOW(JSONArray jsonArray) {
        String string = "<b>Fall of wickets:</b> ";
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                string += jsonObject.getString("score") + " ( " + jsonObject.getString("player") + " , " + jsonObject.getString("over") + " ), ";
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        innings1fallofwickets.setText(Html.fromHtml(string));
    }

    public void setFirstInningsDNB(JSONArray jsonArray) {
        String string = "<b>Did not bat:</b> ";
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                string += jsonObject.getString("playerName") + ", ";
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        innings1dnb.setText(Html.fromHtml(string));
    }


    public void hideFirstInnings() {
        firstInningsContainer.setVisibility(View.GONE);
    }

    public ArrayList<Batsman> preocessBattingList(JSONArray jsonArray) {
        ArrayList<Batsman> batsmen = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String playerId = jsonObject.getJSONObject("player").getString("playerId");
                String name = jsonObject.getJSONObject("player").getString("playerName");

                String out = jsonObject.getString("status");
                String runs = jsonObject.getString("runs");
                String balls = jsonObject.getString("balls");
                String fours = jsonObject.getString("fours");
                String sixes = jsonObject.getString("sixes");
                String sr = jsonObject.getString("strikeRate");
                Batsman batsman = new Batsman(playerId, name, out, runs, balls, fours, sixes, sr);
                batsmen.add(batsman);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return batsmen;
    }

    public ArrayList<Bowler> processBowlingList(JSONArray jsonArray) {
        ArrayList<Bowler> bowlers = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String playerId = jsonObject.getJSONObject("player").getString("playerId");
                String name = jsonObject.getJSONObject("player").getString("playerName");
                String over = jsonObject.getString("overs");
                String maiden = jsonObject.getString("maidens");
                String run = jsonObject.getString("runs");
                String wicket = jsonObject.getString("wickets");
                Bowler bowler = new Bowler(playerId, name, over, maiden, run, wicket);
                bowlers.add(bowler);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return bowlers;
    }

    public void loadEachTeamScore(final String matchID) {
        String url = "http://37.187.95.220:8080/cricket-api/api/match/live";
        Log.d(Constants.TAG, url);

        FetchFromWeb.get(url, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    Log.d(Constants.TAG, response.toString());
                    JSONArray jsonArray = response.getJSONArray("items");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject obj = jsonArray.getJSONObject(i);
                        if (obj.getString("matchId").equals(matchID)) {
                            String team1 = obj.getJSONObject("team1").getString("teamName");
                            String team2 = obj.getJSONObject("team2").getString("teamName");
                            if (obj.getJSONObject("team1").has("score")) {
                                team1 += " " + obj.getJSONObject("team1").getString("score");
                            }
                            if (obj.getJSONObject("team1").has("score1")) {
                                team1 += " & " + obj.getJSONObject("team1").getString("score1");
                            }
                            if (obj.getJSONObject("team2").has("score")) {
                                team2 += " " + obj.getJSONObject("team2").getString("score");
                            }
                            if (obj.getJSONObject("team2").has("score1")) {
                                team2 += " & " + obj.getJSONObject("team2").getString("score1");
                            }
                            labelTeam1.setText(team1);
                            labelTeam2.setText(team2);
                            break;
                        }

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

            }
        });
    }
}
