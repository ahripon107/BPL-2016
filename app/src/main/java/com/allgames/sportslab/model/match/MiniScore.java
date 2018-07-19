package com.allgames.sportslab.model.match;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MiniScore implements Serializable {

    private int battingTeamId;
    private String battingTeamScore;
    private int bowlingTeamId;
    private String bowlingTeamScore;
    private String overs;
    private String bowlingTeamOvers;
    private String currentRunRate;
    private String requiredRunRate;
    private String currentPartnership;
    private String previousOvers;

    private MiniBatsman striker;
    private MiniBatsman nonStriker;
    private MiniBowler bowler;
    private MiniBowler nsbowler;

    public int getBattingTeamId() {
        return battingTeamId;
    }

    @JsonProperty("batteamid")
    public void setBattingTeamId(int battingTeamId) {
        this.battingTeamId = battingTeamId;
    }

    public String getBattingTeamScore() {
        return battingTeamScore;
    }

    @JsonProperty("batteamscore")
    public void setBattingTeamScore(String battingTeamScore) {
        this.battingTeamScore = battingTeamScore;
    }

    public int getBowlingTeamId() {
        return bowlingTeamId;
    }

    @JsonProperty("bowlteamid")
    public void setBowlingTeamId(int bowlingTeamId) {
        this.bowlingTeamId = bowlingTeamId;
    }

    public String getBowlingTeamScore() {
        return bowlingTeamScore;
    }

    @JsonProperty("bowlteamscore")
    public void setBowlingTeamScore(String bowlingTeamScore) {
        this.bowlingTeamScore = bowlingTeamScore;
    }

    public String getOvers() {
        return overs;
    }

    @JsonProperty("overs")
    public void setOvers(String overs) {
        this.overs = overs;
    }

    public String getBowlingTeamOvers() {
        return bowlingTeamOvers;
    }

    @JsonProperty("bowlteamovers")
    public void setBowlingTeamOvers(String bowlingTeamOvers) {
        this.bowlingTeamOvers = bowlingTeamOvers;
    }

    public String getCurrentRunRate() {
        return currentRunRate;
    }

    @JsonProperty("crr")
    public void setCurrentRunRate(String currentRunRate) {
        this.currentRunRate = currentRunRate;
    }

    public String getRequiredRunRate() {
        return requiredRunRate;
    }

    @JsonProperty("rrr")
    public void setRequiredRunRate(String requiredRunRate) {
        this.requiredRunRate = requiredRunRate;
    }

    public String getCurrentPartnership() {
        return currentPartnership;
    }

    @JsonProperty("cprtshp")
    public void setCurrentPartnership(String currentPartnership) {
        this.currentPartnership = currentPartnership;
    }

    public String getPreviousOvers() {
        return previousOvers;
    }

    @JsonProperty("prevOvers")
    public void setPreviousOvers(String previousOvers) {
        this.previousOvers = previousOvers;
    }

    public MiniBatsman getStriker() {
        return striker;
    }

    public void setStriker(MiniBatsman striker) {
        this.striker = striker;
    }

    public MiniBatsman getNonStriker() {
        return nonStriker;
    }

    public void setNonStriker(MiniBatsman nonStriker) {
        this.nonStriker = nonStriker;
    }

    public MiniBowler getBowler() {
        return bowler;
    }

    public void setBowler(MiniBowler bowler) {
        this.bowler = bowler;
    }

    public MiniBowler getNsbowler() {
        return nsbowler;
    }

    public void setNsbowler(MiniBowler nsbowler) {
        this.nsbowler = nsbowler;
    }
}
