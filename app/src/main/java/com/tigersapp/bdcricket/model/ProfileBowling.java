package com.tigersapp.bdcricket.model;

/**
 * @author Ripon
 */
public class ProfileBowling {
    String gameType,Mat,Inns,Balls,Runs,Wkts,BBI,BBM,Ave,Econ,SR,fourWkts,fiveWkts;

    public ProfileBowling(String gameType, String mat, String inns, String balls, String runs, String wkts, String BBI, String BBM, String ave, String econ, String SR, String fourWkts, String fiveWkts) {
        this.gameType = gameType;
        Mat = mat;
        Inns = inns;
        Balls = balls;
        Runs = runs;
        Wkts = wkts;
        this.BBI = BBI;
        this.BBM = BBM;
        Ave = ave;
        Econ = econ;
        this.SR = SR;
        this.fourWkts = fourWkts;
        this.fiveWkts = fiveWkts;
    }

    public ProfileBowling(String mat, String inns, String balls, String runs, String wkts, String BBI, String BBM, String ave, String econ, String SR, String fourWkts, String fiveWkts) {
        Mat = mat;
        Inns = inns;
        Balls = balls;
        Runs = runs;
        Wkts = wkts;
        this.BBI = BBI;
        this.BBM = BBM;
        Ave = ave;
        Econ = econ;
        this.SR = SR;
        this.fourWkts = fourWkts;
        this.fiveWkts = fiveWkts;
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

    public String getBalls() {
        return Balls;
    }

    public void setBalls(String balls) {
        Balls = balls;
    }

    public String getRuns() {
        return Runs;
    }

    public void setRuns(String runs) {
        Runs = runs;
    }

    public String getWkts() {
        return Wkts;
    }

    public void setWkts(String wkts) {
        Wkts = wkts;
    }

    public String getBBI() {
        return BBI;
    }

    public void setBBI(String BBI) {
        this.BBI = BBI;
    }

    public String getBBM() {
        return BBM;
    }

    public void setBBM(String BBM) {
        this.BBM = BBM;
    }

    public String getAve() {
        return Ave;
    }

    public void setAve(String ave) {
        Ave = ave;
    }

    public String getEcon() {
        return Econ;
    }

    public void setEcon(String econ) {
        Econ = econ;
    }

    public String getSR() {
        return SR;
    }

    public void setSR(String SR) {
        this.SR = SR;
    }

    public String getFourWkts() {
        return fourWkts;
    }

    public void setFourWkts(String fourWkts) {
        this.fourWkts = fourWkts;
    }

    public String getFiveWkts() {
        return fiveWkts;
    }

    public void setFiveWkts(String fiveWkts) {
        this.fiveWkts = fiveWkts;
    }

    public String getGameType() {
        return gameType;
    }

    public void setGameType(String gameType) {
        this.gameType = gameType;
    }
}
