package com.allgames.sportslab.model.match;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Header implements Serializable{

    private String startDate;
    private String startTime;
    private String startTimeGMT;
    private String endDate;
    private String matchNumber;
    private String matchDescription;
    private String matchState;
    private String tossWon;
    private String decision;
    private String ground;
    private String city;
    private String country;
    private String status;
    private String manOfTheMatch;
    private String numberOfInnings;

    public String getStartDate() {
        return startDate;
    }

    @JsonProperty("startdt")
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getStartTime() {
        return startTime;
    }

    @JsonProperty("stTme")
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getStartTimeGMT() {
        return startTimeGMT;
    }

    @JsonProperty("stTmeGMT")
    public void setStartTimeGMT(String startTimeGMT) {
        this.startTimeGMT = startTimeGMT;
    }

    public String getEndDate() {
        return endDate;
    }

    @JsonProperty("enddt")
    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getMatchNumber() {
        return matchNumber;
    }

    @JsonProperty("mnum")
    public void setMatchNumber(String matchNumber) {
        this.matchNumber = matchNumber;
    }

    public String getMatchDescription() {
        return matchDescription;
    }

    @JsonProperty("mchDesc")
    public void setMatchDescription(String matchDescription) {
        this.matchDescription = matchDescription;
    }


    public String getMatchState() {
        return matchState;
    }

    @JsonProperty("mchState")
    public void setMatchState(String matchState) {
        this.matchState = matchState;
    }

    public String getTossWon() {
        return tossWon;
    }

    @JsonProperty("TW")
    public void setTossWon(String tossWon) {
        this.tossWon = tossWon;
    }

    public String getDecision() {
        return decision;
    }

    @JsonProperty("decisn")
    public void setDecision(String decision) {
        this.decision = decision;
    }

    public String getGround() {
        return ground;
    }

    @JsonProperty("grnd")
    public void setGround(String ground) {
        this.ground = ground;
    }

    public String getCity() {
        return city;
    }

    @JsonProperty("vcity")
    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    @JsonProperty("vcountry")
    public void setCountry(String country) {
        this.country = country;
    }

    public String getStatus() {
        return status;
    }

    @JsonProperty("status")
    public void setStatus(String status) {
        this.status = status;
    }

    public String getManOfTheMatch() {
        return manOfTheMatch;
    }

    @JsonProperty("MOM")
    public void setManOfTheMatch(String manOfTheMatch) {
        this.manOfTheMatch = manOfTheMatch;
    }

    public String getNumberOfInnings() {
        return numberOfInnings;
    }

    @JsonProperty("NoOfIngs")
    public void setNumberOfInnings(String numberOfInnings) {
        this.numberOfInnings = numberOfInnings;
    }
}
