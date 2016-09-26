package com.tigersapp.bdcricket.model;

/**
 * @author Ripon
 */
public class Player {

    private String name;
    private String picname;
    private String personid;

    public Player(String name, String picname,String personid) {
        this.name = name;
        this.picname = picname;
        this.personid = personid;
    }

    public String getName() {
        return name;
    }

    public String getPicname() {
        return picname;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPicname(String picname) {
        this.picname = picname;
    }

    public String getPersonid() {
        return personid;
    }

    public void setPersonid(String personid) {
        this.personid = personid;
    }

    @Override
    public String toString() {
        return "Player{" +
                "name='" + name + '\'' +
                ", picname='" + picname + '\'' +
                ", personid=" + personid +
                '}';
    }
}
