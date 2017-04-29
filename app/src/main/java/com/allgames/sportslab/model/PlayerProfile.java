package com.allgames.sportslab.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author Ripon
 */
public class PlayerProfile implements Serializable{
    String Profile;
    Style style;
    ArrayList<MatchStats> matchStatses;

    public PlayerProfile(String profile, Style style, ArrayList<MatchStats> matchStatses) {
        this.Profile = profile;
        this.style = style;
        this.matchStatses = matchStatses;
    }

    public String getProfile() {
        return Profile;
    }

    public void setProfile(String profile) {
        Profile = profile;
    }

    public Style getStyle() {
        return style;
    }

    public void setStyle(Style style) {
        this.style = style;
    }

    public ArrayList<MatchStats> getMatchStatses() {
        return matchStatses;
    }

    public void setMatchStatses(ArrayList<MatchStats> matchStatses) {
        this.matchStatses = matchStatses;
    }

    class Style {
        String Batting, Bowling;

        public Style(String batting, String bowling) {
            Batting = batting;
            Bowling = bowling;
        }

        public String getBatting() {
            return Batting;
        }

        public void setBatting(String batting) {
            Batting = batting;
        }

        public String getBowling() {
            return Bowling;
        }

        public void setBowling(String bowling) {
            Bowling = bowling;
        }
    }
    class Batting {
        String Matches,Innings,NotOuts,Runs,Average,StrikeRate,Hundreds,Fifties,Fours,Sixes;

        public Batting(String matches, String innings, String notOuts, String runs, String average, String strikeRate, String hundreds, String fifties, String fours, String sixes) {
            Matches = matches;
            Innings = innings;
            NotOuts = notOuts;
            Runs = runs;
            Average = average;
            StrikeRate = strikeRate;
            Hundreds = hundreds;
            Fifties = fifties;
            Fours = fours;
            Sixes = sixes;
        }
    }
    class Bowling{
        String Matches,Innings,Balls,Runs,Wickets,Average,EconomyRate,FourWickets,FiveWickets,TenWickets;

        public Bowling(String matches, String innings, String balls, String runs, String wickets, String average, String economyRate, String fourWickets, String fiveWickets, String tenWickets) {
            Matches = matches;
            Innings = innings;
            Balls = balls;
            Runs = runs;
            Wickets = wickets;
            Average = average;
            EconomyRate = economyRate;
            FourWickets = fourWickets;
            FiveWickets = fiveWickets;
            TenWickets = tenWickets;
        }
    }
    class MatchStats {
        Batting batting;
        Bowling bowling;

        public MatchStats(Batting batting, Bowling bowling) {
            this.batting = batting;
            this.bowling = bowling;
        }
    }

    @Override
    public String toString() {
        return "PlayerProfile{" +
                "Profile='" + Profile + '\'' +
                ", style=" + style +
                ", matchStatses=" + matchStatses +
                '}';
    }
}
