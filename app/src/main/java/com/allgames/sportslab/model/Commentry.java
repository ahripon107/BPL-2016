package com.allgames.sportslab.model;

/**
 * @author Ripon
 */

public class Commentry {

    private String type;
    private String event;
    private String over;
    private String commentr;

    public Commentry(String type, String event, String over, String commentr) {
        this.type = type;
        this.event = event;
        this.over = over;
        this.commentr = commentr;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getOver() {
        return over;
    }

    public void setOver(String over) {
        this.over = over;
    }

    public String getCommentr() {
        return commentr;
    }

    public void setCommentr(String commentr) {
        this.commentr = commentr;
    }

    @Override
    public String toString() {
        return "Commentry{" +
                "type='" + type + '\'' +
                ", event='" + event + '\'' +
                ", over='" + over + '\'' +
                ", commentr='" + commentr + '\'' +
                '}';
    }
}
