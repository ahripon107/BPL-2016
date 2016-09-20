package com.sfuronlabs.bpl2016.model;

import java.io.Serializable;

/**
 * @author Ripon
 */
public class CricketNews implements Serializable {

    private String author;
    private String link;
    private String pubDate;
    private String title;
    private String description;
    private String newsid;
    private String thumburl;

    public CricketNews(String author, String link, String pubDate, String title, String description, String newsid, String thumburl) {
        this.author = author;
        this.link = link;
        this.pubDate = pubDate;
        this.title = title;
        this.description = description;
        this.newsid = newsid;
        this.thumburl = thumburl;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNewsid() {
        return newsid;
    }

    public void setNewsid(String newsid) {
        this.newsid = newsid;
    }

    public String getThumburl() {
        return thumburl;
    }

    public void setThumburl(String thumburl) {
        this.thumburl = thumburl;
    }

    @Override
    public String toString() {
        return "CricketNews{" +
                "author='" + author + '\'' +
                ", link='" + link + '\'' +
                ", pubDate='" + pubDate + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", newsid='" + newsid + '\'' +
                ", thumburl='" + thumburl + '\'' +
                '}';
    }
}
