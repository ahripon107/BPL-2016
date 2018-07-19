package com.allgames.sportslab.util;

/**
 * @author ripon
 */
public class Constants {

    public static final int SUCCESS = 1;
    public static final int FAILURE = 2;

    public static final String SOLAIMAN_LIPI_FONT = "fonts/solaimanlipi.ttf";
    public static final String TIMES_NEW_ROMAN_FONT = "fonts/timesnewroman.ttf";
    public static final String ONE_PLUS_TEST_DEVICE = "7D3F3DF2A7214E839DBE70BE2132D5B9";
    //public static final String XIAOMI_TEST_DEVICE = "EE613118FFB77F457D6DBDAC46C3658C";
    public static final String XIAOMI_TEST_DEVICE = "F5C90B562D482744906FD29363CF595C";
    public static final String PAST_MATCHES_URL = "http://mapps.cricbuzz.com/cbzandroid/2.0/archivematches.json";
    public static final String RANKING_URL = "http://mapps.cricbuzz.com/cbzvernacular/bengali/stats/rankings";
    public static final String POINT_TABLE_URL = "http://mapps.cricbuzz.com/cricbuzz-android/series/points_table";
    public static final String RECORDS_URL = "http://opera.m.cricbuzz.com/cbzandroid/top-stats";
    public static final String SERIES_STATS_URL = "http://opera.m.cricbuzz.com/cbzandroid/series-stats";
    public static final String FACE_IMAGE = "http://mapps.cricbuzz.com/stats/img/faceImages/";
    public static final String TEAM_IMAGE_FIRST_PART = "http://sng.mapps.cricbuzz.com/cbzandroid/2.0/flags/team_";
    public static final String TEAM_IMAGE_LAST_PART = "_50.png";
    public static final String TAG = "ripons";
    public static final String ACCESS_CHECKER_URL = "http://apisea.xyz/CricketLiveLatest/apis/v1/accessChecker.php";
    public static final String WELCOME_TEXT_URL = "http://apisea.xyz/live2018/api/v1/welcometext.php?key=bl905577";
    public static String SHOW_PLAYER_IMAGE = "false";

    public static String resolveLogo(int teamId) {
        if (SHOW_PLAYER_IMAGE.equals("true")) {
            return "http://i.cricketcb.com/i/stats/flags/web/official_flags/team_" + teamId + ".png";
        } else {
            return "xyz";
        }
    }
}
