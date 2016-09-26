package com.tigersapp.bdcricket.model;

/**
 * @author Ripon
 */
public class RankingTeam {

    private String currentRank;
    private String name;
    private String id;
    private String currentPoints;
    private String currentRating;
    private String matches;

    public RankingTeam(String currentRank, String name, String id, String currentPoints, String currentRating, String matches) {
        this.currentRank = currentRank;
        this.name = name;
        this.id = id;
        this.currentPoints = currentPoints;
        this.currentRating = currentRating;
        this.matches = matches;
    }

    public String getCurrentRank() {
        return currentRank;
    }

    public void setCurrentRank(String currentRank) {
        this.currentRank = currentRank;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCurrentPoints() {
        return currentPoints;
    }

    public void setCurrentPoints(String currentPoints) {
        this.currentPoints = currentPoints;
    }

    public String getCurrentRating() {
        return currentRating;
    }

    public void setCurrentRating(String currentRating) {
        this.currentRating = currentRating;
    }

    public String getMatches() {
        return matches;
    }

    public void setMatches(String matches) {
        this.matches = matches;
    }

    @Override
    public String toString() {
        return "RankingTeam{" +
                "currentRank='" + currentRank + '\'' +
                ", name='" + name + '\'' +
                ", id='" + id + '\'' +
                ", currentPoints='" + currentPoints + '\'' +
                ", currentRating='" + currentRating + '\'' +
                ", matches='" + matches + '\'' +
                '}';
    }
}
