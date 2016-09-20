package com.sfuronlabs.bpl2016.model;

/**
 * Created by amin on 8/24/16.
 */
public class Summary {

    private String tournament;
    private String team1;
    private String team2;
    private String ground;
    private String info;
    private String matchStatus;

    public Summary(String tournament, String team1, String team2, String ground, String info, String matchStatus) {
        this.tournament = tournament;
        this.team1 = team1;
        this.team2 = team2;
        this.ground = ground;
        this.info = info;
        this.matchStatus = matchStatus;
    }

    public String getTournament() {
        return tournament;
    }

    public void setTournament(String tournament) {
        this.tournament = tournament;
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

    public String getGround() {
        return ground;
    }

    public void setGround(String ground) {
        this.ground = ground;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getMatchStatus() {
        return matchStatus;
    }

    public void setMatchStatus(String matchStatus) {
        this.matchStatus = matchStatus;
    }

    @Override
    public String toString() {
        return "Summary{" +
                "tournament='" + tournament + '\'' +
                ", team1='" + team1 + '\'' +
                ", team2='" + team2 + '\'' +
                ", ground='" + ground + '\'' +
                ", info='" + info + '\'' +
                ", matchStatus='" + matchStatus + '\'' +
                '}';
    }
}
