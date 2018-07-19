package com.allgames.sportslab.model.match;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

import java.io.Serializable;

/**
 * @author ripon
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Match implements Serializable {

    private String matchId;
    private String srs;
    private String datapath;
    private Header header;
    private MiniScore miniScore;
    private Team team1;
    private Team team2;

    public String getMatchId() {
        return matchId;
    }

    public void setMatchId(String matchId) {
        this.matchId = matchId;
    }

    public String getSrs() {
        return srs;
    }

    public void setSrs(String srs) {
        this.srs = srs;
    }

    public String getDatapath() {
        return datapath;
    }

    public void setDatapath(String datapath) {
        this.datapath = datapath;
    }

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public MiniScore getMiniScore() {
        return miniScore;
    }

    @JsonProperty("miniscore")
    public void setMiniScore(MiniScore miniScore) {
        this.miniScore = miniScore;
    }

    public Team getTeam1() {
        return team1;
    }

    public void setTeam1(Team team1) {
        this.team1 = team1;
    }

    public Team getTeam2() {
        return team2;
    }

    public void setTeam2(Team team2) {
        this.team2 = team2;
    }
}
