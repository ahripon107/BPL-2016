package com.tigersapp.bdcricket.model;

/**
 * @author Ripon
 */
public class Bowler {

    private String playerId,name, over, maiden, run, wicket;

    public Bowler(String playerId,String name, String over, String maiden, String run, String wicket) {
        this.playerId = playerId;
        this.name = name;
        this.over = over;
        this.maiden = maiden;
        this.run = run;
        this.wicket = wicket;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOver() {
        return over;
    }

    public void setOver(String over) {
        this.over = over;
    }

    public String getMaiden() {
        return maiden;
    }

    public void setMaiden(String maiden) {
        this.maiden = maiden;
    }

    public String getRun() {
        return run;
    }

    public void setRun(String run) {
        this.run = run;
    }

    public String getWicket() {
        return wicket;
    }

    public void setWicket(String wicket) {
        this.wicket = wicket;
    }

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    @Override
    public String toString() {
        return "Bowler{" +
                "playerId='" + playerId + '\'' +
                ", name='" + name + '\'' +
                ", over='" + over + '\'' +
                ", maiden='" + maiden + '\'' +
                ", run='" + run + '\'' +
                ", wicket='" + wicket + '\'' +
                '}';
    }
}
