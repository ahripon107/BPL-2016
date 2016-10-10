package com.tigersapp.bdcricket.model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Ripon
 */

public class BuzzFeed {

    private String sender;

    private long timeInMillis;

    private String feed;

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getTime() {
        //DateFormat dateFormat = new SimpleDateFormat("HH:MM dd-MM-yy");
        DateFormat dateFormat = new SimpleDateFormat("HH:mm yyyy-MM-dd ");
        return dateFormat.format(new Date(timeInMillis));
    }

    public void setTimeInMillis(long timeInMillis) {
        this.timeInMillis = timeInMillis;
    }

    public String getFeed() {
        return feed;
    }

    public void setFeed(String feed) {
        this.feed = feed;
    }
}
