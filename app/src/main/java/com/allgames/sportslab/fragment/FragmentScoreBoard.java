package com.allgames.sportslab.fragment;

import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.allgames.sportslab.R;
import com.allgames.sportslab.adapter.BatsmanAdapter;
import com.allgames.sportslab.adapter.BowlerAdapter;
import com.allgames.sportslab.model.Batsman;
import com.allgames.sportslab.model.Bowler;
import com.allgames.sportslab.model.Match;
import com.allgames.sportslab.util.DefaultMessageHandler;
import com.allgames.sportslab.util.NetworkService;
import com.google.gson.Gson;
import com.google.inject.Inject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import roboguice.fragment.RoboFragment;
import roboguice.inject.InjectView;

/**
 * @author Ripon
 */
public class FragmentScoreBoard extends RoboFragment {

    public static int numberOfInnings = 0;
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
    private JSONObject response;
    private Match match;
    private Map<String, String> playerIdNameMap;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_score_board, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        battingInnings1.setNestedScrollingEnabled(false);
        bowlingInnings1.setNestedScrollingEnabled(false);

        playerIdNameMap = new HashMap<>();

        firstInnings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    JSONObject jsonObject = response.getJSONObject("Innings").getJSONObject("1");
                    populateScorecard(jsonObject.getJSONArray("batsmen"), jsonObject.getJSONArray("bowlers"),
                            jsonObject, jsonObject, jsonObject.getString("nextbatsman"));
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
                    JSONObject jsonObject = response.getJSONObject("Innings").getJSONObject("2");
                    populateScorecard(jsonObject.getJSONArray("batsmen"), jsonObject.getJSONArray("bowlers"),
                            jsonObject, jsonObject, jsonObject.getString("nextbatsman"));
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
                    JSONObject jsonObject = response.getJSONObject("Innings").getJSONObject("3");
                    populateScorecard(jsonObject.getJSONArray("batsmen"), jsonObject.getJSONArray("bowlers"),
                            jsonObject, jsonObject, jsonObject.getString("nextbatsman"));
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
                    JSONObject jsonObject = response.getJSONObject("Innings").getJSONObject("4");
                    populateScorecard(jsonObject.getJSONArray("batsmen"), jsonObject.getJSONArray("bowlers"),
                            jsonObject, jsonObject, jsonObject.getString("nextbatsman"));
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

        match = (Match) getArguments().getSerializable("summary");
        sendRequestForLiveMatchDetails();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                sendRequestForLiveMatchDetails();
            }
        });
    }

    private void sendRequestForLiveMatchDetails() {
        swipeRefreshLayout.setRefreshing(true);

        networkService.fetchMatchDetails(match.getDataPath(), new DefaultMessageHandler(getContext(), false) {
            @Override
            public void onSuccess(Message msg) {
                String string = (String) msg.obj;
                swipeRefreshLayout.setRefreshing(false);

                try {
                    JSONObject response = new JSONObject(string);

                    if (response.toString().equals("{}")) {
                        setMatchSummary(match);
                        setResponse(null, 0);
                        hideFirstInnings();
                    } else {
                        preparePlayerIdNameMap(response.getJSONArray("players"));

                        setMatchSummary(match, response.getJSONObject("header").getString("status"));
                        numberOfInnings = response.getJSONObject("header").getInt("NoOfIngs");

                        setResponse(response, numberOfInnings);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void preparePlayerIdNameMap(JSONArray playersArray) {
        try {
            for (int i = 0; i < playersArray.length(); i++) {
                JSONObject playerObject = playersArray.getJSONObject(i);
                playerIdNameMap.put(playerObject.getString("id"), playerObject.getString("fName"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setMatchSummary(Match matchSummary, String status) {
        if (isAdded()) {
            this.labelTournament.setText(matchSummary.getSeriesName());
            this.labelGround.setText(matchSummary.getVenue());
            this.labelInfo.setText(matchSummary.getMatchNo());
            this.labelTeam1.setText(matchSummary.getTeam1());
            this.labelTeam2.setText(matchSummary.getTeam2());
            this.labelMatchStatus.setText(status);
        }
    }

    private void setMatchSummary(Match matchSummary) {
        if (isAdded()) {
            this.labelTournament.setText(matchSummary.getSeriesName());
            this.labelGround.setText(matchSummary.getVenue());
            this.labelInfo.setText(matchSummary.getMatchNo());
            this.labelTeam1.setText(matchSummary.getTeam1());
            this.labelTeam2.setText(matchSummary.getTeam2());
            this.labelMatchStatus.setText(matchSummary.getMatchStatus());
        }
    }

    private void populateScorecard(JSONArray battingList, JSONArray bowlingList, JSONObject summary, JSONObject fallOfWickets, String didNotBat) {
        setFirstInningsBattingList(battingList);
        setFirstInningsBowlingList(bowlingList);
        setFirstInningsSummary(summary);
        setFirstInningsFOW(fallOfWickets);
        setFirstInningsDNB(didNotBat);
    }

    private void setResponse(JSONObject response, int numberOfInnings) {
        this.response = response;
        FragmentScoreBoard.numberOfInnings = numberOfInnings;

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
                JSONObject jsonObject = response.getJSONObject("Innings").getJSONObject("1");
                firstInnings.setText(response.getJSONObject("Innings").getJSONObject("1").getString("battingteam"));
                populateScorecard(jsonObject.getJSONArray("batsmen"), jsonObject.getJSONArray("bowlers"),
                        jsonObject, jsonObject, jsonObject.getString("nextbatsman"));
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
                JSONObject jsonObject = response.getJSONObject("Innings").getJSONObject("2");
                firstInnings.setText(response.getJSONObject("Innings").getJSONObject("1").getString("battingteam"));
                secondInnings.setText(response.getJSONObject("Innings").getJSONObject("2").getString("battingteam"));
                populateScorecard(jsonObject.getJSONArray("batsmen"), jsonObject.getJSONArray("bowlers"),
                        jsonObject, jsonObject, jsonObject.getString("nextbatsman"));
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
                JSONObject jsonObject = response.getJSONObject("Innings").getJSONObject("3");
                firstInnings.setText(response.getJSONObject("Innings").getJSONObject("1").getString("battingteam"));
                secondInnings.setText(response.getJSONObject("Innings").getJSONObject("2").getString("battingteam"));
                thirdInnings.setText(response.getJSONObject("Innings").getJSONObject("3").getString("battingteam"));
                populateScorecard(jsonObject.getJSONArray("batsmen"), jsonObject.getJSONArray("bowlers"),
                        jsonObject, jsonObject, jsonObject.getString("nextbatsman"));
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
                JSONObject jsonObject = response.getJSONObject("Innings").getJSONObject("4");
                firstInnings.setText(response.getJSONObject("Innings").getJSONObject("1").getString("battingteam"));
                secondInnings.setText(response.getJSONObject("Innings").getJSONObject("2").getString("battingteam"));
                thirdInnings.setText(response.getJSONObject("Innings").getJSONObject("3").getString("battingteam"));
                fourthInnings.setText(response.getJSONObject("Innings").getJSONObject("4").getString("battingteam"));
                populateScorecard(jsonObject.getJSONArray("batsmen"), jsonObject.getJSONArray("bowlers"),
                        jsonObject, jsonObject, jsonObject.getString("nextbatsman"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    private void setFirstInningsBattingList(JSONArray jsonArray) {
        BatsmanAdapter batsmanAdapter = new BatsmanAdapter(getContext(), preocessBattingList(jsonArray));
        battingInnings1.setAdapter(batsmanAdapter);
        battingInnings1.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void setFirstInningsBowlingList(JSONArray jsonArray) {
        BowlerAdapter bowlerAdapter = new BowlerAdapter(getContext(), processBowlingList(jsonArray));
        bowlingInnings1.setAdapter(bowlerAdapter);
        bowlingInnings1.setLayoutManager(new LinearLayoutManager(getContext()));
    }


    private void setFirstInningsSummary(JSONObject jsonObject) {
        try {
            JSONObject extraObject = jsonObject.getJSONObject("extras");
            innings1extra.setText("B-" + extraObject.getString("byes") + ", LB-" + extraObject.getString("legByes") +
                    ", W-" + extraObject.getString("wideBalls") + ", NB-" +
                    extraObject.getString("noBalls") + "  -----  " + extraObject.getString("total"));


            innings1total.setText("(" + jsonObject.getString("overs") + " overs)   " + jsonObject.getString("runs") + " for " + jsonObject.getString("wickets"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setFirstInningsFOW(JSONObject fowJsonObject) {
        String string = "<b>Fall of wickets:</b> ";
        try {
            if (fowJsonObject.has("fow")) {
                JSONArray jsonArray = fowJsonObject.getJSONArray("fow");
                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    string += jsonObject.getString("run") + "/" + jsonObject.getString("wicketnbr") + " ( " +
                            playerIdNameMap.get(jsonObject.getString("outBatsmanId")) + " , " + jsonObject.getString("overnbr") + " ), ";
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        innings1fallofwickets.setText(Html.fromHtml(string));
    }

    private void setFirstInningsDNB(String didNotBat) {
        String string = "<b>Did not bat:</b> " + didNotBat;
        innings1dnb.setText(Html.fromHtml(string));
    }


    private void hideFirstInnings() {
        firstInningsContainer.setVisibility(View.GONE);
    }

    private ArrayList<Batsman> preocessBattingList(JSONArray jsonArray) {
        ArrayList<Batsman> batsmen = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String playerId = jsonObject.getString("batsmanId");
                String name = playerIdNameMap.get(playerId);

                String out = jsonObject.getString("outdescription");
                String runs = jsonObject.getString("run");
                String balls = jsonObject.getString("ball");
                String fours = jsonObject.getString("four");
                String sixes = jsonObject.getString("six");
                String sr = jsonObject.getString("sr");
                Batsman batsman = new Batsman(playerId, name, out, runs, balls, fours, sixes, sr);
                batsmen.add(batsman);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return batsmen;
    }

    private ArrayList<Bowler> processBowlingList(JSONArray jsonArray) {
        ArrayList<Bowler> bowlers = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String playerId = jsonObject.getString("bowlerId");
                String name = playerIdNameMap.get(playerId);
                String over = jsonObject.getString("over");
                String maiden = jsonObject.getString("maiden");
                String run = jsonObject.getString("run");
                String wicket = jsonObject.getString("wicket");
                Bowler bowler = new Bowler(playerId, name, over, maiden, run, wicket);
                bowlers.add(bowler);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return bowlers;
    }
}
