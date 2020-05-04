package com.keir.ratemypet;

import java.io.Serializable;

public class Rating implements Serializable {

    private String id;

    private long cuteScore = 0;
    private long funnyScore = 0;
    private long interestingScore = 0;
    private long happyScore = 0;
    private long surprisingScore = 0;

    public Rating() {

    }

    public Rating(String id) {
        this.id = id;
    }

    public String getId() { return id; }

    public long getCuteScore() {
        return cuteScore;
    }

    public void setCuteScore(long cuteScore) {
        this.cuteScore = cuteScore;
    }

    public long getFunnyScore() {
        return funnyScore;
    }

    public void setFunnyScore(long funnyScore) {
        this.funnyScore = funnyScore;
    }

    public long getInterestingScore() {
        return interestingScore;
    }

    public void setInterestingScore(long interestingScore) {
        this.interestingScore = interestingScore;
    }

    public long getHappyScore() {
        return happyScore;
    }

    public void setHappyScore(long happyScore) {
        this.happyScore = happyScore;
    }

    public long getSurprisingScore() {
        return surprisingScore;
    }

    public void setSurprisingScore(long surprisingScore) {
        this.surprisingScore = surprisingScore;
    }
}
