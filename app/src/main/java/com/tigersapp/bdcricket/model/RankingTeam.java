package com.tigersapp.bdcricket.model;

/**
 * @author Ripon
 */
public class RankingTeam {

    private String rank;
    private String name;
    private String id;
    private String points;
    private String rating;
    private String matches;

    public RankingTeam(String rank, String name, String id, String points, String rating, String matches) {
        this.rank = rank;
        this.name = name;
        this.id = id;
        this.points = points;
        this.rating = rating;
        this.matches = matches;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
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

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
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
                "rank='" + rank + '\'' +
                ", name='" + name + '\'' +
                ", id='" + id + '\'' +
                ", points='" + points + '\'' +
                ", rating='" + rating + '\'' +
                ", matches='" + matches + '\'' +
                '}';
    }
}
