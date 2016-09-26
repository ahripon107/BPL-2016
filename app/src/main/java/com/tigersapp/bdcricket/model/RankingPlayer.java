package com.tigersapp.bdcricket.model;

/**
 * @author Ripon
 */
public class RankingPlayer {

    private String id;
    private String name;
    private String country;
    private String countryId;
    private String currentRank;
    private String currentRating;
    private String avg;

    public RankingPlayer(String id, String name, String country, String countryId, String currentRank, String currentRating, String avg) {
        this.id = id;
        this.name = name;
        this.country = country;
        this.countryId = countryId;
        this.currentRank = currentRank;
        this.currentRating = currentRating;
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

    public String getCountryId() {
        return countryId;
    }

    public void setCountryId(String countryId) {
        this.countryId = countryId;
    }

    public String getCurrentRank() {
        return currentRank;
    }

    public void setCurrentRank(String currentRank) {
        this.currentRank = currentRank;
    }

    public String getCurrentRating() {
        return currentRating;
    }

    public void setCurrentRating(String currentRating) {
        this.currentRating = currentRating;
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
                ", countryId='" + countryId + '\'' +
                ", currentRank='" + currentRank + '\'' +
                ", currentRating='" + currentRating + '\'' +
                ", avg='" + avg + '\'' +
                '}';
    }
}
