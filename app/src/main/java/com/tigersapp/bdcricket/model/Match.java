package com.tigersapp.bdcricket.model;

import java.io.Serializable;

/**
 * @author ripon
 */
public class Match implements Serializable {


    private String team1;
    private String team2;
    private String venue;
    private String time;
    private String seriesName;
    private String matchNo;
    private String matchId;

    public Match(String team1, String team2, String venue, String time, String seriesName, String matchNo, String matchId) {
        this.team1 = team1;
        this.team2 = team2;
        this.venue = venue;
        this.time = time;
        this.seriesName = seriesName;
        this.matchNo = matchNo;
        this.matchId = matchId;
    }

    public String getTeam1() {
        return team1;
    }

    public void setTeam1(String team1) {
        this.team1 = team1;
    }

    public String getTeam2() {
        return team2;
    }

    public void setTeam2(String team2) {
        this.team2 = team2;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getSeriesName() {
        return seriesName;
    }

    public void setSeriesName(String seriesName) {
        this.seriesName = seriesName;
    }

    public String getMatchNo() {
        return matchNo;
    }

    public void setMatchNo(String matchNo) {
        this.matchNo = matchNo;
    }

    public String getMatchId() {
        return matchId;
    }

    public void setMatchId(String matchId) {
        this.matchId = matchId;
    }

    @Override
    public String toString() {
        return "Match{" +
                "team1='" + team1 + '\'' +
                ", team2='" + team2 + '\'' +
                ", venue='" + venue + '\'' +
                ", time='" + time + '\'' +
                ", seriesName='" + seriesName + '\'' +
                ", matchNo='" + matchNo + '\'' +
                ", matchId=" + matchId +
                '}';
    }
}
