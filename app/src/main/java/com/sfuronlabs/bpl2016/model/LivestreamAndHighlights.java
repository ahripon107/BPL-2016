package com.sfuronlabs.bpl2016.model;

/**
 * @author Ripon
 */
public class LivestreamAndHighlights {
    private int id;
    private String title,url,type;

    public LivestreamAndHighlights(int id, String title, String url, String type) {
        this.id = id;
        this.title = title;
        this.url = url;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "LivestreamAndHighlights{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", url='" + url + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
