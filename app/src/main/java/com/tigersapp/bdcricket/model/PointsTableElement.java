package com.tigersapp.bdcricket.model;

/**
 * @author Ripon
 */
public class PointsTableElement {

    private String teamName;
    private String played;
    private String won;
    private String lost;
    private String noresults;
    private String pointsscored;
    private String runrate;

    public PointsTableElement(String teamName, String played, String won, String lost, String noresults, String pointsscored, String runrate) {
        this.teamName = teamName;
        this.played = played;
        this.won = won;
        this.lost = lost;
        this.noresults = noresults;
        this.pointsscored = pointsscored;
        this.runrate = runrate;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getPlayed() {
        return played;
    }

    public void setPlayed(String played) {
        this.played = played;
    }

    public String getWon() {
        return won;
    }

    public void setWon(String won) {
        this.won = won;
    }

    public String getLost() {
        return lost;
    }

    public void setLost(String lost) {
        this.lost = lost;
    }

    public String getNoresults() {
        return noresults;
    }

    public void setNoresults(String noresults) {
        this.noresults = noresults;
    }

    public String getPointsscored() {
        return pointsscored;
    }

    public void setPointsscored(String pointsscored) {
        this.pointsscored = pointsscored;
    }

    public String getRunrate() {
        return runrate;
    }

    public void setRunrate(String runrate) {
        this.runrate = runrate;
    }

    @Override
    public String toString() {
        return "PointsTableElement{" +
                "teamName='" + teamName + '\'' +
                ", played='" + played + '\'' +
                ", won='" + won + '\'' +
                ", lost='" + lost + '\'' +
                ", noresults='" + noresults + '\'' +
                ", pointsscored='" + pointsscored + '\'' +
                ", runrate='" + runrate + '\'' +
                '}';
    }
}
