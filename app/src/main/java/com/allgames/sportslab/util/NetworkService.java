package com.allgames.sportslab.util;

import android.os.Handler;

import com.facebook.Profile;
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

    public void fetchMatchDetails(String matchId, Handler handler) {
        httpClient.get("http://37.187.95.220:8080/cricket-api/api/match/" + matchId, null, new DefaultAsyncHttpResponseHandler(handler));
    }

    public void fetchMatchIdMatcher(String matchId, Handler handler) {
        RequestParams params = new RequestParams();
        params.add("key", "bl905577");
        params.add("cricinfo", matchId);

        httpClient.get("http://apisea.xyz/Cricket/apis/v1/fetchIDMatcher.php", params, new DefaultAsyncHttpResponseHandler(handler));
    }

    public void loadCommentryFromYahoo(String yahooId, Handler handler) {
        String url = "http://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20cricket.commentary%20where%20match_id=" + yahooId + "%20limit%205&format=json&diagnostics=false&env=store%3A%2F%2F0TxIGQMQbObzvU4Apia0V0&callback=";

        httpClient.get(url, null, new DefaultAsyncHttpResponseHandler(handler));
    }

    public void fetchComments(String id, Handler handler) {

        RequestParams requestParams = new RequestParams();
        requestParams.add("key", "bl905577");
        requestParams.add("newsid", id);

        httpClient.get(Constants.FETCH_NEWS_COMMENT_URL, requestParams, new DefaultAsyncHttpResponseHandler(handler));

    }

    public void insertComment(String comment, Profile profile, String id, Handler handler) {
        RequestParams params = new RequestParams();

        params.put("key", "bl905577");
        params.put("newsid", id);
        params.put("name", profile.getName());
        params.put("comment", comment);
        params.put("profileimage", profile.getProfilePictureUri(50, 50).toString());
        params.put("timestamp", System.currentTimeMillis() + "");

        httpClient.post(Constants.INSERT_NEWS_COMMENT_URL, params, new DefaultAsyncHttpResponseHandler(handler));
    }

    public void fetchPlayerProfile(String playerId, Handler handler) {

        httpClient.get("http://cricapi.com/api/playerStats?pid=" + playerId + "&apikey=MScPVINvZoYtOmeNSY7aDVtaa4H2", null, new DefaultAsyncHttpResponseHandler(handler));
    }

    public void fetchPlayerProfileYahoo(String yahooId, Handler handler) {
        String url = "http://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20cricket.player.profile%20where%20player_id=" + yahooId + "&format=json&diagnostics=false&env=store%3A%2F%2F0TxIGQMQbObzvU4Apia0V0&callback=";
        httpClient.get(url, new DefaultAsyncHttpResponseHandler(handler));
    }

    public void fetchLiveScoreSource(Handler handler) {
        RequestParams params = new RequestParams();
        params.add("key", "bl905577");

        httpClient.get(Constants.LIVE_SCORE_SOURCE_URL, params, new DefaultAsyncHttpResponseHandler(handler));
    }

    public void fetchWelcomeText(Handler handler) {
        httpClient.get(Constants.WELCOME_TEXT_URL, new DefaultAsyncHttpResponseHandler(handler));
    }

    public void fetchFixture(Handler handler) {
        httpClient.get(Constants.FIXTURE_URL, new DefaultAsyncHttpResponseHandler(handler));
    }

    public void fetchTriNationSchedule(Handler handler) {
        httpClient.get("http://get.yesdhaka.com/cricket/crickettv/tri_schedule.json", null, new DefaultAsyncHttpResponseHandler(handler));
    }

    public void fetchCTSchedule(Handler handler) {
        httpClient.get("http://get.yesdhaka.com/cricket/champ_schedule.json", null, new DefaultAsyncHttpResponseHandler(handler));
    }

    public void fetchAllPointTables(String url, Handler handler) {
        httpClient.get(url, new DefaultAsyncHttpResponseHandler(handler));
    }

    public void fetchSpecificPointTable(String url, Handler handler) {
        httpClient.get(url, new DefaultAsyncHttpResponseHandler(handler));
    }

    public void fetchBanglanews(Handler handler) {
        httpClient.get("http://www.banglanews24.com/api/category/5", null, new DefaultAsyncHttpResponseHandler(handler));
    }

    public void fetchBdProtidin(Handler handler) {
        httpClient.get("http://www.bd-pratidin.com/api/categorynews/9", null, new DefaultAsyncHttpResponseHandler(handler));
    }

    public void fetchRisingBd(Handler handler) {
        httpClient.get("http://api.risingbd.com/index.php/News?name=Latest&cat_id=3", null, new DefaultAsyncHttpResponseHandler(handler));
    }

    public void fetchProthomAlo(Handler handler) {
        String url = "http://www.prothom-alo.com/api/mobile_api/get_contents?APP_ID=3&start=0&limit=20&content_types=article&pages=216&content_tags=";
        httpClient.addHeader("User-Agent", "Prothom-Alo-MobApp");

        RequestParams requestParams = new RequestParams();
        requestParams.put("APP_KEY", "PAL033B345MOB1L3APP3356D2ALLS3CR3T");

        httpClient.post(url, requestParams, new DefaultAsyncHttpResponseHandler(handler));
    }

    public void fetchRecords(Handler handler) {
        httpClient.get(Constants.RECORDS_URL, null, new DefaultAsyncHttpResponseHandler(handler));
    }

    public void fetchSeriesStats(Handler handler) {
        httpClient.get(Constants.SERIES_STATS_URL, null, new DefaultAsyncHttpResponseHandler(handler));
    }

    public void fetchQuotes(Handler handler) {
        httpClient.get("http://m.cricbuzz.com/cricbuzz-android/quotes", null, new DefaultAsyncHttpResponseHandler(handler));
    }

    public void fetchOpinionComments(String id, Handler handler) {
        RequestParams requestParams = new RequestParams();
        requestParams.add("key", "bl905577");
        requestParams.add("newsid", "opinion" + id);

        httpClient.get(Constants.FETCH_NEWS_COMMENT_URL, requestParams, new DefaultAsyncHttpResponseHandler(handler));
    }

    public void publishOpinionComment(String id, String name, String comment, String image, Handler handler) {
        RequestParams params = new RequestParams();
        params.put("key", "bl905577");
        params.put("newsid", "opinion" + id);
        params.put("name", name);
        params.put("comment", comment);
        params.put("profileimage", image);
        params.put("timestamp", System.currentTimeMillis() + "");

        httpClient.post(Constants.INSERT_NEWS_COMMENT_URL, params, new DefaultAsyncHttpResponseHandler(handler));
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
