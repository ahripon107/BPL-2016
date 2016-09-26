package com.tigersapp.bdcricket.model;

/**
 * @author Ripon
 */
public class PointsTable {

    private String seriesId;
    private String seriesName;
    private String url;

    public PointsTable(String seriesId, String seriesName, String url) {
        this.seriesId = seriesId;
        this.seriesName = seriesName;
        this.url = url;
    }

    public String getSeriesId() {
        return seriesId;
    }

    public void setSeriesId(String seriesId) {
        this.seriesId = seriesId;
    }

    public String getSeriesName() {
        return seriesName;
    }

    public void setSeriesName(String seriesName) {
        this.seriesName = seriesName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "PointsTable{" +
                "seriesId='" + seriesId + '\'' +
                ", seriesName='" + seriesName + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
