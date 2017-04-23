package com.banglacricket.bdcricketteam.model;

/**
 * @author Ripon
 */
public class ProfileBatting {

    String gametype,fifty,hundred,Mat,Inns,NO,Runs,HS,Ave,BF,SR,fours,sixes;

    public ProfileBatting(String gametype, String fifty, String hundred, String mat, String inns, String NO, String runs, String HS, String ave, String BF, String SR, String fours, String sixes) {
        this.gametype = gametype;
        this.fifty = fifty;
        this.hundred = hundred;
        Mat = mat;
        Inns = inns;
        this.NO = NO;
        Runs = runs;
        this.HS = HS;
        Ave = ave;
        this.BF = BF;
        this.SR = SR;
        this.fours = fours;
        this.sixes = sixes;
    }

    public ProfileBatting(String fifty, String hundred, String mat, String inns, String NO, String runs, String HS, String ave, String BF, String SR, String fours, String sixes) {
        this.fifty = fifty;
        this.hundred = hundred;
        Mat = mat;
        Inns = inns;
        this.NO = NO;
        Runs = runs;
        this.HS = HS;
        Ave = ave;
        this.BF = BF;
        this.SR = SR;
        this.fours = fours;
        this.sixes = sixes;
    }

    public String getGametype() {
        return gametype;
    }

    public void setGametype(String gametype) {
        this.gametype = gametype;
    }

    public String getFifty() {
        return fifty;
    }

    public void setFifty(String fifty) {
        this.fifty = fifty;
    }

    public String getHundred() {
        return hundred;
    }

    public void setHundred(String hundred) {
        this.hundred = hundred;
    }

    public String getMat() {
        return Mat;
    }

    public void setMat(String mat) {
        Mat = mat;
    }

    public String getInns() {
        return Inns;
    }

    public void setInns(String inns) {
        Inns = inns;
    }

    public String getNO() {
        return NO;
    }

    public void setNO(String NO) {
        this.NO = NO;
    }

    public String getRuns() {
        return Runs;
    }

    public void setRuns(String runs) {
        Runs = runs;
    }

    public String getHS() {
        return HS;
    }

    public void setHS(String HS) {
        this.HS = HS;
    }

    public String getAve() {
        return Ave;
    }

    public void setAve(String ave) {
        Ave = ave;
    }

    public String getBF() {
        return BF;
    }

    public void setBF(String BF) {
        this.BF = BF;
    }

    public String getSR() {
        return SR;
    }

    public void setSR(String SR) {
        this.SR = SR;
    }

    public String getFours() {
        return fours;
    }

    public void setFours(String fours) {
        this.fours = fours;
    }

    public String getSixes() {
        return sixes;
    }

    public void setSixes(String sixes) {
        this.sixes = sixes;
    }
}
