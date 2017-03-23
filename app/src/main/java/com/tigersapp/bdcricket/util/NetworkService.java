package com.tigersapp.bdcricket.util;

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
        String url = "http://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20cricket.commentary%20where%20match_id="+yahooId+"%20limit%205&format=json&diagnostics=false&env=store%3A%2F%2F0TxIGQMQbObzvU4Apia0V0&callback=";

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
        params.put("profileimage", profile.getProfilePictureUri(50,50).toString());
        params.put("timestamp", System.currentTimeMillis() + "");

        httpClient.post(Constants.INSERT_NEWS_COMMENT_URL, params, new DefaultAsyncHttpResponseHandler(handler));
    }
}
