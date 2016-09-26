package com.tigersapp.bdcricket.model;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * @author Ripon
 */
public class CricketNews implements Serializable,Comparable {

    private String id;
    private String imageUrl;
    private String detailNewsUrl;
    private String title;
    private String date;
    private String source;
    private String sourceBangla;

    public CricketNews(String id, String imageUrl, String detailNewsUrl, String title, String date, String source, String sourceBangla) {
        this.id = id;
        this.imageUrl = imageUrl;
        this.detailNewsUrl = detailNewsUrl;
        this.title = title;
        this.date = date;
        this.source = source;
        this.sourceBangla = sourceBangla;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDetailNewsUrl() {
        return detailNewsUrl;
    }

    public void setDetailNewsUrl(String detailNewsUrl) {
        this.detailNewsUrl = detailNewsUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getSourceBangla() {
        return sourceBangla;
    }

    public void setSourceBangla(String sourceBangla) {
        this.sourceBangla = sourceBangla;
    }

    @Override
    public String toString() {
        return "CricketNews{" +
                "id='" + id + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", detailNewsUrl='" + detailNewsUrl + '\'' +
                ", title='" + title + '\'' +
                ", date='" + date + '\'' +
                ", source='" + source + '\'' +
                ", sourceBangla='" + sourceBangla + '\'' +
                '}';
    }

    @Override
    public int compareTo(Object another) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            long date1 = dateFormat.parse(date).getTime();
            long date2 = dateFormat.parse(((CricketNews)another).getDate()).getTime();
            if (date1<date2) return 1;
            else if (date1>date2) return -1;
            else return 0;

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
