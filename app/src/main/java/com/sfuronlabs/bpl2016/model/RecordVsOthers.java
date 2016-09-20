package com.sfuronlabs.bpl2016.model;

/**
 * Created by Ripon on 12/16/15.
 */
public class RecordVsOthers {

    String against;
    String played;
    String wins;
    String loss;
    String draw;
    String highestInnings;
    String bestBBI;
    String bestInning;
    String maxWkts;
    String maxRuns;

    public RecordVsOthers(String against, String played, String wins, String loss, String draw, String highestInnings, String bestBBI, String bestInning, String maxWkts, String maxRuns) {
        this.against = against;
        this.played = played;
        this.wins = wins;
        this.loss = loss;
        this.draw = draw;
        this.highestInnings = highestInnings;
        this.bestBBI = bestBBI;
        this.bestInning = bestInning;
        this.maxWkts = maxWkts;
        this.maxRuns = maxRuns;
    }

    public String getAgainst() {
        return against;
    }

    public void setAgainst(String against) {
        this.against = against;
    }

    public String getPlayed() {
        return played;
    }

    public void setPlayed(String played) {
        this.played = played;
    }

    public String getWins() {
        return wins;
    }

    public void setWins(String wins) {
        this.wins = wins;
    }

    public String getLoss() {
        return loss;
    }

    public void setLoss(String loss) {
        this.loss = loss;
    }

    public String getDraw() {
        return draw;
    }

    public void setDraw(String draw) {
        this.draw = draw;
    }

    public String getHighestInnings() {
        return highestInnings;
    }

    public void setHighestInnings(String highestInnings) {
        this.highestInnings = highestInnings;
    }

    public String getBestBBI() {
        return bestBBI;
    }

    public void setBestBBI(String bestBBI) {
        this.bestBBI = bestBBI;
    }

    public String getBestInning() {
        return bestInning;
    }

    public void setBestInning(String bestInning) {
        this.bestInning = bestInning;
    }

    public String getMaxWkts() {
        return maxWkts;
    }

    public void setMaxWkts(String maxWkts) {
        this.maxWkts = maxWkts;
    }

    public String getMaxRuns() {
        return maxRuns;
    }

    public void setMaxRuns(String maxRuns) {
        this.maxRuns = maxRuns;
    }

    @Override
    public String toString() {
        return "RecordVsOthers{" +
                "against='" + against + '\'' +
                ", played='" + played + '\'' +
                ", wins='" + wins + '\'' +
                ", loss='" + loss + '\'' +
                ", draw='" + draw + '\'' +
                ", highestInnings='" + highestInnings + '\'' +
                ", bestBBI='" + bestBBI + '\'' +
                ", bestInning='" + bestInning + '\'' +
                ", maxWkts='" + maxWkts + '\'' +
                ", maxRuns='" + maxRuns + '\'' +
                '}';
    }
}
