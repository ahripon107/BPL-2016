package com.tigersapp.bdcricket.model;

/**
 * @author Ripon
 */
public class Batsman {

    private String playerId;
    private String name;
    private String out;
    private String runs, balls, fours, sixes,sr;

    public Batsman(String playerId, String name, String out, String runs, String balls, String fours, String sixes, String sr) {
        this.playerId = playerId;
        this.name = name;
        this.out = out;
        this.runs = runs;
        this.balls = balls;
        this.fours = fours;
        this.sixes = sixes;
        this.sr = sr;
    }

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOut() {
        return out;
    }

    public void setOut(String out) {
        this.out = out;
    }

    public String getRuns() {
        return runs;
    }

    public void setRuns(String runs) {
        this.runs = runs;
    }

    public String getBalls() {
        return balls;
    }

    public void setBalls(String balls) {
        this.balls = balls;
    }

    public String getFours() {
        return fours;
    }

    public void setFours(String fours) {
        this.fours = fours;
    }

    public String getSixes() {
        return sixes;
    }

    public void setSixes(String sixes) {
        this.sixes = sixes;
    }

    public String getSr() {
        return sr;
    }

    public void setSr(String sr) {
        this.sr = sr;
    }
}
