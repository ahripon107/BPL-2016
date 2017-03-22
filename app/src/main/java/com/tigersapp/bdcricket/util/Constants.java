package com.tigersapp.bdcricket.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author ripon
 */
public class Constants {

    public static final String KEY = "key";
    public static final String KEY_VALUE = "bl905577";
    public static final String SOLAIMAN_LIPI_FONT = "fonts/solaimanlipi.ttf";
    public static final String TIMES_NEW_ROMAN_FONT = "fonts/timesnewroman.ttf";
    public static final String ONE_PLUS_TEST_DEVICE = "7D3F3DF2A7214E839DBE70BE2132D5B9";
    //public static final String XIAOMI_TEST_DEVICE = "EE613118FFB77F457D6DBDAC46C3658C";
    public static final String XIAOMI_TEST_DEVICE = "F5C90B562D482744906FD29363CF595C";
    public static final String NEWS_URL = "http://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20cricket.news(0,50)%20where%20region=%22IN%22&format=json&diagnostics=false&env=store%3A%2F%2F0TxIGQMQbObzvU4Apia0V0&callback=";
    public static final String FIXTURE_URL = "http://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20cricket.upcoming_matches(0,50)&format=json&diagnostics=false&env=store%3A%2F%2F0TxIGQMQbObzvU4Apia0V0&callback=";
    public static final String COMMENTARY_URL = "http://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20cricket.commentary%20where%20match_id=193958%20limit%205&format=json&diagnostics=false&env=store%3A%2F%2F0TxIGQMQbObzvU4Apia0V0&callback=";
    public static final String PAST_MATCHES_URL = "http://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20cricket.past_matches%20(0,30)&format=json&diagnostics=false&env=store%3A%2F%2F0TxIGQMQbObzvU4Apia0V0&callback=";
    public static final String RANKING_URL = "http://mapps.cricbuzz.com/cbzvernacular/bengali/stats/rankings";
    public static final String POINT_TABLE_URL = "http://mapps.cricbuzz.com/cricbuzz-android/series/points_table";
    public static final String RECORDS_URL = "http://opera.m.cricbuzz.com/cbzandroid/top-stats";
    public static final String SERIES_STATS_URL = "http://opera.m.cricbuzz.com/cbzandroid/series-stats";
    public static final String FACE_IMAGE = "http://mapps.cricbuzz.com/stats/img/faceImages/";
    public static final String TEAM_IMAGE_FIRST_PART = "http://sng.mapps.cricbuzz.com/cbzandroid/2.0/flags/team_";
    public static final String TEAM_IMAGE_LAST_PART = "_50.png";
    public static final String TAG = "ripons";

    public static String SHOW_PLAYER_IMAGE = "false";
    public static String QUIZ_ID = "1";

    public static final String ACCESS_CHECKER_URL = "http://apisea.xyz/BPL2016/apis/v5/accessChecker.php";
    public static final String OPINION_QUES_URL = "http://apisea.xyz/BPL2016/apis/v4/fetchOpinionQuestions.php";

    public static final String WELCOME_TEXT_URL = "http://apisea.xyz/BPL2016/apis/v5/welcometext.php?key=bl905577";

    public static final String AUS_TEAM_LOGO_URL = "https://s.yimg.com/qx/cricket/fufp/images/1_l-11-2-2012-975ee512e59ffc420e26dee2813daa27.png";
    public static final String BD_TEAM_LOGO_URL = "https://s.yimg.com/qx/cricket/fufp/images/2_l-11-2-2012-a853bb5b43497e2b3a38791dc389adc0.png";
    public static final String ENG_TEAM_LOGO_URL = "https://s.yimg.com/qx/cricket/fufp/images/3_l-11-2-2012-5593f23256a4c5327fda1729a771f8ff.png";
    public static final String IND_TEAm_LOGO_URL = "https://s.yimg.com/qx/cricket/fufp/images/4_l-11-2-2012-b8ce58599a586b164189b090b45c07e1.png";
    public static final String NZ_TEAM_LOGO_URL = "https://s.yimg.com/qx/cricket/fufp/images/5_l-11-2-2012-78e43d8e16ee76e5f28da1a679de2eff.png";
    public static final String PAK_TEAM_LOGO_URL = "https://s.yimg.com/qx/cricket/fufp/images/6_l-11-2-2012-472fc93551b80e5564b9fd0355191a4e.png";
    public static final String SA_TEAM_LOGO_URL = "https://s.yimg.com/qx/cricket/fufp/images/7_l-11-2-2012-6ffcebebcba7787554927c94de0b3109.png";
    public static final String SL_TEAM_LOGO_URL = "https://s.yimg.com/qx/cricket/fufp/images/8_l-11-2-2012-42596125ceefa9b3960e6231785d0ab6.png";
    public static final String WI_TEAM_LOGO_URL = "https://s.yimg.com/qx/cricket/fufp/images/9_l-11-2-2012-da7e8c6fe82f0fa6ec7ee4d3051d3ba6.png";
    public static final String ZIM_TEAM_LOGO_URL = "https://s.yimg.com/qx/cricket/fufp/images/10_l-11-2-2012-4dc0861b3616d2c3ad05e5e7e94207df.png";
    public static final String BER_TEAM_LOGO_URL = "https://s.yimg.com/qx/cricket/fufp/images/11_l-11-2-2012-6ecde77190ebd426fbe58ba1831c46e0.png";
    public static final String CAN_TEAM_LOGO_URL = "https://s.yimg.com/qx/cricket/fufp/images/12_l-11-2-2012-20696bbe027266dca5a9179724632ee5.png";
    public static final String IRE_TEAM_LOGO_URL = "https://s.yimg.com/qx/cricket/fufp/images/13_l-11-2-2012-82d89a321ceca0fdb9f33b33efe5ba7c.png";
    public static final String KEN_TEAM_LOGO_URL = "https://s.yimg.com/qx/cricket/fufp/images/14_l-11-2-2012-c0c6bb390d8b680ff18a6198d3a239ef.png";
    public static final String NED_TEAM_LOGO_URL = "https://s.yimg.com/qx/cricket/fufp/images/15_l-11-2-2012-1d31b4506a132712ed228b4b62d4b81c.png";
    public static final String SCO_TEAM_LOGO_URL = "https://s.yimg.com/qx/cricket/fufp/images/16_l-11-2-2012-c7bedaa78b23b63de75b4cfc7533e0ae.png";

    public static final String CRICBUZZ_LIVE_MATCHES = "http://mapps.cricbuzz.com/cbzandroid/2.0/currentmatches.json";

    public static final String INSERT_NEWS_COMMENT_URL = "http://apisea.xyz/BPL2016/apis/v6/insertnewscomment.php";
    public static final String FETCH_NEWS_COMMENT_URL = "http://apisea.xyz/BPL2016/apis/v6/fetchnewscomments.php";
    public static final String FETCH_QUIZ_QUESTIONS = "http://apisea.xyz/BPL2016/apis/v4/fetchQuizQuestions.php";
    public static final String INSERT_QUIZ_MARKS = "http://apisea.xyz/BPL2016/apis/v4/insertQuizNumber.php";

    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final int DAY_MILLIS = 24 * HOUR_MILLIS;

    public static String getTimeAgo(long time) {
        if (time < 1000000000000L) {
            time *= 1000;
        }

        long now = System.currentTimeMillis();
        if (time > now || time <= 0) {
            return null;
        }

        final long diff = now - time;
        if (diff < MINUTE_MILLIS) {
            return "Just now";
        } else if (diff < 2 * MINUTE_MILLIS) {
            return "a minute ago";
        } else if (diff < 50 * MINUTE_MILLIS) {
            return diff / MINUTE_MILLIS + " minutes ago";
        } else if (diff < 90 * MINUTE_MILLIS) {
            return "an hour ago";
        } else if (diff < 119 * MINUTE_MILLIS) {
            return "1 hour ago";
        } else if (diff < 24 * HOUR_MILLIS) {
            return diff / HOUR_MILLIS + " hours ago";
        } else if (diff < 48 * HOUR_MILLIS) {
            return "Yesterday";
        } else {
            return diff / DAY_MILLIS + " days ago";
        }
    }

    public static String timestamp(String dateAndTime) {
        String arr[] = dateAndTime.split("T");
        String date = arr[0];
        String arr1[] = arr[1].split("-");
        String time = arr1[0];
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd.HH:mm:ss");
        String timeAgo = "";
        try {
            Date date1 = dateFormat.parse(date+"."+time);
            long time1 = date1.getTime();
            timeAgo = getTimeAgo(time1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timeAgo;
    }

    public static String resolveLogo(String teamName) {
        if (teamName.contains("Australia"))
            return Constants.AUS_TEAM_LOGO_URL;
        else if (teamName.contains("Bangladesh"))
            return Constants.BD_TEAM_LOGO_URL;
        else if (teamName.contains("England"))
            return Constants.ENG_TEAM_LOGO_URL;
        else if (teamName.contains("India"))
            return Constants.IND_TEAm_LOGO_URL;
        else if (teamName.contains("New Zealand"))
            return Constants.NZ_TEAM_LOGO_URL;
        else if (teamName.contains("Pakistan"))
            return Constants.PAK_TEAM_LOGO_URL;
        else if (teamName.contains("South Africa"))
            return Constants.SA_TEAM_LOGO_URL;
        else if (teamName.contains("Sri Lanka"))
            return Constants.SL_TEAM_LOGO_URL;
        else if (teamName.contains("West Indies"))
            return Constants.WI_TEAM_LOGO_URL;
        else if (teamName.contains("Zimbabwe"))
            return Constants.ZIM_TEAM_LOGO_URL;
        else if (teamName.contains("Bermuda"))
            return Constants.BER_TEAM_LOGO_URL;
        else if (teamName.contains("Canada"))
            return Constants.CAN_TEAM_LOGO_URL;
        else if (teamName.contains("Ireland"))
            return Constants.IRE_TEAM_LOGO_URL;
        else if (teamName.contains("Kenya"))
            return Constants.KEN_TEAM_LOGO_URL;
        else if (teamName.contains("Netherland"))
            return Constants.NED_TEAM_LOGO_URL;
        else if (teamName.contains("Scotland"))
            return Constants.SCO_TEAM_LOGO_URL;
        else return "n";
    }
}
