package com.allgames.sportslab.model;

/**
 * @author Ripon
 */

public class Gallery {

    private String name;
    private String url;
    private String date;
    private String img;

    public Gallery(String name, String url, String date, String img) {
        this.name = name;
        this.url = url;
        this.date = date;
        this.img = img;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    @Override
    public String toString() {
        return "Gallery{" +
                "name='" + name + '\'' +
                ", url='" + url + '\'' +
                ", date='" + date + '\'' +
                ", img='" + img + '\'' +
                '}';
    }
}
