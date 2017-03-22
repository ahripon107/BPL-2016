package com.tigersapp.bdcricket.fragment;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
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

import com.loopj.android.http.JsonHttpResponseHandler;
import com.tigersapp.bdcricket.R;
import com.tigersapp.bdcricket.adapter.BatsmanAdapter;
import com.tigersapp.bdcricket.adapter.BowlerAdapter;
import com.tigersapp.bdcricket.model.Batsman;
import com.tigersapp.bdcricket.model.Bowler;
import com.tigersapp.bdcricket.model.Summary;
import com.tigersapp.bdcricket.util.Constants;
import com.tigersapp.bdcricket.util.FetchFromWeb;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * @author Ripon
 */
public class FragmentScoreBoard extends Fragment {

    RecyclerView battingInnings1;

    RecyclerView bowlingInnings1;


    TextView innings1extra,innings1total,innings1fallofwickets,innings1dnb;

    LinearLayout firstInningsContainer;

    private TextView labelGround;
    private TextView labelInfo;
    private TextView labelMatchStatus;
    private TextView labelTeam1;
    private TextView labelTeam2;
    private TextView labelTournament;
    private JSONObject response;
    private int numberOfInnings;

    private Button firstInnings, secondInnings, thirdInnings, fourthInnings;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_score_board,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        battingInnings1 = (RecyclerView) view.findViewById(R.id.lvBattingInnings1);

        bowlingInnings1 = (RecyclerView) view.findViewById(R.id.lvBowlingInnings1);

        battingInnings1.setNestedScrollingEnabled(false);

        bowlingInnings1.setNestedScrollingEnabled(false);

        innings1extra = (TextView) view.findViewById(R.id.innings1extra);
        innings1total = (TextView) view.findViewById(R.id.innings1total);
        innings1fallofwickets = (TextView) view.findViewById(R.id.innings1fow);
        innings1dnb = (TextView) view.findViewById(R.id.innings1DNB);

        firstInningsContainer = (LinearLayout) view.findViewById(R.id.firstinningscontainer);

        this.labelTournament = (TextView) view.findViewById(R.id.labelTournament);
        this.labelTeam1 = (TextView) view.findViewById(R.id.labelTeam1);
        this.labelTeam2 = (TextView) view.findViewById(R.id.labelTeam2);
        this.labelGround = (TextView) view.findViewById(R.id.labelGround);
        this.labelInfo = (TextView) view.findViewById(R.id.labelInfo);
        this.labelMatchStatus = (TextView) view.findViewById(R.id.labelMatchStatus);

        firstInnings = (Button) view.findViewById(R.id.btn_first_inns);
        secondInnings = (Button) view.findViewById(R.id.btn_second_inns);
        thirdInnings = (Button) view.findViewById(R.id.btn_third_inns);
        fourthInnings = (Button) view.findViewById(R.id.btn_fourth_inns);

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
        BatsmanAdapter batsmanAdapter = new BatsmanAdapter(getContext(),preocessBattingList(jsonArray));
        battingInnings1.setAdapter(batsmanAdapter);
        battingInnings1.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    public void setFirstInningsBowlingList(JSONArray jsonArray) {
        BowlerAdapter bowlerAdapter = new BowlerAdapter(getContext(),processBowlingList(jsonArray));
        bowlingInnings1.setAdapter(bowlerAdapter);
        bowlingInnings1.setLayoutManager(new LinearLayoutManager(getContext()));
    }


    public void setFirstInningsSummary(JSONObject jsonObject) {
        try {
            if (jsonObject.getJSONObject("extra").has("details")) {
                innings1extra.setText(jsonObject.getJSONObject("extra").getString("details")+" --- "+jsonObject.getJSONObject("extra").getString("total"));
            } else {
                innings1extra.setText("0");
            }
            if (jsonObject.getJSONObject("total").has("wickets")) {
                innings1total.setText("("+jsonObject.getJSONObject("total").getString("overs") +" overs)   "+jsonObject.getJSONObject("total").getString("score") +" for "+jsonObject.getJSONObject("total").getString("wickets"));
            } else {
                innings1total.setText("("+jsonObject.getJSONObject("total").getString("overs") +" overs)   "+jsonObject.getJSONObject("total").getString("score"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setFirstInningsFOW(JSONArray jsonArray) {
        String string = "<b>Fall of wickets:</b> ";
        for (int i=0;i<jsonArray.length();i++) {
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                string += jsonObject.getString("score")+" ( "+jsonObject.getString("player")+" , "+jsonObject.getString("over")+" ), ";
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        innings1fallofwickets.setText(Html.fromHtml(string));
    }

    public void setFirstInningsDNB(JSONArray jsonArray) {
        String string = "<b>Did not bat:</b> ";
        for (int i=0;i<jsonArray.length();i++) {
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                string += jsonObject.getString("playerName")+", ";
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
        for (int i=0;i<jsonArray.length();i++) {
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
                Batsman batsman = new Batsman(playerId,name,out,runs,balls,fours,sixes,sr);
                batsmen.add(batsman);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return batsmen;
    }

    public ArrayList<Bowler> processBowlingList(JSONArray jsonArray) {
        ArrayList<Bowler> bowlers = new ArrayList<>();
        for (int i=0;i<jsonArray.length();i++) {
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String playerId = jsonObject.getJSONObject("player").getString("playerId");
                String name =jsonObject.getJSONObject("player").getString("playerName");
                String over = jsonObject.getString("overs");
                String maiden = jsonObject.getString("maidens");
                String run = jsonObject.getString("runs");
                String wicket = jsonObject.getString("wickets");
                Bowler bowler = new Bowler(playerId,name,over,maiden,run,wicket);
                bowlers.add(bowler);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return bowlers;
    }

    public void loadEachTeamScore (final String matchID) {
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
                                team1+= " "+obj.getJSONObject("team1").getString("score");
                            }
                            if (obj.getJSONObject("team1").has("score1")) {
                                team1+= " & "+obj.getJSONObject("team1").getString("score1");
                            }
                            if (obj.getJSONObject("team2").has("score")) {
                                team2+= " "+obj.getJSONObject("team2").getString("score");
                            }
                            if (obj.getJSONObject("team2").has("score1")) {
                                team2+= " & "+obj.getJSONObject("team2").getString("score1");
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
