package com.banglacricket.bdcricketteam.model;

/**
 * @author Ripon
 */
public class RecordModel {

    private String type;
    private String header;
    private String url;

    public RecordModel(String type, String header, String url) {
        this.type = type;
        this.header = header;
        this.url = url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "RecordModel{" +
                "type='" + type + '\'' +
                ", header='" + header + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
