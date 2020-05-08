package com.keir.ratemypet;

import java.io.Serializable;

public class Rating implements Serializable {

    private String ratingId;
    private String uploadId;
    private String uploaderId;

    public long cuteScore = 0;
    public long funnyScore = 0;
    public long interestingScore = 0;
    public long happyScore = 0;
    public long surprisingScore = 0;

    public Rating() {
    }

    public Rating(String ratingId, String uploadId, String uploaderId) {
        this.ratingId = ratingId;
        this.uploadId = uploadId;
        this.uploaderId = uploaderId;
    }

    public Rating(Rating otherRating) {
        this.ratingId = otherRating.ratingId;
        this.uploadId = otherRating.uploadId;
        this.uploaderId = otherRating.uploaderId;

        this.cuteScore = otherRating.cuteScore;
        this.funnyScore = otherRating.funnyScore;
        this.interestingScore = otherRating.interestingScore;
        this.happyScore = otherRating.happyScore;
        this.surprisingScore = otherRating.surprisingScore;
    }

    public String getRatingId() { return ratingId; }
    public String getUploadId() { return uploadId; }
    public String getUploaderId() { return uploaderId; }

}