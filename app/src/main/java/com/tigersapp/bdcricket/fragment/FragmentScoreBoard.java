package com.tigersapp.bdcricket.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
    RecyclerView battingInnings2;
    RecyclerView battingInnings3;
    RecyclerView battingInnings4;
    RecyclerView bowlingInnings1;
    RecyclerView bowlingInnings2;
    RecyclerView bowlingInnings3;
    RecyclerView bowlingInnings4;

    TextView innings1extra,innings1total,innings1fallofwickets,innings1dnb;
    TextView innings2extra,innings2total,innings2fallofwickets,innings2dnb;
    TextView innings3extra,innings3total,innings3fallofwickets,innings3dnb;
    TextView innings4extra,innings4total,innings4fallofwickets,innings4dnb;

    LinearLayout firstInningsContainer,secondInningsContainer,thirdInningsContainer,fourthInningsContainer;

    private TextView labelGround;
    private TextView labelInfo;
    private TextView labelMatchStatus;
    private TextView labelTeam1;
    private TextView labelTeam2;
    private TextView labelTournament;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_score_board,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        battingInnings1 = (RecyclerView) view.findViewById(R.id.lvBattingInnings1);
        battingInnings2 = (RecyclerView) view.findViewById(R.id.lvBattingInnings2);
        battingInnings3 = (RecyclerView) view.findViewById(R.id.lvBattingInnings3);
        battingInnings4 = (RecyclerView) view.findViewById(R.id.lvBattingInnings4);

        bowlingInnings1 = (RecyclerView) view.findViewById(R.id.lvBowlingInnings1);
        bowlingInnings2 = (RecyclerView) view.findViewById(R.id.lvBowlingInnings2);
        bowlingInnings3 = (RecyclerView) view.findViewById(R.id.lvBowlingInnings3);
        bowlingInnings4 = (RecyclerView) view.findViewById(R.id.lvBowlingInnings4);

        battingInnings1.setNestedScrollingEnabled(false);
        battingInnings2.setNestedScrollingEnabled(false);
        battingInnings3.setNestedScrollingEnabled(false);
        battingInnings4.setNestedScrollingEnabled(false);

        bowlingInnings1.setNestedScrollingEnabled(false);
        bowlingInnings2.setNestedScrollingEnabled(false);
        bowlingInnings3.setNestedScrollingEnabled(false);
        bowlingInnings4.setNestedScrollingEnabled(false);

        innings1extra = (TextView) view.findViewById(R.id.innings1extra);
        innings1total = (TextView) view.findViewById(R.id.innings1total);
        innings1fallofwickets = (TextView) view.findViewById(R.id.innings1fow);
        innings1dnb = (TextView) view.findViewById(R.id.innings1DNB);

        innings2extra = (TextView) view.findViewById(R.id.innings2extra);
        innings2total = (TextView) view.findViewById(R.id.innings2total);
        innings2fallofwickets = (TextView) view.findViewById(R.id.innings2fow);
        innings2dnb = (TextView) view.findViewById(R.id.innings2DNB);

        innings3extra = (TextView) view.findViewById(R.id.innings3extra);
        innings3total = (TextView) view.findViewById(R.id.innings3total);
        innings3fallofwickets = (TextView) view.findViewById(R.id.innings3fow);
        innings3dnb = (TextView) view.findViewById(R.id.innings3DNB);

        innings4extra = (TextView) view.findViewById(R.id.innings4extra);
        innings4total = (TextView) view.findViewById(R.id.innings4total);
        innings4fallofwickets = (TextView) view.findViewById(R.id.innings4fow);
        innings4dnb = (TextView) view.findViewById(R.id.innings4DNB);

        fourthInningsContainer = (LinearLayout) view.findViewById(R.id.fourthinningscontainer);
        thirdInningsContainer = (LinearLayout) view.findViewById(R.id.thirdinningscontainer);
        secondInningsContainer = (LinearLayout) view.findViewById(R.id.secondInningsContainer);
        firstInningsContainer = (LinearLayout) view.findViewById(R.id.firstinningscontainer);

        this.labelTournament = (TextView) view.findViewById(R.id.labelTournament);
        this.labelTeam1 = (TextView) view.findViewById(R.id.labelTeam1);
        this.labelTeam2 = (TextView) view.findViewById(R.id.labelTeam2);
        this.labelGround = (TextView) view.findViewById(R.id.labelGround);
        this.labelInfo = (TextView) view.findViewById(R.id.labelInfo);
        this.labelMatchStatus = (TextView) view.findViewById(R.id.labelMatchStatus);
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

    public void setFirstInningsBattingList(JSONArray jsonArray) {
        BatsmanAdapter batsmanAdapter = new BatsmanAdapter(getContext(),preocessBattingList(jsonArray));
        battingInnings1.setAdapter(batsmanAdapter);
        battingInnings1.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    public void setSecondInningsBattingList(JSONArray jsonArray) {
        BatsmanAdapter batsmanAdapter = new BatsmanAdapter(getContext(),preocessBattingList(jsonArray));
        battingInnings2.setAdapter(batsmanAdapter);
        battingInnings2.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    public void setThirdInningsBattingList(JSONArray jsonArray) {
        BatsmanAdapter batsmanAdapter = new BatsmanAdapter(getContext(),preocessBattingList(jsonArray));
        battingInnings3.setAdapter(batsmanAdapter);
        battingInnings3.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    public void setFourthInningsBattingList(JSONArray jsonArray) {
        BatsmanAdapter batsmanAdapter = new BatsmanAdapter(getContext(),preocessBattingList(jsonArray));
        battingInnings4.setAdapter(batsmanAdapter);
        battingInnings4.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    public void setFirstInningsBowlingList(JSONArray jsonArray) {
        BowlerAdapter bowlerAdapter = new BowlerAdapter(getContext(),processBowlingList(jsonArray));
        bowlingInnings1.setAdapter(bowlerAdapter);
        bowlingInnings1.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    public void setSecondInningsBowlingList(JSONArray jsonArray) {
        BowlerAdapter bowlerAdapter = new BowlerAdapter(getContext(),processBowlingList(jsonArray));
        bowlingInnings2.setAdapter(bowlerAdapter);
        bowlingInnings2.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    public void setThirdInningsBowlingList(JSONArray jsonArray) {
        BowlerAdapter bowlerAdapter = new BowlerAdapter(getContext(),processBowlingList(jsonArray));
        bowlingInnings3.setAdapter(bowlerAdapter);
        bowlingInnings3.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    public void setFourthInningsBowlingList(JSONArray jsonArray) {
        BowlerAdapter bowlerAdapter = new BowlerAdapter(getContext(),processBowlingList(jsonArray));
        bowlingInnings4.setAdapter(bowlerAdapter);
        bowlingInnings4.setLayoutManager(new LinearLayoutManager(getContext()));
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

    public void setSecondInningsSummary(JSONObject jsonObject) {
        try {
            if (jsonObject.getJSONObject("extra").has("details")) {
                innings2extra.setText(jsonObject.getJSONObject("extra").getString("details")+" --- "+jsonObject.getJSONObject("extra").getString("total"));
            } else {
                innings2extra.setText("0");
            }

            if (jsonObject.getJSONObject("total").has("wickets")) {
                innings2total.setText("("+jsonObject.getJSONObject("total").getString("overs") +" overs)   "+jsonObject.getJSONObject("total").getString("score") +" for "+jsonObject.getJSONObject("total").getString("wickets"));
            } else {
                innings2total.setText("("+jsonObject.getJSONObject("total").getString("overs") +" overs)   "+jsonObject.getJSONObject("total").getString("score"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setSecondInningsFOW(JSONArray jsonArray) {
        String string = "<b>Fall of wickets:</b> ";
        for (int i=0;i<jsonArray.length();i++) {
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                string += jsonObject.getString("score")+" ( "+jsonObject.getString("player")+" , "+jsonObject.getString("over")+" ), ";
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        innings2fallofwickets.setText(Html.fromHtml(string));
    }

    public void setSecondInningsDNB(JSONArray jsonArray) {
        String string = "<b>Did not bat:</b> ";
        for (int i=0;i<jsonArray.length();i++) {
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                string += jsonObject.getString("playerName")+", ";
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        innings2dnb.setText(Html.fromHtml(string));
    }

    public void setThirdInningsSummary(JSONObject jsonObject) {
        try {
            if (jsonObject.getJSONObject("extra").has("details")) {
                innings3extra.setText(jsonObject.getJSONObject("extra").getString("details")+" --- "+jsonObject.getJSONObject("extra").getString("total"));
            } else {
                innings3extra.setText("0");
            }

            if (jsonObject.getJSONObject("total").has("wickets")) {
                innings3total.setText("("+jsonObject.getJSONObject("total").getString("overs") +" overs)   "+jsonObject.getJSONObject("total").getString("score") +" for "+jsonObject.getJSONObject("total").getString("wickets"));
            } else {
                innings3total.setText("("+jsonObject.getJSONObject("total").getString("overs") +" overs)   "+jsonObject.getJSONObject("total").getString("score"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setThirdInningsFOW(JSONArray jsonArray) {
        String string = "<b>Fall of wickets:</b> ";
        for (int i=0;i<jsonArray.length();i++) {
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                string += jsonObject.getString("score")+" ( "+jsonObject.getString("player")+" , "+jsonObject.getString("over")+" ), ";
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        innings3fallofwickets.setText(Html.fromHtml(string));
    }

    public void setThirdInningsDNB(JSONArray jsonArray) {
        String string = "<b>Did not bat:</b> ";
        for (int i=0;i<jsonArray.length();i++) {
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                string += jsonObject.getString("playerName")+", ";
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        innings3dnb.setText(Html.fromHtml(string));
    }

    public void setFourthInningsSummary(JSONObject jsonObject) {
        try {
            if (jsonObject.getJSONObject("extra").has("details")) {
                innings4extra.setText(jsonObject.getJSONObject("extra").getString("details")+" --- "+jsonObject.getJSONObject("extra").getString("total"));
            } else {
                innings4extra.setText("0");
            }

            if (jsonObject.getJSONObject("total").has("wickets")) {
                innings4total.setText("("+jsonObject.getJSONObject("total").getString("overs") +" overs)   "+jsonObject.getJSONObject("total").getString("score") +" for "+jsonObject.getJSONObject("total").getString("wickets"));
            } else {
                innings4total.setText("("+jsonObject.getJSONObject("total").getString("overs") +" overs)   "+jsonObject.getJSONObject("total").getString("score"));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setFourthInningsFOW(JSONArray jsonArray) {
        String string = "<b>Fall of wickets:</b> ";
        for (int i=0;i<jsonArray.length();i++) {
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                string += jsonObject.getString("score")+" ( "+jsonObject.getString("player")+" , "+jsonObject.getString("over")+" ), ";
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        innings4fallofwickets.setText(Html.fromHtml(string));
    }

    public void setFourthInningsDNB(JSONArray jsonArray) {
        String string = "<b>Did not bat:</b> ";
        for (int i=0;i<jsonArray.length();i++) {
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                string += jsonObject.getString("playerName")+", ";
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        innings4dnb.setText(Html.fromHtml(string));
    }

    public void hideFirstInnings() {
        firstInningsContainer.setVisibility(View.GONE);
    }
    public void hideSecondInnings() {
        secondInningsContainer.setVisibility(View.GONE);
    }
    public void hideThirdInnings() {
        thirdInningsContainer.setVisibility(View.GONE);
    }
    public void hideFourthInnings() {
        fourthInningsContainer.setVisibility(View.GONE);
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
