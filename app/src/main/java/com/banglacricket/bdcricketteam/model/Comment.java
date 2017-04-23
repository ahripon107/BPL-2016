package com.banglacricket.bdcricketteam.model;

/**
 * @author Ripon
 */

public class Comment {

    private String name;
    private String comment;
    private String profileimage;
    private String timestamp;

    public Comment(String name, String comment, String profileimage, String timestamp) {
        this.name = name;
        this.comment = comment;
        this.profileimage = profileimage;
        this.timestamp = timestamp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getProfileimage() {
        return profileimage;
    }

    public void setProfileimage(String profileimage) {
        this.profileimage = profileimage;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "name='" + name + '\'' +
                ", comment='" + comment + '\'' +
                ", timestamp='" + timestamp + '\'' +
                '}';
    }
}
