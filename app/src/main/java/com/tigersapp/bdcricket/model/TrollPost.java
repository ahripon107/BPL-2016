package com.tigersapp.bdcricket.model;

/**
 * Created by Ripon on 8/12/16.
 */
public class TrollPost {

    private int id;
    private String imageurl;
    private String imagetext;
    private String courtesy;

    public TrollPost(int id, String imageurl, String imagetext, String courtesy) {
        this.id = id;
        this.imageurl = imageurl;
        this.imagetext = imagetext;
        this.courtesy = courtesy;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getImagetext() {
        return imagetext;
    }

    public void setImagetext(String imagetext) {
        this.imagetext = imagetext;
    }

    public String getCourtesy() {
        return courtesy;
    }

    public void setCourtesy(String courtesy) {
        this.courtesy = courtesy;
    }

    @Override
    public String toString() {
        return "TrollPost{" +
                "id=" + id +
                ", imageurl='" + imageurl + '\'' +
                ", imagetext='" + imagetext + '\'' +
                ", courtesy='" + courtesy + '\'' +
                '}';
    }
}
