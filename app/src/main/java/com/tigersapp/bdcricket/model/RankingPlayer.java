package com.tigersapp.bdcricket.model;

/**
 * @author Ripon
 */
public class RankingPlayer {

    private String id;
    private String name;
    private String country;
    private String country_id;
    private String rank;
    private String rating;
    private String avg;

    public RankingPlayer(String id, String name, String country, String country_id, String rank, String rating, String avg) {
        this.id = id;
        this.name = name;
        this.country = country;
        this.rank = rank;
        this.rating = rating;
        this.country_id = country_id;
        this.avg = avg;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCountry_id() {
        return country_id;
    }

    public void setCountry_id(String country_id) {
        this.country_id = country_id;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getAvg() {
        return avg;
    }

    public void setAvg(String avg) {
        this.avg = avg;
    }

    @Override
    public String toString() {
        return "RankingPlayer{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", country='" + country + '\'' +
                ", country_id='" + country_id + '\'' +
                ", rank='" + rank + '\'' +
                ", rating='" + rating + '\'' +
                ", avg='" + avg + '\'' +
                '}';
    }
}
