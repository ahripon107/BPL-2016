package com.allgames.sportslab.model;

import java.io.Serializable;

/**
 * @author Ripon
 */

class BlogPost implements Serializable {

    private int id;
    private String name;
    private String title;
    private String details;
    private String image;
    private String timestamp;
    private int readtimes;

    public BlogPost(int id, String name, String title, String details, String image, String timestamp) {
        this.id = id;
        this.name = name;
        this.title = title;
        this.details = details;
        this.image = image;
        this.timestamp = timestamp;
    }

    public BlogPost(String name, String title, String details, String image, String timestamp) {
        this.name = name;
        this.title = title;
        this.details = details;
        this.image = image;
        this.timestamp = timestamp;
        this.readtimes = readtimes;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public int getReadtimes() {
        return readtimes;
    }

    public void setReadtimes(int readtimes) {
        this.readtimes = readtimes;
    }

    @Override
    public String toString() {
        return "BlogPost{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", title='" + title + '\'' +
                ", details='" + details + '\'' +
                ", image='" + image + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", readtimes=" + readtimes +
                '}';
    }
}
