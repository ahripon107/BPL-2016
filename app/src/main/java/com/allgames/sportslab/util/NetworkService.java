package com.allgames.sportslab.util;

import android.os.Handler;
import android.util.Log;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;

/**
 * @author Ripon
 */
@Singleton
public class NetworkService {

    @Inject
    private AsyncHttpClient httpClient;

    public void fetchMatchDetails(String dataPath, Handler handler) {
        Log.d("matchdetails", "http://sng.mapps.cricbuzz.com/cbzandroid/3.0/match/" + dataPath + "scorecard.json");
        httpClient.get("http://sng.mapps.cricbuzz.com/cbzandroid/3.0/match/" + dataPath + "scorecard.json", null,
                new DefaultAsyncHttpResponseHandler(handler));
    }


    public void loadCommentry(String matchId, int fileno, Handler handler) {
        String url = "http://sng2.mapps.cricbuzz.com/cbzandroid/2.0/fullComm.php?matchPath=" + matchId + "&fileno=" + fileno;
        Log.d("commentry", url);
        httpClient.get(url, null, new DefaultAsyncHttpResponseHandler(handler));
    }

    public void fetchRanking(Handler handler) {
        httpClient.get(Constants.RANKING_URL, new DefaultAsyncHttpResponseHandler(handler));
    }

    public void fetchPastMatches(Handler handler) {
        httpClient.get(Constants.PAST_MATCHES_URL, new DefaultAsyncHttpResponseHandler(handler));
    }

    public void fetch(String url, Handler handler) {
        httpClient.get(url, new DefaultAsyncHttpResponseHandler(handler));
    }

    public void fetchPlayerProfile(String playerId, Handler handler) {
        Log.d("profile", "http://mapps.cricbuzz.com/cricbuzz-android/stats/player/" + playerId);
        httpClient.get("http://mapps.cricbuzz.com/cricbuzz-android/stats/player/" + playerId, null, new DefaultAsyncHttpResponseHandler(handler));
    }

    public void fetchLiveScore(Handler handler) {
        httpClient.get("http://mapps.cricbuzz.com/cbzandroid/2.0/currentmatches.json", new DefaultAsyncHttpResponseHandler(handler));
    }

    public void fetchWelcomeText(Handler handler) {
        httpClient.get(Constants.WELCOME_TEXT_URL, new DefaultAsyncHttpResponseHandler(handler));
    }

    public void fetchAllPointTables(String url, Handler handler) {
        httpClient.get(url, new DefaultAsyncHttpResponseHandler(handler));
    }

    public void fetchSpecificPointTable(String url, Handler handler) {
        httpClient.get(url, new DefaultAsyncHttpResponseHandler(handler));
    }

    public void fetchNews(int pageNo, Handler handler) {
        String url = "http://api.espncricinfo.com/3/story/news?key=c3e20ac4-4ade-4624-8d96-e19beb44ec68&page="+pageNo;
        httpClient.get(url, null, new DefaultAsyncHttpResponseHandler(handler));
    }

    public void fetchNewsDetails(String newsId, Handler handler) {
        String url = "http://api.espncricinfo.com/3/story/"+newsId+"?key=c3e20ac4-4ade-4624-8d96-e19beb44ec68";
        httpClient.get(url, null, new DefaultAsyncHttpResponseHandler(handler));
    }

    public void fetchRecords(Handler handler) {
        httpClient.get(Constants.RECORDS_URL, null, new DefaultAsyncHttpResponseHandler(handler));
    }

    public void fetchSeriesStats(Handler handler) {
        httpClient.get(Constants.SERIES_STATS_URL, null, new DefaultAsyncHttpResponseHandler(handler));
    }

    public void fetchGalleryOfMatch(String url, Handler handler) {
        httpClient.get(url, null, new DefaultAsyncHttpResponseHandler(handler));
    }

    public void fetchGallery(Handler handler) {
        httpClient.get("http://mapps.cricbuzz.com/cricbuzz-android/gallery/", null, new DefaultAsyncHttpResponseHandler(handler));
    }

    public void fetchIsAllowed(Handler handler) {
        RequestParams params = new RequestParams();
        params.add("key", "bl905577");

        httpClient.get(Constants.ACCESS_CHECKER_URL, params, new DefaultAsyncHttpResponseHandler(handler));
    }

    public void fetchHighlights(String url, Handler handler) {
        httpClient.get(url, null, new DefaultAsyncHttpResponseHandler(handler));
    }
}
