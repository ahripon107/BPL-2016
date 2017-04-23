package com.banglacricket.bdcricketteam.model;

import java.io.Serializable;

/**
 * @author Ripon
 */

public class ProfileBattingBowlingRow implements Serializable{

    private String property;
    private String test;
    private String odi;
    private String t20i;
    private String firstClass;
    private String listA;
    private String twenty20;

    public ProfileBattingBowlingRow() {

    }

    public ProfileBattingBowlingRow(String property, String test, String odi, String t20i, String firstClass, String listA, String twenty20) {
        this.property = property;
        this.test = test;
        this.odi = odi;
        this.t20i = t20i;
        this.firstClass = firstClass;
        this.listA = listA;
        this.twenty20 = twenty20;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public String getTest() {
        return test;
    }

    public void setTest(String test) {
        this.test = test;
    }

    public String getOdi() {
        return odi;
    }

    public void setOdi(String odi) {
        this.odi = odi;
    }

    public String getT20i() {
        return t20i;
    }

    public void setT20i(String t20i) {
        this.t20i = t20i;
    }

    public String getFirstClass() {
        return firstClass;
    }

    public void setFirstClass(String firstClass) {
        this.firstClass = firstClass;
    }

    public String getListA() {
        return listA;
    }

    public void setListA(String listA) {
        this.listA = listA;
    }

    public String getTwenty20() {
        return twenty20;
    }

    public void setTwenty20(String twenty20) {
        this.twenty20 = twenty20;
    }
}
